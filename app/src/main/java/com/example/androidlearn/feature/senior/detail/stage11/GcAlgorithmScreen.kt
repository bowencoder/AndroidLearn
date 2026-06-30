package com.example.androidlearn.feature.senior.detail.stage11

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【GC 垃圾回收算法】专属学习页
//  stageIndex=10, topicIndex=1
//  阶段颜色：深青 0xFF009688（高级扩展 Stage 10）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "GC 垃圾回收算法",
    description = "GCRoot 算法、标记-复制-整理-分代回收、7 种 GC 收集器对比",
    overview = "GC 是 JVM/ART 内存管理的核心，理解 GCRoot 可达性分析、各种回收算法的优劣，以及 Android ART 采用的并发复制（CC）算法，是解决 OOM 和 GC 停顿问题的基础。",
    keyPoints = listOf(
        "引用计数（旧方案）：无法解决循环引用，Python 用此方案；Java/ART 不用",
        "GCRoot 可达性分析：从根（栈变量/静态字段/JNI 引用）出发，不可达即垃圾",
        "标记-清除（Mark-Sweep）：碎片化严重，CMS 使用此算法",
        "标记-复制（Mark-Copy）：将存活对象复制到 To 区，无碎片，适合短命对象（Young 区）",
        "标记-整理（Mark-Compact）：将存活对象移到一端，无碎片，适合 Old 区",
        "Android ART CC（并发复制）：Android 8+ 默认，GC 与应用并发执行，减少 STW"
    ),
    codeSnippet = """
// GCRoot 类型（这些对象不会被回收）
// 1. 线程栈中的局部变量
// 2. 静态字段（Static Fields）
// 3. JNI 全局引用
// 4. 活跃的 Java 线程本身

// 常见内存泄漏 = 垃圾对象被 GCRoot 持有
class LeakExample : Activity() {

    companion object {
        // 静态字段持有 Activity → GCRoot 引用链，永不被回收
        var leakedActivity: Activity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leakedActivity = this  // 泄漏！
    }
}

// 四种引用强度（控制 GC 行为）
val strong = "强引用"          // 永不回收
val soft = SoftReference("软引用")    // 内存不足时回收（适合图片缓存）
val weak = WeakReference("弱引用")    // GC 时立即回收（WeakHashMap）
val phantom = PhantomReference("虚引用", ReferenceQueue())  // 跟踪回收通知

// 触发 GC 的时机（ART）
// Minor GC：Young 区满时，回收短命对象（频繁，STW 短）
// Major GC：Old 区满时，回收长命对象（不频繁，STW 长）
// Full GC：完整堆回收，Android 尽量避免

// LeakCanary 工作原理：监听 onDestroy → WeakReference → ReferenceQueue
// 若 Activity 销毁后仍不在队列中 → 内存泄漏
    """.trimIndent(),
    tips = listOf(
        "ART CC 算法让 GC 与业务代码并发执行，大幅减少了 STW（Stop The World）停顿",
        "软引用适合内存缓存：内存充足时保留，内存紧张时自动释放，比 LruCache 更安全",
        "Log 中看到 GC freed 频繁出现 → 内存抖动，检查是否在循环中大量创建对象"
    )
)

@Composable
fun GcAlgorithmScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF009688),
        stageTitle = "Android 虚拟机原理",
        onBack = onBack
    )
}
