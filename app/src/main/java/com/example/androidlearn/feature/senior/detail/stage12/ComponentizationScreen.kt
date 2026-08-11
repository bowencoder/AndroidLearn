package com.example.androidlearn.feature.senior.detail.stage12

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val componentizationData = NoteData(
    title = "组件化架构设计",
    subtitle = "路由框架（ARouter），模块间通信，组件独立运行，依赖治理",
    color = Color.parseColor("#8BC34A"),
    chapters = listOf(
        ChapterItem("1",   "分层架构"),
        ChapterItem("1.1", "app壳 > 业务模块 > 功能模块 > 基础模块，依赖方向单向向下"),
        ChapterItem("2",   "ARouter 路由"),
        ChapterItem("2.1", "@Route 标注目标页面，ARouter.getInstance().build('/module/page').navigation()"),
        ChapterItem("3",   "模块间通信"),
        ChapterItem("3.1", "接口下沉 + 实现上移，通过服务发现（IProvider）实现解耦调用"),
        ChapterItem("4",   "组件独立运行"),
        ChapterItem("4.1", "每个模块可配置 isModule=true 单独打包成 App 调试"),
        ChapterItem("5",   "资源隔离"),
        ChapterItem("5.1", "各模块 res 文件名加前缀（如 home_、user_）防止资源命名冲突"),
        ChapterItem("6",   "Gradle 依赖治理"),
        ChapterItem("6.1", "Convention Plugin 统一版本管理，避免各模块版本不一致"),
    )
)
