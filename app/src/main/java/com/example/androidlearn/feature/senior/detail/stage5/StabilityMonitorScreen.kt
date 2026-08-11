package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val stabilityMonitorData = NoteData(
    title = "稳定性监控体系",
    subtitle = "Crash/ANR 监控，Firebase Crashlytics，线下排查工具",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "崩溃分类"),
        ChapterItem("1.1", "Java Crash、Native Crash（信号捕获）、ANR（主线程超时）"),
        ChapterItem("2",   "Firebase Crashlytics"),
        ChapterItem("2.1", "自动捕获崩溃，符号化还原，非致命异常上报"),
        ChapterItem("3",   "ANR 分析"),
        ChapterItem("3.1", "/data/anr/traces.txt，主线程调用栈，Watchdog 机制"),
        ChapterItem("4",   "自定义 UncaughtExceptionHandler"),
        ChapterItem("4.1", "崩溃前写日志到磁盘"),
        ChapterItem("5",   "BlockCanary / ANR-WatchDog"),
        ChapterItem("5.1", "线下实时检测主线程阻塞"),
        ChapterItem("6",   "Matrix（微信）"),
        ChapterItem("6.1", "综合 APM 框架，覆盖卡顿/内存/IO 等多维度"),
    )
)
