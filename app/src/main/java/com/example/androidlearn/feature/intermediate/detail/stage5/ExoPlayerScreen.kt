package com.example.androidlearn.feature.intermediate.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 音视频与 ExoPlayer
 * 官方文档：https://developer.android.com/guide/topics/media/exoplayer
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  播放方案选择
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  MediaPlayer ──────────────────────────────────────────────────────────
 *
 *  · 适合简单本地文件播放
 *  · 生命周期：Idle → Initialized → Prepared → Started → Paused → Stopped
 *  · 状态机复杂，容易出错
 *
 * ── 1.2  ExoPlayer（Media3，推荐） ───────────────────────────────────────────
 *
 *  · Google 官方推荐，支持网络流、DRM、字幕、多轨道
 *  · 支持 DASH、HLS、SmoothStreaming、自适应码率
 *  · 新项目使用 androidx.media3:media3-exoplayer
 *
 *  val player = ExoPlayer.Builder(context).build().also {
 *      playerView.player = it
 *  }
 *  val mediaItem = MediaItem.fromUri("https://example.com/video.mp4")
 *  player.setMediaItem(mediaItem)
 *  player.prepare()
 *  player.playWhenReady = true
 *
 *  // onStop 中释放
 *  player.release()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  音频焦点
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · requestAudioFocus()：避免多 App 同时出声
 *  · AUDIOFOCUS_LOSS：失去焦点，暂停播放
 *  · AUDIOFOCUS_GAIN：重新获得焦点，恢复播放
 *  · AUDIOFOCUS_LOSS_TRANSIENT：短暂失去（如来电），可降低音量
 *
 *  val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
 *      .setOnAudioFocusChangeListener { change ->
 *          when (change) {
 *              AudioManager.AUDIOFOCUS_LOSS -> player.pause()
 *              AudioManager.AUDIOFOCUS_GAIN -> player.play()
 *          }
 *      }.build()
 *  audioManager.requestAudioFocus(focusRequest)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  MediaSession 系统集成
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 锁屏控制：在锁屏界面显示播放控件
 *  · 通知栏媒体控件：下拉通知栏显示播放信息
 *  · 蓝牙耳机按键：响应耳机的播放/暂停/下一首按键
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  后台播放
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 前台 Service + MediaSession + Notification 三件套
 *  · 后台播放必须使用前台 Service，否则 Android 8+ 会杀进程
 *  · 在 onStart/onStop 配对初始化/释放，而不是 onCreate/onDestroy
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 新项目使用 androidx.media3:media3-exoplayer
 *  · 在 onStart/onStop 配对初始化/释放
 *  · 后台播放必须使用前台 Service
 */

val exoPlayerData = NoteData(
    title = "音视频与 ExoPlayer",
    subtitle = "多媒体与系统能力 · MediaPlayer · ExoPlayer · 音频焦点",
    color = Color.parseColor("#00BCD4"),
    chapters = listOf(
        ChapterItem("1",   "播放方案选择"),
        ChapterItem("1.1", "MediaPlayer"),
        ChapterItem("1.2", "ExoPlayer（Media3，推荐）"),
        ChapterItem("2",   "音频焦点"),
        ChapterItem("3",   "MediaSession 系统集成"),
        ChapterItem("4",   "后台播放"),
        ChapterItem("5",   "最佳实践"),
    )
)
