package com.example.androidlearn.feature.senior.detail.stage15

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val surfaceViewData = NoteData(
    title = "SurfaceView 与自定义渲染",
    subtitle = "SurfaceView/TextureView 区别、双缓冲机制、Canvas 绘制与 OpenGL ES 集成",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "SurfaceView"),
        ChapterItem("1.1", "独立 Window（不在 View 层级中），绘制在子线程，性能高，但不支持普通 View 动画变换"),
        ChapterItem("2",   "TextureView"),
        ChapterItem("2.1", "在 View 层级中，复用硬件加速层（SurfaceTexture），支持 alpha/旋转等变换，但消耗更多内存"),
        ChapterItem("3",   "SurfaceHolder"),
        ChapterItem("3.1", "SurfaceView 的控制接口，通过回调（surfaceCreated/Changed/Destroyed）感知 Surface 生命周期"),
        ChapterItem("4",   "双缓冲机制"),
        ChapterItem("4.1", "前缓冲（显示中）和后缓冲（绘制中）交替显示，避免撕裂（Tearing）"),
        ChapterItem("5",   "Canvas 绘制流程"),
        ChapterItem("5.1", "lockCanvas() 获取画布 → 绘制内容 → unlockCanvasAndPost() 提交到 Surface"),
        ChapterItem("6",   "GLSurfaceView"),
        ChapterItem("6.1", "SurfaceView 的子类，内置 OpenGL ES 上下文和渲染线程，用于游戏和 3D 渲染"),
    )
)
