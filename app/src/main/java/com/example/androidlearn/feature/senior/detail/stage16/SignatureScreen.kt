package com.example.androidlearn.feature.senior.detail.stage16

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【APK 签名机制】专属学习页
//  stageIndex=15, topicIndex=1
//  阶段颜色：青绿 0xFF00897B（工程化进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "APK 签名机制",
    description = "V1/V2/V3/V4 签名方案、密钥管理、签名校验流程与 App Bundle 签名",
    overview = "APK 签名是 Android 安全体系的核心。签名保证了 APK 来源可信和内容未被篡改，系统在安装和升级时会校验签名一致性。理解不同签名方案的原理和差异，是保障发布安全、处理签名升级和做多渠道包的必备知识。",
    keyPoints = listOf(
        "V1 签名（JAR 签名）：对每个文件单独签名并将摘要写入 META-INF/，可篡改 ZIP 目录区外的数据（有漏洞）",
        "V2 签名（APK 签名方案 v2，Android 7+）：对整个 APK 字节流签名，在 ZIP 中央目录前插入「APK 签名块」，防篡改",
        "V3 签名（Android 9+）：在 V2 基础上支持密钥轮换（Key Rotation），允许用新密钥替换旧密钥同时保持历史信任链",
        "V4 签名（Android 11+）：独立的 .idsig 文件，配合 ADB 增量安装，只传输修改的部分",
        "密钥管理：Keystore 文件存储私钥，需备份保管；Google Play App Signing 可由 Google 托管签名密钥",
        "签名校验：系统安装时校验签名块完整性 → 升级时校验新旧签名是否一致（或 V3 密钥链有效）"
    ),
    codeSnippet = """
// 生成签名密钥（keytool）
// $ keytool -genkey -v
//     -keystore my-release-key.jks     // 输出 Keystore 文件
//     -keyalg RSA -keysize 2048
//     -validity 10000                  // 有效期（天）
//     -alias my-key-alias

// 查看签名信息
// $ keytool -list -v -keystore my-release-key.jks
// $ apksigner verify --verbose --print-certs app-release.apk

// build.gradle.kts 配置签名
android {
    signingConfigs {
        create("release") {
            // 从环境变量读取（避免密钥硬编码到代码仓库）
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "keystore/release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

// V2 签名结构（APK 文件布局）
// ┌──────────────────────────────┐
// │     ZIP 文件内容区            │ ← 原始文件内容
// ├──────────────────────────────┤
// │     APK 签名块               │ ← V2/V3 签名数据（新增区域）
// │   （ID=0x7109871a）          │
// ├──────────────────────────────┤
// │     ZIP 中央目录              │
// ├──────────────────────────────┤
// │     ZIP 中央目录结束记录       │
// └──────────────────────────────┘

// 运行时校验签名（防二次打包）
fun verifySignature(context: Context): Boolean {
    val signatures = PackageInfoCompat.getSignatures(
        context.packageManager,
        context.packageName
    )
    val cert = signatures[0].toByteArray()
    val expectedMd5 = "你的正版签名 MD5"
    val actualMd5 = MessageDigest.getInstance("MD5")
        .digest(cert).joinToString("") { "%02x".format(it) }
    return actualMd5 == expectedMd5
}
    """.trimIndent(),
    tips = listOf(
        "签名密钥丢失后无法再发布更新（Play Store 同一 Package Name 必须同签名），务必多处备份 Keystore 文件",
        "Google Play App Signing 将密钥托管给 Google，即使本地 Keystore 丢失也可恢复，强烈推荐新 App 使用",
        "多渠道包（如友盟多渠道）在 V2 签名后通过修改 APK 注释区写入渠道信息，不破坏签名；Zip 内容区修改则会导致 V2 校验失败"
    )
)

@Composable
fun SignatureScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00897B),
        stageTitle = "工程化进阶",
        onBack = onBack
    )
}
