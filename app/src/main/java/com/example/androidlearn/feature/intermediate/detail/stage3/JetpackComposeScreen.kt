package com.example.androidlearn.feature.intermediate.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Jetpack Compose",
    description = "声明式 UI，状态管理，布局系统",
    overview = "Jetpack Compose 是 Android 现代 UI 框架，用声明式代码描述 UI，状态驱动界面自动重组。",
    keyPoints = listOf(
        "@Composable：标记可组合函数，描述 UI",
        "State & remember：在重组间保持状态",
        "Modifier：链式修饰符，控制尺寸、间距、点击、背景",
        "布局：Column / Row / Box / LazyColumn / LazyGrid",
        "重组：状态变化时只更新用到该状态的 Composable",
        "副作用：LaunchedEffect / SideEffect / DisposableEffect"
    ),
    codeSnippet = """
@Composable
fun Greeting(name: String) {
    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello, ${'$'}name! 点击了 ${'$'}count 次")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { count++ }) { Text("点击我") }
    }
}
    """.trimIndent(),
    tips = listOf(
        "保持 Composable 函数无副作用，副作用用 LaunchedEffect",
        "状态提升：将 state 上移到父级，提升可复用性",
        "用 @Preview 注解实时预览，无需运行模拟器"
    )
)

@Composable
fun JetpackComposeScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF9C27B0),
        stageTitle = "现代架构体系",
        onBack = onBack
    )
}
