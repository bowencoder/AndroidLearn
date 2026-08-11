package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val appStartupData = NoteData(
    title = "启动性能优化",
    subtitle = "冷启动/温启动，App Startup，异步初始化，SplashScreen API",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "冷/温/热启动"),
        ChapterItem("1.1", "冷启动：从进程创建到首帧渲染的完整链路"),
        ChapterItem("1.2", "温启动：进程存在但 Activity 需重建"),
        ChapterItem("1.3", "热启动：Activity 在后台栈中直接恢复"),
        ChapterItem("2",   "App Startup 库"),
        ChapterItem("2.1", "用 Initializer 替换多个 ContentProvider"),
        ChapterItem("2.2", "串行/并行初始化可控"),
        ChapterItem("3",   "Trace 埋点"),
        ChapterItem("3.1", "Trace.beginSection() 精确定位各阶段耗时"),
        ChapterItem("4",   "异步初始化"),
        ChapterItem("4.1", "非关键 SDK 放到 IO 线程或 IdleHandler 中延迟初始化"),
        ChapterItem("5",   "SplashScreen API"),
        ChapterItem("5.1", "Android 12+ 统一启动画面，避免白/黑屏"),
        ChapterItem("6",   "启动时 StrictMode"),
        ChapterItem("6.1", "检测主线程 I/O，强制暴露阻塞操作"),
    )
)
