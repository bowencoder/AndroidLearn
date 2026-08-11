package com.example.androidlearn.feature.senior.detail.stage16

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val agpTransformData = NoteData(
    title = "AGP 与 Transform API",
    subtitle = "Android Gradle Plugin 架构、Task 依赖、Transform API（字节码插桩）与 AGP 8.x 新 API",
    color = Color.parseColor("#00897B"),
    chapters = listOf(
        ChapterItem("1",   "AGP 架构"),
        ChapterItem("1.1", "基于 Gradle 的 Task DAG（有向无环图），每个构建步骤是一个 Task"),
        ChapterItem("2",   "Transform API（AGP 7.x 前）"),
        ChapterItem("2.1", "在 .class → .dex 阶段插入字节码处理逻辑，可遍历所有 class 文件进行 AOP 插桩"),
        ChapterItem("3",   "AGP 8.x 新 API"),
        ChapterItem("3.1", "Transform 废弃，改用 Instrumentation API（TransformAction）和 AsmClassVisitorFactory"),
        ChapterItem("4",   "自定义 Gradle Plugin"),
        ChapterItem("4.1", "实现 Plugin<Project> 接口，通过 project.tasks.register 注册 Task，在 afterEvaluate 时配置依赖"),
        ChapterItem("5",   "Variant API"),
        ChapterItem("5.1", "访问 Build Variant（Debug/Release）的 Artifact，可在构建流程中修改 Manifest、资源、代码"),
        ChapterItem("6",   "构建缓存"),
        ChapterItem("6.1", "Gradle Build Cache 和 Configuration Cache 是加速构建的关键，Task 输入/输出声明完整才能命中缓存"),
    )
)
