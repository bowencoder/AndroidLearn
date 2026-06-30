package com.example.androidlearn.feature.senior.detail.stage7

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "线程同步与并发原理",
    description = "synchronized / volatile，CAS，Java 内存模型，协程调度",
    overview = "并发是面试高频考点，也是实际开发中最容易出错的领域。理解 JMM、锁机制和协程调度器，才能写出正确高效的并发代码。",
    keyPoints = listOf(
        "JMM（Java Memory Model）：主内存与工作内存，happens-before 规则",
        "volatile：保证可见性 + 禁止指令重排，但不保证原子性",
        "synchronized：互斥锁，保证原子性 + 可见性 + 有序性",
        "CAS（Compare And Swap）：无锁乐观并发，AtomicInteger / AtomicReference",
        "ReentrantLock：可重入锁，支持公平/非公平、tryLock、Condition",
        "协程调度器：Dispatchers.IO（64 线程池）/ Default（CPU 核心数）/ Main（主线程）"
    ),
    codeSnippet = """
// 双重检查锁单例（正确写法）
class Singleton private constructor() {
    companion object {
        @Volatile  // 保证可见性，禁止重排导致返回未初始化对象
        private var instance: Singleton? = null

        fun getInstance(): Singleton {
            return instance ?: synchronized(this) {
                instance ?: Singleton().also { instance = it }
            }
        }
    }
}

// 使用 CAS 无锁计数器
val counter = AtomicInteger(0)
fun increment() = counter.incrementAndGet()

// 协程中用 Mutex 替代 synchronized
val mutex = Mutex()
var sharedState = 0

suspend fun safeIncrement() {
    mutex.withLock {
        sharedState++  // 协程挂起期间不持有线程，比 synchronized 更高效
    }
}

// Flow 的线程安全收集
viewModelScope.launch {
    flow.flowOn(Dispatchers.IO)   // 生产者在 IO 线程
        .collect { value ->       // 消费者在 Main 线程
            updateUi(value)
        }
}
    """.trimIndent(),
    tips = listOf(
        "@Volatile 的双重检查锁是 Kotlin 单例的标准写法，必须掌握",
        "协程中避免使用 synchronized，改用 Mutex 或 Channel 保证并发安全",
        "面试常见题：volatile 为什么不能保证原子性？答：i++ 是三步操作（读-改-写）"
    )
)

@Composable
fun ThreadSyncScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF795548),
        stageTitle = "底层原理与面试重点",
        onBack = onBack
    )
}
