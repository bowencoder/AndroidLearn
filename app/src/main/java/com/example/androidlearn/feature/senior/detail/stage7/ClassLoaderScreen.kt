package com.example.androidlearn.feature.senior.detail.stage7

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "类加载与热修复原理",
    description = "ClassLoader 双亲委派，dex 加载，热修复 Patch 原理",
    overview = "Android 的类加载机制基于 dalvik.system.DexClassLoader，热修复正是利用 ClassLoader 加载顺序插入补丁 dex 实现 Bug 修复的。",
    keyPoints = listOf(
        "双亲委派：先委托父 ClassLoader 加载，父找不到才由子加载，防止核心类被覆盖",
        "PathClassLoader：加载已安装 APK 的 dex，作为 App 的默认 ClassLoader",
        "DexClassLoader：可动态加载外部 dex/apk 文件",
        "dexElements 数组：ClassLoader 按数组顺序遍历 dex，找到类即返回",
        "热修复原理：将补丁 dex 插入 dexElements 数组头部，优先加载修复后的类",
        "Tinker / Robust / QZone：各方案在 Dex / Native / 字节码层面的不同实现"
    ),
    codeSnippet = """
// 手动加载外部 dex（热修复核心思路）
fun loadPatchDex(patchDexPath: String) {
    val classLoader = classLoader as? BaseDexClassLoader ?: return

    // 反射获取 DexPathList
    val pathListField = BaseDexClassLoader::class.java
        .getDeclaredField("pathList").also { it.isAccessible = true }
    val pathList = pathListField.get(classLoader)

    // 反射获取 dexElements 数组
    val elementsField = pathList.javaClass
        .getDeclaredField("dexElements").also { it.isAccessible = true }
    val originalElements = elementsField.get(pathList) as Array<*>

    // 构造补丁 dex 的 Element
    val patchLoader = DexClassLoader(
        patchDexPath, cacheDir.absolutePath, null, classLoader
    )
    val patchPathList = pathListField.get(patchLoader)
    val patchElements = elementsField.get(patchPathList) as Array<*>

    // 将补丁 elements 合并到头部
    val merged = patchElements + originalElements
    elementsField.set(pathList, merged)
}
    """.trimIndent(),
    tips = listOf(
        "Android N+ 加入 PGO 和 AOT，热修复兼容性变复杂，生产建议用成熟框架",
        "Robust（美团）基于 Instant Run，在方法入口插桩，兼容性最好",
        "热修复只能修复 Java 层逻辑，So 文件修复需要 Native 热修复方案"
    )
)

@Composable
fun ClassLoaderScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF795548),
        stageTitle = "底层原理与面试重点",
        onBack = onBack
    )
}
