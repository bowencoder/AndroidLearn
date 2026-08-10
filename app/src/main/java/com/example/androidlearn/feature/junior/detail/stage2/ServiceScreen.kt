package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
 * Service 与后台处理笔记
 * 官方文档：https://developer.android.com/guide/components/services
 *
 * ── 1  Service 分类 ───────────────────────────────────────────────────────────
 *
 *  启动型 Service（Started Service）：
 *  · 通过 startService() / startForegroundService() 启动
 *  · 独立运行，与启动者生命周期无关
 *  · 需调用 stopSelf() 或 stopService() 停止
 *
 *  绑定型 Service（Bound Service）：
 *  · 通过 bindService() 绑定，提供客户端-服务端接口
 *  · 所有绑定者解绑后自动销毁
 *  · 通过 IBinder 与调用方通信
 *
 *  混合型：同时支持启动和绑定，需两者都停止才销毁
 *
 *
 * ── 2  Service 生命周期 ───────────────────────────────────────────────────────
 *
 *  启动型：
 *  onCreate → onStartCommand（每次 startService 调用）→ onDestroy
 *
 *  绑定型：
 *  onCreate → onBind → [客户端使用] → onUnbind → onDestroy
 *
 *  // 定义 Service
 *  class MyService : Service() {
 *      override fun onCreate() { super.onCreate() /* 初始化资源 */ }
 *
 *      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
 *          // 处理任务（不要在此做耗时操作，应开协程/线程）
 *          return START_STICKY
 *      }
 *
 *      override fun onBind(intent: Intent): IBinder? = null  // 启动型返回 null
 *
 *      override fun onDestroy() { super.onDestroy() /* 释放资源 */ }
 *  }
 *
 *  // AndroidManifest.xml 注册
 *  <service android:name=".MyService" android:exported="false" />
 *
 *  onStartCommand 返回值：
 *  · START_STICKY：被杀后重启，intent 为 null（适合音乐播放）
 *  · START_NOT_STICKY：被杀后不重启（适合一次性任务）
 *  · START_REDELIVER_INTENT：被杀后重启，重新传递最后一个 intent
 *
 *
 * ── 3  前台 Service ───────────────────────────────────────────────────────────
 *
 *  · 必须显示持久通知，优先级高，不易被系统杀死
 *  · 适合：音乐播放、导航、文件下载、健身追踪
 *  · Android 8+：后台 App 必须用 startForegroundService()，5 秒内调用 startForeground()
 *  · Android 14+：需声明 foregroundServiceType 并申请对应权限
 *
 *  class MusicService : Service() {
 *      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
 *          // 创建通知渠道（Android 8+）
 *          val channel = NotificationChannel("music", "音乐播放", NotificationManager.IMPORTANCE_LOW)
 *          getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
 *
 *          val notification = NotificationCompat.Builder(this, "music")
 *              .setContentTitle("正在播放")
 *              .setContentText("歌曲名称")
 *              .setSmallIcon(R.drawable.ic_music)
 *              .build()
 *
 *          startForeground(1, notification)
 *          return START_STICKY
 *      }
 *
 *      override fun onBind(intent: Intent): IBinder? = null
 *
 *      override fun onDestroy() {
 *          super.onDestroy()
 *          stopForeground(STOP_FOREGROUND_REMOVE)
 *      }
 *  }
 *
 *  // AndroidManifest.xml（Android 14+）
 *  <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 *  <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
 *  <service android:foregroundServiceType="mediaPlayback" ... />
 *
 *
 * ── 4  绑定型 Service（Bound Service）────────────────────────────────────────
 *
 *  // Service 端：通过 LocalBinder 暴露自身
 *  class MusicService : Service() {
 *      private val binder = LocalBinder()
 *
 *      inner class LocalBinder : Binder() {
 *          fun getService(): MusicService = this@MusicService
 *      }
 *
 *      override fun onBind(intent: Intent): IBinder = binder
 *
 *      fun play(url: String) { /* 播放逻辑 */ }
 *      fun pause() { /* 暂停逻辑 */ }
 *  }
 *
 *  // Activity 端：绑定并调用方法
 *  private var musicService: MusicService? = null
 *  private val connection = object : ServiceConnection {
 *      override fun onServiceConnected(name: ComponentName, binder: IBinder) {
 *          musicService = (binder as MusicService.LocalBinder).getService()
 *      }
 *      override fun onServiceDisconnected(name: ComponentName) {
 *          musicService = null
 *      }
 *  }
 *
 *  override fun onStart() {
 *      super.onStart()
 *      bindService(Intent(this, MusicService::class.java), connection, BIND_AUTO_CREATE)
 *  }
 *  override fun onStop() {
 *      super.onStop()
 *      unbindService(connection)
 *  }
 *
 *
 * ── 5  后台限制（Android 8+）─────────────────────────────────────────────────
 *
 *  · Android 8.0+：后台 App 不能启动后台 Service
 *    → 改用 startForegroundService() 启动前台 Service
 *    → 或使用 WorkManager 处理延迟/周期任务
 *
 *  · Android 12+：精确闹钟需申请 SCHEDULE_EXACT_ALARM 权限
 *
 *  // 兼容写法
 *  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *      context.startForegroundService(Intent(context, MyService::class.java))
 *  } else {
 *      context.startService(Intent(context, MyService::class.java))
 *  }
 *
 *
 * ── 6  WorkManager（推荐替代方案）────────────────────────────────────────────
 *
 *  · 适合：可延迟、需保证执行的后台任务（上传日志、同步数据）
 *  · 持久化：App 重启或设备重启后仍能执行
 *  · 支持约束条件：网络、充电状态、存储空间
 *
 *  // 定义 Worker
 *  class SyncWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
 *      override suspend fun doWork(): Result {
 *          return try {
 *              syncData()
 *              Result.success()
 *          } catch (e: Exception) {
 *              Result.retry()
 *          }
 *      }
 *  }
 *
 *  // 提交任务
 *  val constraints = Constraints.Builder()
 *      .setRequiredNetworkType(NetworkType.CONNECTED)
 *      .build()
 *
 *  val request = OneTimeWorkRequestBuilder<SyncWorker>()
 *      .setConstraints(constraints)
 *      .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
 *      .build()
 *
 *  WorkManager.getInstance(context).enqueue(request)
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 后台任务首选 WorkManager（持久化、可重试、省电）
 *  · 需要实时长期运行（音乐/导航）用前台 Service + 通知
 *  · onStartCommand 中不要直接做耗时操作，开协程/线程处理
 *  · 绑定型 Service 在 onStart/onStop 中对称绑定/解绑
 *  · Android 14+ 前台 Service 必须声明 foregroundServiceType
 *  · IntentService 已废弃，不要在新项目中使用
 */

private val Blue = Color(0xFF2196F3)

private data class ServiceChapter(val num: String, val title: String)

private val chapters = listOf(
    ServiceChapter("1", "Service 分类"),
    ServiceChapter("2", "Service 生命周期"),
    ServiceChapter("3", "前台 Service"),
    ServiceChapter("4", "绑定型 Service（Bound Service）"),
    ServiceChapter("5", "后台限制（Android 8+）"),
    ServiceChapter("6", "WorkManager（推荐替代方案）"),
    ServiceChapter("7", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Service 与后台处理", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "启动/绑定/前台 Service · WorkManager",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters.size) { i -> ChapterRowService(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowService(chapter: ServiceChapter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Blue.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
