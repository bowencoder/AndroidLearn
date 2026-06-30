package com.example.androidlearn.feature.senior.detail.stage11

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【内存溢出与分析工具】专属学习页
//  stageIndex=10, topicIndex=3
//  阶段颜色：深青 0xFF009688（高级扩展 Stage 10）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "内存溢出与分析工具",
    description = "OOM 分类、MAT 堆快照分析、Profiler 实战、MinorGC/FullGC 日志解读",
    overview = "OOM 是 Android 线上最致命的问题之一。掌握 MAT（Memory Analyzer Tool）和 Android Profiler 的使用，能快速定位内存泄漏根因并制定修复方案。",
    keyPoints = listOf(
        "OOM 分类：Java 堆 OOM / 内存泄漏导致 OOM / 图片加载 OOM / 线程过多 OOM",
        "Heap Dump：adb shell am dumpheap 或 Profiler 导出 .hprof 文件",
        "MAT 分析：Dominator Tree（最大对象树）/ Histogram（对象计数）/ Leak Suspects（泄漏建议）",
        "Shallow Size vs Retained Size：浅大小=对象本身；保留大小=释放该对象能回收的总内存",
        "GC 日志解读：GC freed X objects(Yk) ... / Explicit GC / Background GC",
        "onTrimMemory()：系统内存不足时回调，主动释放缓存避免被 kill"
    ),
    codeSnippet = """
// 主动监控和响应内存压力
class MyApplication : Application() {

    private val imageCache = LruCache<String, Bitmap>(calculateCacheSize())

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            // App 在前台，系统内存开始紧张
            TRIM_MEMORY_RUNNING_LOW -> {
                imageCache.trimToSize(imageCache.maxSize / 2)
                Log.w("Memory", "内存紧张，缓存减半")
            }
            // App 在前台，内存极度不足
            TRIM_MEMORY_RUNNING_CRITICAL -> {
                imageCache.evictAll()
                Log.w("Memory", "内存极度不足，清空缓存")
            }
            // App 退到后台
            TRIM_MEMORY_UI_HIDDEN -> {
                imageCache.trimToSize(imageCache.maxSize / 4)
            }
            // 系统即将 kill 后台进程
            TRIM_MEMORY_COMPLETE -> {
                imageCache.evictAll()
            }
        }
    }

    private fun calculateCacheSize(): Int {
        // 使用 JVM 最大内存的 1/8 作为图片缓存
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        return maxMemory / 8
    }
}

// MAT 常用分析步骤：
// 1. Profiler → Memory → Dump Heap → 导出 .hprof
// 2. Android Studio：Convert to standard .hprof format
// 3. MAT 打开：Window → Heap Dump Details
// 4. Dominator Tree：找 Retained Size 最大的对象
// 5. 右键 → Path to GC Roots → 查看引用链
// 6. 找到最终持有者（通常是 Context/静态字段/线程）
    """.trimIndent(),
    tips = listOf(
        "MAT 的 Leak Suspects 报告能自动识别 90% 的常见泄漏模式，是分析起点",
        "Retained Size 比 Shallow Size 更重要：它代表释放该对象能回收多少内存",
        "图片 OOM 用 Glide/Coil 自动管理，不要手动 new Bitmap；大图用 inSampleSize 缩放"
    )
)

@Composable
fun OomAnalysisScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF009688),
        stageTitle = "Android 虚拟机原理",
        onBack = onBack
    )
}
