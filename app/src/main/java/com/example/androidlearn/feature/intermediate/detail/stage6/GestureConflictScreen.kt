package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 手势冲突解决方案
 * 官方文档：https://developer.android.com/training/gestures/viewgroup
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  外部拦截法  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  原理 ─────────────────────────────────────────────────────────────────
 *
 *  · 在父 ViewGroup.onInterceptTouchEvent() 中根据方向判断是否拦截
 *  · DOWN 绝不拦截，让子 View 先有机会处理
 *  · MOVE 时根据滑动方向决定是否拦截
 *  · 更常用，逻辑集中在父 View，不需要修改子 View
 *
 * ── 1.2  代码示例 ─────────────────────────────────────────────────────────────
 *
 *  class DirectionAwareLayout @JvmOverloads constructor(
 *      ctx: Context, attrs: AttributeSet? = null
 *  ) : ViewGroup(ctx, attrs) {
 *
 *      private var lastX = 0f; private var lastY = 0f
 *
 *      override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
 *          var intercept = false
 *          when (ev.actionMasked) {
 *              MotionEvent.ACTION_DOWN -> {
 *                  lastX = ev.x; lastY = ev.y
 *                  intercept = false          // DOWN 绝不拦截
 *              }
 *              MotionEvent.ACTION_MOVE -> {
 *                  val dx = Math.abs(ev.x - lastX)
 *                  val dy = Math.abs(ev.y - lastY)
 *                  intercept = dy > dx        // 纵向 → 父级处理
 *              }
 *              MotionEvent.ACTION_UP -> intercept = false
 *          }
 *          return intercept
 *      }
 *      override fun onLayout(c: Boolean, l: Int, t: Int, r: Int, b: Int) {}
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  内部拦截法  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  原理 ─────────────────────────────────────────────────────────────────
 *
 *  · 子 View 调用 requestDisallowInterceptTouchEvent(true) 禁止父级拦截
 *  · 适合无法修改父 View 源码的场景（如系统 ScrollView）
 *
 * ── 2.2  代码示例 ─────────────────────────────────────────────────────────────
 *
 *  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
 *      when (ev.actionMasked) {
 *          MotionEvent.ACTION_DOWN -> {
 *              parent.requestDisallowInterceptTouchEvent(true)  // 先禁止父级拦截
 *          }
 *          MotionEvent.ACTION_MOVE -> {
 *              val dx = Math.abs(ev.x - lastX); val dy = Math.abs(ev.y - lastY)
 *              if (dy > dx) {
 *                  parent.requestDisallowInterceptTouchEvent(false)  // 让父级接管
 *              }
 *          }
 *      }
 *      return super.dispatchTouchEvent(ev)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  NestedScrolling 协议  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · NestedScrollingParent / Child：官方接口，协商滚动量
 *  · onStartNestedScroll / onNestedScroll：父子协商滚动消费
 *  · CoordinatorLayout + AppBarLayout 就是基于此协议实现
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Compose nestedScroll  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Modifier.nestedScroll + NestedScrollConnection，声明式处理
 *
 *  val nestedScrollConnection = object : NestedScrollConnection {
 *      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
 *          // 消费部分滚动量
 *          return Offset.Zero
 *      }
 *  }
 *  Box(Modifier.nestedScroll(nestedScrollConnection)) { ... }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  常见场景  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ViewPager2 内嵌 RecyclerView：VP2 内置 TouchSlop 机制，横向优先消费
 *  · WebView 嵌套：重写 WebView.onTouchEvent，配合 requestDisallowIntercept
 *  · RecyclerView 内嵌 ViewPager：外部拦截法，横向给 VP，纵向给 RV
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 外部拦截法更常用，逻辑集中在父 View，不需要修改子 View
 *  · 内部拦截法适合无法修改父 View 源码的场景
 *  · Compose 中 nestedScroll 是官方推荐方案
 */

val gestureConflictData = NoteData(
    title = "手势冲突解决方案",
    subtitle = "事件机制与动态编程 · 外部拦截 · 内部拦截 · NestedScrolling",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "外部拦截法  ★ 必学"),
        ChapterItem("1.1", "原理"),
        ChapterItem("1.2", "代码示例"),
        ChapterItem("2",   "内部拦截法  ★ 必学"),
        ChapterItem("2.1", "原理"),
        ChapterItem("2.2", "代码示例"),
        ChapterItem("3",   "NestedScrolling 协议  ★ 常用"),
        ChapterItem("4",   "Compose nestedScroll  ★ 常用"),
        ChapterItem("5",   "常见场景  ★ 必学"),
        ChapterItem("6",   "最佳实践  ★ 必学"),
    )
)
