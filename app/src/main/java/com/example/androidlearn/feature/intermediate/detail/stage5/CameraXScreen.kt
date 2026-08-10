package com.example.androidlearn.feature.intermediate.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * CameraX 相机开发
 * 官方文档：https://developer.android.com/training/camerax
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心用例（Use Case）
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  四种用例 ─────────────────────────────────────────────────────────────
 *
 *  · Preview：相机预览，绑定到 PreviewView
 *  · ImageCapture：拍照，支持 File 或 ImageProxy 两种输出
 *  · ImageAnalysis：实时分析每一帧，配合 ML Kit
 *  · VideoCapture：录制视频，配合 Recorder 和 PendingRecording
 *
 * ── 1.2  绑定生命周期 ─────────────────────────────────────────────────────────
 *
 *  val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
 *  cameraProviderFuture.addListener({
 *      val cameraProvider = cameraProviderFuture.get()
 *
 *      val preview = Preview.Builder().build().also {
 *          it.setSurfaceProvider(previewView.surfaceProvider)
 *      }
 *      val imageCapture = ImageCapture.Builder().build()
 *
 *      cameraProvider.bindToLifecycle(
 *          lifecycleOwner,
 *          CameraSelector.DEFAULT_BACK_CAMERA,
 *          preview,
 *          imageCapture
 *      )
 *  }, ContextCompat.getMainExecutor(context))
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  拍照
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
 *  imageCapture.takePicture(
 *      outputOptions, executor,
 *      object : ImageCapture.OnImageSavedCallback {
 *          override fun onImageSaved(output: ImageCapture.OutputFileResults) { }
 *          override fun onError(exc: ImageCaptureException) { }
 *      }
 *  )
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  图像分析（ML Kit）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 实时分析每一帧，配合 ML Kit 实现人脸/条码/文字识别
 *  · STRATEGY_KEEP_ONLY_LATEST：丢弃积压帧，只处理最新帧
 *
 *  val imageAnalysis = ImageAnalysis.Builder()
 *      .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
 *      .build()
 *  imageAnalysis.setAnalyzer(executor) { imageProxy ->
 *      // 分析图像
 *      imageProxy.close()  // 必须关闭，否则分析器停止工作
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Compose 集成
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 通过 AndroidView 嵌入 PreviewView
 *
 *  AndroidView(
 *      factory = { ctx ->
 *          PreviewView(ctx).also { previewView ->
 *              // 绑定相机
 *          }
 *      },
 *      modifier = Modifier.fillMaxSize()
 *  )
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · CameraX 会自动处理旋转、纵横比、设备兼容性，无需手动处理
 *  · ImageAnalysis 设置 STRATEGY_KEEP_ONLY_LATEST 避免分析积压
 *  · 权限：需在运行时申请 CAMERA 权限，录音还需 RECORD_AUDIO
 */

val cameraXData = NoteData(
    title = "CameraX 相机开发",
    subtitle = "多媒体与系统能力 · PreviewView · ImageCapture · ML Kit",
    color = Color.parseColor("#00BCD4"),
    chapters = listOf(
        ChapterItem("1",   "核心用例（Use Case）"),
        ChapterItem("1.1", "四种用例"),
        ChapterItem("1.2", "绑定生命周期"),
        ChapterItem("2",   "拍照"),
        ChapterItem("3",   "图像分析（ML Kit）"),
        ChapterItem("4",   "Compose 集成"),
        ChapterItem("5",   "最佳实践"),
    )
)
