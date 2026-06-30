package com.example.androidlearn.feature.senior.detail.stage10

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【线程池深度原理】专属学习页
//  stageIndex=9, topicIndex=4
//  阶段颜色：粉红 0xFFE91E63（高级扩展 Stage 9）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "线程池深度原理",
    description = "ThreadPoolExecutor 参数、排队机制、阻塞队列、优先级线程池实战",
    overview = "线程池是 Android 异步任务的核心基础设施，理解 ThreadPoolExecutor 的 7 个参数和任务排队机制，才能针对不同场景选择合适的配置。",
    keyPoints = listOf(
        "7 个核心参数：corePoolSize、maximumPoolSize、keepAliveTime、unit、workQueue、threadFactory、handler",
        "任务处理流程：线程数 < core → 创建线程；core 已满 → 入队；队列满且 < max → 创建线程；max 也满 → 执行拒绝策略",
        "阻塞队列类型：LinkedBlockingQueue（无界）/ ArrayBlockingQueue（有界）/ SynchronousQueue（直传）/ PriorityBlockingQueue（优先级）",
        "拒绝策略：AbortPolicy（抛异常）/ CallerRunsPolicy（调用者执行）/ DiscardPolicy（丢弃）",
        "线程数设置经验：IO 密集 = CPU核数 * 2；CPU 密集 = CPU核数 + 1",
        "异步模式之工作线程（Worker Thread）：避免线程饥饿，设置合理超时清理空闲线程"
    ),
    codeSnippet = """
// 自定义线程池（推荐方式，禁止直接用 Executors.newFixedThreadPool）
val threadPool = ThreadPoolExecutor(
    4,                          // corePoolSize：常驻线程数
    8,                          // maximumPoolSize：最大线程数
    60L, TimeUnit.SECONDS,      // 空闲线程存活时间
    LinkedBlockingQueue(100),   // 有界队列，防止 OOM
    Executors.defaultThreadFactory(),
    ThreadPoolExecutor.CallerRunsPolicy()  // 队列满时调用者自己执行
)

// 优先级线程池实战
data class PriorityTask(
    val priority: Int,
    val task: Runnable
) : Comparable<PriorityTask> {
    override fun compareTo(other: PriorityTask) =
        other.priority - this.priority  // 优先级高的先执行
}

val priorityPool = ThreadPoolExecutor(
    2, 4, 60L, TimeUnit.SECONDS,
    PriorityBlockingQueue<Runnable>()  // 优先级队列
)

// Android 中推荐用协程替代手动线程池
viewModelScope.launch(Dispatchers.IO) {
    // Dispatchers.IO 内部维护了一个线程池（最多 64 个线程）
    val result = fetchData()
    withContext(Dispatchers.Main) { updateUi(result) }
}
    """.trimIndent(),
    tips = listOf(
        "Executors.newFixedThreadPool 使用无界队列，高负载时会 OOM，生产环境禁用",
        "corePoolSize 不是越大越好，线程切换有开销，IO 密集任务参考 CPU核*2",
        "Android 中优先用 Kotlin 协程 + Dispatchers.IO，自动管理线程池"
    )
)

@Composable
fun ThreadPoolScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFE91E63),
        stageTitle = "并发编程深度",
        onBack = onBack
    )
}
