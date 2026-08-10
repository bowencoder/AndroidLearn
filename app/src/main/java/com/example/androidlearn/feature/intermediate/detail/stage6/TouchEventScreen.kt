package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 触摸事件分发机制
 * 官方文档：https://developer.android.com/develop/ui/views/touch-and-input/gestures/viewgroup
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  分发链路
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  Activity → PhoneWindow → DecorView → ViewGroup → View
 *
 *  · 事件从顶层向下传递，消费后不再继续向下
 *  · 未被消费的事件沿原路向上回传
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  三个核心方法
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  dispatchTouchEvent ───────────────────────────────────────────────────
 *
 *  · 决定事件是否继续向下传递
 *  · return true = 消费，不再向下传递
 *  · return super.dispatchTouchEvent() = 走正常分发流程
 *
 * ── 2.2  onInterceptTouchEvent（仅 ViewGroup）─────────────────────────────────
 *
 *  · 拦截事件，不传给子 View
 *  · return true = 拦截，交给自身 onTouchEvent 处理
 *  · return false = 不拦截，继续向子 View 传递
 *
 * ── 2.3  onTouchEvent ─────────────────────────────────────────────────────────
 *
 *  · 真正处理事件
 *  · return true = 消费，不再向上回传
 *  · return false = 不消费，向上回传给父 View
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  ACTION 事件序列规则
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · DOWN → MOVE(n) → UP：正常点击/滑动序列
 *  · DOWN → MOVE(n) → CANCEL：父 View 中途拦截时子 View 收到 CANCEL
 *  · ACTION_DOWN 规则：谁消费了 DOWN，后续 MOVE/UP 就归谁处理
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  代码示例
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  class EventDemoGroup @JvmOverloads constructor(
 *      ctx: Context, attrs: AttributeSet? = null
 *  ) : FrameLayout(ctx, attrs) {
 *
 *      private var lastX = 0f
 *      private var lastY = 0f
 *
 *      // 1. 分发：优先判断自身是否处理
 *      override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
 *          Log.d("Touch", "ViewGroup dispatchTouchEvent: ${ev.actionMasked}")
 *          return super.dispatchTouchEvent(ev)
 *      }
 *
 *      // 2. 拦截：决定是否截断向子 View 的传递
 *      override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
 *          return when (ev.actionMasked) {
 *              MotionEvent.ACTION_DOWN -> {
 *                  lastX = ev.x; lastY = ev.y
 *                  false   // DOWN 不拦截，让子 View 先有机会处理
 *              }
 *              MotionEvent.ACTION_MOVE -> {
 *                  val dx = Math.abs(ev.x - lastX)
 *                  val dy = Math.abs(ev.y - lastY)
 *                  dy > dx   // 纵向滑动 → 拦截，交给自身 onTouchEvent
 *              }
 *              else -> false
 *          }
 *      }
 *
 *      // 3. 消费：处理事件
 *      override fun onTouchEvent(ev: MotionEvent): Boolean {
 *          Log.d("Touch", "ViewGroup onTouchEvent: ${ev.actionMasked}")
 *          return true   // 消费，不再向上回传
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Compose 中的事件处理
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Compose 中事件分发由 Modifier.pointerInput 处理，不再有三个方法
 *  · detectTapGestures / detectDragGestures：高级手势检测
 *  · awaitPointerEventScope：低级别事件处理，可精确控制消费
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · DOWN 绝不能在 onInterceptTouchEvent 中拦截，否则子 View 永远收不到事件
 *  · 子 View 返回 false 消费 DOWN，后续 MOVE/UP 就不会再来了
 *  · 理解事件分发是解决手势冲突的前提，配合外部/内部拦截法使用
 */

val touchEventData = NoteData(
    title = "触摸事件分发机制",
    subtitle = "事件机制与动态编程 · dispatch · intercept · onTouchEvent",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "分发链路"),
        ChapterItem("2",   "三个核心方法"),
        ChapterItem("2.1", "dispatchTouchEvent"),
        ChapterItem("2.2", "onInterceptTouchEvent（仅 ViewGroup）"),
        ChapterItem("2.3", "onTouchEvent"),
        ChapterItem("3",   "ACTION 事件序列规则"),
        ChapterItem("4",   "代码示例"),
        ChapterItem("5",   "Compose 中的事件处理"),
        ChapterItem("6",   "最佳实践"),
    )
)
