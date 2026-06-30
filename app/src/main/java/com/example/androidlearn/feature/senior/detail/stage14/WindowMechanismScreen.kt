package com.example.androidlearn.feature.senior.detail.stage14

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【Window 机制】专属学习页
//  stageIndex=13, topicIndex=3
//  阶段颜色：深蓝紫 0xFF3F51B5（系统核心原理）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "Window 机制",
    description = "WMS、WindowToken、Window 层级体系、Surface 与 SurfaceFlinger 合成渲染",
    overview = "Android 的 UI 渲染依赖 Window 体系。WindowManagerService（WMS）管理所有窗口的层级、位置和生命周期；Surface 是实际绘制画布；SurfaceFlinger 负责将多个 Surface 合成为最终屏幕帧。理解 Window 机制是解决悬浮窗、Dialog 崩溃、View 渲染异常等问题的关键。",
    keyPoints = listOf(
        "Window 类型：Application Window（Activity）/ Sub Window（Dialog、PopupWindow）/ System Window（Toast、状态栏）",
        "WindowToken：每个 Window 必须持有合法 Token，防止恶意窗口添加。Activity 的 Token 由 AMS 颁发",
        "ViewRootImpl：连接 View 树与 WMS 的桥梁，负责 measure/layout/draw 的触发和 Choreographer 调度",
        "Surface：每个 Window 对应一个 Surface（BufferQueue），App 在 Surface 上绘制，WMS 管理层级",
        "SurfaceFlinger：系统级合成服务，将各窗口 Surface 按 Z-order 合成，通过 HWC（硬件合成）输出到屏幕",
        "Vsync 信号：SurfaceFlinger 产生 Vsync 信号，Choreographer 监听后触发 UI 刷新，保证 60/120fps"
    ),
    codeSnippet = """
// 添加系统悬浮窗（需要 SYSTEM_ALERT_WINDOW 权限）
val wm = getSystemService(WINDOW_SERVICE) as WindowManager
val params = WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.WRAP_CONTENT,
    // Android 8+ 必须用 TYPE_APPLICATION_OVERLAY
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
).apply {
    gravity = Gravity.TOP or Gravity.START
    x = 100; y = 200
}
wm.addView(floatView, params)

// Window 层级体系（Z-order，数值越大越靠前）
// TYPE_BASE_APPLICATION = 1        // Activity 窗口
// TYPE_APPLICATION_PANEL = 1000    // PopupWindow / Spinner 下拉
// TYPE_APPLICATION_OVERLAY = 2038  // 应用可添加的最高层悬浮窗
// TYPE_STATUS_BAR = 2000           // 状态栏（系统专用）
// TYPE_CURSOR = 2000+              // 鼠标指针等（系统专用）

// ViewRootImpl 触发绘制流程
// Choreographer.doFrame() → ViewRootImpl.performTraversals()
//   → performMeasure() → performLayout() → performDraw()
//     → Surface.lockCanvas() → View.draw() → Surface.unlockCanvasAndPost()

// Dialog BadTokenException 根因：Window Token 已失效
// 解决：用 ApplicationContext 创建 Dialog 时改用 TYPE_APPLICATION_OVERLAY
// 或在 Activity.onDestroy() 前关闭 Dialog
    """.trimIndent(),
    tips = listOf(
        "PopupWindow 本质是 Sub Window，依附于 Activity 的 Window，Activity 销毁后 PopupWindow 必须手动 dismiss",
        "卡顿的本质是某帧绘制超过 16.6ms（60fps），导致 SurfaceFlinger 跳过该帧（掉帧）",
        "硬件加速（GPU 渲染）从 Android 3.0 引入，Android 4.0+ 默认开启，DisplayList 缓存避免重复绘制"
    )
)

@Composable
fun WindowMechanismScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "Android 系统核心原理",
        onBack = onBack
    )
}
