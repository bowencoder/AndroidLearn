package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Fragment",
    description = "Fragment 生命周期、FragmentManager、回退栈",
    overview = "Fragment 是可复用的 UI 片段，支持在 Activity 中动态添加/替换，是模块化 UI 的基础单元。",
    keyPoints = listOf(
        "生命周期：onAttach → onCreate → onCreateView → onResume → onDestroyView",
        "FragmentManager：add / replace / remove / commit 操作",
        "回退栈：addToBackStack，按返回键弹出 Fragment",
        "Fragment 通信：ViewModel 共享、setFragmentResult",
        "DialogFragment：用 Fragment 实现对话框，旋转屏幕不消失",
        "ViewPager2 + Fragment：滑动页面容器"
    ),
    codeSnippet = """
supportFragmentManager.beginTransaction()
    .replace(R.id.container, MyFragment())
    .addToBackStack(null)
    .commit()

// Fragment 间传递数据
setFragmentResult("key", bundleOf("data" to "value"))
parentFragmentManager.setFragmentResultListener("key", this) { _, bundle ->
    val result = bundle.getString("data")
}
    """.trimIndent(),
    tips = listOf(
        "优先使用 ViewModel 在 Fragment 间共享数据",
        "使用 viewLifecycleOwner 而非 this 作为 Observer",
        "Compose 时代可用 NavHost 代替 FragmentManager"
    )
)

@Composable
fun FragmentScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "核心UI与数据组件",
        onBack = onBack
    )
}
