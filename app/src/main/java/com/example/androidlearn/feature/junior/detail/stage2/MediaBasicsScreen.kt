package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "文件与多媒体基础",
    description = "文件读写 · MediaPlayer · 相机拍照 · MediaStore",
    overview = "Android 提供了丰富的多媒体 API，包括文件 I/O、音频/视频播放、相机拍照和媒体库访问，是开发音视频、图片类 App 的入门基础。",
    keyPoints = listOf(
        "文件读写：Internal Storage（应用私有）vs External Storage（公开）、File API、BufferedReader/Writer",
        "MediaPlayer：prepare() → start() / pause() / stop() 生命周期、资源释放 release()",
        "相机拍照：ActivityResultContracts.TakePicture()、FileProvider 授权 URI、临时文件处理",
        "MediaStore：通过 ContentResolver 访问图片/视频/音频、权限申请（READ_MEDIA_*）",
        "分区存储（Scoped Storage）：Android 10+ 限制直接访问外部存储，需通过 MediaStore 或 SAF",
        "SAF（Storage Access Framework）：让用户选择文件，使用 ActivityResultContracts.OpenDocument()"
    ),
    codeSnippet = """
// 相机拍照（推荐方式）
val takePicture = rememberLauncherForActivityResult(
    ActivityResultContracts.TakePicture()
) { success ->
    if (success) { /* 使用 photoUri 加载图片 */ }
}
val photoUri = FileProvider.getUriForFile(
    context, "${'$'}{context.packageName}.provider", tempFile
)
takePicture.launch(photoUri)

// MediaPlayer 播放音频
val player = MediaPlayer.create(context, R.raw.music)
player.start()
// 别忘了释放：player.release()

// MediaStore 查询图片
val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)
contentResolver.query(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    projection, null, null, null
)?.use { cursor -> /* 遍历 cursor */ }
    """.trimIndent(),
    tips = listOf(
        "Android 10+ 请用 MediaStore API 访问媒体文件，避免直接路径操作",
        "相机拍照必须通过 FileProvider 提供 URI，否则 Android 7+ 抛 FileUriExposedException",
        "播放完毕或页面销毁时务必调用 MediaPlayer.release()，防止资源泄漏"
    )
)

@Composable
fun MediaBasicsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF009688),
        stageTitle = "UI 组件与数据基础",
        onBack = onBack
    )
}
