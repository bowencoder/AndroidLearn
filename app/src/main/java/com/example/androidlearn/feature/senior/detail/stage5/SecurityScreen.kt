package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val securityData = NoteData(
    title = "安全与代码保护",
    subtitle = "HTTPS 证书固定，数据加密，代码混淆，Root 检测",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "证书固定（Certificate Pinning）"),
        ChapterItem("1.1", "防中间人，校验服务器证书指纹"),
        ChapterItem("2",   "Network Security Config"),
        ChapterItem("2.1", "XML 配置 HTTPS 策略，禁止明文传输"),
        ChapterItem("3",   "数据加密"),
        ChapterItem("3.1", "AES-256-GCM 加密本地敏感数据"),
        ChapterItem("3.2", "AndroidKeyStore 管理密钥"),
        ChapterItem("4",   "R8 混淆"),
        ChapterItem("4.1", "方法/类名混淆，反编译只能看到 a/b/c 等无意义名称"),
        ChapterItem("5",   "Root/越狱检测"),
        ChapterItem("5.1", "检查 su 文件、Magisk、RootBeer 库"),
        ChapterItem("6",   "代码完整性"),
        ChapterItem("6.1", "APK 签名校验，防止二次打包"),
    )
)
