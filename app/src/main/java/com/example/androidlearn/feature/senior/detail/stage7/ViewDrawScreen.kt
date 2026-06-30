package com.example.androidlearn.feature.senior.detail.stage7

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "View 绘制全流程",
    description = "measure / layout / draw 三大流程，硬件加速，RenderThread",
    overview = "从 ViewRootImpl 到屏幕像素，View 绘制经过测量、布局、绘制三大流程，理解每一步才能做到真正的渲染优化。",
    keyPoints = listOf(
        "performTraversals：ViewRootImpl 驱动三大流程，每帧 VSYNC 触发",
        "measure：MeasureSpec 封装父对 View 的尺寸约束，onMeasure 实现",
        "layout：确定 View 左上右下坐标，onLayout 放置子 View",
        "draw：Canvas 绘制背景→自身→子 View→装饰，硬件加速录制 DisplayList",
        "RenderThread：硬件加速下，UI 线程录制指令，RenderThread 独立执行 GPU 渲染",
        "invalidate vs requestLayout：重绘 vs 重新测量布局，按需选择"
    ),
    codeSnippet = """
// 自定义 ViewGroup - 实现水平均分布局
class EvenLayout @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null
) : ViewGroup(ctx, attrs) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val w = MeasureSpec.getSize(widthSpec)
        val childW = w / childCount
        var maxH = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(
                MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY),
                heightSpec
            )
            maxH = maxOf(maxH, child.measuredHeight)
        }
        setMeasuredDimension(w, maxH)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childW = width / childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(i * childW, 0, (i + 1) * childW, child.measuredHeight)
        }
    }
}
    """.trimIndent(),
    tips = listOf(
        "减少嵌套层级，每层 measure/layout 调用都有开销",
        "硬件加速开启时，draw 录制 DisplayList 而非直接操作 Canvas",
        "使用 Systrace 观察 Choreographer#doFrame，找到掉帧的具体环节"
    )
)

@Composable
fun ViewDrawScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF795548),
        stageTitle = "底层原理与面试重点",
        onBack = onBack
    )
}
