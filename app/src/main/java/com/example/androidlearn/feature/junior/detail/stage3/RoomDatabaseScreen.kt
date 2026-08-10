package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Room 数据库笔记
 * 官方文档：https://developer.android.com/training/data-storage/room
 *
 * ── 1  Room 三大核心组件 ──────────────────────────────────────────────────────
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
 * ── 2  @Entity 定义表结构 ─────────────────────────────────────────────────────
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
 *  @Entity(
 *      tableName = "user_books",
 *      primaryKeys = ["userId", "bookId"]
 *  )
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
 *      @Ignore val tempFlag: Boolean = false   // 不映射到数据库
 *  )
 *
 *
 * ── 3  @Dao 定义操作接口 ──────────────────────────────────────────────────────
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
 *      suspend fun insert(user: User): Long   // 返回新行 rowId
 *
 *      @Insert
 *      suspend fun insertAll(users: List<User>)
 *
 *      // 更新（根据主键匹配）
 *      @Update
 *      suspend fun update(user: User): Int    // 返回受影响行数
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
 * ── 4  @Database 创建数据库 ───────────────────────────────────────────────────
 *
 *  @Database(
 *      entities = [User::class, Post::class],
 *      version = 1,
 *      exportSchema = true   // 导出 schema 文件，便于版本管理
 *  )
 *  abstract class AppDatabase : RoomDatabase() {
 *      abstract fun userDao(): UserDao
 *      abstract fun postDao(): PostDao
 *
 *      companion object {
 *          @Volatile
 *          private var INSTANCE: AppDatabase? = null
 *
 *          fun getInstance(context: Context): AppDatabase =
 *              INSTANCE ?: synchronized(this) {
 *                  Room.databaseBuilder(
 *                      context.applicationContext,
 *                      AppDatabase::class.java,
 *                      "app_database"
 *                  )
 *                  .fallbackToDestructiveMigration()  // 开发阶段：版本不匹配时重建
 *                  .build()
 *                  .also { INSTANCE = it }
 *              }
 *      }
 *  }
 *
 *  // 推荐通过 Hilt 注入（避免手动单例）
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
 * ── 5  TypeConverter 自定义类型 ───────────────────────────────────────────────
 *
 *  · SQLite 只支持基本类型，复杂类型需要 TypeConverter 转换
 *
 *  // 将 List<String> 存为 JSON 字符串
 *  class Converters {
 *      @TypeConverter
 *      fun fromList(list: List<String>): String = Gson().toJson(list)
 *
 *      @TypeConverter
 *      fun toList(json: String): List<String> =
 *          Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
 *
 *      // Date 转 Long
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
 * ── 6  数据库迁移（Migration）────────────────────────────────────────────────
 *
 *  · 版本升级时保留用户数据，不能直接 fallbackToDestructiveMigration（会清空数据）
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
 * ── 7  关联查询（Relations）──────────────────────────────────────────────────
 *
 *  // 一对多：一个 User 有多个 Post
 *  data class UserWithPosts(
 *      @Embedded val user: User,
 *      @Relation(
 *          parentColumn = "id",
 *          entityColumn = "author_id"
 *      )
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
 * ── 8  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · Database 设为单例（通过 Hilt 注入），避免重复创建开销
 *  · DAO 查询返回 Flow，UI 自动响应数据变化，无需手动刷新
 *  · 所有 DAO 操作用 suspend 函数，在协程中调用，不阻塞主线程
 *  · 生产环境用 addMigrations()，不要用 fallbackToDestructiveMigration（会清空数据）
 *  · 复杂 SQL 先在 DB Browser for SQLite 中验证，再写入 @Query
 *  · 关联查询加 @Transaction，保证原子性
 *  · exportSchema = true 并将 schema 文件纳入版本控制，便于追踪数据库变更
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1", "Room 三大核心组件"),
    NoteChapter("2", "@Entity 定义表结构"),
    NoteChapter("3", "@Dao 定义操作接口"),
    NoteChapter("4", "@Database 创建数据库"),
    NoteChapter("5", "TypeConverter 自定义类型"),
    NoteChapter("6", "数据库迁移（Migration）"),
    NoteChapter("7", "关联查询（Relations）"),
    NoteChapter("8", "最佳实践"),
)

@Composable
fun RoomDatabaseScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Room 数据库",
        subtitle = "Entity · DAO · Database · Migration · Relations",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
