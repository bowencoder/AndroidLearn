package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * BroadcastReceiver 笔记
 * 官方文档：https://developer.android.com/guide/components/broadcasts
 *
 * ── 1  两种注册方式 ───────────────────────────────────────────────────────────
 *
 *  静态注册（AndroidManifest.xml）：
 *  · App 未运行也能接收；Android 8+ 大部分隐式广播不再支持静态注册
 *  · 仍支持静态注册的广播：BOOT_COMPLETED、LOCKED_BOOT_COMPLETED 等少数显式广播
 *
 *  <receiver android:name=".BootReceiver" android:exported="true">
 *      <intent-filter>
 *          <action android:name="android.intent.action.BOOT_COMPLETED"/>
 *      </intent-filter>
 *  </receiver>
 *
 *  动态注册（代码，推荐）：
 *  · 跟随组件生命周期，必须配对注销，否则内存泄漏
 *  · Android 13+ 动态注册需声明 RECEIVER_NOT_EXPORTED 或 RECEIVER_EXPORTED
 *
 *  val receiver = NetworkReceiver()
 *  val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
 *  // Android 13+ 需加 flags 参数
 *  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
 *      registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
 *  } else {
 *      registerReceiver(receiver, filter)
 *  }
 *  // onPause / onStop 中注销
 *  unregisterReceiver(receiver)
 *
 *
 * ── 2  定义 BroadcastReceiver ─────────────────────────────────────────────────
 *
 *  class MyReceiver : BroadcastReceiver() {
 *      override fun onReceive(context: Context, intent: Intent) {
 *          when (intent.action) {
 *              "com.example.MY_ACTION" -> {
 *                  val data = intent.getStringExtra("data")
 *                  // 注意：onReceive 运行在主线程，不能做耗时操作
 *                  // 超过 10 秒未返回会触发 ANR
 *              }
 *          }
 *      }
 *  }
 *
 *
 * ── 3  发送自定义广播 ─────────────────────────────────────────────────────────
 *
 *  // 普通广播（所有匹配 Receiver 同时收到）
 *  val intent = Intent("com.example.MY_ACTION").apply {
 *      putExtra("data", "hello")
 *      setPackage(packageName)   // Android 8+ 必须指定包名（显式广播）
 *  }
 *  sendBroadcast(intent)
 *
 *  // 有序广播（Receiver 按 priority 顺序依次接收，可中止传播）
 *  sendOrderedBroadcast(intent, null)
 *
 *  // 在高优先级 Receiver 中中止传播
 *  override fun onReceive(context: Context, intent: Intent) {
 *      abortBroadcast()   // 后续 Receiver 不再收到
 *  }
 *
 *  // 带权限的广播（只有声明该权限的 App 才能接收）
 *  sendBroadcast(intent, "com.example.MY_PERMISSION")
 *
 *
 * ── 4  常用系统广播 ───────────────────────────────────────────────────────────
 *
 *  · BOOT_COMPLETED：开机完成（需 RECEIVE_BOOT_COMPLETED 权限，静态注册）
 *  · ACTION_BATTERY_LOW / ACTION_BATTERY_OKAY：电量低/恢复（只能动态注册）
 *  · ACTION_SCREEN_ON / ACTION_SCREEN_OFF：屏幕亮/灭（只能动态注册）
 *  · ACTION_AIRPLANE_MODE_CHANGED：飞行模式切换
 *  · ACTION_LOCALE_CHANGED：系统语言切换
 *  · ACTION_PACKAGE_ADDED / REMOVED：应用安装/卸载
 *  · ConnectivityManager.CONNECTIVITY_ACTION：网络状态变化（Android 7+ 不支持静态注册）
 *
 *  // 监听网络变化（推荐用 NetworkCallback 替代）
 *  val filter = IntentFilter().apply {
 *      addAction(ConnectivityManager.CONNECTIVITY_ACTION)
 *      addAction(Intent.ACTION_BATTERY_LOW)
 *  }
 *  registerReceiver(myReceiver, filter)
 *
 *
 * ── 5  应用内通信替代方案 ──────────────────────────────────────────────────────
 *
 *  LocalBroadcastManager（已废弃，不推荐新项目使用）：
 *  · 仅在应用内传播，不跨进程，安全性高
 *  · 已在 AndroidX 1.1.0 废弃，用 LiveData / SharedFlow 替代
 *
 *  // 推荐替代：SharedFlow（应用内事件总线）
 *  object EventBus {
 *      private val _events = MutableSharedFlow<AppEvent>()
 *      val events: SharedFlow<AppEvent> = _events.asSharedFlow()
 *
 *      suspend fun emit(event: AppEvent) = _events.emit(event)
 *  }
 *
 *  // 发送事件
 *  viewModelScope.launch { EventBus.emit(AppEvent.UserLoggedOut) }
 *
 *  // 接收事件（在 ViewModel 或 Composable 中）
 *  lifecycleScope.launch {
 *      repeatOnLifecycle(Lifecycle.State.STARTED) {
 *          EventBus.events.collect { event -> handleEvent(event) }
 *      }
 *  }
 *
 *
 * ── 6  goAsync：在 onReceive 中异步处理 ──────────────────────────────────────
 *
 *  · onReceive 默认在主线程，超时 10s 触发 ANR
 *  · goAsync() 延长处理时间（约 10s），配合协程做轻量异步
 *  · 重量级任务应转交 WorkManager
 *
 *  class DataReceiver : BroadcastReceiver() {
 *      override fun onReceive(context: Context, intent: Intent) {
 *          val pendingResult = goAsync()
 *          CoroutineScope(Dispatchers.IO).launch {
 *              try {
 *                  doLightWork()
 *              } finally {
 *                  pendingResult.finish()   // 必须调用，否则系统认为 ANR
 *              }
 *          }
 *      }
 *  }
 *
 *  // 重量级任务：转交 WorkManager
 *  override fun onReceive(context: Context, intent: Intent) {
 *      WorkManager.getInstance(context)
 *          .enqueue(OneTimeWorkRequestBuilder<SyncWorker>().build())
 *  }
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · onReceive 不能做耗时操作；轻量异步用 goAsync()，重量级用 WorkManager
 *  · 应用内通信用 LiveData / SharedFlow，不要用 LocalBroadcastManager（已废弃）
 *  · Android 8+ 自定义广播必须用显式广播（setPackage 或 setComponent）
 *  · Android 13+ 动态注册需声明 RECEIVER_NOT_EXPORTED / RECEIVER_EXPORTED
 *  · 发送广播时加权限参数，防止恶意 App 接收敏感广播
 *  · 监听网络变化推荐用 ConnectivityManager.registerNetworkCallback()
 *  · 动态注册必须在 onStop/onDestroy 中注销，避免内存泄漏
 */

private val Blue = Color(0xFF2196F3)

private val chapters = listOf(
    NoteChapter("1", "两种注册方式"),
    NoteChapter("2", "定义 BroadcastReceiver"),
    NoteChapter("3", "发送自定义广播"),
    NoteChapter("4", "常用系统广播"),
    NoteChapter("5", "应用内通信替代方案"),
    NoteChapter("6", "goAsync：在 onReceive 中异步处理"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun BroadcastScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "BroadcastReceiver",
        subtitle = "静态/动态注册 · 有序广播 · 系统广播 · goAsync",
        color = Blue,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
