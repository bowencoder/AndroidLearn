package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val packageSizeData = NoteData(
    title = "包体积优化",
    subtitle = "R8/ProGuard，资源压缩，App Bundle，动态功能模块",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "R8 全模式"),
        ChapterItem("1.1", "比 ProGuard 更激进的死代码删除和优化"),
        ChapterItem("2",   "资源压缩"),
        ChapterItem("2.1", "shrinkResources = true 删除未引用资源"),
        ChapterItem("3",   "App Bundle（AAB）"),
        ChapterItem("3.1", "Google Play 按设备按需下发，平均节省 15-35%"),
        ChapterItem("4",   "ABI 过滤"),
        ChapterItem("4.1", "abiFilters 只打包目标架构的 .so 文件"),
        ChapterItem("5",   "WebP 转换"),
        ChapterItem("5.1", "lossless 模式下比 PNG 小 26%，支持透明"),
        ChapterItem("6",   "Dynamic Feature Module"),
        ChapterItem("6.1", "将大功能模块做成按需下载"),
    )
)
