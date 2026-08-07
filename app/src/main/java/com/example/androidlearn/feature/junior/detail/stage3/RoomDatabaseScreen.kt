package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Room 数据库",
    description = "Entity、DAO、Database，SQLite 封装层",
    overview = "Room 是 Jetpack 提供的 SQLite 抽象层，编译期验证 SQL 语句，与协程/Flow 完美集成。",
    keyPoints = listOf(
        "@Entity：定义数据库表结构，@PrimaryKey 设置主键",
        "@Dao：定义数据库操作接口，@Query / @Insert / @Update / @Delete",
        "@Database：创建数据库实例，声明所有 Entity 和 DAO",
        "Flow 集成：DAO 返回 Flow<List<T>>，数据变化自动通知",
        "Migration：数据库版本升级，addMigration 添加迁移脚本",
        "TypeConverter：将自定义类型转换为 SQLite 支持的类型"
    ),
    codeSnippet = """
@Entity(tableName = "users")
data class User(@PrimaryKey val id: Int, val name: String)

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
}

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
    """.trimIndent(),
    tips = listOf(
        "将 Database 设为单例，避免重复创建开销",
        "使用 Flow 返回查询结果，UI 自动响应数据变化",
        "复杂查询先在 DB Browser for SQLite 中验证 SQL"
    )
)

@Composable
fun RoomDatabaseScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "核心UI与数据组件",
        onBack = onBack
    )
}
