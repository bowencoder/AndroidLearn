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
 *  @Entity(tableName = "users")
 *  data class User(
 *      @PrimaryKey(autoGenerate = true) val id: Int = 0,
 *      @ColumnInfo(name = "user_name") val name: String,
 *      val email: String,
 *      val createdAt: Long = System.currentTimeMillis()
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
 *      // 插入（冲突策略：REPLACE / IGNORE / ABORT）
 *      @Insert(onConflict = OnConflictStrategy.REPLACE)
 *      suspend fun insert(user: User): Long
 *
 *      // 更新（根据主键匹配）
 *      @Update
 *      suspend fun update(user: User): Int
 *
 *      // 删除
 *      @Delete
 *      suspend fun delete(user: User): Int
 *
 *      @Query("DELETE FROM users")
 *      suspend fun deleteAll()
 *  }
 *
 *
 * ── 1.4  @Database 创建数据库 ────────────────────────────────────────────────
 *
 *  @Database(entities = [User::class], version = 1, exportSchema = false)
 *  abstract class AppDatabase : RoomDatabase() {
 *      abstract fun userDao(): UserDao
 *
 *      companion object {
 *          @Volatile private var INSTANCE: AppDatabase? = null
 *
 *          fun getInstance(context: Context): AppDatabase =
 *              INSTANCE ?: synchronized(this) {
 *                  Room.databaseBuilder(
 *                      context.applicationContext,
 *                      AppDatabase::class.java,
 *                      "app_db"
 *                  ).build().also { INSTANCE = it }
 *              }
 *      }
 *  }
 *
 *  // 在 Activity / Repository 中使用
 *  val db = AppDatabase.getInstance(context)
 *  val userDao = db.userDao()
 *
 *  // DAO 操作必须在协程中调用
 *  lifecycleScope.launch {
 *      userDao.insert(User(name = "张三", email = "zs@example.com"))
 *      val users = userDao.getAllUsers().first()
 *  }
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
 *
 *  // 读取
 *  val name   = prefs.getString("name", "默认值")
 *  val age    = prefs.getInt("age", 0)
 *  val isDark = prefs.getBoolean("dark_mode", false)
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
 *
 * ── 2.2  SharedPreferences 的问题 ────────────────────────────────────────────
 *
 *  · 主线程 I/O：首次调用在主线程读取文件，可能导致 ANR
 *  · apply() 虽然异步写入，但在 Activity.onStop 时会等待写入完成，仍可能卡顿
 *  · 类型不安全：key 是字符串，容易拼写错误
 *  · 不支持 Flow/协程，无法响应式监听
 *  → 新项目推荐使用 DataStore 替代
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
 * ── 4  存储方案选型 ───────────────────────────────────────────────────────────
 *
 *  SharedPreferences：
 *  · 维护旧项目；简单配置项；不需要响应式
 *
 *  DataStore Preferences：
 *  · 新项目替代 SP；需要 Flow 响应式；键值对结构
 *
 *  Room Database：
 *  · 大量结构化数据；需要查询/排序；列表数据
 *
 *
 * ── 5  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  Room：
 *  · Database 设为单例，避免重复创建开销
 *  · DAO 查询返回 Flow，UI 自动响应数据变化，无需手动刷新
 *  · 所有 DAO 操作用 suspend 函数，在协程中调用，不阻塞主线程
 *
 *  SharedPreferences / DataStore：
 *  · 新项目优先使用 DataStore，SP 在主线程 I/O 可能导致 ANR
 *  · 不要用 SP/DataStore 存储大量数据，改用 Room
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
    // ── 一级：SharedPreferences ───────────────────────────────
    NoteChapter("2",   "SharedPreferences"),
    NoteChapter("2.1", "基础用法"),
    NoteChapter("2.2", "SharedPreferences 的问题"),
    // ── 一级：DataStore ───────────────────────────────────────
    NoteChapter("3",   "DataStore"),
    NoteChapter("3.1", "简介与优势（对比 SP）"),
    NoteChapter("3.2", "Preferences DataStore 基础用法"),
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
        subtitle = "Room · SharedPreferences · DataStore",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
