package com.example.androidlearn.feature.intermediate.detail.stage8

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【触摸事件分发机制】专属学习页
//  stageIndex=7, topicIndex=2
//  阶段颜色：青色 0xFF00BCD4（中级扩展 Stage 7）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "触摸事件分发机制",
    description = "dispatch / intercept / onTouchEvent 三级传递、ACTION 序列规则",
    overview = "Android 的触摸事件从 Activity → ViewGroup → View 逐级传递，通过三个核心方法控制事件的分发、拦截和消费，理解这套机制是解决手势冲突的前提。",
    keyPoints = listOf(
        "分发链：Activity → PhoneWindow → DecorView → ViewGroup → View",
        "dispatchTouchEvent：决定事件是否继续向下传递，return true = 消费",
        "onInterceptTouchEvent（仅 ViewGroup）：拦截事件，不传给子 View",
        "onTouchEvent：真正处理事件，return true = 消费；false = 向上回传",
        "ACTION_DOWN 规则：谁消费了 DOWN，后续 MOVE/UP 就归谁处理",
        "ACTION_CANCEL：父 ViewGroup 中途拦截时通知子 View 取消当前事件序列"
    ),
    codeSnippet = """
// 自定义 ViewGroup - 演示三个核心方法的协作
class EventDemoGroup @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null
) : FrameLayout(ctx, attrs) {

    private var lastX = 0f
    private var lastY = 0f

    // 1. 分发：优先判断自身是否处理
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("Touch", "ViewGroup dispatchTouchEvent: ${'$'}{ev.actionMasked}")
        return super.dispatchTouchEvent(ev)
    }

    // 2. 拦截：决定是否截断向子 View 的传递
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x; lastY = ev.y
                false   // DOWN 不拦截，让子 View 先有机会处理
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(ev.x - lastX)
                val dy = Math.abs(ev.y - lastY)
                dy > dx   // 纵向滑动 → 拦截，交给自身 onTouchEvent
            }
            else -> false
        }
    }

    // 3. 消费：处理事件
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("Touch", "ViewGroup onTouchEvent: ${'$'}{ev.actionMasked}")
        return true   // 消费，不再向上回传
    }
}

// ACTION 事件序列说明：
// DOWN → MOVE(n) → UP  （正常点击/滑动序列）
// DOWN → MOVE(n) → CANCEL（父 View 中途拦截时子 View 收到）
    """.trimIndent(),
    tips = listOf(
        "DOWN 绝不能在 onInterceptTouchEvent 中拦截，否则子 View 永远收不到事件",
        "子 View 返回 false 消费 DOWN，后续 MOVE/UP 就不会再来了",
        "Compose 中事件分发由 Modifier.pointerInput 处理，不再有三个方法"
    )
)

@Composable
fun TouchEventScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "事件与通信机制",
        onBack = onBack
    )
}
