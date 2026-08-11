package com.example.androidlearn.feature.senior.detail.stage16

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val signatureData = NoteData(
    title = "APK 签名机制",
    subtitle = "V1/V2/V3/V4 签名方案、密钥管理、签名校验流程与 App Bundle 签名",
    color = Color.parseColor("#00897B"),
    chapters = listOf(
        ChapterItem("1",   "V1 签名（JAR 签名）"),
        ChapterItem("1.1", "对每个文件单独签名并将摘要写入 META-INF/，可篡改 ZIP 目录区外的数据（有漏洞）"),
        ChapterItem("2",   "V2 签名（Android 7+）"),
        ChapterItem("2.1", "对整个 APK 字节流签名，在 ZIP 中央目录前插入「APK 签名块」，防篡改"),
        ChapterItem("3",   "V3 签名（Android 9+）"),
        ChapterItem("3.1", "在 V2 基础上支持密钥轮换（Key Rotation），允许用新密钥替换旧密钥同时保持历史信任链"),
        ChapterItem("4",   "V4 签名（Android 11+）"),
        ChapterItem("4.1", "独立的 .idsig 文件，配合 ADB 增量安装，只传输修改的部分"),
        ChapterItem("5",   "密钥管理"),
        ChapterItem("5.1", "Keystore 文件存储私钥，需备份保管；Google Play App Signing 可由 Google 托管签名密钥"),
        ChapterItem("6",   "签名校验"),
        ChapterItem("6.1", "系统安装时校验签名块完整性 → 升级时校验新旧签名是否一致（或 V3 密钥链有效）"),
    )
)
