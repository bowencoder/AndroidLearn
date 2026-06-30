package com.example.androidlearn.feature.senior.detail.stage11

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【类与对象内存结构】专属学习页
//  stageIndex=10, topicIndex=2
//  阶段颜色：深青 0xFF009688（高级扩展 Stage 10）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "类与对象内存结构",
    description = "klass 内存分布、markword 数据分析、对象分配过程、逃逸分析",
    overview = "每个 Java/Kotlin 对象在内存中由对象头（Header）+ 实例数据（Instance Data）+ 对齐填充（Padding）三部分组成，理解对象内存结构是优化内存占用的基础。",
    keyPoints = listOf(
        "对象头（Object Header）：markword（8字节）+ klass pointer（4/8字节）",
        "markword：存储锁状态/GC年龄/hashCode等，64位系统占 8 字节，随锁状态变化",
        "klass pointer：指向方法区中的类元数据（Class 对象），描述类型信息",
        "方法表（vtable）：类的虚方法表，实现多态，子类方法覆盖父类对应槽位",
        "对象分配：TLAB（Thread-Local Allocation Buffer）无锁快速分配 → 失败则 CAS → 大对象直接进 Old 区",
        "逃逸分析：JVM 判断对象是否逃出方法范围，未逃逸则栈上分配（避免 GC）"
    ),
    codeSnippet = """
// 用 JOL（Java Object Layout）查看对象内存布局
// 依赖：org.openjdk.jol:jol-core:0.17

// 输出示例（64位 JVM，压缩指针开启）：
// java.lang.Object object internals:
// OFF  SZ   TYPE DESCRIPTION
//   0   8        (object header: mark)
//   8   4        (object header: class)
//  12   4        (object alignment gap)
// Instance size: 16 bytes

// data class 对象内存估算
data class Point(val x: Int, val y: Int)
// 对象头：12 字节（4字节klass + 8字节markword，开启指针压缩）
// 实例数据：x(4) + y(4) = 8 字节
// 对齐填充：0 字节（已是 8 的倍数）
// 总计：20 → 对齐为 24 字节

// 逃逸分析示例
fun createPoint(): Int {
    val p = Point(1, 2)  // p 没有逃出方法，JVM 可在栈上分配
    return p.x + p.y     // 甚至可以标量替换，直接用 1 和 2 计算
}

// 内存布局优化建议
// 字段顺序随意（差）
class Bad { val a: Boolean = true; val b: Long = 0L; val c: Boolean = false }
// 布局：a(1) + pad(7) + b(8) + c(1) + pad(7) = 24 字节

// 按类型大小从大到小排列（好），减少 padding
class Good { val b: Long = 0L; val a: Boolean = true; val c: Boolean = false }
// 布局：b(8) + a(1) + c(1) + pad(6) = 16 字节（节省 8 字节！）
    """.trimIndent(),
    tips = listOf(
        "用 Android Profiler 的 Heap Dump 查看每个对象的 shallow size 和 retained size",
        "逃逸分析是 JIT 优化，Android ART 支持，但不像服务端 JVM 那么激进",
        "面试题：new Object() 在内存中占多少字节？答：16 字节（12 字节头 + 4 字节对齐）"
    )
)

@Composable
fun ObjectStructureScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF009688),
        stageTitle = "Android 虚拟机原理",
        onBack = onBack
    )
}
