package com.example.androidlearn.feature.senior.detail.stage6

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Kotlin Multiplatform (KMM)",
    description = "共享业务逻辑，expect/actual，与 iOS 互操作",
    overview = "KMM 允许 Android 与 iOS 共享 Kotlin 业务逻辑层（Domain + Data），各平台保留原生 UI，兼顾代码复用与用户体验。",
    keyPoints = listOf(
        "共享模块：commonMain 编写跨平台代码，androidMain/iosMain 平台特定",
        "expect / actual：声明期望 API，各平台提供 actual 实现",
        "Ktor：KMM 友好的多平台 HTTP 客户端",
        "SQLDelight：多平台 SQLite 类型安全查询",
        "Compose Multiplatform：UI 层也跨平台（iOS 仍在 Alpha）",
        "与 iOS Swift 互操作：Kotlin/Native 编译为 .xcframework"
    ),
    codeSnippet = """
// commonMain - 共享业务逻辑
expect class PlatformInfo() {
    val name: String  // Android: "Android", iOS: "iOS"
}

class Greeting {
    private val platform = PlatformInfo()
    fun greet() = "Hello from ${'$'}{platform.name}!"
}

// androidMain
actual class PlatformInfo actual constructor() {
    actual val name = "Android ${"\$"}{android.os.Build.VERSION.SDK_INT}"
}

// iosMain
actual class PlatformInfo actual constructor() {
    actual val name = UIDevice.currentDevice.systemName() +
        " " + UIDevice.currentDevice.systemVersion
}
    """.trimIndent(),
    tips = listOf(
        "共享层只放纯逻辑，不放 Context/Activity 等平台 API",
        "gradlePlugin: org.jetbrains.kotlin.multiplatform 配置多平台目标",
        "KMM 成熟度：共享逻辑 = 稳定；共享 UI (CMP) = Beta，谨慎用于生产"
    )
)

@Composable
fun KmmScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "架构设计与前沿技术",
        onBack = onBack
    )
}
