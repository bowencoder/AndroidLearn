package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val renderOptimizationData = NoteData(
    title = "渲染与内存优化",
    subtitle = "Perfetto 分析，Compose 重组优化，LeakCanary，MAT",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "过度绘制"),
        ChapterItem("1.1", "开发者选项开启色块检测，减少 overdraw"),
        ChapterItem("2",   "Compose Layout Inspector"),
        ChapterItem("2.1", "查看重组次数，定位频繁重组的 Composable"),
        ChapterItem("3",   "@Stable / @Immutable"),
        ChapterItem("3.1", "告知编译器类型稳定，跳过不必要重组"),
        ChapterItem("4",   "derivedStateOf"),
        ChapterItem("4.1", "派生状态，只在计算结果变化时触发重组"),
        ChapterItem("5",   "LeakCanary"),
        ChapterItem("5.1", "自动检测 Activity/Fragment/ViewModel 内存泄漏"),
        ChapterItem("6",   "Memory Profiler"),
        ChapterItem("6.1", "Heap Dump 分析对象引用链"),
    )
)
