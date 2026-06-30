package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Kotlin 基础语法",
    description = "变量、函数、类、Lambda、扩展函数",
    overview = "Kotlin 是 Android 官方首选语言，以简洁、安全著称。掌握核心语法是 Android 开发的第一步。",
    keyPoints = listOf(
        "val / var：不可变与可变变量，优先使用 val",
        "数据类 data class：自动生成 equals / hashCode / copy",
        "空安全：? 可空类型、?. 安全调用、?: Elvis 运算符",
        "Lambda 表达式：函数类型、高阶函数、it 隐式参数",
        "扩展函数：给已有类添加方法，无需继承",
        "作用域函数：let / run / apply / also / with"
    ),
    codeSnippet = """
val name: String = "Android"
var count = 0

// 扩展函数
fun String.greet() = "Hello, ${'$'}this!"

// Lambda
val sum = { a: Int, b: Int -> a + b }

// 空安全
val len = name?.length ?: 0
    """.trimIndent(),
    tips = listOf(
        "优先使用 val，只有需要修改时才用 var",
        "善用作用域函数减少临时变量",
        "使用 data class 代替普通 POJO"
    )
)

@Composable
fun KotlinSyntaxScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
