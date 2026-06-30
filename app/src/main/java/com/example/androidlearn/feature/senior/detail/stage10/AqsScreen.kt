package com.example.androidlearn.feature.senior.detail.stage10

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【AQS 并发框架】专属学习页
//  stageIndex=9, topicIndex=3
//  阶段颜色：粉红 0xFFE91E63（高级扩展 Stage 9）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "AQS 并发框架",
    description = "ReentrantLock、读写锁、CountDownLatch、CyclicBarrier、Semaphore",
    overview = "AQS（AbstractQueuedSynchronizer）是 Java 并发包的核心框架，ReentrantLock、CountDownLatch 等都基于它实现。理解 AQS 就掌握了 J.U.C 的精髓。",
    keyPoints = listOf(
        "AQS 核心：state 整型变量 + CLH 等待队列，子类通过 tryAcquire/tryRelease 定义语义",
        "ReentrantLock：可重入、可公平/非公平、支持 tryLock(timeout)、Condition 条件等待",
        "ReadWriteLock：读共享写独占，适合读多写少场景，state 高 16 位=读锁，低 16 位=写锁",
        "CountDownLatch：一次性，倒计时到 0 触发，常用于等待多线程任务完成",
        "CyclicBarrier：可重复使用，所有线程都到达屏障后统一放行",
        "Semaphore：许可证计数，控制最大并发数（连接池、限流）"
    ),
    codeSnippet = """
// ReentrantLock vs synchronized
val lock = ReentrantLock()

fun safeUpdate() {
    lock.lock()
    try {
        // 临界区
    } finally {
        lock.unlock()  // 必须在 finally 中释放
    }
}

// 尝试获取锁（避免死等）
if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
    try { /* 执行操作 */ } finally { lock.unlock() }
} else {
    // 获取锁超时，走降级逻辑
}

// Condition 精准唤醒（生产者-消费者）
val notFull = lock.newCondition()
val notEmpty = lock.newCondition()

fun produce(item: Any) {
    lock.lock()
    try {
        while (queue.isFull()) notFull.await()  // 等待不满
        queue.add(item)
        notEmpty.signal()  // 通知消费者
    } finally { lock.unlock() }
}

// CountDownLatch：等待多个任务完成
val latch = CountDownLatch(3)
repeat(3) { i ->
    thread {
        doTask(i)
        latch.countDown()  // 每完成一个 -1
    }
}
latch.await()  // 等待 count = 0

// Semaphore：限制并发连接数
val semaphore = Semaphore(10)  // 最多 10 个并发
fun accessResource() {
    semaphore.acquire()
    try { useResource() }
    finally { semaphore.release() }
}
    """.trimIndent(),
    tips = listOf(
        "ReentrantLock 一定要在 finally 中 unlock()，否则异常时锁永远不释放",
        "CountDownLatch 不可重用；CyclicBarrier 可以 reset() 重复使用",
        "面试题：AQS 如何实现公平锁？答：tryAcquire 时先检查队列是否有等待线程"
    )
)

@Composable
fun AqsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFE91E63),
        stageTitle = "并发编程深度",
        onBack = onBack
    )
}
