package com.example.androidlearn.feature.senior.detail.stage11

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val oomAnalysisData = NoteData(
    title = "内存溢出与分析工具",
    subtitle = "OOM 分类、MAT 堆快照分析、Profiler 实战、MinorGC/FullGC 日志解读",
    color = Color.parseColor("#009688"),
    chapters = listOf(
        ChapterItem("1",   "OOM 分类"),
        ChapterItem("1.1", "Java 堆 OOM / 内存泄漏导致 OOM / 图片加载 OOM / 线程过多 OOM"),
        ChapterItem("2",   "Heap Dump"),
        ChapterItem("2.1", "adb shell am dumpheap 或 Profiler 导出 .hprof 文件"),
        ChapterItem("3",   "MAT 分析"),
        ChapterItem("3.1", "Dominator Tree（最大对象树）/ Histogram（对象计数）/ Leak Suspects"),
        ChapterItem("4",   "Shallow Size vs Retained Size"),
        ChapterItem("4.1", "浅大小=对象本身；保留大小=释放该对象能回收的总内存"),
        ChapterItem("5",   "GC 日志解读"),
        ChapterItem("5.1", "GC freed X objects(Yk) ... / Explicit GC / Background GC"),
        ChapterItem("6",   "onTrimMemory()"),
        ChapterItem("6.1", "系统内存不足时回调，主动释放缓存避免被 kill"),
    )
)
