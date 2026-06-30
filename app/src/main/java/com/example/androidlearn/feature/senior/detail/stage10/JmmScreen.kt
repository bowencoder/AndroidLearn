package com.example.androidlearn.feature.senior.detail.stage10

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【JMM 并发内存模型】专属学习页
//  stageIndex=9, topicIndex=0
//  阶段颜色：粉红 0xFFE91E63（高级扩展 Stage 9）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "JMM 并发内存模型",
    description = "主内存与工作内存、happens-before、volatile 可见性与有序性",
    overview = "Java Memory Model（JMM）定义了多线程程序中内存访问的规则，是理解 volatile、synchronized 和并发 Bug 的理论基础。",
    keyPoints = listOf(
        "主内存与工作内存：线程有本地缓存副本，写回主内存才对其他线程可见",
        "happens-before：保证操作顺序的规则集，满足即可保证可见性和有序性",
        "volatile 可见性：写操作立即刷新到主内存；读操作从主内存读取最新值",
        "volatile 有序性：禁止编译器和 CPU 对 volatile 前后指令重排序",
        "volatile 局限：不保证原子性（i++ 是三步操作：读-改-写）",
        "指令重排序：CPU 为提升效率会乱序执行，happens-before 规则限制了重排边界"
    ),
    codeSnippet = """
// volatile 保证可见性
class Singleton private constructor() {
    companion object {
        @Volatile  // 禁止重排：new Singleton() 可能先返回引用再执行构造体
        private var instance: Singleton? = null

        fun get() = instance ?: synchronized(this) {
            instance ?: Singleton().also { instance = it }
        }
    }
}

// volatile 不保证原子性演示
var counter = 0

fun increment() {
    counter++  // 非原子！= 读取 + 加1 + 写回 三步
}

// 正确做法：使用 AtomicInteger
val atomicCounter = AtomicInteger(0)
fun safeIncrement() = atomicCounter.incrementAndGet()

// happens-before 关系举例
// 1. 线程 start() 前的写对启动后的线程可见
// 2. synchronized 的 unlock() happens-before 随后的 lock()
// 3. volatile 写 happens-before 随后的 volatile 读
// 4. 线程结束 happens-before join() 返回
    """.trimIndent(),
    tips = listOf(
        "volatile 双重检查锁是 Kotlin 单例标准写法，缺了 @Volatile 会出现半初始化对象",
        "i++ 看起来一步，实际是 getfield/iadd/putfield 三条字节码指令，非原子",
        "面试必问：volatile 和 synchronized 的区别（可见性/有序性 vs 原子性/互斥）"
    )
)

@Composable
fun JmmScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFE91E63),
        stageTitle = "并发编程深度",
        onBack = onBack
    )
}
