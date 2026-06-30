package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "稳定性监控体系",
    description = "Crash/ANR 监控，Firebase Crashlytics，线下排查工具",
    overview = "线上稳定性是 App 质量的底线，需建立从采集、聚合、告警到复现的完整监控链路，将崩溃率控制在 0.1% 以内。",
    keyPoints = listOf(
        "崩溃分类：Java Crash、Native Crash（信号捕获）、ANR（主线程超时）",
        "Firebase Crashlytics：自动捕获崩溃，符号化还原，非致命异常上报",
        "ANR 分析：/data/anr/traces.txt，主线程调用栈，Watchdog 机制",
        "自定义 Thread.UncaughtExceptionHandler：崩溃前写日志到磁盘",
        "BlockCanary / ANR-WatchDog：线下实时检测主线程阻塞",
        "Matrix（微信）：综合 APM 框架，覆盖卡顿/内存/IO 等多维度"
    ),
    codeSnippet = """
// 全局崩溃处理 + 写日志
class CrashHandler : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            val log = Log.getStackTraceString(e)
            File(context.filesDir, "crash.log").appendText(log)
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            defaultHandler?.uncaughtException(t, e)
        }
    }
}

// Application.onCreate 中注册
Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
    """.trimIndent(),
    tips = listOf(
        "崩溃率 = 崩溃 session 数 / 总 session 数，关注 P50/P95 设备分布",
        "Native 崩溃需上传符号表（.so + breakpad）才能还原堆栈",
        "ANR 根因 90% 是主线程做了 I/O、锁等待或 Binder 调用"
    )
)

@Composable
fun StabilityMonitorScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
