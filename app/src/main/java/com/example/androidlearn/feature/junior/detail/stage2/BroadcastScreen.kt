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
 * BroadcastReceiver 笔记
 * 官方文档：https://developer.android.com/guide/components/broadcasts
 *
 * ── 1  两种注册方式 ───────────────────────────────────────────────────────────
 *
 *  静态注册（AndroidManifest.xml）：
 *  · App 未运行也能接收；Android 8+ 大部分隐式广播不再支持静态注册
 *  <receiver android:name=".BootReceiver" android:exported="true">
 *      <intent-filter>
 *          <action android:name="android.intent.action.BOOT_COMPLETED"/>
 *      </intent-filter>
 *  </receiver>
 *
 *  动态注册（代码，推荐）：
 *  · 跟随组件生命周期，必须配对注销，否则内存泄漏
 *  val receiver = NetworkReceiver()
 *  val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
 *  registerReceiver(receiver, filter)      // onResume 中注册
 *  unregisterReceiver(receiver)            // onPause 中注销
 *
 *
 * ── 2  定义与发送广播 ─────────────────────────────────────────────────────────
 *
 *  // 定义 Receiver
 *  class MyReceiver : BroadcastReceiver() {
 *      override fun onReceive(context: Context, intent: Intent) {
 *          val data = intent.getStringExtra("data")
 *          // 注意：不能做耗时操作，超时 10s 触发 ANR
 *      }
 *  }
 *
 *  // 发送自定义广播（Android 8+ 需指定包名，即显式广播）
 *  val intent = Intent("com.example.MY_ACTION").apply {
 *      putExtra("data", "hello")
 *      setPackage(packageName)
 *  }
 *  sendBroadcast(intent)
 *
 *  // 有序广播：Receiver 按 priority 顺序接收，可中止传播
 *  sendOrderedBroadcast(intent, null)
 *  // 在 Receiver 中中止：abortBroadcast()
 *
 *
 * ── 3  常用系统广播 ───────────────────────────────────────────────────────────
 *
 *  · BOOT_COMPLETED：开机完成（需 RECEIVE_BOOT_COMPLETED 权限）
 *  · ACTION_BATTERY_LOW / ACTION_BATTERY_OKAY：电量低/恢复
 *  · ACTION_SCREEN_ON / ACTION_SCREEN_OFF：屏幕亮/灭（只能动态注册）
 *  · ACTION_AIRPLANE_MODE_CHANGED：飞行模式切换
 *  · ACTION_LOCALE_CHANGED：系统语言切换
 *
 *
 * ── 4  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · onReceive 不能做耗时操作；需要异步用 goAsync() + 协程，或转交 WorkManager
 *  · 应用内通信用 LiveData / SharedFlow 替代 LocalBroadcastManager（已废弃）
 *  · Android 8+ 只有少数系统广播（BOOT_COMPLETED 等）允许静态注册
 *  · 发送广播时加 setPackage() 限制接收方，避免安全风险
 *
 *  // goAsync 示例（在 onReceive 中异步处理）
 *  override fun onReceive(context: Context, intent: Intent) {
 *      val pendingResult = goAsync()
 *      CoroutineScope(Dispatchers.IO).launch {
 *          doHeavyWork()
 *          pendingResult.finish()
 *      }
 *  }
 */

private val Blue = Color(0xFF2196F3)

private data class BroadcastChapter(val num: String, val title: String)

private val chapters = listOf(
    BroadcastChapter("1", "两种注册方式"),
    BroadcastChapter("2", "定义与发送广播"),
    BroadcastChapter("3", "常用系统广播"),
    BroadcastChapter("4", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("BroadcastReceiver", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "静态/动态注册 · 有序广播 · 系统广播",
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
            items(chapters.size) { i -> ChapterRowBroadcast(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowBroadcast(chapter: BroadcastChapter) {
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
