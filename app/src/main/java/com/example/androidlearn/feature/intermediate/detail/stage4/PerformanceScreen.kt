package com.example.androidlearn.feature.intermediate.detail.stage4

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "性能优化",
    description = "卡顿排查、内存泄漏、启动速度优化",
    overview = "性能优化从流畅度、内存、启动速度三个维度全面提升用户体验，是高级工程师的必备技能。",
    keyPoints = listOf(
        "卡顿：主线程超过 16ms 未完成绘制，用 Perfetto 定位",
        "内存泄漏：LeakCanary 自动检测，Context 被静态持有是常见原因",
        "冷启动：减少 Application.onCreate 耗时，使用 SplashScreen API",
        "布局优化：减少层级，用 Merge 标签，开启硬件加速",
        "Compose 优化：avoid 不必要重组，remember / derivedStateOf",
        "APK 体积：R8 混淆、资源压缩、App Bundle 按需分发"
    ),
    codeSnippet = """
// Compose 避免不必要重组
val expensiveValue by remember(key) {
    derivedStateOf { compute(key) }
}

// 冷启动 - 异步初始化非关键 SDK
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MainScope().launch(Dispatchers.IO) {
            Analytics.init(this@App)
        }
    }
}
    """.trimIndent(),
    tips = listOf(
        "用 Android Profiler 定位 CPU / Memory / Network 瓶颈",
        "Compose 中用 @Stable / @Immutable 优化重组",
        "冷启动目标 < 1秒；温启动 < 500ms"
    )
)

@Composable
fun PerformanceScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF9800),
        stageTitle = "进阶开发能力",
        onBack = onBack
    )
}
