package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Android Studio",
    description = "项目结构、调试、Logcat、模拟器",
    overview = "Android Studio 是基于 IntelliJ IDEA 的官方 IDE，熟悉其核心功能可以大幅提升开发效率。",
    keyPoints = listOf(
        "项目结构：app/src/main/java、res、AndroidManifest.xml",
        "Logcat：过滤日志、按 Tag/级别筛选、查看崩溃堆栈",
        "调试器：断点、Step Over/Into、变量监视、Evaluate Expression",
        "模拟器 AVD：创建不同版本设备、模拟网络/位置/传感器",
        "Gradle：sync 同步、Build Variants、APK 分析器",
        "Layout Inspector：实时查看 UI 层级与属性"
    ),
    codeSnippet = """
import android.util.Log

val TAG = "MyActivity"
Log.d(TAG, "Debug 信息")
Log.e(TAG, "错误信息", exception)
    """.trimIndent(),
    tips = listOf(
        "善用 Cmd+Shift+F 全局搜索，快速定位代码",
        "Logcat 中用 package:mine 只看当前应用日志",
        "遇到 Gradle 问题先 File → Invalidate Caches 重启"
    )
)

@Composable
fun AndroidStudioScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
