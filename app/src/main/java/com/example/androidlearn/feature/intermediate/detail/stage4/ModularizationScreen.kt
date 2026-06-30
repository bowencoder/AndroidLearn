package com.example.androidlearn.feature.intermediate.detail.stage4

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "组件化/模块化",
    description = "多 Module 拆分，动态功能模块",
    overview = "模块化将大型应用拆分为独立 Module，提升编译速度、代码隔离性和团队协作效率。",
    keyPoints = listOf(
        "功能模块：按业务拆分 feature:home / feature:search",
        "基础模块：core:network / core:database / core:ui",
        "模块通信：接口依赖倒置，通过公共接口依赖",
        "Dynamic Feature Module：按需下载功能，减少安装包体积",
        "编译优化：并行编译独立 Module，增量编译",
        "依赖管理：libs.versions.toml 统一版本"
    ),
    codeSnippet = """
// settings.gradle.kts
include(":app")
include(":core:network")
include(":core:ui")
include(":feature:home")
include(":feature:profile")

// feature:home/build.gradle.kts
dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
}
    """.trimIndent(),
    tips = listOf(
        "从单模块逐步拆分，先抽 core 层，再拆 feature 层",
        "模块间通信优先通过 Hilt 注入接口实现",
        "用 convention plugin 统一各模块 Gradle 配置"
    )
)

@Composable
fun ModularizationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF9800),
        stageTitle = "进阶开发能力",
        onBack = onBack
    )
}
