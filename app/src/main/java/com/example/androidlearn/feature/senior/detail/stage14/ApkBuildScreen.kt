package com.example.androidlearn.feature.senior.detail.stage14

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【APK 编译与文件格式】专属学习页
//  stageIndex=13, topicIndex=0
//  阶段颜色：深蓝紫 0xFF3F51B5（系统核心原理）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "APK 编译与文件格式",
    description = "AAPT2 资源编译、D8/R8 字节码编译、APK 结构拆解与 V2/V3 签名",
    overview = "APK 是 Android 应用的分发格式，本质是 ZIP 压缩包。理解从源码到 APK 的完整编译流程，以及 APK 内部结构和签名机制，是做包体优化、逆向分析和构建系统定制的基础。",
    keyPoints = listOf(
        "编译流程：AAPT2 编译资源 → kotlinc/javac 编译代码 → D8 转 DEX → apkbuilder 打包 → zipalign 对齐 → apksigner 签名",
        "AAPT2：将 res/ 资源编译为二进制 XML + resources.pb（proto 格式），支持增量编译",
        "D8 编译器：将 .class 字节码编译为 Dalvik DEX；R8 在 D8 基础上集成 ProGuard 混淆与裁剪",
        "APK 结构：AndroidManifest.xml（二进制）、classes.dex、res/、resources.arsc、lib/（so）、assets/、META-INF/（签名）",
        "resources.arsc：资源 ID 到具体值的映射表，支持多语言/屏幕密度适配",
        "V1/V2/V3 签名：V1=JAR 签名（可篡改非签名文件）；V2=APK 整体签名（防篡改）；V3=轮换密钥支持"
    ),
    codeSnippet = """
// APK 结构速览（unzip -l app-release.apk）
// ├── AndroidManifest.xml    ← 二进制 XML（AXML 格式）
// ├── classes.dex            ← 主 DEX
// ├── classes2.dex           ← 多 DEX（MultiDex）
// ├── resources.arsc         ← 资源映射表
// ├── res/
// │   ├── layout/activity_main.xml   ← 二进制 XML
// │   └── drawable-xxhdpi/ic_launcher.png
// ├── lib/
// │   ├── arm64-v8a/libnative.so
// │   └── armeabi-v7a/libnative.so
// ├── assets/               ← 原始文件，不编译
// └── META-INF/
//     ├── MANIFEST.MF       ← V1 签名文件列表
//     ├── CERT.SF           ← V1 签名摘要
//     └── CERT.RSA          ← V1 签名块（包含公钥证书）

// 查看签名信息
// $ apksigner verify --verbose app-release.apk
// $ keytool -printcert -jarfile app-release.apk

// R8 混淆配置片段（proguard-rules.pro）
-keep class com.example.model.** { *; }
-keepattributes Signature, InnerClasses
-dontobfuscate   // 仅裁剪，不混淆（调试用）

// build.gradle 开启 R8
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                          'proguard-rules.pro'
        }
    }
}
    """.trimIndent(),
    tips = listOf(
        "zipalign 将 APK 中未压缩文件按 4 字节对齐，减少运行时内存映射开销，必须在签名前执行",
        "Android App Bundle（.aab）是更现代的分发格式，由 Google Play 动态生成适配设备的 APK",
        "使用 jadx 或 apktool 可以将 APK 反编译为可读代码，是逆向分析和 APK 瘦身分析的常用工具"
    )
)

@Composable
fun ApkBuildScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "Android 系统核心原理",
        onBack = onBack
    )
}
