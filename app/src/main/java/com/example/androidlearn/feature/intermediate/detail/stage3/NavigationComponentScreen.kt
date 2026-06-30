package com.example.androidlearn.feature.intermediate.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Navigation Component",
    description = "导航图、SafeArgs、深链接",
    overview = "Navigation Component 统一管理应用内导航，支持 Fragment、Compose、深链接，解决回退栈管理难题。",
    keyPoints = listOf(
        "NavHost / NavController：导航容器和控制器",
        "composable(\"route\")：定义导航目的地",
        "navigate(route) / popBackStack()：跳转与返回",
        "参数传递：路由中定义 {arg}，通过 backStackEntry.arguments 获取",
        "Deep Link：URI 映射到页面，支持外部跳转",
        "底部导航：currentBackStackEntryAsState 同步选中状态"
    ),
    codeSnippet = """
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable(
        "detail/{id}",
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStack ->
        val id = backStack.arguments?.getInt("id") ?: 0
        DetailScreen(id = id)
    }
}

// 跳转
navController.navigate("detail/42")
    """.trimIndent(),
    tips = listOf(
        "使用 launchSingleTop = true 防止重复创建页面实例",
        "配合 saveState / restoreState 保存 Tab 切换时的页面状态",
        "复杂应用可用嵌套导航图组织路由模块"
    )
)

@Composable
fun NavigationComponentScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF9C27B0),
        stageTitle = "现代架构体系",
        onBack = onBack
    )
}
