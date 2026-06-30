package com.example.androidlearn.feature.senior.detail.stage16

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【序列化框架】专属学习页
//  stageIndex=15, topicIndex=5
//  阶段颜色：青绿 0xFF00897B（工程化进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "序列化框架",
    description = "Serializable vs Parcelable vs Protobuf vs kotlinx.serialization 性能对比与选型",
    overview = "序列化是将对象转换为字节流的过程，在 Android 中广泛用于 Intent 数据传递、进程间通信（Binder）、网络传输和数据持久化。不同序列化框架在性能、易用性和跨平台性上各有差异，选择合适的框架对 App 性能有直接影响。",
    keyPoints = listOf(
        "Serializable（Java 标准）：实现简单（implements Serializable），但基于反射，性能差（约是 Parcelable 10倍慢），会产生大量临时对象",
        "Parcelable（Android 专用）：手动或注解生成序列化代码，性能极高（基于内存共享），适合 Intent/Binder 数据传递",
        "Gson/Moshi/kotlinx.serialization：JSON 序列化库，用于网络数据解析；kotlinx.serialization 支持 K2 编译器和 Multiplatform",
        "Protobuf（Protocol Buffers）：Google 开源的二进制序列化协议，体积小（比 JSON 小 3-10x）、速度快，适合大数据量场景",
        "@Parcelize（Kotlin）：Kotlin 编译器插件，自动生成 Parcelable 实现，一行注解替代数十行模板代码",
        "DataStore（替代 SharedPreferences）：基于 Protobuf 或 Preferences 的异步数据持久化，支持协程，线程安全"
    ),
    codeSnippet = """
// 1. Parcelable + @Parcelize（推荐，Intent 传递）
@Parcelize
data class User(
    val id: Int,
    val name: String,
    val avatar: String
) : Parcelable

// 使用
intent.putExtra("user", user)
val user: User? = intent.getParcelableExtra("user")
// Android 13+ 需要指定类型
val user: User? = intent.getParcelableExtra("user", User::class.java)

// 2. kotlinx.serialization（JSON，网络请求）
@Serializable
data class ApiResponse<T>(
    @SerialName("code") val code: Int,
    @SerialName("data") val data: T?,
    @SerialName("msg") val message: String
)

// 序列化/反序列化
val json = Json { ignoreUnknownKeys = true; isLenient = true }
val jsonStr = json.encodeToString(user)
val decoded = json.decodeFromString<User>(jsonStr)

// 与 Retrofit 集成
val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()

// 3. Protobuf（高性能二进制，适合大数据/跨语言）
// user.proto
// message User {
//     int32 id = 1;
//     string name = 2;
//     string avatar = 3;
// }
// 序列化
val bytes = user.toByteArray()
// 反序列化
val user = User.parseFrom(bytes)

// 4. DataStore（替代 SharedPreferences）
val Context.dataStore by preferencesDataStore(name = "settings")
val KEY_THEME = booleanPreferencesKey("is_dark_mode")
// 写入（协程）
context.dataStore.edit { prefs -> prefs[KEY_THEME] = true }
// 读取（Flow）
val isDark: Flow<Boolean> = context.dataStore.data.map { prefs ->
    prefs[KEY_THEME] ?: false
}

// 序列化性能对比（相对参考值）
// Parcelable：★★★★★（最快，内存直接映射）
// Protobuf：  ★★★★☆（快，二进制小）
// kotlinx.ser：★★★☆☆（快，支持 KMP）
// Gson/Moshi：★★★☆☆（中等）
// Serializable：★★☆☆☆（慢，有反射）
    """.trimIndent(),
    tips = listOf(
        "Intent 跨组件传递数据优先用 Parcelable（@Parcelize），不要用 Serializable，性能差距在大数据量下非常明显",
        "Gson 对 Kotlin data class 有 null 安全问题（无法调用构造函数默认值），推荐用 Moshi 或 kotlinx.serialization",
        "SharedPreferences 在主线程同步读写，有 ANR 风险；新项目应使用 DataStore 替代，它基于协程且数据一致性更好"
    )
)

@Composable
fun SerializationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00897B),
        stageTitle = "工程化进阶",
        onBack = onBack
    )
}
