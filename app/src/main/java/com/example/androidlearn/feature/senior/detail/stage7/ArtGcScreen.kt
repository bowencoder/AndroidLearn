package com.example.androidlearn.feature.senior.detail.stage7

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "ART 内存管理与 GC",
    description = "堆分区，GC 算法，OOM 分析，Large Object Space",
    overview = "ART（Android Runtime）采用分代 GC，理解堆布局和 GC 触发条件，是解决 OOM 和内存抖动的根本。",
    keyPoints = listOf(
        "堆分区：Young Generation（Eden + Survivor）→ Old Generation（Tenured）→ Large Object Space",
        "GC 算法：CMS（并发标记清除）→ Android 9+ 改为 CC（并发复制）",
        "GC Root：栈变量、静态字段、JNI 全局引用，可达即不回收",
        "内存抖动：大量短命对象频繁触发 GC，导致帧率抖动",
        "Large Object Space：> 12KB 对象直接分配到 LOS，GC 单独处理",
        "OOM 分析：Heap Dump → MAT / Android Profiler 分析引用链"
    ),
    codeSnippet = """
// 监控内存使用，主动在低内存时释放缓存
class MyApplication : Application() {

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            TRIM_MEMORY_RUNNING_CRITICAL,
            TRIM_MEMORY_COMPLETE -> {
                // 系统内存极低，主动释放图片缓存、非必要单例
                ImageCache.getInstance().evictAll()
                Log.w("Memory", "Memory critically low, cache cleared")
            }
            TRIM_MEMORY_UI_HIDDEN -> {
                // App 进入后台，释放 UI 相关资源
                ImageCache.getInstance().trimToSize(
                    ImageCache.getInstance().maxSize / 2
                )
            }
        }
    }
}

// 避免内存抖动 - 复用对象而非频繁 new
class ParticleSystem {
    // 使用对象池避免每帧都创建 Particle 对象
    private val pool = ArrayDeque<Particle>(100)
    fun acquire() = pool.removeFirstOrNull() ?: Particle()
    fun recycle(p: Particle) { p.reset(); pool.addFirst(p) }
}
    """.trimIndent(),
    tips = listOf(
        "Android Profiler 的 Memory 选项卡可实时看到堆大小和 GC 频率",
        "Heap Dump 后用 MAT 的 Dominator Tree 找占用最多内存的对象",
        "避免在循环或 onDraw 中创建对象，提前分配好复用"
    )
)

@Composable
fun ArtGcScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF795548),
        stageTitle = "底层原理与面试重点",
        onBack = onBack
    )
}
