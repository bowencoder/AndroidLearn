package com.example.androidlearn.feature.senior.detail.stage16

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val versionCompatData = NoteData(
    title = "版本适配策略",
    subtitle = "targetSdkVersion 与 API 兼容性、Android 各版本重大变更汇总与 BuildCompat 适配方案",
    color = Color.parseColor("#00897B"),
    chapters = listOf(
        ChapterItem("1",   "minSdk / targetSdk"),
        ChapterItem("1.1", "minSdk：App 支持的最低版本；targetSdk：声明已针对该版本测试，系统根据此值决定是否应用新行为变更"),
        ChapterItem("2",   "compileSdk"),
        ChapterItem("2.1", "编译时使用的 API 级别，决定可使用的 API 上限，应始终设为最新版本"),
        ChapterItem("3",   "Android 10（Q）重大变更"),
        ChapterItem("3.1", "后台位置限制、scoped storage（分区存储）、设备标识符限制（禁止获取 IMEI）"),
        ChapterItem("4",   "Android 11（R）重大变更"),
        ChapterItem("4.1", "软件包可见性（需声明 queries 或 QUERY_ALL_PACKAGES）、强制分区存储、单次权限"),
        ChapterItem("5",   "Android 12（S）重大变更"),
        ChapterItem("5.1", "精确闹钟权限（SCHEDULE_EXACT_ALARM）、显式 PendingIntent、蓝牙权限拆分"),
        ChapterItem("6",   "Android 13（T）重大变更"),
        ChapterItem("6.1", "细化媒体权限（READ_MEDIA_IMAGES/VIDEO/AUDIO 替代 READ_EXTERNAL_STORAGE）、通知权限（POST_NOTIFICATIONS）"),
    )
)
