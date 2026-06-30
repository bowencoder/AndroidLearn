package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "BroadcastReceiver",
    description = "静态/动态注册，有序广播，LocalBroadcastManager，系统广播",
    overview = "BroadcastReceiver 是 Android 四大组件之一，用于接收系统或应用发出的广播消息。支持静态注册（Manifest）和动态注册（代码），是事件驱动编程的重要机制。",
    keyPoints = listOf(
        "静态注册：在 AndroidManifest.xml 中声明，App 未运行也能接收（受 Android 8+ 限制）",
        "动态注册：在代码中 registerReceiver / unregisterReceiver，跟随组件生命周期",
        "有序广播：sendOrderedBroadcast，Receiver 按 priority 顺序接收，可中止传播",
        "LocalBroadcastManager（已废弃）：应用内广播，推荐改用 LiveData/Flow/EventBus",
        "系统广播：BOOT_COMPLETED（开机）、CONNECTIVITY_CHANGE（网络变化）、ACTION_BATTERY_LOW",
        "Android 8+ 限制：大部分隐式广播不再允许静态注册，改为显式广播或动态注册"
    ),
    codeSnippet = """
// 定义 BroadcastReceiver
class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ConnectivityManager.CONNECTIVITY_ACTION -> {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val isConnected = cm.activeNetworkInfo?.isConnected == true
                // 处理网络变化
            }
        }
    }
}

// 动态注册（推荐）
class MainActivity : AppCompatActivity() {
    private val receiver = NetworkReceiver()

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver) // 必须配对注销，否则内存泄漏
    }
}

// 发送自定义广播
val intent = Intent("com.example.MY_ACTION").apply {
    putExtra("data", "hello")
    setPackage(packageName) // Android 8+ 需要指定包名（显式广播）
}
sendBroadcast(intent)
    """.trimIndent(),
    tips = listOf(
        "不要在 onReceive 中做耗时操作（超时 10s 会触发 ANR），改用 goAsync() 或 WorkManager",
        "应用内通信推荐用 LiveData/SharedFlow 替代 LocalBroadcastManager",
        "Android 8+ 后，只有少数系统广播（如 BOOT_COMPLETED）允许静态注册"
    )
)

@Composable
fun BroadcastScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "四大组件与核心 UI",
        onBack = onBack
    )
}
