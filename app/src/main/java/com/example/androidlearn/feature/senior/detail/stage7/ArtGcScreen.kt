package com.example.androidlearn.feature.senior.detail.stage7

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val artGcData = NoteData(
    title = "ART 内存管理与 GC",
    subtitle = "堆分区，GC 算法，OOM 分析，Large Object Space",
    color = Color.parseColor("#795548"),
    chapters = listOf(
        ChapterItem("1",   "堆分区"),
        ChapterItem("1.1", "Young Generation（Eden + Survivor）→ Old Generation → Large Object Space"),
        ChapterItem("2",   "GC 算法"),
        ChapterItem("2.1", "CMS（并发标记清除）→ Android 9+ 改为 CC（并发复制）"),
        ChapterItem("3",   "GC Root"),
        ChapterItem("3.1", "栈变量、静态字段、JNI 全局引用，可达即不回收"),
        ChapterItem("4",   "内存抖动"),
        ChapterItem("4.1", "大量短命对象频繁触发 GC，导致帧率抖动"),
        ChapterItem("5",   "Large Object Space"),
        ChapterItem("5.1", "> 12KB 对象直接分配到 LOS，GC 单独处理"),
        ChapterItem("6",   "OOM 分析"),
        ChapterItem("6.1", "Heap Dump → MAT / Android Profiler 分析引用链"),
    )
)
