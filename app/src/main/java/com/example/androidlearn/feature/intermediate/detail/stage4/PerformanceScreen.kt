package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 性能优化
 * 官方文档：https://developer.android.com/topic/performance
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  流畅度优化
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  卡顿排查 ─────────────────────────────────────────────────────────────
 *
 *  · 主线程超过 16ms 未完成绘制即产生卡顿（60fps = 16ms/帧）
 *  · Perfetto / Systrace：分析主线程耗时，定位卡顿原因
 *  · StrictMode：开发期检测主线程 IO 操作
 *
 * ── 1.2  布局优化 ─────────────────────────────────────────────────────────────
 *
 *  · 减少布局层级，用 ConstraintLayout 替代多层嵌套
 *  · 使用 <merge> 标签减少冗余根节点
 *  · 开启硬件加速（默认已开启）
 *
 * ── 1.3  Compose 重组优化 ─────────────────────────────────────────────────────
 *
 *  · 避免不必要重组：remember / derivedStateOf
 *  · @Stable / @Immutable：标记稳定类型，跳过重组
 *
 *  val expensiveValue by remember(key) {
 *      derivedStateOf { compute(key) }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  内存优化
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  内存泄漏 ─────────────────────────────────────────────────────────────
 *
 *  · LeakCanary：自动检测内存泄漏
 *  · 常见原因：Context 被静态持有、Handler 内部类、未注销监听器
 *
 * ── 2.2  内存分析工具 ─────────────────────────────────────────────────────────
 *
 *  · Android Profiler：实时查看内存占用
 *  · MAT（Memory Analyzer Tool）：分析 heap dump
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  启动速度优化
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  冷启动优化 ───────────────────────────────────────────────────────────
 *
 *  · 减少 Application.onCreate 耗时，使用 SplashScreen API
 *  · 异步初始化非关键 SDK
 *
 *  class App : Application() {
 *      override fun onCreate() {
 *          super.onCreate()
 *          MainScope().launch(Dispatchers.IO) {
 *              Analytics.init(this@App)   // 异步初始化
 *          }
 *      }
 *  }
 *
 * ── 3.2  Baseline Profile ─────────────────────────────────────────────────────
 *
 *  · 预编译关键代码路径，减少 JIT 编译时间
 *  · 冷启动目标 < 1秒；温启动 < 500ms
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  APK 体积优化
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · R8 混淆裁剪：移除未使用的代码和资源
 *  · 资源压缩：shrinkResources = true
 *  · App Bundle：按设备按需分发，减少下载体积
 *  · WebP：替换 PNG/JPG，体积减少 25-34%
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 用 Android Profiler 定位 CPU / Memory / Network 瓶颈
 *  · Compose 中用 @Stable / @Immutable 优化重组
 *  · 冷启动目标 < 1秒；温启动 < 500ms
 */

val performanceData = NoteData(
    title = "性能优化",
    subtitle = "进阶开发能力 · 卡顿 · 内存泄漏 · 启动速度",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("1",   "流畅度优化"),
        ChapterItem("1.1", "卡顿排查"),
        ChapterItem("1.2", "布局优化"),
        ChapterItem("1.3", "Compose 重组优化"),
        ChapterItem("2",   "内存优化"),
        ChapterItem("2.1", "内存泄漏"),
        ChapterItem("2.2", "内存分析工具"),
        ChapterItem("3",   "启动速度优化"),
        ChapterItem("3.1", "冷启动优化"),
        ChapterItem("3.2", "Baseline Profile"),
        ChapterItem("4",   "APK 体积优化"),
        ChapterItem("5",   "最佳实践"),
    )
)
