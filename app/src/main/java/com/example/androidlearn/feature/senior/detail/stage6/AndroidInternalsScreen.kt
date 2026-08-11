package com.example.androidlearn.feature.senior.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val androidInternalsData = NoteData(
    title = "Android 系统深度理解",
    subtitle = "Binder IPC，Handler/Looper，AMS/WMS，渲染管线",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "Binder"),
        ChapterItem("1.1", "Android 核心 IPC 机制，一次拷贝，安全性高"),
        ChapterItem("2",   "Handler / Looper / MessageQueue"),
        ChapterItem("2.1", "主线程消息循环，UI 操作线程安全的保证"),
        ChapterItem("3",   "AMS（ActivityManagerService）"),
        ChapterItem("3.1", "管理 Activity 生命周期、进程优先级、Task 栈"),
        ChapterItem("4",   "WMS（WindowManagerService）"),
        ChapterItem("4.1", "管理窗口层级（z-order）、Surface 分配"),
        ChapterItem("5",   "VSYNC 与渲染管线"),
        ChapterItem("5.1", "Choreographer → SurfaceFlinger → HWComposer"),
        ChapterItem("6",   "进程优先级"),
        ChapterItem("6.1", "前台/可见/服务/后台进程，系统内存不足时按优先级 kill"),
    )
)
