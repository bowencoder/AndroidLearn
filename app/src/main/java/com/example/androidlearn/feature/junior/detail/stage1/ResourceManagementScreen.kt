package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "资源管理",
    description = "strings.xml、colors.xml、dimens.xml、图片资源",
    overview = "Android 资源系统支持多语言、多分辨率和不同配置的适配，合理管理资源是国际化和适配的基础。",
    keyPoints = listOf(
        "strings.xml：字符串资源，支持多语言 i18n",
        "colors.xml：颜色定义，统一管理主题色",
        "dimens.xml：尺寸规范，dp/sp 单位",
        "drawable：矢量图 VectorDrawable、点九图、Shape",
        "mipmap：应用图标专用目录，不同分辨率图标",
        "资源限定符：values-zh-rCN、layout-land（横屏）"
    ),
    codeSnippet = """
<!-- res/values/strings.xml -->
<resources>
    <string name="app_name">Android Learn</string>
    <string name="welcome">欢迎，%1${'$'}s！</string>
</resources>

// 在代码中使用
val appName = getString(R.string.app_name)
val welcome = getString(R.string.welcome, "用户")
    """.trimIndent(),
    tips = listOf(
        "所有用户可见文字放入 strings.xml，方便国际化",
        "图标使用 VectorDrawable（SVG），无需多套分辨率",
        "颜色和尺寸集中定义，修改主题只需改一处"
    )
)

@Composable
fun ResourceManagementScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
