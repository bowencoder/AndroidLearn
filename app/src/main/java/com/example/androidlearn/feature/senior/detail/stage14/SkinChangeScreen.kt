package com.example.androidlearn.feature.senior.detail.stage14

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【换肤与资源隔离】专属学习页
//  stageIndex=13, topicIndex=5
//  阶段颜色：深蓝紫 0xFF3F51B5（系统核心原理）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "换肤与资源隔离",
    description = "AssetManager 替换方案、Resources 代理、皮肤包加载与 Dark Mode 适配",
    overview = "换肤框架的核心是在不重启 App 的情况下动态替换 UI 资源（颜色、图片、字体等）。主流方案包括：AssetManager 反射注入皮肤包、Resources 代理拦截资源获取，以及利用 Android 10+ 原生 Dark Mode 机制。理解换肤原理也是理解 Android 资源管理机制的绝佳切入点。",
    keyPoints = listOf(
        "皮肤包方案：将皮肤资源打包为独立 APK（不含代码），运行时用反射创建 AssetManager 加载皮肤包",
        "资源代理方案：替换 Activity 的 Resources 对象（或代理 getDrawable/getColor），拦截资源获取请求并返回皮肤资源",
        "LayoutInflater.Factory2：Hook 布局加载，在 View 创建时记录需要换肤的 View 和属性，换肤时统一刷新",
        "Dark Mode（Android 10+）：通过 AppCompatDelegate.setDefaultNightMode() 切换，结合 -night 资源限定符实现官方换肤",
        "资源隔离（多包）：利用 AssetManager 的 addAssetPath 支持多路径，实现基包资源 + 皮肤包资源的叠加查找",
        "字体换肤：通过 Typeface.createFromAsset() 动态加载皮肤包内的 .ttf 文件，配合自定义 TextView 应用"
    ),
    codeSnippet = """
// 方案一：AssetManager 反射注入皮肤包
fun loadSkin(skinPath: String): Resources {
    val assetManager = AssetManager::class.java.newInstance()
    AssetManager::class.java
        .getDeclaredMethod("addAssetPath", String::class.java)
        .apply { isAccessible = true }
        .invoke(assetManager, skinPath)
    return Resources(assetManager, resources.displayMetrics, resources.configuration)
}

// 获取皮肤包中对应资源（通过资源名匹配，避免 ID 不一致问题）
fun getSkinDrawable(skinRes: Resources, skinPkg: String, resId: Int): Drawable? {
    val resName = resources.getResourceEntryName(resId)   // 如 "ic_logo"
    val resType = resources.getResourceTypeName(resId)    // 如 "drawable"
    val skinId = skinRes.getIdentifier(resName, resType, skinPkg)
    return if (skinId != 0) skinRes.getDrawable(skinId, null) else null
}

// 方案二：Dark Mode（推荐现代 App 使用）
// 1. 在 res/values-night/colors.xml 定义夜间颜色
// 2. 代码切换模式
AppCompatDelegate.setDefaultNightMode(
    AppCompatDelegate.MODE_NIGHT_YES    // 强制夜间
    // AppCompatDelegate.MODE_NIGHT_NO  // 强制日间
    // AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM  // 跟随系统
)
// 3. Activity 不重建（可选，需在 Manifest 配置）
// android:configChanges="uiMode"
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    val isDark = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    updateTheme(isDark)
}

// LayoutInflater.Factory2 Hook（换肤框架核心）
LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        val view = delegate.createView(parent, name, context, attrs)
        // 记录 view 需要换肤的属性（background、textColor 等）
        skinManager.record(view, attrs)
        return view
    }
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet) = null
})
    """.trimIndent(),
    tips = listOf(
        "皮肤包方案要注意资源 ID 不一致问题：宿主 App 和皮肤包的 ID 会不同，必须用资源名（Entry Name）做匹配",
        "Android 12+ 引入 Dynamic Color（Material You），可根据壁纸动态生成主题色，是更现代的换肤思路",
        "换肤切换后需遍历所有已创建的 View 重新应用资源，RecyclerView 的 item 需要在 onBindViewHolder 中感知皮肤变化"
    )
)

@Composable
fun SkinChangeScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "Android 系统核心原理",
        onBack = onBack
    )
}
