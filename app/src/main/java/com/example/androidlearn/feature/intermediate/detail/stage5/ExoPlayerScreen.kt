package com.example.androidlearn.feature.intermediate.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "音视频与 ExoPlayer",
    description = "MediaPlayer · ExoPlayer · 音频焦点 · MediaSession",
    overview = "Android 提供 MediaPlayer 和 ExoPlayer 两套播放方案。ExoPlayer 功能强大、可扩展，是大多数音视频 App 的首选，支持 DASH、HLS、自适应码率等高级特性。",
    keyPoints = listOf(
        "MediaPlayer：适合简单本地文件播放，生命周期：Idle→Initialized→Prepared→Started",
        "ExoPlayer (Media3)：Google 官方推荐，支持网络流、DRM、字幕、多轨道",
        "PlayerView：ExoPlayer 的 UI 组件，支持完全自定义控制栏",
        "音频焦点（Audio Focus）：requestAudioFocus()，避免多 App 同时出声",
        "MediaSession：提供系统集成——锁屏控制、通知栏媒体控件、蓝牙耳机按键",
        "后台播放：前台 Service + MediaSession + Notification 三件套"
    ),
    codeSnippet = """
// ExoPlayer 基本使用
val player = ExoPlayer.Builder(context).build().also {
    playerView.player = it
}
val mediaItem = MediaItem.fromUri("https://example.com/video.mp4")
player.setMediaItem(mediaItem)
player.prepare()
player.playWhenReady = true

// 音频焦点处理
val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
    .setOnAudioFocusChangeListener { change ->
        when (change) {
            AudioManager.AUDIOFOCUS_LOSS -> player.pause()
            AudioManager.AUDIOFOCUS_GAIN -> player.play()
        }
    }.build()
audioManager.requestAudioFocus(focusRequest)

// onStop 中释放
player.release()
    """.trimIndent(),
    tips = listOf(
        "新项目使用 androidx.media3:media3-exoplayer，是 ExoPlayer 2.x 的官方继任者",
        "在 onStart/onStop 配对初始化/释放，而不是 onCreate/onDestroy",
        "后台播放必须使用前台 Service，否则 Android 8+ 会杀进程"
    )
)

@Composable
fun ExoPlayerScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "多媒体与系统能力",
        onBack = onBack
    )
}
