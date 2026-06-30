package com.example.androidlearn.feature.intermediate.detail.stage8

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【手势冲突解决方案】专属学习页
//  stageIndex=7, topicIndex=3
//  阶段颜色：青色 0xFF00BCD4（中级扩展 Stage 7）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "手势冲突解决方案",
    description = "外部拦截法、内部拦截法、NestedScrolling 协议、Compose nestedScroll",
    overview = "嵌套滚动控件（RecyclerView 内嵌 ViewPager、ScrollView 内嵌 WebView）是最常见的手势冲突场景，Android 提供了两种经典解法和一套现代协议。",
    keyPoints = listOf(
        "外部拦截法：在父 ViewGroup.onInterceptTouchEvent() 中根据方向判断是否拦截",
        "内部拦截法：子 View 调用 requestDisallowInterceptTouchEvent(true) 禁止父级拦截",
        "NestedScrollingParent / Child：官方接口，onStartNestedScroll/onNestedScroll 协商滚动量",
        "Compose nestedScroll：Modifier.nestedScroll + NestedScrollConnection，声明式处理",
        "ViewPager2 内嵌 RecyclerView：VP2 内置 TouchSlop 机制，横向优先消费",
        "WebView 嵌套：重写 WebView.onTouchEvent，配合 requestDisallowIntercept"
    ),
    codeSnippet = """
// 外部拦截法：父 ViewGroup 根据方向决定是否拦截
class DirectionAwareLayout @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null
) : ViewGroup(ctx, attrs) {

    private var lastX = 0f; private var lastY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x; lastY = ev.y
                intercept = false          // DOWN 绝不拦截
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(ev.x - lastX)
                val dy = Math.abs(ev.y - lastY)
                intercept = dy > dx        // 纵向 → 父级处理；横向 → 子 View 处理
            }
            MotionEvent.ACTION_UP -> intercept = false
        }
        return intercept
    }
    override fun onLayout(c: Boolean, l: Int, t: Int, r: Int, b: Int) {}
}

// 内部拦截法：子 View 主动通知父级是否可拦截
class HorizontalSwipeChild @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null
) : View(ctx, attrs) {

    private var lastX = 0f; private var lastY = 0f

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x; lastY = ev.y
                parent.requestDisallowInterceptTouchEvent(true)  // 先禁止父级拦截
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(ev.x - lastX); val dy = Math.abs(ev.y - lastY)
                if (dy > dx) {
                    // 纵向滑动 → 让父级接管
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                lastX = ev.x; lastY = ev.y
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
    """.trimIndent(),
    tips = listOf(
        "外部拦截法更常用，逻辑集中在父 View，不需要修改子 View",
        "内部拦截法适合无法修改父 View 源码的场景（如系统 ScrollView）",
        "Compose 中 nestedScroll 是官方推荐方案，CollapsingToolbarLayout 的 Compose 替代"
    )
)

@Composable
fun GestureConflictScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "事件与通信机制",
        onBack = onBack
    )
}
