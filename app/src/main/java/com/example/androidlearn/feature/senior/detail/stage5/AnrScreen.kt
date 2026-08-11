package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val anrData = NoteData(
    title = "ANR 排查与治理",
    subtitle = "主线程耗时分析，Systrace，ANR Watchdog，StrictMode，Trace 埋点",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "ANR 触发条件"),
        ChapterItem("1.1", "主线程 5s 无响应（输入事件）"),
        ChapterItem("1.2", "BroadcastReceiver 10s / Service 20s"),
        ChapterItem("2",   "traces.txt 解读"),
        ChapterItem("2.1", "/data/anr/traces.txt 记录各线程栈"),
        ChapterItem("2.2", "main 线程的 WAITING/BLOCKED 是重点"),
        ChapterItem("3",   "Systrace 分析"),
        ChapterItem("3.1", "通过 atrace 采集 CPU 调度信息"),
        ChapterItem("3.2", "可视化查看主线程帧率与阻塞"),
        ChapterItem("4",   "StrictMode 检测"),
        ChapterItem("4.1", "开发阶段开启，强制暴露主线程磁盘 I/O / 网络请求"),
        ChapterItem("5",   "ANR Watchdog"),
        ChapterItem("5.1", "子线程定时向主线程 post 任务，超时未执行则主动上报"),
        ChapterItem("6",   "Trace 埋点"),
        ChapterItem("6.1", "Trace.beginSection / endSection 精确定位各业务代码耗时"),
    )
)
