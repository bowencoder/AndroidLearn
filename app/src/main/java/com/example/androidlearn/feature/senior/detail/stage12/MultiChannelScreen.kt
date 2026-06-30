package com.example.androidlearn.feature.senior.detail.stage12

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "多渠道打包方案",
    description = "Walle/VasDolly，APK 签名校验，渠道信息写入，自动化打包",
    overview = "多渠道打包是 Android 分发场景的刚需，需要针对不同应用市场（华为、小米、应用宝等）生成带渠道标识的 APK。传统 productFlavors 方式速度慢，Walle/VasDolly 利用 APK 签名块空白区域写入渠道信息，实现毫秒级批量打包。",
    keyPoints = listOf(
        "productFlavors 方案：Gradle 官方支持，但每个渠道都要重新编译，百渠道需数小时",
        "Walle（美团）：写入 APK Signing Block 的自定义 ID-Value，不破坏签名校验",
        "VasDolly（腾讯）：支持 V1/V2/V3 签名，写入 ZIP Comment 或 APK Signing Block",
        "APK 签名校验：V1=JAR签名，V2=整个文件签名，V3=支持密钥轮转，V4=增量更新",
        "渠道信息读取：运行时通过反射读取 APK Signing Block 或 ZIP Comment 获取渠道标识",
        "自动化打包：结合 Jenkins/GitHub Actions，一次触发批量生成所有渠道包并上传"
    ),
    codeSnippet = """
// Walle 渠道信息写入（打包脚本）
// $ java -jar walle-cli.jar put -c "huawei" app-release.apk app-huawei.apk

// 运行时读取渠道信息
fun getChannel(context: Context): String {
    return WalleChannelReader.getChannel(context) ?: "default"
}

// Gradle 批量打包脚本（使用 Walle Gradle 插件）
// build.gradle.kts
walle {
    apkOutputFolder = File("${'$'}{project.buildDir}/channels")
    apkFileNameFormat = '${'$'}{appName}-${'$'}{packageName}-${'$'}{channel}-${'$'}{buildType}-v${'$'}{versionName}.apk'
    channelFile = file("channel.txt") // 每行一个渠道名
}

// 执行打包
// $ ./gradlew assembleReleaseChannels

// channel.txt 示例：
// huawei
// xiaomi
// oppo
// vivo
// tencent
// baidu

// 验证 APK 签名
// $ apksigner verify --verbose app-release.apk
// $ apksigner verify --print-certs app-release.apk
    """.trimIndent(),
    tips = listOf(
        "V2 签名后不能修改 APK 内容（包括写入 ZIP Comment），Walle 利用的是 APK Signing Block 的扩展区域",
        "国内市场推荐 Walle，它的批量生成速度是 productFlavors 的数百倍",
        "打包完成后用 apksigner verify 校验每个渠道包的签名完整性"
    )
)

@Composable
fun MultiChannelScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF8BC34A),
        stageTitle = "插件化与热修复",
        onBack = onBack
    )
}
