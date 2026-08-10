package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Navigation Component
 * 官方文档：https://developer.android.com/guide/navigation
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心组件
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  NavHost & NavController ─────────────────────────────────────────────
 *
 *  · NavHost：导航容器，定义所有目的地
 *  · NavController：导航控制器，执行跳转和返回
 *  · rememberNavController()：在 Compose 中创建 NavController
 *
 * ── 1.2  定义路由 ─────────────────────────────────────────────────────────────
 *
 *  NavHost(navController, startDestination = "home") {
 *      composable("home") { HomeScreen(navController) }
 *      composable(
 *          "detail/{id}",
 *          arguments = listOf(navArgument("id") { type = NavType.IntType })
 *      ) { backStack ->
 *          val id = backStack.arguments?.getInt("id") ?: 0
 *          DetailScreen(id = id)
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  导航操作
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  跳转与返回 ───────────────────────────────────────────────────────────
 *
 *  navController.navigate("detail/42")      // 跳转并传参
 *  navController.popBackStack()             // 返回上一页
 *  navController.navigate("home") {         // 清栈跳首页
 *      popUpTo("home") { inclusive = true }
 *  }
 *
 * ── 2.2  底部导航同步 ─────────────────────────────────────────────────────────
 *
 *  · currentBackStackEntryAsState()：监听当前路由，同步底部导航选中状态
 *
 *  val currentRoute by navController.currentBackStackEntryAsState()
 *  val isSelected = currentRoute?.destination?.route == tab.route
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  高级特性
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  Deep Link ────────────────────────────────────────────────────────────
 *
 *  · URI 映射到页面，支持外部 App 跳转
 *
 *  composable(
 *      "detail/{id}",
 *      deepLinks = listOf(navDeepLink { uriPattern = "myapp://detail/{id}" })
 *  ) { ... }
 *
 * ── 3.2  嵌套导航图 ───────────────────────────────────────────────────────────
 *
 *  · 复杂应用可用嵌套导航图组织路由模块
 *
 *  NavHost(...) {
 *      navigation(startDestination = "login", route = "auth") {
 *          composable("login") { LoginScreen() }
 *          composable("register") { RegisterScreen() }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 使用 launchSingleTop = true 防止重复创建页面实例
 *  · 配合 saveState / restoreState 保存 Tab 切换时的页面状态
 *  · 复杂应用可用嵌套导航图组织路由模块
 */

val navigationComponentData = NoteData(
    title = "Navigation Component",
    subtitle = "现代架构体系 · 导航图 · 参数传递 · 深链接",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "核心组件"),
        ChapterItem("1.1", "NavHost & NavController"),
        ChapterItem("1.2", "定义路由"),
        ChapterItem("2",   "导航操作"),
        ChapterItem("2.1", "跳转与返回"),
        ChapterItem("2.2", "底部导航同步"),
        ChapterItem("3",   "高级特性"),
        ChapterItem("3.1", "Deep Link"),
        ChapterItem("3.2", "嵌套导航图"),
        ChapterItem("4",   "最佳实践"),
    )
)
