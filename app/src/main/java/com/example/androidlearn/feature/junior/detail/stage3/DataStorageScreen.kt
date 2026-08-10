package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 数据存储笔记（Room · SharedPreferences · DataStore）
 * 官方文档：https://developer.android.com/training/data-storage/room
 *           https://developer.android.com/topic/libraries/architecture/datastore
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  Room 数据库
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  三大核心组件 ─────────────────────────────────────────────────────────
 *
 *  · @Entity：映射数据库表，每个字段对应一列
 *  · @Dao：数据访问对象，定义增删改查接口
 *  · @Database：数据库入口，声明所有 Entity 和 DAO
 *
 *  // 依赖（build.gradle.kts）
 *  implementation("androidx.room:room-runtime:2.6.1")
 *  implementation("androidx.room:room-ktx:2.6.1")       // 协程/Flow 支持
 *  ksp("androidx.room:room-compiler:2.6.1")             // 注解处理器（KSP）
 *
 *
 * ── 1.2  @Entity 定义表结构 ──────────────────────────────────────────────────
 *
 *  // 基础 Entity
 *  @Entity(tableName = "users")
 *  data class User(
 *      @PrimaryKey(autoGenerate = true) val id: Int = 0,
 *      @ColumnInfo(name = "user_name") val name: String,
 *      val email: String,
 *      val createdAt: Long = System.currentTimeMillis()
 *  )
 *
 *  // 复合主键
 *  @Entity(tableName = "user_books", primaryKeys = ["userId", "bookId"])
 *  data class UserBook(val userId: Int, val bookId: Int)
 *
 *  // 索引（加速查询）
 *  @Entity(
 *      tableName = "posts",
 *      indices = [Index(value = ["author_id"]), Index(value = ["title"], unique = true)]
 *  )
 *  data class Post(
 *      @PrimaryKey(autoGenerate = true) val id: Int = 0,
 *      @ColumnInfo(name = "author_id") val authorId: Int,
 *      val title: String,
 *      val content: String
 *  )
 *
 *  // 忽略字段（不存入数据库）
 *  @Entity
 *  data class Product(
 *      @PrimaryKey val id: Int,
 *      val name: String,
 *      @Ignore val tempFlag: Boolean = false
 *  )
 *
 *
 * ── 1.3  @Dao 定义操作接口 ───────────────────────────────────────────────────
 *
 *  @Dao
 *  interface UserDao {
 *      // 查询（返回 Flow，数据变化自动推送）
 *      @Query("SELECT * FROM users ORDER BY user_name ASC")
 *      fun getAllUsers(): Flow<List<User>>
 *
 *      // 条件查询
 *      @Query("SELECT * FROM users WHERE id = :userId")
 *      suspend fun getUserById(userId: Int): User?
 *
 *      // 模糊查询
 *      @Query("SELECT * FROM users WHERE user_name LIKE '%' || :keyword || '%'")
 *      fun searchUsers(keyword: String): Flow<List<User>>
 *
 *      // 插入（冲突策略：REPLACE / IGNORE / ABORT）
 *      @Insert(onConflict = OnConflictStrategy.REPLACE)
 *      suspend fun insert(user: User): Long
 *
 *      @Insert
 *      suspend fun insertAll(users: List<User>)
 *
 *      // 更新（根据主键匹配）
 *      @Update
 *      suspend fun update(user: User): Int
 *
 *      // 删除
 *      @Delete
 *      suspend fun delete(user: User): Int
 *
 *      @Query("DELETE FROM users WHERE id = :userId")
 *      suspend fun deleteById(userId: Int)
 *
 *      @Query("DELETE FROM users")
 *      suspend fun deleteAll()
 *
 *      // 统计
 *      @Query("SELECT COUNT(*) FROM users")
 *      fun getUserCount(): Flow<Int>
 *  }
 *
 *
 * ── 1.4  @Database 创建数据库 ────────────────────────────────────────────────
 *
 *  @Database(
 *      entities = [User::class, Post::class],
 *      version = 1,
 *      exportSchema = true
 *  )
 *  abstract class AppDatabase : RoomDatabase() {
 *      abstract fun userDao(): UserDao
 *      abstract fun postDao(): PostDao
 *
 *      companion object {
 *          @Volatile private var INSTANCE: AppDatabase? = null
 *
 *          fun getInstance(context: Context): AppDatabase =
 *              INSTANCE ?: synchronized(this) {
 *                  Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_db")
 *                      .fallbackToDestructiveMigration()  // 开发阶段用，生产用 addMigrations
 *                      .build().also { INSTANCE = it }
 *              }
 *      }
 *  }
 *
 *  // 推荐通过 Hilt 注入
 *  @Module @InstallIn(SingletonComponent::class)
 *  object DatabaseModule {
 *      @Provides @Singleton
 *      fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
 *          Room.databaseBuilder(ctx, AppDatabase::class.java, "app_db").build()
 *
 *      @Provides
 *      fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
 *  }
 *
 *
 * ── 1.5  TypeConverter 自定义类型 ────────────────────────────────────────────
 *
 *  · SQLite 只支持基本类型，复杂类型需要 TypeConverter 转换
 *
 *  class Converters {
 *      @TypeConverter
 *      fun fromList(list: List<String>): String = Gson().toJson(list)
 *
 *      @TypeConverter
 *      fun toList(json: String): List<String> =
 *          Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
 *
 *      @TypeConverter
 *      fun fromDate(date: Date?): Long? = date?.time
 *
 *      @TypeConverter
 *      fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }
 *  }
 *
 *  // 在 @Database 上声明
 *  @Database(entities = [User::class], version = 1)
 *  @TypeConverters(Converters::class)
 *  abstract class AppDatabase : RoomDatabase() { ... }
 *
 *
 * ── 1.6  数据库迁移（Migration）─────────────────────────────────────────────
 *
 *  // version 1 → 2：新增 phone 列
 *  val MIGRATION_1_2 = object : Migration(1, 2) {
 *      override fun migrate(database: SupportSQLiteDatabase) {
 *          database.execSQL("ALTER TABLE users ADD COLUMN phone TEXT DEFAULT '' NOT NULL")
 *      }
 *  }
 *
 *  // version 2 → 3：新增 posts 表
 *  val MIGRATION_2_3 = object : Migration(2, 3) {
 *      override fun migrate(database: SupportSQLiteDatabase) {
 *          database.execSQL("""
 *              CREATE TABLE IF NOT EXISTS posts (
 *                  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 *                  title TEXT NOT NULL,
 *                  author_id INTEGER NOT NULL
 *              )
 *          """.trimIndent())
 *      }
 *  }
 *
 *  // 注册迁移
 *  Room.databaseBuilder(ctx, AppDatabase::class.java, "app_db")
 *      .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
 *      .build()
 *
 *
 * ── 1.7  关联查询（Relations）───────────────────────────────────────────────
 *
 *  // 一对多：一个 User 有多个 Post
 *  data class UserWithPosts(
 *      @Embedded val user: User,
 *      @Relation(parentColumn = "id", entityColumn = "author_id")
 *      val posts: List<Post>
 *  )
 *
 *  @Dao
 *  interface UserDao {
 *      @Transaction
 *      @Query("SELECT * FROM users WHERE id = :userId")
 *      fun getUserWithPosts(userId: Int): Flow<UserWithPosts>
 *  }
 *
 *  // 多对多：User 和 Book 通过 UserBook 关联
 *  data class UserWithBooks(
 *      @Embedded val user: User,
 *      @Relation(
 *          parentColumn = "id",
 *          entityColumn = "id",
 *          associateBy = Junction(UserBook::class, parentColumn = "userId", entityColumn = "bookId")
 *      )
 *      val books: List<Book>
 *  )
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  SharedPreferences
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  基础用法 ─────────────────────────────────────────────────────────────
 *
 *  · 轻量级键值对存储，数据持久化到 XML 文件
 *  · 适合：用户偏好设置、少量配置项
 *  · 不适合：大量数据、频繁写入、复杂结构
 *
 *  // 获取实例
 *  val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
 *  val prefs = PreferenceManager.getDefaultSharedPreferences(context)  // 默认文件
 *
 *  // 读取
 *  val name   = prefs.getString("name", "默认值")
 *  val age    = prefs.getInt("age", 0)
 *  val isDark = prefs.getBoolean("dark_mode", false)
 *  val score  = prefs.getFloat("score", 0f)
 *  val count  = prefs.getLong("count", 0L)
 *
 *  // 写入
 *  prefs.edit()
 *      .putString("name", "张三")
 *      .putInt("age", 25)
 *      .putBoolean("dark_mode", true)
 *      .apply()    // 异步写入（推荐）
 *      // .commit() // 同步写入，主线程慎用
 *
 *  // 删除
 *  prefs.edit().remove("name").apply()
 *  prefs.edit().clear().apply()
 *
 *  // 监听变化（需持有强引用，否则被 GC 回收）
 *  val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
 *      if (key == "dark_mode") updateTheme(prefs.getBoolean(key, false))
 *  }
 *  prefs.registerOnSharedPreferenceChangeListener(listener)
 *
 *
 * ── 2.2  SharedPreferences 的问题 ────────────────────────────────────────────
 *
 *  · 主线程 I/O：首次调用在主线程读取文件，可能导致 ANR
 *  · apply() 虽然异步写入，但在 Activity.onStop 时会等待写入完成，仍可能卡顿
 *  · 类型不安全：key 是字符串，容易拼写错误
 *  · 不支持 Flow/协程，无法响应式监听
 *  · 多进程不安全（MODE_MULTI_PROCESS 已废弃）
 *
 *
 * ── 2.3  EncryptedSharedPreferences（敏感数据加密）──────────────────────────
 *
 *  · 对 key 和 value 都进行加密，适合存储 Token、密码等敏感信息
 *  · 基于 Android Keystore，密钥由系统管理
 *
 *  // 依赖
 *  // implementation("androidx.security:security-crypto:1.1.0-alpha06")
 *
 *  val masterKey = MasterKey.Builder(context)
 *      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
 *      .build()
 *
 *  val encryptedPrefs = EncryptedSharedPreferences.create(
 *      context, "secret_prefs", masterKey,
 *      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
 *      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
 *  )
 *
 *  // 用法与普通 SP 完全相同
 *  encryptedPrefs.edit().putString("token", "Bearer xxx").apply()
 *  val token = encryptedPrefs.getString("token", null)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  DataStore
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  简介与优势（对比 SP）────────────────────────────────────────────────
 *
 *  · Jetpack 官方推荐的键值对持久化方案，替代 SharedPreferences
 *  · 基于 Kotlin 协程和 Flow，完全异步，不阻塞主线程
 *  · 两种实现：Preferences DataStore（键值对）和 Proto DataStore（强类型）
 *
 *  对比 SharedPreferences：
 *  ┌─────────────────────┬──────────────────┬──────────────────┐
 *  │ 特性                │ SharedPreferences│ DataStore        │
 *  ├─────────────────────┼──────────────────┼──────────────────┤
 *  │ 线程安全            │ 否               │ 是               │
 *  │ 主线程 I/O          │ 可能阻塞         │ 不阻塞           │
 *  │ 类型安全            │ 否（字符串 key） │ 是（类型化 key） │
 *  │ 响应式              │ 需手动监听       │ Flow 自动推送    │
 *  │ 错误处理            │ 无               │ catch 异常       │
 *  └─────────────────────┴──────────────────┴──────────────────┘
 *
 *  // 依赖（build.gradle.kts）
 *  implementation("androidx.datastore:datastore-preferences:1.1.1")
 *
 *
 * ── 3.2  Preferences DataStore 基础用法 ──────────────────────────────────────
 *
 *  // 创建 DataStore（顶层属性，保证单例）
 *  val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
 *
 *  // 定义类型安全的 Key
 *  val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
 *  val USER_NAME_KEY = stringPreferencesKey("user_name")
 *  val FONT_SIZE_KEY = intPreferencesKey("font_size")
 *  val VOLUME_KEY    = floatPreferencesKey("volume")
 *
 *  // 读取（返回 Flow，数据变化自动推送）
 *  val isDarkFlow: Flow<Boolean> = context.dataStore.data
 *      .catch { e ->
 *          if (e is IOException) emit(emptyPreferences())
 *          else throw e
 *      }
 *      .map { prefs -> prefs[DARK_MODE_KEY] ?: false }
 *
 *  // 写入（挂起函数，事务性原子写入）
 *  suspend fun setDarkMode(enabled: Boolean) {
 *      context.dataStore.edit { prefs ->
 *          prefs[DARK_MODE_KEY] = enabled
 *      }
 *  }
 *
 *  // 删除单个 key / 清空所有
 *  suspend fun clearUserName() { context.dataStore.edit { it.remove(USER_NAME_KEY) } }
 *  suspend fun clearAll()      { context.dataStore.edit { it.clear() } }
 *
 *
 * ── 3.3  在 ViewModel 中使用 ─────────────────────────────────────────────────
 *
 *  class SettingsViewModel(
 *      private val dataStore: DataStore<Preferences>
 *  ) : ViewModel() {
 *
 *      val isDark: StateFlow<Boolean> = dataStore.data
 *          .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
 *          .map { it[DARK_MODE_KEY] ?: false }
 *          .stateIn(
 *              scope = viewModelScope,
 *              started = SharingStarted.WhileSubscribed(5_000),
 *              initialValue = false
 *          )
 *
 *      fun setDarkMode(enabled: Boolean) {
 *          viewModelScope.launch {
 *              dataStore.edit { it[DARK_MODE_KEY] = enabled }
 *          }
 *      }
 *  }
 *
 *  // Compose UI 中收集
 *  val isDark by viewModel.isDark.collectAsStateWithLifecycle()
 *
 *
 * ── 3.4  Proto DataStore（强类型）───────────────────────────────────────────
 *
 *  · 使用 Protocol Buffers 定义数据结构，完全类型安全，适合复杂配置
 *
 *  // 1. 定义 .proto 文件（src/main/proto/user_prefs.proto）
 *  // syntax = "proto3";
 *  // option java_package = "com.example.app";
 *  // message UserPreferences {
 *  //     bool dark_mode = 1;
 *  //     string user_name = 2;
 *  //     int32 font_size = 3;
 *  // }
 *
 *  // 2. 实现 Serializer
 *  object UserPreferencesSerializer : Serializer<UserPreferences> {
 *      override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
 *
 *      override suspend fun readFrom(input: InputStream): UserPreferences =
 *          try { UserPreferences.parseFrom(input) }
 *          catch (e: InvalidProtocolBufferException) { throw CorruptionException("无法读取 proto", e) }
 *
 *      override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
 *          t.writeTo(output)
 *  }
 *
 *  // 3. 创建 & 读写
 *  val Context.protoDataStore by dataStore(
 *      fileName = "user_prefs.pb",
 *      serializer = UserPreferencesSerializer
 *  )
 *
 *  val darkModeFlow: Flow<Boolean> = context.protoDataStore.data.map { it.darkMode }
 *
 *  suspend fun setDarkMode(enabled: Boolean) {
 *      context.protoDataStore.updateData { prefs ->
 *          prefs.toBuilder().setDarkMode(enabled).build()
 *      }
 *  }
 *
 *
 * ── 3.5  从 SharedPreferences 迁移 ───────────────────────────────────────────
 *
 *  · 提供 SharedPreferencesMigration，一次性自动迁移旧数据，迁移完成后旧 SP 文件自动删除
 *
 *  val Context.dataStore by preferencesDataStore(
 *      name = "settings",
 *      produceMigrations = { context ->
 *          listOf(
 *              SharedPreferencesMigration(
 *                  context = context,
 *                  sharedPreferencesName = "old_prefs",
 *                  keysToMigrate = setOf("dark_mode", "user_name")  // 不传则迁移全部
 *              )
 *          )
 *      }
 *  )
 *
 *
 * ── 4  存储方案选型 ───────────────────────────────────────────────────────────
 *
 *  SharedPreferences：
 *  · 维护旧项目；简单配置项；不需要响应式
 *
 *  DataStore Preferences：
 *  · 新项目替代 SP；需要 Flow 响应式；键值对结构
 *
 *  DataStore Proto：
 *  · 复杂配置对象；需要强类型保证；需要版本迁移
 *
 *  EncryptedSharedPreferences：
 *  · Token、密码等敏感数据；安全要求高的场景
 *
 *  Room Database：
 *  · 大量结构化数据；需要查询/排序/关联；列表数据
 *
 *
 * ── 5  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  Room：
 *  · Database 设为单例（通过 Hilt 注入），避免重复创建开销
 *  · DAO 查询返回 Flow，UI 自动响应数据变化，无需手动刷新
 *  · 所有 DAO 操作用 suspend 函数，在协程中调用，不阻塞主线程
 *  · 生产环境用 addMigrations()，不要用 fallbackToDestructiveMigration（会清空数据）
 *  · 关联查询加 @Transaction，保证原子性
 *  · exportSchema = true 并将 schema 文件纳入版本控制
 *
 *  SharedPreferences / DataStore：
 *  · 新项目优先使用 DataStore，SP 在主线程 I/O 可能导致 ANR
 *  · 不要用 SP/DataStore 存储大量数据，改用 Room
 *  · 敏感信息（Token/密码）用 EncryptedSharedPreferences 加密
 *  · DataStore 操作放在 ViewModel 中，通过 StateFlow 暴露给 UI
 *  · 读取 DataStore 时加 catch 处理 IOException，防止数据损坏崩溃
 *  · SP 的 apply() 优于 commit()，避免主线程阻塞
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    // ── 一级：Room 数据库 ──────────────────────────────────────
    NoteChapter("1",   "Room 数据库"),
    NoteChapter("1.1", "三大核心组件"),
    NoteChapter("1.2", "@Entity 定义表结构"),
    NoteChapter("1.3", "@Dao 定义操作接口"),
    NoteChapter("1.4", "@Database 创建数据库"),
    NoteChapter("1.5", "TypeConverter 自定义类型"),
    NoteChapter("1.6", "数据库迁移（Migration）"),
    NoteChapter("1.7", "关联查询（Relations）"),
    // ── 一级：SharedPreferences ───────────────────────────────
    NoteChapter("2",   "SharedPreferences"),
    NoteChapter("2.1", "基础用法"),
    NoteChapter("2.2", "SharedPreferences 的问题"),
    NoteChapter("2.3", "EncryptedSharedPreferences（敏感数据加密）"),
    // ── 一级：DataStore ───────────────────────────────────────
    NoteChapter("3",   "DataStore"),
    NoteChapter("3.1", "简介与优势（对比 SP）"),
    NoteChapter("3.2", "Preferences DataStore 基础用法"),
    NoteChapter("3.3", "在 ViewModel 中使用"),
    NoteChapter("3.4", "Proto DataStore（强类型）"),
    NoteChapter("3.5", "从 SharedPreferences 迁移"),
    // ── 一级：综合 ────────────────────────────────────────────
    NoteChapter("4",   "存储方案选型"),
    NoteChapter("5",   "最佳实践"),
)

@Composable
fun DataStorageScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "数据存储",
        subtitle = "Room · SharedPreferences · DataStore · 加密存储",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
