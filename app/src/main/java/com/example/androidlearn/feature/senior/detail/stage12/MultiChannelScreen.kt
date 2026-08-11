package com.example.androidlearn.feature.senior.detail.stage12

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val multiChannelData = NoteData(
    title = "多渠道打包方案",
    subtitle = "Walle/VasDolly，APK 签名校验，渠道信息写入，自动化打包",
    color = Color.parseColor("#8BC34A"),
    chapters = listOf(
        ChapterItem("1",   "productFlavors 方案"),
        ChapterItem("1.1", "Gradle 官方支持，但每个渠道都要重新编译，百渠道需数小时"),
        ChapterItem("2",   "Walle（美团）"),
        ChapterItem("2.1", "写入 APK Signing Block 的自定义 ID-Value，不破坏签名校验"),
        ChapterItem("3",   "VasDolly（腾讯）"),
        ChapterItem("3.1", "支持 V1/V2/V3 签名，写入 ZIP Comment 或 APK Signing Block"),
        ChapterItem("4",   "APK 签名校验"),
        ChapterItem("4.1", "V1=JAR签名，V2=整个文件签名，V3=支持密钥轮转，V4=增量更新"),
        ChapterItem("5",   "渠道信息读取"),
        ChapterItem("5.1", "运行时通过反射读取 APK Signing Block 或 ZIP Comment 获取渠道标识"),
        ChapterItem("6",   "自动化打包"),
        ChapterItem("6.1", "结合 Jenkins/GitHub Actions，一次触发批量生成所有渠道包并上传"),
    )
)
