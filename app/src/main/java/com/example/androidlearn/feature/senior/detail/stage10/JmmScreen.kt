package com.example.androidlearn.feature.senior.detail.stage10

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val jmmData = NoteData(
    title = "JMM 并发内存模型",
    subtitle = "主内存与工作内存、happens-before、volatile 可见性与有序性",
    color = Color.parseColor("#E91E63"),
    chapters = listOf(
        ChapterItem("1",   "主内存与工作内存"),
        ChapterItem("1.1", "线程有本地缓存副本，写回主内存才对其他线程可见"),
        ChapterItem("2",   "happens-before"),
        ChapterItem("2.1", "保证操作顺序的规则集，满足即可保证可见性和有序性"),
        ChapterItem("3",   "volatile 可见性"),
        ChapterItem("3.1", "写操作立即刷新到主内存；读操作从主内存读取最新值"),
        ChapterItem("4",   "volatile 有序性"),
        ChapterItem("4.1", "禁止编译器和 CPU 对 volatile 前后指令重排序"),
        ChapterItem("5",   "volatile 局限"),
        ChapterItem("5.1", "不保证原子性（i++ 是三步操作：读-改-写）"),
        ChapterItem("6",   "指令重排序"),
        ChapterItem("6.1", "CPU 为提升效率会乱序执行，happens-before 规则限制了重排边界"),
    )
)
