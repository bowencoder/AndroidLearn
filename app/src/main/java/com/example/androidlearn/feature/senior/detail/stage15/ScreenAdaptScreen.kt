package com.example.androidlearn.feature.senior.detail.stage15

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【屏幕适配】专属学习页
//  stageIndex=14, topicIndex=0
//  阶段颜色：橙红 0xFFFF5722（UI 进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "屏幕适配",
    description = "dp/px/dpi 关系、今日头条方案、smallestWidth 方案与折叠屏/大屏适配",
    overview = "Android 碎片化严重，屏幕尺寸/密度各异。屏幕适配的目标是让 UI 在不同设备上呈现一致的视觉比例。核心概念是 dp（与密度无关的像素）和 sp（可随字体缩放），主流适配方案有 smallestWidth 限定符方案和今日头条 px 转换方案。",
    keyPoints = listOf(
        "px = dp × (dpi / 160)；DisplayMetrics.density = dpi / 160（如 xxhdpi density=3）",
        "sp：与 dp 类似，但会随用户字体大小设置缩放（scaledDensity），应始终用于文字尺寸",
        "smallestWidth 方案：为不同 sw（最小宽度）生成对应 values-swXXXdp/dimens.xml，覆盖主流机型",
        "今日头条方案：修改 DisplayMetrics.density = 设计稿宽度 / 屏幕实际宽度px，使 1dp = 1设计稿px",
        "折叠屏适配：使用 WindowSizeClass 区分 Compact/Medium/Expanded，配合 SlidingPaneLayout 或 NavRail 布局",
        "刘海屏/挖孔屏：使用 WindowInsets 处理状态栏/导航栏/刘海区域，避免内容被遮挡"
    ),
    codeSnippet = """
// 今日头条适配方案（修改 density）
// 设计稿宽度 375dp（iPhone 设计稿），以屏幕宽度换算
object ScreenAdaptUtil {
    private var appDensity = 0f
    private var appScaledDensity = 0f

    fun setCustomDensity(activity: Activity, application: Application) {
        val appDisplayMetrics = application.resources.displayMetrics
        if (appDensity == 0f) {
            appDensity = appDisplayMetrics.density
            appScaledDensity = appDisplayMetrics.scaledDensity
            // 监听字体大小变化
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        appScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }
                override fun onLowMemory() {}
            })
        }
        val targetDensity = appDisplayMetrics.widthPixels / 375f  // 设计稿宽 375dp
        val targetScaledDensity = targetDensity * (appScaledDensity / appDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }
}

// WindowSizeClass（折叠屏/平板适配，Jetpack）
val windowSizeClass = calculateWindowSizeClass(this)
when (windowSizeClass.widthSizeClass) {
    WindowWidthSizeClass.Compact  -> { /* 手机竖屏：单栏布局 */ }
    WindowWidthSizeClass.Medium   -> { /* 折叠屏展开/小平板：双栏或 NavRail */ }
    WindowWidthSizeClass.Expanded -> { /* 大平板：三栏布局 */ }
}

// 处理刘海屏（WindowInsets）
ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
    val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
    insets
}
    """.trimIndent(),
    tips = listOf(
        "今日头条方案会影响全局 density，与三方库可能冲突；smallestWidth 方案更安全但文件数量多",
        "字体尺寸始终使用 sp，不要强制 sp == dp（用户无障碍功能依赖字体缩放）",
        "在 AndroidManifest 中声明 android:resizeableActivity 和 android:supportsPictureInPicture 以支持分屏和 PIP"
    )
)

@Composable
fun ScreenAdaptScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "Android UI 进阶",
        onBack = onBack
    )
}
