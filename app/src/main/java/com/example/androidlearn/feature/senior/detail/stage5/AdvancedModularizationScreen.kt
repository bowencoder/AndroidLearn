package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "深度组件化架构",
    description = "多 Module 依赖治理，Convention Plugin，接口解耦",
    overview = "大型项目的模块化不只是拆文件夹，核心是依赖方向控制、编译速度优化和模块间通信解耦，需要系统性设计。",
    keyPoints = listOf(
        "依赖层次：app → feature → domain → data → core，禁止反向依赖",
        "Convention Plugin：将重复的 Gradle 配置提取为 buildSrc Plugin",
        "接口模块：feature 间通过 :feature:xxx:api 模块暴露接口，隔离实现",
        "Gradle 配置缓存：configurationCache = true 大幅加速二次构建",
        "模块化 Hilt：@InstallIn 绑定模块生命周期，跨模块注入",
        "Baseline Profile：预编译热路径代码，提升运行期 JIT 效率"
    ),
    codeSnippet = """
// buildSrc/src/main/kotlin/AndroidFeaturePlugin.kt
class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")
            pluginManager.apply("com.google.dagger.hilt.android")
            androidExtension.defaultConfig.minSdk = 24
        }
    }
}

// feature:home:api/build.gradle.kts
plugins { id("android-lib-convention") }
// 只暴露接口，不依赖实现细节

// settings.gradle.kts
gradle.startParameter.isContinuous = false
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
    """.trimIndent(),
    tips = listOf(
        "先画依赖图再写代码，有向无环图（DAG）是模块化的核心约束",
        "Convention Plugin 让所有模块 Gradle 配置一致，降低维护成本",
        "启用 --scan 生成构建报告，找到编译耗时的任务"
    )
)

@Composable
fun AdvancedModularizationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
