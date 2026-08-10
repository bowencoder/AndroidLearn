package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 组件化 / 模块化
 * 官方文档：https://developer.android.com/topic/modularization
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  模块划分
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  功能模块 ─────────────────────────────────────────────────────────────
 *
 *  · 按业务功能拆分：feature:home / feature:search / feature:profile
 *  · 每个 feature 模块只包含该功能的 UI 和业务逻辑
 *  · feature 模块依赖 core 模块，不互相依赖
 *
 * ── 1.2  基础模块 ─────────────────────────────────────────────────────────────
 *
 *  · core:network：网络请求封装（Retrofit / OkHttp）
 *  · core:database：数据库封装（Room）
 *  · core:ui：公共 UI 组件、主题、样式
 *  · core:common：工具类、扩展函数
 *
 *  // settings.gradle.kts
 *  include(":app")
 *  include(":core:network")
 *  include(":core:ui")
 *  include(":feature:home")
 *  include(":feature:profile")
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  模块间通信
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  接口依赖倒置 ─────────────────────────────────────────────────────────
 *
 *  · 模块间通过公共接口依赖，不直接依赖实现类
 *  · 接口定义在 core 层，实现在 feature 层
 *  · 通过 Hilt 注入接口实现
 *
 * ── 2.2  依赖管理 ─────────────────────────────────────────────────────────────
 *
 *  · libs.versions.toml 统一版本管理
 *  · convention plugin 统一各模块 Gradle 配置
 *
 *  // feature:home/build.gradle.kts
 *  dependencies {
 *      implementation(project(":core:network"))
 *      implementation(project(":core:ui"))
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Dynamic Feature Module
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 按需下载功能，减少安装包体积
 *  · 用户触发时才下载对应功能模块
 *  · 适合：地图、AR、高级编辑等非核心功能
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 从单模块逐步拆分，先抽 core 层，再拆 feature 层
 *  · 模块间通信优先通过 Hilt 注入接口实现
 *  · 用 convention plugin 统一各模块 Gradle 配置
 */

val modularizationData = NoteData(
    title = "组件化 / 模块化",
    subtitle = "进阶开发能力 · 多 Module 拆分 · 动态功能模块",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("1",   "模块划分"),
        ChapterItem("1.1", "功能模块"),
        ChapterItem("1.2", "基础模块"),
        ChapterItem("2",   "模块间通信"),
        ChapterItem("2.1", "接口依赖倒置"),
        ChapterItem("2.2", "依赖管理"),
        ChapterItem("3",   "Dynamic Feature Module"),
        ChapterItem("4",   "最佳实践"),
    )
)
