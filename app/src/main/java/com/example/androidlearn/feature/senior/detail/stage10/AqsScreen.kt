package com.example.androidlearn.feature.senior.detail.stage10

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val aqsData = NoteData(
    title = "AQS 并发框架",
    subtitle = "ReentrantLock、读写锁、CountDownLatch、CyclicBarrier、Semaphore",
    color = Color.parseColor("#E91E63"),
    chapters = listOf(
        ChapterItem("1",   "AQS 核心"),
        ChapterItem("1.1", "state 整型变量 + CLH 等待队列，子类通过 tryAcquire/tryRelease 定义语义"),
        ChapterItem("2",   "ReentrantLock"),
        ChapterItem("2.1", "可重入、可公平/非公平、支持 tryLock(timeout)、Condition 条件等待"),
        ChapterItem("3",   "ReadWriteLock"),
        ChapterItem("3.1", "读共享写独占，适合读多写少场景，state 高 16 位=读锁，低 16 位=写锁"),
        ChapterItem("4",   "CountDownLatch"),
        ChapterItem("4.1", "一次性，倒计时到 0 触发，常用于等待多线程任务完成"),
        ChapterItem("5",   "CyclicBarrier"),
        ChapterItem("5.1", "可重复使用，所有线程都到达屏障后统一放行"),
        ChapterItem("6",   "Semaphore"),
        ChapterItem("6.1", "许可证计数，控制最大并发数（连接池、限流）"),
    )
)
