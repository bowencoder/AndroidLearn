package com.example.androidlearn.feature.senior.detail.stage14

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val apkBuildData = NoteData(
    title = "APK 编译与文件格式",
    subtitle = "AAPT2 资源编译、D8/R8 字节码编译、APK 结构拆解与 V2/V3 签名",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "编译流程"),
        ChapterItem("1.1", "AAPT2 编译资源 → kotlinc/javac → D8 转 DEX → apkbuilder → zipalign → apksigner"),
        ChapterItem("2",   "AAPT2"),
        ChapterItem("2.1", "将 res/ 资源编译为二进制 XML + resources.pb（proto 格式），支持增量编译"),
        ChapterItem("3",   "D8 编译器"),
        ChapterItem("3.1", "将 .class 字节码编译为 Dalvik DEX；R8 在 D8 基础上集成 ProGuard 混淆与裁剪"),
        ChapterItem("4",   "APK 结构"),
        ChapterItem("4.1", "AndroidManifest.xml（二进制）、classes.dex、res/、resources.arsc、lib/、assets/、META-INF/"),
        ChapterItem("5",   "resources.arsc"),
        ChapterItem("5.1", "资源 ID 到具体值的映射表，支持多语言/屏幕密度适配"),
        ChapterItem("6",   "V1/V2/V3 签名"),
        ChapterItem("6.1", "V1=JAR 签名（可篡改非签名文件）；V2=APK 整体签名（防篡改）；V3=轮换密钥支持"),
    )
)
