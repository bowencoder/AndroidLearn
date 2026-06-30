package com.example.androidlearn.feature.senior.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "渲染与内存优化",
    description = "Perfetto 分析，Compose 重组优化，LeakCanary，MAT",
    overview = "60/120fps 流畅渲染要求每帧 8-16ms 内完成，内存泄漏导致 OOM，两者都是高级工程师必须精通的优化领域。",
    keyPoints = listOf(
        "过度绘制：开发者选项开启色块检测，减少 overdraw",
        "Compose Layout Inspector：查看重组次数，定位频繁重组的 Composable",
        "@Stable / @Immutable：告知编译器类型稳定，跳过不必要重组",
        "derivedStateOf：派生状态，只在计算结果变化时触发重组",
        "LeakCanary：自动检测 Activity/Fragment/ViewModel 内存泄漏",
        "Android Studio Memory Profiler：Heap Dump 分析对象引用链"
    ),
    codeSnippet = """
// 用 @Stable 标记稳定类，优化重组
@Stable
data class UserState(val name: String, val age: Int)

// derivedStateOf 减少重组
val isScrolled by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 }
}

// 避免在 Composable 中创建 Lambda（每次重组都创建新对象）
// 错误写法：
// Button(onClick = { viewModel.doAction() })

// 正确写法：
val onClick = remember { { viewModel.doAction() } }
Button(onClick = onClick)
    """.trimIndent(),
    tips = listOf(
        "Layout Inspector 的 Recomposition counts 直接显示每个节点重组次数",
        "内存泄漏 90% 来自 Context 被静态变量或单例持有",
        "用 WeakReference 持有外部引用，打破泄漏引用链"
    )
)

@Composable
fun RenderOptimizationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF5722),
        stageTitle = "性能优化与工程化",
        onBack = onBack
    )
}
