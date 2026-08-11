package com.example.androidlearn.feature.senior.detail.stage7

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val viewDrawData = NoteData(
    title = "View 绘制全流程",
    subtitle = "measure / layout / draw 三大流程，硬件加速，RenderThread",
    color = Color.parseColor("#795548"),
    chapters = listOf(
        ChapterItem("1",   "performTraversals"),
        ChapterItem("1.1", "ViewRootImpl 驱动三大流程，每帧 VSYNC 触发"),
        ChapterItem("2",   "measure"),
        ChapterItem("2.1", "MeasureSpec 封装父对 View 的尺寸约束，onMeasure 实现"),
        ChapterItem("3",   "layout"),
        ChapterItem("3.1", "确定 View 左上右下坐标，onLayout 放置子 View"),
        ChapterItem("4",   "draw"),
        ChapterItem("4.1", "Canvas 绘制背景→自身→子 View→装饰，硬件加速录制 DisplayList"),
        ChapterItem("5",   "RenderThread"),
        ChapterItem("5.1", "硬件加速下，UI 线程录制指令，RenderThread 独立执行 GPU 渲染"),
        ChapterItem("6",   "invalidate vs requestLayout"),
        ChapterItem("6.1", "重绘 vs 重新测量布局，按需选择"),
    )
)
