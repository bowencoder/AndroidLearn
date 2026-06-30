package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "启动性能优化",
    description = "冷启动/温启动，App Startup，异步初始化，SplashScreen API",
    overview = "启动速度直接影响用户留存，冷启动目标 < 1s。需从 Application 初始化、ContentProvider、首帧渲染三个维度系统优化。",
    keyPoints = listOf(
        "冷/温/热启动：从进程创建到首帧渲染的完整链路",
        "App Startup 库：用 Initializer 替换多个 ContentProvider，串行/并行可控",
        "Trace 埋点：Trace.beginSection() 精确定位各阶段耗时",
        "异步初始化：非关键 SDK 放到 IO 线程或 IdleHandler 中延迟初始化",
        "SplashScreen API：Android 12+ 统一启动画面，避免白/黑屏",
        "启动时 StrictMode：检测主线程 I/O，强制暴露阻塞操作"
    ),
    codeSnippet = """
// App Startup Initializer
class AnalyticsInitializer : Initializer<AnalyticsClient> {
    override fun create(context: Context): AnalyticsClient {
        return AnalyticsClient.initialize(context)
    }
    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}

// Trace 埋点
Trace.beginSection("InitDatabase")
val db = Room.databaseBuilder(context, AppDB::class.java, "app.db").build()
Trace.endSection()

// IdleHandler 延迟任务
Looper.myQueue().addIdleHandler {
    NonCriticalSDK.init(context)
    false // 只执行一次
}
    """.trimIndent(),
    tips = listOf(
        "用 Perfetto 录制启动 trace，分析 App 进程和 System 进程交互",
        "避免在 Application.onCreate() 做磁盘 I/O 和网络请求",
        "SplashScreen windowSplashScreenAnimatedIcon 配置启动帧动画"
    )
)

@Composable
fun AppStartupScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
