package com.example.androidlearn.feature.senior.detail.stage12

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "热修复原理与实践",
    description = "Tinker、Robust、Sophix，dex 差量合并，代码/资源热替换",
    overview = "热修复允许在不发版的情况下修复线上 Bug。主流方案分为三类：dex 差量替换（Tinker）、方法代理插桩（Robust）、native 替换 ArtMethod（Sophix）。各有其适用场景和 Android 版本兼容性。",
    keyPoints = listOf(
        "Tinker（微信）：diff 算法生成补丁 dex，合并后整体替换，修复能力强，需重启生效",
        "Robust（美团）：编译时在每个方法插桩，运行时通过接口代理实现方法替换，即时生效",
        "Sophix（阿里）：综合方案，Android 版本兼容好，支持代码+资源+So 热修复",
        "dex 差量合并：BSDiff 算法生成 patch，合并到完整 dex，ClassLoader 优先加载补丁类",
        "ClassLoader 方案：将补丁 dex 插入到 DexPathList.dexElements 数组的最前面",
        "资源热修复：重新创建 AssetManager 加载补丁资源包，替换全局 Resources 引用"
    ),
    codeSnippet = """
// ClassLoader 方案核心：将补丁 dex 插入 dexElements 最前面
fun injectDex(context: Context, patchDexPath: String) {
    val classLoader = context.classLoader
    val pathListField = classLoader.javaClass.superclass
        ?.getDeclaredField("pathList")
        ?.also { it.isAccessible = true }
    val pathList = pathListField?.get(classLoader)

    val dexElementsField = pathList?.javaClass
        ?.getDeclaredField("dexElements")
        ?.also { it.isAccessible = true }
    val oldElements = dexElementsField?.get(pathList) as? Array<*>

    // 加载补丁 dex
    val patchClassLoader = DexClassLoader(
        patchDexPath, context.cacheDir.path, null, classLoader
    )
    val patchPathList = pathListField?.get(patchClassLoader)
    val patchElements = dexElementsField?.get(patchPathList) as? Array<*>

    // 合并：补丁放前面，原始放后面
    val combined = (patchElements.orEmpty() + oldElements.orEmpty())
    dexElementsField?.set(pathList, combined)
}

// Robust 插桩示意（编译时自动生成）
// 原始方法被改写为：
fun originalMethod(): String {
    if (changeQuickRedirect != null) {
        // 命中补丁，走代理
        return PatchProxy.accessDispatch(changeQuickRedirect, args) as String
    }
    return "original logic"
}
    """.trimIndent(),
    tips = listOf(
        "ClassLoader 方案在 Android 7+ Art 有 inline cache 优化，需要额外处理 verify 标记",
        "Robust 即时生效无需重启，但代码体积增加约 10%（每个方法都插桩）",
        "生产环境推荐集成成熟 SDK（Sophix/Tinker），自研热修复成本极高"
    )
)

@Composable
fun HotfixScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF8BC34A),
        stageTitle = "插件化与热修复",
        onBack = onBack
    )
}
