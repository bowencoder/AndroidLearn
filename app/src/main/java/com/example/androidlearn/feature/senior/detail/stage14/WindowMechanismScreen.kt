package com.example.androidlearn.feature.senior.detail.stage14

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val windowMechanismData = NoteData(
    title = "Window 机制",
    subtitle = "WMS、WindowToken、Window 层级体系、Surface 与 SurfaceFlinger 合成渲染",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "Window 类型"),
        ChapterItem("1.1", "Application Window（Activity）/ Sub Window（Dialog）/ System Window（Toast、状态栏）"),
        ChapterItem("2",   "WindowToken"),
        ChapterItem("2.1", "每个 Window 必须持有合法 Token，防止恶意窗口添加。Activity 的 Token 由 AMS 颁发"),
        ChapterItem("3",   "ViewRootImpl"),
        ChapterItem("3.1", "连接 View 树与 WMS 的桥梁，负责 measure/layout/draw 的触发和 Choreographer 调度"),
        ChapterItem("4",   "Surface"),
        ChapterItem("4.1", "每个 Window 对应一个 Surface（BufferQueue），App 在 Surface 上绘制，WMS 管理层级"),
        ChapterItem("5",   "SurfaceFlinger"),
        ChapterItem("5.1", "系统级合成服务，将各窗口 Surface 按 Z-order 合成，通过 HWC（硬件合成）输出到屏幕"),
        ChapterItem("6",   "Vsync 信号"),
        ChapterItem("6.1", "SurfaceFlinger 产生 Vsync 信号，Choreographer 监听后触发 UI 刷新，保证 60/120fps"),
    )
)
