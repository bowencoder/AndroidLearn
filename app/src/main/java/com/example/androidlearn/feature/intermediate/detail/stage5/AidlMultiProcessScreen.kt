package com.example.androidlearn.feature.intermediate.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "多进程与 AIDL",
    description = "进程间通信 · AIDL · Messenger · 数据同步",
    overview = "Android 支持多进程架构，通过 IPC（进程间通信）机制共享数据。AIDL 是 Android 官方 IPC 解决方案，底层基于 Binder 驱动，适合高频、复杂的跨进程通信。",
    keyPoints = listOf(
        "多进程配置：AndroidManifest 中 android:process=\":remote\" 创建独立进程",
        "多进程问题：Application 多次初始化、单例失效、SharedPreferences 不安全",
        "AIDL：定义 .aidl 接口文件，编译器自动生成 Stub/Proxy 代码",
        "Messenger：基于 AIDL 的简单封装，适合单线程消息传递",
        "Parcelable：AIDL 中传递自定义对象必须实现 Parcelable 接口",
        "跨进程数据同步：ContentProvider、广播、文件共享（注意并发）"
    ),
    codeSnippet = """
// ICalculator.aidl
interface ICalculator {
    int add(int a, int b);
    List<String> getHistory();
}

// Service 端实现
class CalcService : Service() {
    private val binder = object : ICalculator.Stub() {
        override fun add(a: Int, b: Int) = a + b
        override fun getHistory() = history
    }
    override fun onBind(intent: Intent) = binder
}

// Client 端绑定
val conn = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val calc = ICalculator.Stub.asInterface(service)
        val result = calc.add(3, 5)
    }
    override fun onServiceDisconnected(name: ComponentName) {}
}
bindService(intent, conn, Context.BIND_AUTO_CREATE)
    """.trimIndent(),
    tips = listOf(
        "AIDL 方法运行在 Binder 线程池，服务端需处理线程安全问题",
        "简单场景用 Messenger，复杂高频接口用 AIDL",
        "跨进程传递大数据考虑 Parcelable + ashmem（匿名共享内存）"
    )
)

@Composable
fun AidlMultiProcessScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "多媒体与系统能力",
        onBack = onBack
    )
}
