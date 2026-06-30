package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【ANR 排查与治理】专属学习页
//  stageIndex=4, topicIndex=3
//  阶段颜色：深橙 0xFFFF5722（高级 Stage 4）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "ANR 排查与治理",
    description = "主线程耗时分析，Systrace，ANR Watchdog，StrictMode，Trace 埋点",
    overview = "ANR（Application Not Responding）是 Android 中严重的用户体验问题，主线程超过 5s（Activity）或 10s（BroadcastReceiver）未响应即触发。系统化的 ANR 治理需要覆盖监控、分析、预防三个维度。",
    keyPoints = listOf(
        "ANR 触发条件：主线程 5s 无响应（输入事件）/ BroadcastReceiver 10s / Service 20s",
        "traces.txt 解读：系统在 /data/anr/traces.txt 记录发生 ANR 时各线程栈，main 线程的 WAITING/BLOCKED 是重点",
        "Systrace 分析：通过 atrace 采集 CPU 调度信息，可视化查看主线程帧率与阻塞",
        "StrictMode 检测：开发阶段开启，强制暴露主线程磁盘 I/O / 网络请求等违规操作",
        "ANR Watchdog：第三方库，子线程定时向主线程 post 任务，超时未执行则主动上报",
        "Trace 埋点：Trace.beginSection / endSection 精确定位各业务代码耗时分布"
    ),
    codeSnippet = """
// StrictMode 配置（仅调试）
if (BuildConfig.DEBUG) {
    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyLog()
            .build()
    )
}

// Trace 埋点定位耗时
Trace.beginSection("LoadUserData")
val user = userRepository.getUser() // 同步操作
Trace.endSection()

// 自定义 ANR Watchdog（简化版）
class AnrWatchdog(private val timeout: Long = 5000L) : Thread() {
    private var tick = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun run() {
        while (!isInterrupted) {
            val prevTick = tick
            handler.post { tick++ }
            sleep(timeout)
            if (prevTick == tick) {
                // 主线程未响应，上报堆栈
                val trace = Looper.getMainLooper().thread.stackTrace
                reportAnr(trace)
            }
        }
    }
}

// traces.txt 分析要点：
// "main" prio=5 tid=1 Blocked
//   at com.example.Foo.bar(Foo.kt:42)   ← 主线程卡住的位置
//   waiting to lock <0x...> held by thread 15  ← 锁被其他线程持有
    """.trimIndent(),
    tips = listOf(
        "线上 ANR 优先查 traces.txt 的 main 线程状态：Blocked 说明有死锁，Native 说明 JNI 阻塞",
        "避免在主线程做：SharedPreferences.commit()、大量 View 操作、同步 IPC 调用",
        "使用 Perfetto 替代 Systrace，支持更长时间采集和更细粒度分析"
    )
)

@Composable
fun AnrScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
