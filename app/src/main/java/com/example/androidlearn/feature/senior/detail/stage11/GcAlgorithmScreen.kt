package com.example.androidlearn.feature.senior.detail.stage11

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val gcAlgorithmData = NoteData(
    title = "GC 垃圾回收算法",
    subtitle = "GCRoot 算法、标记-复制-整理-分代回收、7 种 GC 收集器对比",
    color = Color.parseColor("#009688"),
    chapters = listOf(
        ChapterItem("1",   "引用计数（旧方案）"),
        ChapterItem("1.1", "无法解决循环引用，Python 用此方案；Java/ART 不用"),
        ChapterItem("2",   "GCRoot 可达性分析"),
        ChapterItem("2.1", "从根（栈变量/静态字段/JNI 引用）出发，不可达即垃圾"),
        ChapterItem("3",   "标记-清除（Mark-Sweep）"),
        ChapterItem("3.1", "碎片化严重，CMS 使用此算法"),
        ChapterItem("4",   "标记-复制（Mark-Copy）"),
        ChapterItem("4.1", "将存活对象复制到 To 区，无碎片，适合短命对象（Young 区）"),
        ChapterItem("5",   "标记-整理（Mark-Compact）"),
        ChapterItem("5.1", "将存活对象移到一端，无碎片，适合 Old 区"),
        ChapterItem("6",   "Android ART CC（并发复制）"),
        ChapterItem("6.1", "Android 8+ 默认，GC 与应用并发执行，减少 STW"),
    )
)
