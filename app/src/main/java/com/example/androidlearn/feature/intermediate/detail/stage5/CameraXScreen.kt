package com.example.androidlearn.feature.intermediate.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "CameraX 相机开发",
    description = "PreviewView · ImageCapture · VideoCapture · ML Kit 分析",
    overview = "CameraX 是 Jetpack 相机库，统一了碎片化严重的 Camera API，提供一致的跨设备行为，并与 Lifecycle 深度集成。",
    keyPoints = listOf(
        "用例（Use Case）：Preview、ImageCapture、ImageAnalysis、VideoCapture 四种用例",
        "PreviewView：在 Compose 中通过 AndroidView 嵌入相机预览",
        "ImageCapture：takePicture() 拍照，支持 File 或 ImageProxy 两种输出",
        "ImageAnalysis：实时分析每一帧，配合 ML Kit 实现人脸/条码/文字识别",
        "VideoCapture：录制视频，配合 Recorder 和 PendingRecording",
        "权限：需在运行时申请 CAMERA 权限，录音还需 RECORD_AUDIO"
    ),
    codeSnippet = """
// 绑定相机生命周期
val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
cameraProviderFuture.addListener({
    val cameraProvider = cameraProviderFuture.get()

    val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }
    val imageCapture = ImageCapture.Builder().build()

    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        CameraSelector.DEFAULT_BACK_CAMERA,
        preview,
        imageCapture
    )
}, ContextCompat.getMainExecutor(context))

// 拍照
val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
imageCapture.takePicture(
    outputOptions, executor,
    object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(output: ImageCapture.OutputFileResults) { }
        override fun onError(exc: ImageCaptureException) { }
    }
)
    """.trimIndent(),
    tips = listOf(
        "CameraX 会自动处理旋转、纵横比、设备兼容性，无需手动处理",
        "ImageAnalysis 设置 STRATEGY_KEEP_ONLY_LATEST 避免分析积压",
        "Compose 中使用 AndroidView { PreviewView(...) } 嵌入预览"
    )
)

@Composable
fun CameraXScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "多媒体与系统能力",
        onBack = onBack
    )
}
