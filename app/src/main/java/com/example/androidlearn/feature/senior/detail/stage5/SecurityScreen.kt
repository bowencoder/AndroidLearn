package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "安全与代码保护",
    description = "HTTPS 证书固定，数据加密，代码混淆，Root 检测",
    overview = "移动端安全包括通信安全、数据安全和代码安全三层，需要防中间人攻击、数据泄露和逆向破解。",
    keyPoints = listOf(
        "证书固定（Certificate Pinning）：防中间人，校验服务器证书指纹",
        "Network Security Config：XML 配置 HTTPS 策略，禁止明文传输",
        "数据加密：AES-256-GCM 加密本地敏感数据，AndroidKeyStore 管理密钥",
        "R8 混淆：方法/类名混淆，反编译只能看到 a/b/c 等无意义名称",
        "Root/越狱检测：检查 su 文件、Magisk、RootBeer 库",
        "代码完整性：APK 签名校验，防止二次打包"
    ),
    codeSnippet = """
// OkHttp 证书固定
val certificatePinner = CertificatePinner.Builder()
    .add("api.example.com",
         "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
    .build()

val client = OkHttpClient.Builder()
    .certificatePinner(certificatePinner)
    .build()

// AndroidKeyStore 生成密钥
val keyGenerator = KeyGenerator.getInstance(
    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
)
keyGenerator.init(
    KeyGenParameterSpec.Builder("my_key",
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
)
    """.trimIndent(),
    tips = listOf(
        "密钥不要硬编码在代码里，使用 AndroidKeyStore 硬件级安全存储",
        "Debug 包关闭混淆，Release 包必须开启混淆+资源压缩",
        "定期更换证书 Pin，避免证书到期导致 App 无法访问"
    )
)

@Composable
fun SecurityScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
