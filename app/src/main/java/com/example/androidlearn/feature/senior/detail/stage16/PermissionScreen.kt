package com.example.androidlearn.feature.senior.detail.stage16

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val permissionData = NoteData(
    title = "Android 权限机制",
    subtitle = "Install-time/Runtime 权限、权限分组、Android 12+ 精确位置与后台权限演变",
    color = Color.parseColor("#00897B"),
    chapters = listOf(
        ChapterItem("1",   "权限类型"),
        ChapterItem("1.1", "Normal（自动授予）/ Dangerous（需运行时请求）/ Signature（同签名自动授予）/ AppOp（系统级）"),
        ChapterItem("2",   "运行时权限（Android 6+）"),
        ChapterItem("2.1", "危险权限必须在运行时用 requestPermissions() 请求，用户可随时撤销"),
        ChapterItem("3",   "权限分组"),
        ChapterItem("3.1", "同组权限一旦有一个被授予，同组其他权限自动授予（Android 8+ 修改了此行为，需逐一请求）"),
        ChapterItem("4",   "后台位置（Android 10+）"),
        ChapterItem("4.1", "需要单独声明 ACCESS_BACKGROUND_LOCATION，且只能在前台权限已授予后才能请求"),
        ChapterItem("5",   "精确/模糊位置（Android 12+）"),
        ChapterItem("5.1", "ACCESS_FINE_LOCATION 和 ACCESS_COARSE_LOCATION 分开，用户可选择只授予模糊位置"),
        ChapterItem("6",   "一次性权限（Android 11+）"),
        ChapterItem("6.1", "用户可授予「仅此一次」，App 进入后台后权限自动撤销"),
    )
)
