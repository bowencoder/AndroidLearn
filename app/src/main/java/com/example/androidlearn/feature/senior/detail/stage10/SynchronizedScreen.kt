package com.example.androidlearn.feature.senior.detail.stage10

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【synchronized 锁机制】专属学习页
//  stageIndex=9, topicIndex=1
//  阶段颜色：粉红 0xFFE91E63（高级扩展 Stage 9）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "synchronized 锁机制",
    description = "CPU物理核架构、Monitor对象、锁升级(偏向→轻量→重量)、临界区",
    overview = "synchronized 是 Java 最基础的互斥锁，通过 Monitor 对象实现。JVM 针对低竞争场景进行了偏向锁、轻量级锁优化，理解锁升级是排查锁性能问题的关键。",
    keyPoints = listOf(
        "CPU 物理核架构：多核共享 L3 缓存，缓存一致性协议（MESI）保证数据同步",
        "Monitor 对象：每个 Java 对象关联一个 Monitor，monitorenter/monitorexit 字节码",
        "偏向锁：无竞争时记录线程 ID 到 markword，无需 CAS，效率最高",
        "轻量级锁：有竞争时升级，CAS 将锁记录写入 markword，失败则再升级",
        "重量级锁：竞争激烈时升级，阻塞等待，操作系统级别的 mutex",
        "锁只能升级不能降级（JVM 默认），偏向锁撤销有一定开销"
    ),
    codeSnippet = """
// synchronized 同步块
class Counter {
    private var count = 0

    // 方法级锁：锁定 this 对象
    @Synchronized
    fun increment() { count++ }

    // 代码块级锁：更细粒度
    fun incrementBlock() {
        synchronized(this) { count++ }
    }

    // 静态方法锁定 Class 对象
    companion object {
        @Synchronized
        fun staticMethod() { /* 锁定 Counter::class.java */ }
    }
}

// 锁升级过程（markword 状态变化）
// 初始对象：markword = 无锁（01）
// 首次加锁：偏向锁（01），记录线程 ID
// 第二个线程竞争：轻量级锁（00），CAS 自旋
// 自旋失败：重量级锁（10），阻塞等待

// 避免锁粒度过大
class OrderService {
    private val orderLock = Object()  // 专用锁对象，不用 this
    private val inventoryLock = Object()

    fun createOrder() {
        synchronized(orderLock) { /* 只锁订单操作 */ }
        synchronized(inventoryLock) { /* 只锁库存操作 */ }
        // 比 synchronized(this) 并发度高很多
    }
}
    """.trimIndent(),
    tips = listOf(
        "JDK 15+ 默认禁用偏向锁（撤销开销大于收益），高并发服务可加 -XX:-UseBiasedLocking",
        "synchronized 可重入：同一线程多次获取同一把锁不会死锁",
        "死锁四条件：互斥、占有等待、不可剥夺、循环等待，破坏任一即可"
    )
)

@Composable
fun SynchronizedScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFE91E63),
        stageTitle = "并发编程深度",
        onBack = onBack
    )
}
