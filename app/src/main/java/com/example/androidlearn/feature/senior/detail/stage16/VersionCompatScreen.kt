package com.example.androidlearn.feature.senior.detail.stage16

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【版本适配策略】专属学习页
//  stageIndex=15, topicIndex=4
//  阶段颜色：青绿 0xFF00897B（工程化进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "版本适配策略",
    description = "targetSdkVersion 与 API 兼容性、Android 各版本重大变更汇总与 BuildCompat 适配方案",
    overview = "Android 每年发布新版本，每个版本都会带来行为变更（Behavior Changes），影响已安装 App 的运行。合理管理 minSdk/targetSdk、使用 AndroidX 兼容库、针对重大变更做好适配，是保证 App 在全版本稳定运行的核心工作。",
    keyPoints = listOf(
        "minSdk：App 支持的最低 Android 版本；targetSdk：App 声明已针对该版本测试，系统根据此值决定是否应用新行为变更",
        "compileSdk：编译时使用的 API 级别，决定可使用的 API 上限，应始终设为最新版本",
        "Android 10（Q）重大变更：后台位置限制、scoped storage（分区存储）、设备标识符限制（禁止获取 IMEI）",
        "Android 11（R）重大变更：软件包可见性（需声明 queries 或 QUERY_ALL_PACKAGES）、强制分区存储、单次权限",
        "Android 12（S）重大变更：精确闹钟权限（SCHEDULE_EXACT_ALARM）、显式 PendingIntent、蓝牙权限拆分",
        "Android 13（T）重大变更：细化媒体权限（READ_MEDIA_IMAGES/VIDEO/AUDIO 替代 READ_EXTERNAL_STORAGE）、通知权限（POST_NOTIFICATIONS）"
    ),
    codeSnippet = """
// 版本判断标准写法（避免 Java 反射或硬编码数字）
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    // Android 13+ 专有逻辑
    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
}

// BuildCompat（AndroidX）：预发布版本检测
if (BuildCompat.isAtLeastU()) {   // Android 14+
    // 使用 Android 14 API
}

// 分区存储适配（Android 10+ scoped storage）
// Android 10+（targetSdk >= 29）强制分区存储
// 使用 MediaStore API 访问共享媒体文件
val resolver = contentResolver
val imageUri = resolver.insert(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Android 10+ 可指定相对路径
        put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/MyApp/")
    }
)

// 软件包可见性适配（Android 11+）
// 在 AndroidManifest.xml 中声明需要交互的包
// <queries>
//     <package android:name="com.example.other.app"/>
//     <intent>
//         <action android:name="android.intent.action.SEND"/>
//     </intent>
// </queries>
val intent = Intent(Intent.ACTION_SEND)
// Android 11+ 需要检查 resolveActivity 是否为 null
if (intent.resolveActivity(packageManager) != null) {
    startActivity(intent)
}

// PendingIntent 显式 flag（Android 12+）
val pendingIntent = PendingIntent.getActivity(
    context, 0, intent,
    // Android 12+ 必须声明 FLAG_IMMUTABLE 或 FLAG_MUTABLE
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)

// 版本适配辅助工具（版本对应关系）
// Android 14 = API 34 = UPSIDE_DOWN_CAKE
// Android 13 = API 33 = TIRAMISU
// Android 12L= API 32 = S_V2
// Android 12 = API 31 = S
// Android 11 = API 30 = R
// Android 10 = API 29 = Q
// Android 9  = API 28 = P
    """.trimIndent(),
    tips = listOf(
        "每次 targetSdk 升级前，必须在对应系统版本的真机或模拟器上全量测试，Google Play 要求 App 的 targetSdk 不低于前一年度要求",
        "使用 AndroidX 库而非 Android Support Library，AndroidX 持续更新并自动处理多版本兼容细节",
        "在 Android 兼容性矩阵（developer.android.com/distribute/best-practices/develop/target-sdk）中跟踪每年 Play Store 的 targetSdk 最低要求"
    )
)

@Composable
fun VersionCompatScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00897B),
        stageTitle = "工程化进阶",
        onBack = onBack
    )
}
