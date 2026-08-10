package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Jetpack Compose 入门
 * 官方文档：https://developer.android.com/jetpack/compose
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心概念
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  @Composable 函数 ─────────────────────────────────────────────────────
 *
 *  · @Composable：标记可组合函数，描述 UI
 *  · 无返回值，直接描述 UI 树
 *  · 可以调用其他 @Composable 函数
 *
 *  @Composable
 *  fun Greeting(name: String) {
 *      Text("Hello, $name!")
 *  }
 *
 * ── 1.2  状态管理 ─────────────────────────────────────────────────────────────
 *
 *  · remember：在重组间保持状态，不跨越 Composable 生命周期
 *  · rememberSaveable：跨越配置变更（旋转屏幕）保持状态
 *  · 状态提升：将 state 上移到父级，提升可复用性
 *
 *  var count by remember { mutableStateOf(0) }
 *
 * ── 1.3  Modifier ─────────────────────────────────────────────────────────────
 *
 *  · 链式修饰符，控制尺寸、间距、点击、背景等
 *  · 顺序很重要：padding 在 background 前后效果不同
 *
 *  Modifier
 *      .fillMaxWidth()
 *      .padding(16.dp)
 *      .background(Color.Blue)
 *      .clickable { }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  布局系统
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  基础布局 ─────────────────────────────────────────────────────────────
 *
 *  · Column：垂直排列
 *  · Row：水平排列
 *  · Box：层叠布局（类似 FrameLayout）
 *
 * ── 2.2  列表布局 ─────────────────────────────────────────────────────────────
 *
 *  · LazyColumn：垂直懒加载列表（类似 RecyclerView）
 *  · LazyRow：水平懒加载列表
 *  · LazyVerticalGrid：网格布局
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  重组与副作用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  重组机制 ─────────────────────────────────────────────────────────────
 *
 *  · 状态变化时只更新用到该状态的 Composable
 *  · 重组可能频繁发生，Composable 函数应保持幂等
 *
 * ── 3.2  副作用 API ───────────────────────────────────────────────────────────
 *
 *  · LaunchedEffect(key)：key 变化时重新执行协程
 *  · SideEffect：每次重组后执行（同步副作用）
 *  · DisposableEffect：需要清理的副作用（注册/注销监听器）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 保持 Composable 函数无副作用，副作用用 LaunchedEffect
 *  · 状态提升：将 state 上移到父级，提升可复用性
 *  · 用 @Preview 注解实时预览，无需运行模拟器
 */

val jetpackComposeData = NoteData(
    title = "Jetpack Compose 入门",
    subtitle = "现代架构体系 · 声明式 UI · 状态管理 · 布局系统",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "核心概念"),
        ChapterItem("1.1", "@Composable 函数"),
        ChapterItem("1.2", "状态管理"),
        ChapterItem("1.3", "Modifier"),
        ChapterItem("2",   "布局系统"),
        ChapterItem("2.1", "基础布局"),
        ChapterItem("2.2", "列表布局"),
        ChapterItem("3",   "重组与副作用"),
        ChapterItem("3.1", "重组机制"),
        ChapterItem("3.2", "副作用 API"),
        ChapterItem("4",   "最佳实践"),
    )
)
