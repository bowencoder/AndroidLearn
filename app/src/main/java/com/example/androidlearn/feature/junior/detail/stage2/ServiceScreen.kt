package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Service 与后台处理",
    description = "前台/后台 Service，IntentService，生命周期，绑定服务",
    overview = "Service 是 Android 四大组件之一，用于在后台执行长时间运行的操作，不提供 UI 界面。分为启动型 Service 和绑定型 Service，前台 Service 需要显示通知。",
    keyPoints = listOf(
        "启动型 Service：startService / stopSelf，生命周期：onCreate → onStartCommand → onDestroy",
        "绑定型 Service：bindService / unbindService，通过 IBinder 与 Activity 通信",
        "前台 Service：必须调用 startForeground() 显示通知，避免被系统杀死（适合音乐播放）",
        "IntentService（已废弃）：自动在子线程处理任务，处理完自动停止，推荐用 WorkManager 替代",
        "onStartCommand 返回值：START_STICKY（被杀后重启）、START_NOT_STICKY（不重启）",
        "Android 8+ 后台限制：后台 App 不能启动后台 Service，改用前台 Service 或 WorkManager"
    ),
    codeSnippet = """
// 定义 Service
class MusicService : Service() {
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 启动前台通知（Android 8+ 必须）
        val notification = NotificationCompat.Builder(this, "music_channel")
            .setContentTitle("正在播放")
            .setSmallIcon(R.drawable.ic_music)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}

// Activity 绑定 Service
private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, binder: IBinder) {
        val service = (binder as MusicService.LocalBinder).getService()
        // 可以直接调用 service 的方法
    }
    override fun onServiceDisconnected(name: ComponentName) {}
}

bindService(Intent(this, MusicService::class.java), connection, BIND_AUTO_CREATE)
    """.trimIndent(),
    tips = listOf(
        "后台任务首选 WorkManager（持久化、可重试），Service 适合需要长期运行的实时任务",
        "前台 Service 在 Android 14+ 需要声明 foregroundServiceType 并申请对应权限",
        "避免在 onStartCommand 直接做耗时操作，应另起协程或线程处理"
    )
)

@Composable
fun ServiceScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "四大组件与核心 UI",
        onBack = onBack
    )
}
