package com.example.androidlearn.feature.senior.detail.stage7

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Binder 机制深度解析",
    description = "一次拷贝原理，ServiceManager，AIDL 全链路",
    overview = "Binder 是 Android 最核心的 IPC 机制，几乎所有系统服务通信都依赖它。理解 Binder 原理是排查跨进程问题和系统级开发的基础。",
    keyPoints = listOf(
        "内核驱动：/dev/binder，通过 mmap 实现发送端→内核→接收端一次拷贝",
        "ServiceManager：Binder 的「DNS」，注册与查询系统服务",
        "Stub / Proxy：服务端实现 Stub，客户端调用 Proxy，框架自动序列化",
        "线程池：Binder 驱动默认为每个进程分配 15+1 个线程处理请求",
        "AIDL：Android Interface Definition Language，自动生成跨进程代码",
        "linkToDeath：监听远程服务进程死亡，及时重连"
    ),
    codeSnippet = """
// IMyService.aidl
interface IMyService {
    String getData(int id);
    void registerCallback(IMyCallback cb);
}

// Service 端
class MyService : Service() {
    private val binder = object : IMyService.Stub() {
        override fun getData(id: Int) = "data_${'$'}id"
        override fun registerCallback(cb: IMyCallback?) { /* ... */ }
    }
    override fun onBind(intent: Intent) = binder
}

// Client 端
val conn = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val myService = IMyService.Stub.asInterface(service)
        service.linkToDeath({ reconnect() }, 0)
        Log.d("Binder", myService.getData(1))
    }
    override fun onServiceDisconnected(name: ComponentName) { reconnect() }
}
bindService(intent, conn, BIND_AUTO_CREATE)
    """.trimIndent(),
    tips = listOf(
        "Binder 调用默认同步阻塞，耗时操作的远程服务端要用 oneway 修饰",
        "Binder 事务数据上限约 1MB，传递大数据改用 ParcelFileDescriptor",
        "用 Perfetto 的 Binder 泳道可直接看到跨进程调用耗时"
    )
)

@Composable
fun BinderScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF795548),
        stageTitle = "底层原理与面试重点",
        onBack = onBack
    )
}
