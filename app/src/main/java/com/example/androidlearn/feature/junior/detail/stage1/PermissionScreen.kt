package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "权限申请基础",
    description = "Manifest 声明 · 运行时权限 · ActivityResultContracts",
    overview = "Android 权限分为普通权限（自动授予）和危险权限（需运行时申请）。从 Android 6.0 起，危险权限必须在运行时向用户请求，拒绝后需优雅降级。",
    keyPoints = listOf(
        "普通权限：在 AndroidManifest.xml 中声明即可自动授予（如 INTERNET）",
        "危险权限：摄像头/麦克风/联系人/位置等，需运行时 requestPermissions()",
        "ActivityResultContracts.RequestPermission：推荐的现代权限申请 API",
        "ActivityResultContracts.RequestMultiplePermissions：同时申请多个权限",
        "shouldShowRequestPermissionRationale()：判断是否需要向用户解释权限用途",
        "特殊权限：MANAGE_EXTERNAL_STORAGE / SYSTEM_ALERT_WINDOW 需跳转设置页"
    ),
    codeSnippet = """
// 单个权限申请（Compose）
val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) openCamera()
    else showDeniedMessage()
}

// 触发申请
Button(onClick = {
    if (ContextCompat.checkSelfPermission(context, CAMERA) == PERMISSION_GRANTED) {
        openCamera()
    } else {
        launcher.launch(CAMERA)
    }
}) { Text("打开相机") }

// 多权限
val multiLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->
    val cameraGranted = permissions[CAMERA] ?: false
    val audioGranted = permissions[RECORD_AUDIO] ?: false
}
multiLauncher.launch(arrayOf(CAMERA, RECORD_AUDIO))
    """.trimIndent(),
    tips = listOf(
        "用户拒绝两次后系统不再弹窗，需引导用户去设置页手动开启",
        "shouldShowRequestPermissionRationale() 返回 false 且未授权，说明已永久拒绝",
        "Android 12+ 位置权限分精确/模糊，优先请求模糊位置降低门槛"
    )
)

@Composable
fun PermissionScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "四大组件",
        onBack = onBack
    )
}
