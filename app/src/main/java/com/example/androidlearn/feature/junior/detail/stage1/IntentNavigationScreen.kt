package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Intent 与页面跳转",
    description = "显式/隐式 Intent，数据传递，结果回调",
    overview = "Intent 是 Android 组件间通信的核心机制，用于启动 Activity、Service 或发送广播。",
    keyPoints = listOf(
        "显式 Intent：指定目标组件类名，用于应用内跳转",
        "隐式 Intent：指定 Action/Category，系统匹配合适组件",
        "putExtra：传递基本类型、String、Parcelable 数据",
        "startActivityForResult → ActivityResultLauncher（新 API）",
        "Intent Flags：FLAG_ACTIVITY_CLEAR_TOP 等控制回退栈",
        "Deep Link：通过 URI 从外部打开应用特定页面"
    ),
    codeSnippet = """
val intent = Intent(this, DetailActivity::class.java)
intent.putExtra("id", 42)
startActivity(intent)

// 接收结果（新 API）
val launcher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == RESULT_OK) {
        val data = result.data?.getStringExtra("key")
    }
}
    """.trimIndent(),
    tips = listOf(
        "使用 ActivityResultLauncher 替代已废弃的 startActivityForResult",
        "传递复杂对象实现 Parcelable，性能优于 Serializable",
        "隐式 Intent 发送前用 resolveActivity 检查是否有应用可处理"
    )
)

@Composable
fun IntentNavigationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
