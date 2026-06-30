package com.example.androidlearn.feature.senior.detail.stage12

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "插件化原理与实践",
    description = "VirtualAPK、Shadow 框架，Hook 技术，动态加载 Activity/Service",
    overview = "插件化技术使 App 可以在不发版的情况下动态加载新功能模块（插件 APK）。核心难点在于：Activity/Service 等组件的生命周期管理、资源加载隔离和类加载器隔离。",
    keyPoints = listOf(
        "核心原理：用 DexClassLoader 加载插件 APK，Hook AMS/PMS 欺骗系统完成组件注册",
        "Activity 插件化：预注册占坑 Activity，在 H.handleMessage 处 Hook 替换为插件 Activity",
        "资源插件化：反射 AssetManager.addAssetPath() 加载插件资源，或合并 Resources",
        "VirtualAPK（滴滴）：功能全面，支持四大组件，业界最早成熟方案之一",
        "Shadow（腾讯）：零反射，利用 Fragment 模拟 Activity 生命周期，兼容性好",
        "类加载隔离：每个插件独立 ClassLoader，父委托链接宿主 ClassLoader"
    ),
    codeSnippet = """
// DexClassLoader 加载插件
val pluginApkPath = "/sdcard/plugin.apk"
val dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE).absolutePath
val pluginClassLoader = DexClassLoader(
    pluginApkPath, dexOutputDir, null,
    context.classLoader // 父 ClassLoader = 宿主
)

// 反射调用插件中的类
val pluginClass = pluginClassLoader.loadClass("com.plugin.FeatureImpl")
val method = pluginClass.getMethod("doWork")
method.invoke(pluginClass.newInstance())

// 加载插件资源（反射 addAssetPath）
val am = AssetManager::class.java.newInstance()
AssetManager::class.java
    .getDeclaredMethod("addAssetPath", String::class.java)
    .also { it.isAccessible = true }
    .invoke(am, pluginApkPath)
    """.trimIndent(),
    tips = listOf(
        "Android 9+ 对非公开 API 的反射限制越来越严，新项目优先考虑动态 Feature Module",
        "Shadow 方案将 Activity 替换为 PluginActivity（继承 Fragment），无需 Hook 系统，兼容性更好",
        "插件化主要用于超级 App 场景，普通 App 推荐用 Google Play Feature Delivery"
    )
)

@Composable
fun PluginizationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF8BC34A),
        stageTitle = "插件化与热修复",
        onBack = onBack
    )
}
