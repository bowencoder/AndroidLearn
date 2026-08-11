package com.example.androidlearn.feature.senior.detail.stage10

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val threadPoolData = NoteData(
    title = "线程池深度原理",
    subtitle = "ThreadPoolExecutor 参数、排队机制、阻塞队列、优先级线程池实战",
    color = Color.parseColor("#E91E63"),
    chapters = listOf(
        ChapterItem("1",   "7 个核心参数"),
        ChapterItem("1.1", "corePoolSize、maximumPoolSize、keepAliveTime、unit、workQueue、threadFactory、handler"),
        ChapterItem("2",   "任务处理流程"),
        ChapterItem("2.1", "线程数 < core → 创建线程；core 已满 → 入队；队列满且 < max → 创建线程；max 也满 → 拒绝策略"),
        ChapterItem("3",   "阻塞队列类型"),
        ChapterItem("3.1", "LinkedBlockingQueue（无界）/ ArrayBlockingQueue（有界）/ SynchronousQueue（直传）"),
        ChapterItem("4",   "拒绝策略"),
        ChapterItem("4.1", "AbortPolicy（抛异常）/ CallerRunsPolicy（调用者执行）/ DiscardPolicy（丢弃）"),
        ChapterItem("5",   "线程数设置经验"),
        ChapterItem("5.1", "IO 密集 = CPU核数 * 2；CPU 密集 = CPU核数 + 1"),
        ChapterItem("6",   "协程替代"),
        ChapterItem("6.1", "Android 中优先用 Kotlin 协程 + Dispatchers.IO，自动管理线程池"),
    )
)
