package com.example.androidlearn.feature.senior.detail.stage7

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val activityStartupData = NoteData(
    title = "Activity 启动全链路",
    subtitle = "AMS / ATMS 调度，进程创建，生命周期回调时序",
    color = Color.parseColor("#795548"),
    chapters = listOf(
        ChapterItem("1",   "Launcher → AMS/ATMS"),
        ChapterItem("1.1", "startActivity 经 Binder 发送给 ActivityTaskManagerService"),
        ChapterItem("2",   "ATMS 处理"),
        ChapterItem("2.1", "检查权限、处理 Task 栈、决定是否需要新进程"),
        ChapterItem("3",   "Zygote fork"),
        ChapterItem("3.1", "若目标进程不存在，Socket 通知 Zygote fork 新进程"),
        ChapterItem("4",   "ActivityThread.main()"),
        ChapterItem("4.1", "新进程入口，初始化 Looper / Application / Activity"),
        ChapterItem("5",   "H 类消息"),
        ChapterItem("5.1", "LAUNCH_ACTIVITY 消息触发 performLaunchActivity，调用 onCreate"),
        ChapterItem("6",   "Window 挂载"),
        ChapterItem("6.1", "setContentView → DecorView → ViewRootImpl → 第一帧渲染"),
    )
)
