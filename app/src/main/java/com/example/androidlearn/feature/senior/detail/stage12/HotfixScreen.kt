package com.example.androidlearn.feature.senior.detail.stage12

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val hotfixData = NoteData(
    title = "热修复原理与实践",
    subtitle = "Tinker、Robust、Sophix，dex 差量合并，代码/资源热替换",
    color = Color.parseColor("#8BC34A"),
    chapters = listOf(
        ChapterItem("1",   "Tinker（微信）"),
        ChapterItem("1.1", "diff 算法生成补丁 dex，合并后整体替换，修复能力强，需重启生效"),
        ChapterItem("2",   "Robust（美团）"),
        ChapterItem("2.1", "编译时在每个方法插桩，运行时通过接口代理实现方法替换，即时生效"),
        ChapterItem("3",   "Sophix（阿里）"),
        ChapterItem("3.1", "综合方案，Android 版本兼容好，支持代码+资源+So 热修复"),
        ChapterItem("4",   "dex 差量合并"),
        ChapterItem("4.1", "BSDiff 算法生成 patch，合并到完整 dex，ClassLoader 优先加载补丁类"),
        ChapterItem("5",   "ClassLoader 方案"),
        ChapterItem("5.1", "将补丁 dex 插入到 DexPathList.dexElements 数组的最前面"),
        ChapterItem("6",   "资源热修复"),
        ChapterItem("6.1", "重新创建 AssetManager 加载补丁资源包，替换全局 Resources 引用"),
    )
)
