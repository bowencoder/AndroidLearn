package com.example.androidlearn.feature.senior.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val advancedModularizationData = NoteData(
    title = "深度组件化架构",
    subtitle = "多 Module 依赖治理，Convention Plugin，接口解耦",
    color = Color.parseColor("#FF5722"),
    chapters = listOf(
        ChapterItem("1",   "依赖层次"),
        ChapterItem("1.1", "app → feature → domain → data → core，禁止反向依赖"),
        ChapterItem("2",   "Convention Plugin"),
        ChapterItem("2.1", "将重复的 Gradle 配置提取为 buildSrc Plugin"),
        ChapterItem("3",   "接口模块"),
        ChapterItem("3.1", "feature 间通过 :feature:xxx:api 模块暴露接口，隔离实现"),
        ChapterItem("4",   "Gradle 配置缓存"),
        ChapterItem("4.1", "configurationCache = true 大幅加速二次构建"),
        ChapterItem("5",   "模块化 Hilt"),
        ChapterItem("5.1", "@InstallIn 绑定模块生命周期，跨模块注入"),
        ChapterItem("6",   "Baseline Profile"),
        ChapterItem("6.1", "预编译热路径代码，提升运行期 JIT 效率"),
    )
)
