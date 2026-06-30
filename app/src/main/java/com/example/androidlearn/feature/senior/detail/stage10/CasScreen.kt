package com.example.androidlearn.feature.senior.detail.stage10

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【CAS 与无锁并发】专属学习页
//  stageIndex=9, topicIndex=2
//  阶段颜色：粉红 0xFFE91E63（高级扩展 Stage 9）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "CAS 与无锁并发",
    description = "CAS 原理与 ABA 问题、Atomic 类、LongAdder、无锁并发策略",
    overview = "CAS（Compare And Swap）是 CPU 级别的原子操作，是实现无锁并发数据结构的基础，比 synchronized 在低竞争场景下性能更好。",
    keyPoints = listOf(
        "CAS 原语：compareAndSwap(内存地址, 期望值, 新值)，CPU 指令级原子操作",
        "ABA 问题：值从 A→B→A，CAS 无法感知中间变化，用 AtomicStampedReference 解决",
        "Unsafe 类：Java 直接操作内存的后门，Atomic 类内部通过 Unsafe 实现 CAS",
        "AtomicInteger/AtomicLong/AtomicReference：基于 CAS 的原子类",
        "LongAdder：分段累加（Cell 数组），高并发下性能远优于 AtomicLong",
        "自旋等待：CAS 失败后循环重试（自旋），高竞争时会浪费 CPU"
    ),
    codeSnippet = """
// AtomicInteger 源码核心（简化）
class AtomicInteger {
    @Volatile private var value: Int = 0

    fun incrementAndGet(): Int {
        while (true) {
            val current = value
            val next = current + 1
            // CAS：只有 value 还是 current 时才更新为 next
            if (compareAndSet(current, next)) return next
            // 失败则重试（自旋）
        }
    }
}

// ABA 问题解决：AtomicStampedReference（带版本号）
val ref = AtomicStampedReference("A", 0)  // value=A, stamp=0
// 即使值改回 "A"，stamp 也变了，CAS 会失败
ref.compareAndSet("A", "B", 0, 1)

// LongAdder vs AtomicLong（高并发计数器选 LongAdder）
val counter = LongAdder()
// 多线程并发递增
repeat(1000) {
    thread { counter.increment() }
}
println(counter.sum())  // 最终汇总所有 Cell 的值

// 无锁并发 vs 有锁的选择原则
// 低竞争（读多写少）→ CAS / ReadWriteLock
// 高竞争（写多） → synchronized / ReentrantLock
// 计数统计 → LongAdder
// 简单原子操作 → AtomicInteger/AtomicLong
    """.trimIndent(),
    tips = listOf(
        "CAS 不是万能的：高竞争下自旋浪费 CPU，反而不如 synchronized（会让线程休眠）",
        "LongAdder 在高并发写时比 AtomicLong 快 5-10 倍，但求和有一定误差窗口",
        "面试题：CAS 为什么能保证原子性？答：CPU cmpxchg 指令 + LOCK 前缀，总线锁/缓存锁"
    )
)

@Composable
fun CasScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFE91E63),
        stageTitle = "并发编程深度",
        onBack = onBack
    )
}
