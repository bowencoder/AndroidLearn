package com.example.androidlearn.feature.senior.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val gradlePluginData = NoteData(
    title = "自定义 Gradle 插件",
    subtitle = "buildSrc / Composite Build，Transform API，字节码插桩",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "buildSrc"),
        ChapterItem("1.1", "项目内 Gradle 插件，自动加入 classpath，适合小型插件"),
        ChapterItem("2",   "Composite Build"),
        ChapterItem("2.1", "独立插件工程，通过 includeBuild 引入，适合发布插件"),
        ChapterItem("3",   "Plugin<Project> 接口"),
        ChapterItem("3.1", "实现 apply() 配置项目"),
        ChapterItem("4",   "Transform API（AGP 7+）→ AsmClassVisitorFactory"),
        ChapterItem("4.1", "字节码变换"),
        ChapterItem("5",   "ASM"),
        ChapterItem("5.1", "轻量字节码操作框架，插入方法耗时统计、日志等"),
        ChapterItem("6",   "Task 依赖"),
        ChapterItem("6.1", "dependsOn / finalizedBy 控制任务执行顺序"),
    )
)
