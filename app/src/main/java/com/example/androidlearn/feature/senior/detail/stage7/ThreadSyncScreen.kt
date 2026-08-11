package com.example.androidlearn.feature.senior.detail.stage7

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val threadSyncData = NoteData(
    title = "线程同步与并发原理",
    subtitle = "synchronized / volatile，CAS，Java 内存模型，协程调度",
    color = Color.parseColor("#795548"),
    chapters = listOf(
        ChapterItem("1",   "JMM（Java Memory Model）"),
        ChapterItem("1.1", "主内存与工作内存，happens-before 规则"),
        ChapterItem("2",   "volatile"),
        ChapterItem("2.1", "保证可见性 + 禁止指令重排，但不保证原子性"),
        ChapterItem("3",   "synchronized"),
        ChapterItem("3.1", "互斥锁，保证原子性 + 可见性 + 有序性"),
        ChapterItem("4",   "CAS（Compare And Swap）"),
        ChapterItem("4.1", "无锁乐观并发，AtomicInteger / AtomicReference"),
        ChapterItem("5",   "ReentrantLock"),
        ChapterItem("5.1", "可重入锁，支持公平/非公平、tryLock、Condition"),
        ChapterItem("6",   "协程调度器"),
        ChapterItem("6.1", "Dispatchers.IO（64 线程池）/ Default（CPU 核心数）/ Main（主线程）"),
    )
)
