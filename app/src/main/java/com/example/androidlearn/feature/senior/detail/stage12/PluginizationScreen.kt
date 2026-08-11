package com.example.androidlearn.feature.senior.detail.stage12

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val pluginizationData = NoteData(
    title = "插件化原理与实践",
    subtitle = "VirtualAPK、Shadow 框架，Hook 技术，动态加载 Activity/Service",
    color = Color.parseColor("#8BC34A"),
    chapters = listOf(
        ChapterItem("1",   "核心原理"),
        ChapterItem("1.1", "用 DexClassLoader 加载插件 APK，Hook AMS/PMS 欺骗系统完成组件注册"),
        ChapterItem("2",   "Activity 插件化"),
        ChapterItem("2.1", "预注册占坑 Activity，在 H.handleMessage 处 Hook 替换为插件 Activity"),
        ChapterItem("3",   "资源插件化"),
        ChapterItem("3.1", "反射 AssetManager.addAssetPath() 加载插件资源，或合并 Resources"),
        ChapterItem("4",   "VirtualAPK（滴滴）"),
        ChapterItem("4.1", "功能全面，支持四大组件，业界最早成熟方案之一"),
        ChapterItem("5",   "Shadow（腾讯）"),
        ChapterItem("5.1", "零反射，利用 Fragment 模拟 Activity 生命周期，兼容性好"),
        ChapterItem("6",   "类加载隔离"),
        ChapterItem("6.1", "每个插件独立 ClassLoader，父委托链接宿主 ClassLoader"),
    )
)
