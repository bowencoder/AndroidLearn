package com.example.androidlearn.feature.intermediate.detail.stage4

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "自定义 View",
    description = "Canvas 绘制、事件分发机制、动画",
    overview = "自定义 View 让你突破系统控件限制，实现独特 UI 效果，掌握 Canvas 绘制和事件分发是关键。",
    keyPoints = listOf(
        "onMeasure：确定 View 尺寸，处理 MeasureSpec",
        "onLayout：确定 ViewGroup 中子 View 位置",
        "onDraw：用 Canvas + Paint 绘制图形、文字、路径",
        "事件分发：dispatchTouchEvent → onInterceptTouchEvent → onTouchEvent",
        "属性动画：ValueAnimator / ObjectAnimator / AnimatorSet",
        "Compose Canvas：DrawScope API，声明式绘制"
    ),
    codeSnippet = """
class CircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        color = Color.BLUE
    }

    var progress = 0f
        set(value) { field = value; invalidate() }

    override fun onDraw(canvas: Canvas) {
        val rect = RectF(40f, 40f, width - 40f, height - 40f)
        canvas.drawArc(rect, -90f, 360f * progress, false, paint)
    }
}
    """.trimIndent(),
    tips = listOf(
        "Paint 对象在 onDraw 外初始化，避免频繁 GC",
        "调用 invalidate() 触发重绘",
        "Compose 中优先用 Canvas { } 绘制"
    )
)

@Composable
fun CustomViewScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF9800),
        stageTitle = "进阶开发能力",
        onBack = onBack
    )
}
