package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 自定义 View
 * 官方文档：https://developer.android.com/guide/topics/ui/custom-components
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  测量与布局
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  onMeasure ────────────────────────────────────────────────────────────
 *
 *  · 确定 View 的尺寸，处理 MeasureSpec
 *  · MeasureSpec.EXACTLY：父级指定精确尺寸
 *  · MeasureSpec.AT_MOST：不超过父级给定的最大值（wrap_content）
 *  · MeasureSpec.UNSPECIFIED：无限制
 *
 *  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
 *      val width = MeasureSpec.getSize(widthMeasureSpec)
 *      val height = MeasureSpec.getSize(heightMeasureSpec)
 *      setMeasuredDimension(width, height)
 *  }
 *
 * ── 1.2  onLayout ─────────────────────────────────────────────────────────────
 *
 *  · 确定 ViewGroup 中子 View 的位置
 *  · 调用 child.layout(left, top, right, bottom)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  绘制
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  onDraw ───────────────────────────────────────────────────────────────
 *
 *  · 用 Canvas + Paint 绘制图形、文字、路径
 *  · Paint 对象在 onDraw 外初始化，避免频繁 GC
 *  · 调用 invalidate() 触发重绘
 *
 *  class CircleProgressView @JvmOverloads constructor(
 *      context: Context, attrs: AttributeSet? = null
 *  ) : View(context, attrs) {
 *
 *      private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
 *          style = Paint.Style.STROKE
 *          strokeWidth = 8f
 *          color = Color.BLUE
 *      }
 *
 *      var progress = 0f
 *          set(value) { field = value; invalidate() }
 *
 *      override fun onDraw(canvas: Canvas) {
 *          val rect = RectF(40f, 40f, width - 40f, height - 40f)
 *          canvas.drawArc(rect, -90f, 360f * progress, false, paint)
 *      }
 *  }
 *
 * ── 2.2  Compose Canvas ───────────────────────────────────────────────────────
 *
 *  · DrawScope API，声明式绘制
 *
 *  Canvas(modifier = Modifier.size(200.dp)) {
 *      drawCircle(color = Color.Blue, radius = size.minDimension / 2)
 *      drawArc(color = Color.Red, startAngle = -90f, sweepAngle = 270f, useCenter = false)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  事件分发
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · dispatchTouchEvent → onInterceptTouchEvent → onTouchEvent
 *  · GestureDetector：封装常用手势（单击、双击、长按、滑动）
 *  · ScaleGestureDetector：缩放手势
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  属性动画
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ValueAnimator：对任意值做动画，在 AnimatorUpdateListener 中更新 View
 *  · ObjectAnimator：直接对 View 属性做动画（translationX / alpha / scaleX）
 *  · AnimatorSet：组合多个动画（playTogether / playSequentially）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Paint 对象在 onDraw 外初始化，避免频繁 GC
 *  · 调用 invalidate() 触发重绘
 *  · Compose 中优先用 Canvas { } 绘制
 */

val customViewData = NoteData(
    title = "自定义 View",
    subtitle = "进阶开发能力 · Canvas 绘制 · 事件分发 · 动画",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("1",   "测量与布局"),
        ChapterItem("1.1", "onMeasure"),
        ChapterItem("1.2", "onLayout"),
        ChapterItem("2",   "绘制"),
        ChapterItem("2.1", "onDraw"),
        ChapterItem("2.2", "Compose Canvas"),
        ChapterItem("3",   "事件分发"),
        ChapterItem("4",   "属性动画"),
        ChapterItem("5",   "最佳实践"),
    )
)
