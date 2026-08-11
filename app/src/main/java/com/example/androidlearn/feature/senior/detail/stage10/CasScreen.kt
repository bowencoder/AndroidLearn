package com.example.androidlearn.feature.senior.detail.stage10

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val casData = NoteData(
    title = "CAS 与无锁并发",
    subtitle = "CAS 原理与 ABA 问题、Atomic 类、LongAdder、无锁并发策略",
    color = Color.parseColor("#E91E63"),
    chapters = listOf(
        ChapterItem("1",   "CAS 原语"),
        ChapterItem("1.1", "compareAndSwap(内存地址, 期望值, 新值)，CPU 指令级原子操作"),
        ChapterItem("2",   "ABA 问题"),
        ChapterItem("2.1", "值从 A→B→A，CAS 无法感知中间变化，用 AtomicStampedReference 解决"),
        ChapterItem("3",   "Unsafe 类"),
        ChapterItem("3.1", "Java 直接操作内存的后门，Atomic 类内部通过 Unsafe 实现 CAS"),
        ChapterItem("4",   "Atomic 类"),
        ChapterItem("4.1", "AtomicInteger/AtomicLong/AtomicReference：基于 CAS 的原子类"),
        ChapterItem("5",   "LongAdder"),
        ChapterItem("5.1", "分段累加（Cell 数组），高并发下性能远优于 AtomicLong"),
        ChapterItem("6",   "自旋等待"),
        ChapterItem("6.1", "CAS 失败后循环重试（自旋），高竞争时会浪费 CPU"),
    )
)
