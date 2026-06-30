package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "网络请求",
    description = "Retrofit + OkHttp，协程集成，错误处理",
    overview = "Retrofit 是类型安全的 HTTP 客户端，配合 OkHttp 和协程是 Android 网络请求的业界标准。",
    keyPoints = listOf(
        "Retrofit：用注解定义 API，@GET / @POST / @Path / @Query / @Body",
        "OkHttp：底层 HTTP 客户端，Interceptor 添加 Header/日志",
        "Gson / Moshi / Kotlinx.serialization：JSON 解析库",
        "suspend 函数：Retrofit 原生支持协程，自动切换 IO 线程",
        "错误处理：try-catch / sealed class Result / runCatching",
        "Repository 模式：封装网络层，隔离 ViewModel 与数据源"
    ),
    codeSnippet = """
interface ApiService {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

viewModelScope.launch {
    val result = runCatching { api.getUser(1) }
    result.onSuccess { user -> /* 更新 UI */ }
         .onFailure { e -> /* 处理错误 */ }
}
    """.trimIndent(),
    tips = listOf(
        "添加 HttpLoggingInterceptor 在 Debug 版本打印请求/响应日志",
        "用 sealed class 封装 Result<T>，统一处理成功/失败状态",
        "网络请求放在 Repository 层，ViewModel 不直接调用 API"
    )
)

@Composable
fun NetworkRequestScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "核心UI与数据组件",
        onBack = onBack
    )
}
