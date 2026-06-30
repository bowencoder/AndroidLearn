package com.example.androidlearn.feature.senior.detail.stage14

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【资源管理机制】专属学习页
//  stageIndex=13, topicIndex=4
//  阶段颜色：深蓝紫 0xFF3F51B5（系统核心原理）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "资源管理机制",
    description = "AssetManager、resources.arsc 结构、资源 ID 生成规则与资源冲突解决",
    overview = "Android 资源管理由 AssetManager（Native 层）和 Resources（Java 层）协作完成。resources.arsc 是核心资源索引文件，记录了所有资源 ID 到实际文件/值的映射。理解资源 ID 规则、多 Module 资源合并和 AssetManager 替换，是做换肤框架和资源优化的基础。",
    keyPoints = listOf(
        "资源 ID 格式：0xPPTTEEEE，PP=包ID（0x7F=App，0x01=系统），TT=资源类型（layout/string/...），EEEE=资源在类型中的序号",
        "resources.arsc：二进制资源映射表，包含所有语言/屏幕密度的资源路径或直接值（如 String）",
        "AssetManager（Native）：实际负责根据资源 ID 和当前 Configuration（语言、屏幕密度等）查找最佳匹配资源",
        "资源限定符优先级：MCC > 语言 > 布局方向 > 屏幕宽高 > 屏幕密度 > 平台版本（取最优匹配）",
        "多模块资源合并：AGP 将所有 Module 的资源合并，PP 段统一为 0x7F，同名资源以主 Module 为准",
        "运行时 Configuration 变更：屏幕旋转/语言切换触发 onConfigurationChanged，AssetManager 重新匹配资源"
    ),
    codeSnippet = """
// 资源 ID 分析
// R.layout.activity_main = 0x7F0A0001
// 0x7F = 当前 App 包（非系统）
// 0x0A = layout 类型
// 0x0001 = 该类型中第 2 个资源（从 0000 开始）

// 手动创建 AssetManager 加载外部 APK/资源包（换肤核心）
val assetManager = AssetManager::class.java.newInstance()
val addAssetPath = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
addAssetPath.invoke(assetManager, skinApkPath)  // 加载皮肤包路径
val skinResources = Resources(
    assetManager,
    resources.displayMetrics,
    resources.configuration
)
// 之后通过 skinResources.getDrawable()/getString() 获取皮肤资源

// 资源获取流程
// Resources.getDrawable(id)
//   → ResourcesImpl.loadDrawable()
//     → AssetManager.openNonAsset()（Native 层）
//       → 根据 Configuration 在 resources.arsc 中查找最优资源路径
//         → 打开对应文件并解码（如 PNG → BitmapDrawable）

// 查看 resources.arsc 内容
// $ aapt2 dump resources app-release.apk | grep layout
// $ adb shell am get-config    // 查看当前设备 Configuration

// 避免资源 ID 冲突（多 Module 开发）
// 在 module 的 build.gradle 中设置前缀
android {
    resourcePrefix "module_name_"  // 强制所有资源名以此前缀开头
}
    """.trimIndent(),
    tips = listOf(
        "assets/ 目录下的文件没有资源 ID，通过 AssetManager.open(路径) 直接访问，不受资源合并影响",
        "多语言资源要注意 RTL（阿拉伯语/希伯来语）布局镜像，使用 start/end 代替 left/right",
        "Android 10+ 引入 Dark Mode 资源限定符（-night），通过 Configuration.uiMode 匹配"
    )
)

@Composable
fun ResourceMgrScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "Android 系统核心原理",
        onBack = onBack
    )
}
