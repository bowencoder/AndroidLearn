package com.example.androidlearn.feature.senior.detail.stage7

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val classLoaderData = NoteData(
    title = "类加载与热修复原理",
    subtitle = "ClassLoader 双亲委派，dex 加载，热修复 Patch 原理",
    color = Color.parseColor("#795548"),
    chapters = listOf(
        ChapterItem("1",   "双亲委派"),
        ChapterItem("1.1", "先委托父 ClassLoader 加载，父找不到才由子加载，防止核心类被覆盖"),
        ChapterItem("2",   "PathClassLoader"),
        ChapterItem("2.1", "加载已安装 APK 的 dex，作为 App 的默认 ClassLoader"),
        ChapterItem("3",   "DexClassLoader"),
        ChapterItem("3.1", "可动态加载外部 dex/apk 文件"),
        ChapterItem("4",   "dexElements 数组"),
        ChapterItem("4.1", "ClassLoader 按数组顺序遍历 dex，找到类即返回"),
        ChapterItem("5",   "热修复原理"),
        ChapterItem("5.1", "将补丁 dex 插入 dexElements 数组头部，优先加载修复后的类"),
        ChapterItem("6",   "主流方案"),
        ChapterItem("6.1", "Tinker / Robust / QZone：各方案在 Dex / Native / 字节码层面的不同实现"),
    )
)
