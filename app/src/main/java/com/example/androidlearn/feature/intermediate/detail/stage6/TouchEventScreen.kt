package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 触摸事件分发机制
 * 官方文档：https://developer.android.com/develop/ui/views/touch-and-input/gestures/viewgroup
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  分发链路  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  Activity → PhoneWindow → DecorView → ViewGroup → View
 *
 *  · 事件从顶层向下传递（dispatch），消费后不再继续向下
 *  · 未被消费的事件沿原路向上回传（bubble）
 *  · 整个链路的入口是 Activity.dispatchTouchEvent()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  三个核心方法  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  dispatchTouchEvent ───────────────────────────────────────────────────
 *
 *  · 所有 View / ViewGroup / Activity 都有，是事件分发的入口
 *  · return true  = 消费，不再向下传递
 *  · return false = 不消费，回传给父 View 的 onTouchEvent
 *  · return super.dispatchTouchEvent() = 走正常分发流程（推荐）
 *
 * ── 2.2  onInterceptTouchEvent（仅 ViewGroup）★ 必学 ──────────────────────────
 *
 *  · 拦截事件，不传给子 View；View 没有此方法
 *  · return true  = 拦截，交给自身 onTouchEvent 处理
 *  · return false = 不拦截，继续向子 View 传递（默认）
 *  · ⚠️ DOWN 事件绝不能在此拦截，否则子 View 永远收不到事件
 *
 * ── 2.3  onTouchEvent ★ 必学 ──────────────────────────────────────────────────
 *
 *  · 真正处理事件的地方
 *  · return true  = 消费，不再向上回传
 *  · return false = 不消费，向上回传给父 View 的 onTouchEvent
 *  · ⚠️ 子 View 的 onTouchEvent 返回 false 消费 DOWN，后续 MOVE/UP 就不会再来
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  分发伪代码流程  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // ViewGroup.dispatchTouchEvent 简化逻辑
 *  fun dispatchTouchEvent(ev: MotionEvent): Boolean {
 *      var consumed = false
 *
 *      if (!onInterceptTouchEvent(ev)) {
 *          // 未拦截 → 传给子 View
 *          consumed = child.dispatchTouchEvent(ev)
 *      }
 *
 *      if (!consumed) {
 *          // 子 View 未消费 → 自身处理
 *          consumed = onTouchEvent(ev)
 *      }
 *
 *      return consumed
 *  }
 *
 *  · 关键结论：
 *    - 谁消费了 DOWN，后续 MOVE/UP 就归谁处理（mFirstTouchTarget 记录）
 *    - 父 View 中途拦截（MOVE 时 intercept=true），子 View 收到 CANCEL
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  ACTION 事件序列规则  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · DOWN → MOVE(n) → UP：正常点击/滑动序列
 *  · DOWN → MOVE(n) → CANCEL：父 View 中途拦截时，子 View 收到 CANCEL
 *  · ACTION_DOWN 规则：谁消费了 DOWN，后续 MOVE/UP 就归谁处理
 *
 *  ┌──────────────┬──────────────────────────────────────────────────────┐
 *  │   事件类型    │                      含义                            │
 *  ├──────────────┼──────────────────────────────────────────────────────┤
 *  │ ACTION_DOWN  │ 手指按下，一个事件序列的起点                           │
 *  │ ACTION_MOVE  │ 手指移动，可能触发多次                                 │
 *  │ ACTION_UP    │ 手指抬起，序列结束                                     │
 *  │ ACTION_CANCEL│ 事件被父 View 中途拦截，子 View 应清理状态              │
 *  └──────────────┴──────────────────────────────────────────────────────┘
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  代码示例  ★ 常用
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
 *                  false   // DOWN 绝不拦截
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
 *  6  Compose 中的事件处理  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Compose 中事件分发由 Modifier.pointerInput 处理，不再有三个方法
 *  · detectTapGestures：点击/长按/双击
 *  · detectDragGestures：拖拽手势
 *  · awaitPointerEventScope：低级别事件处理，可精确控制消费
 *
 *  Box(
 *      Modifier.pointerInput(Unit) {
 *          detectTapGestures(
 *              onTap = { offset -> println("tap at $offset") },
 *              onLongPress = { println("long press") }
 *          )
 *      }
 *  )
 *
 *  // 低级别：精确控制消费
 *  Modifier.pointerInput(Unit) {
 *      awaitPointerEventScope {
 *          while (true) {
 *              val event = awaitPointerEvent()
 *              event.changes.forEach { it.consume() }  // 消费所有指针事件
 *          }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · DOWN 绝不能在 onInterceptTouchEvent 中拦截，否则子 View 永远收不到事件
 *  · 理解事件分发是解决手势冲突的前提，配合外部/内部拦截法使用
 *  · 子 View 收到 ACTION_CANCEL 时应清理动画/状态，避免 UI 异常
 *
 *  ❌ 不应该做：
 *  · 不要在 onTouchEvent 中返回 false 消费 DOWN（后续 MOVE/UP 不会再来）
 *  · 不要在 dispatchTouchEvent 中直接 return true 跳过正常分发流程
 */

val touchEventData = NoteData(
    title = "触摸事件分发机制",
    subtitle = "dispatch · intercept · onTouchEvent · ACTION 序列",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "分发链路  ★ 必学"),
        ChapterItem("2",   "三个核心方法  ★ 必学"),
        ChapterItem("2.1", "dispatchTouchEvent"),
        ChapterItem("2.2", "onInterceptTouchEvent（仅 ViewGroup）"),
        ChapterItem("2.3", "onTouchEvent"),
        ChapterItem("3",   "分发伪代码流程  ★ 必学"),
        ChapterItem("4",   "ACTION 事件序列规则  ★ 必学"),
        ChapterItem("5",   "代码示例  ★ 常用"),
        ChapterItem("6",   "Compose 中的事件处理  ★ 常用"),
        ChapterItem("7",   "最佳实践  ★ 必学"),
    )
)
