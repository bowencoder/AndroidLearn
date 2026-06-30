package com.example.androidlearn.feature.senior.detail.stage15

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【动画进阶】专属学习页
//  stageIndex=14, topicIndex=3
//  阶段颜色：橙红 0xFFFF5722（UI 进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "动画进阶",
    description = "属性动画原理、插值器/估值器、Lottie 矢量动画与 Compose 动画 API",
    overview = "Android 动画分为视图动画（补间动画）、帧动画、属性动画（ValueAnimator/ObjectAnimator）三大类。属性动画是现代 Android 开发的标准，通过修改对象的真实属性实现动画效果。Jetpack Compose 提供了更声明式的动画 API。",
    keyPoints = listOf(
        "属性动画原理：ValueAnimator 按时间进度（0~1）→ 插值器（非线性映射）→ 估值器（计算属性值）→ 监听器更新 UI",
        "插值器（Interpolator）：控制动画节奏，如 AccelerateDecelerateInterpolator（先加速后减速）、OvershootInterpolator（回弹）",
        "估值器（TypeEvaluator）：将 0~1 的 fraction 映射为实际属性值，默认 IntEvaluator/FloatEvaluator，可自定义（如 ArgbEvaluator 颜色渐变）",
        "ObjectAnimator：针对对象的具体属性（translationX、alpha、scaleX 等）的动画，必须有对应的 getter/setter",
        "AnimatorSet：组合多个动画，支持顺序（playSequentially）、同时（playTogether）和依赖（after/before/with）",
        "Compose 动画：animate*AsState（状态驱动）、AnimatedVisibility（显隐动画）、Transition（多属性同步动画）"
    ),
    codeSnippet = """
// ValueAnimator 基础（最底层动画 API）
ValueAnimator.ofFloat(0f, 1f).apply {
    duration = 500
    interpolator = OvershootInterpolator()
    addUpdateListener { animator ->
        val fraction = animator.animatedValue as Float
        view.alpha = fraction
        view.translationY = (1 - fraction) * 100f
    }
    start()
}

// ObjectAnimator（直接操作 View 属性）
ObjectAnimator.ofFloat(view, "translationX", 0f, 200f).apply {
    duration = 300
    repeatMode = ValueAnimator.REVERSE
    repeatCount = 1
    start()
}

// AnimatorSet 组合动画
val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
val slideUp = ObjectAnimator.ofFloat(view, "translationY", 200f, 0f)
AnimatorSet().apply {
    playTogether(fadeIn, slideUp)   // 同时执行
    duration = 400
    interpolator = DecelerateInterpolator()
    start()
}

// 自定义 TypeEvaluator（动画自定义对象）
data class Point(val x: Float, val y: Float)
val pointEvaluator = TypeEvaluator<Point> { fraction, start, end ->
    Point(
        start.x + fraction * (end.x - start.x),
        start.y + fraction * (end.y - start.y)
    )
}
ValueAnimator.ofObject(pointEvaluator, Point(0f, 0f), Point(300f, 500f)).start()

// Jetpack Compose 动画（animate*AsState）
val isExpanded by remember { mutableStateOf(false) }
val height by animateDpAsState(
    targetValue = if (isExpanded) 200.dp else 60.dp,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "height"
)
Box(Modifier.height(height)) { ... }

// AnimatedVisibility（显隐动画）
AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn() + slideInVertically(),
    exit = fadeOut() + slideOutVertically()
) {
    Card { ... }
}
    """.trimIndent(),
    tips = listOf(
        "属性动画修改的是对象的真实属性，而补间动画只改变绘制效果（View 实际位置不变），点击区域不随动画移动",
        "Lottie 是 Airbnb 开源的矢量动画库，将 AE 导出的 JSON 在 Android 上实时渲染，适合复杂 UI 动效",
        "在 RecyclerView item 上做动画时，优先使用 ItemAnimator 而非手动为 View 添加动画，避免与 RecyclerView 复用机制冲突"
    )
)

@Composable
fun AnimationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "Android UI 进阶",
        onBack = onBack
    )
}
