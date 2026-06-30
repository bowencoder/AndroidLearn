package com.example.androidlearn.feature.senior.detail.stage16

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【Android 权限机制】专属学习页
//  stageIndex=15, topicIndex=3
//  阶段颜色：青绿 0xFF00897B（工程化进阶）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "Android 权限机制",
    description = "Install-time/Runtime 权限、权限分组、Android 12+ 精确位置与后台权限演变",
    overview = "Android 权限体系随版本不断收紧。Android 6.0 引入运行时权限，Android 10+ 增加后台位置限制，Android 12 进一步拆分精确/模糊位置。深入理解权限机制，是构建合规 App 和处理各版本兼容的必备知识。",
    keyPoints = listOf(
        "权限类型：Normal（自动授予，如 INTERNET）/ Dangerous（需运行时请求）/ Signature（同签名自动授予）/ AppOp（系统级）",
        "运行时权限（Android 6+）：危险权限必须在运行时用 requestPermissions() 请求，用户可随时撤销",
        "权限分组：同组权限（如 READ_CONTACTS/WRITE_CONTACTS）一旦有一个被授予，同组其他权限自动授予（Android 8+ 修改了此行为，需逐一请求）",
        "后台位置（Android 10+）：需要单独声明 ACCESS_BACKGROUND_LOCATION，且只能在前台权限已授予后才能请求",
        "精确/模糊位置（Android 12+）：ACCESS_FINE_LOCATION 和 ACCESS_COARSE_LOCATION 分开，用户可选择只授予模糊位置",
        "一次性权限（Android 11+）：用户可授予「仅此一次」，App 进入后台后权限自动撤销"
    ),
    codeSnippet = """
// ActivityResultLauncher 请求单个权限（推荐方式）
val requestPermission = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        // 权限已授予
        startCamera()
    } else {
        // 权限被拒绝：区分「永久拒绝」和「本次拒绝」
        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            // 用户勾选「不再询问」→ 引导用户去设置手动开启
            showGoToSettingsDialog()
        } else {
            showPermissionDeniedHint()
        }
    }
}

// 检查并请求权限
fun checkAndRequestCameraPermission() {
    when {
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED -> {
            startCamera()  // 已有权限，直接使用
        }
        shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
            // 显示说明对话框，解释为什么需要此权限
            showRationaleDialog {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
        else -> {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }
}

// 请求多个权限
val requestMultiplePermissions = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->
    val allGranted = permissions.values.all { it }
    if (allGranted) startFeature()
}
requestMultiplePermissions.launch(
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
)

// Android 12+ 精确/模糊位置（同时声明，让用户选择）
// AndroidManifest.xml
// <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
// <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
// 请求时传入两个，用户可选择「精确位置」或「大致位置」
requestMultiplePermissions.launch(
    arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
)
    """.trimIndent(),
    tips = listOf(
        "targetSdkVersion 决定权限行为：target 低于 23 则运行时权限退化为安装时权限。务必保持 targetSdk 为最新版本",
        "权限请求要有上下文，在用户触发需要权限的操作时再请求，不要在 App 启动时批量请求所有权限（会被用户拒绝）",
        "MANAGE_EXTERNAL_STORAGE（Android 11+）是特殊权限，Play Store 审核非常严格，普通 App 应使用 MediaStore/SAF 替代"
    )
)

@Composable
fun PermissionScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00897B),
        stageTitle = "工程化进阶",
        onBack = onBack
    )
}
