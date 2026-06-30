package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "包体积优化",
    description = "R8/ProGuard，资源压缩，App Bundle，动态功能模块",
    overview = "APK 体积影响下载转化率，每减少 10MB 可提升约 1% 的安装率。需从代码、资源、Native 库三个维度综合治理。",
    keyPoints = listOf(
        "R8 全模式：比 ProGuard 更激进的死代码删除和优化",
        "资源压缩：shrinkResources = true 删除未引用资源",
        "App Bundle（AAB）：Google Play 按设备按需下发，平均节省 15-35%",
        "ABI 过滤：abiFilters 只打包目标架构的 .so 文件",
        "WebP 转换：lossless 模式下比 PNG 小 26%，支持透明",
        "Dynamic Feature Module：将大功能模块做成按需下载"
    ),
    codeSnippet = """
// build.gradle.kts
android {
    buildTypes {
        release {
            isMinifyEnabled = true       // R8 开启
            isShrinkResources = true     // 资源压缩
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // 只保留 arm64 和 x86_64
    defaultConfig {
        ndk { abiFilters += listOf("arm64-v8a", "x86_64") }
    }
    bundle {
        language { enableSplit = true }
        density  { enableSplit = true }
        abi      { enableSplit = true }
    }
}
    """.trimIndent(),
    tips = listOf(
        "用 APK Analyzer 查看各部分大小占比，找到优化重点",
        "第三方 SDK 未使用的功能通过 Proguard 规则主动剔除",
        "图片资源使用 VectorDrawable（SVG），无需多套分辨率"
    )
)

@Composable
fun PackageSizeScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
