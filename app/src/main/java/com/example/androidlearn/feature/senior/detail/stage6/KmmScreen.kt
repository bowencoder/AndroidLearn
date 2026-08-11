package com.example.androidlearn.feature.senior.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val kmmData = NoteData(
    title = "Kotlin Multiplatform (KMM)",
    subtitle = "共享业务逻辑，expect/actual，与 iOS 互操作",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "共享模块"),
        ChapterItem("1.1", "commonMain 编写跨平台代码，androidMain/iosMain 平台特定"),
        ChapterItem("2",   "expect / actual"),
        ChapterItem("2.1", "声明期望 API，各平台提供 actual 实现"),
        ChapterItem("3",   "Ktor"),
        ChapterItem("3.1", "KMM 友好的多平台 HTTP 客户端"),
        ChapterItem("4",   "SQLDelight"),
        ChapterItem("4.1", "多平台 SQLite 类型安全查询"),
        ChapterItem("5",   "Compose Multiplatform"),
        ChapterItem("5.1", "UI 层也跨平台（iOS 仍在 Alpha）"),
        ChapterItem("6",   "与 iOS Swift 互操作"),
        ChapterItem("6.1", "Kotlin/Native 编译为 .xcframework"),
    )
)
