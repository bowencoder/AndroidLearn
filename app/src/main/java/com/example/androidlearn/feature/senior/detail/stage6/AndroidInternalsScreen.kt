package com.example.androidlearn.feature.senior.detail.stage6

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Android 系统深度理解",
    description = "Binder IPC，Handler/Looper，AMS/WMS，渲染管线",
    overview = "理解 Android 系统底层机制，是排查疑难问题、做极致优化和进行系统级开发的基础。",
    keyPoints = listOf(
        "Binder：Android 核心 IPC 机制，一次拷贝，安全性高，系统服务通信基础",
        "Handler / Looper / MessageQueue：主线程消息循环，UI 操作线程安全的保证",
        "AMS（ActivityManagerService）：管理 Activity 生命周期、进程优先级、Task 栈",
        "WMS（WindowManagerService）：管理窗口层级（z-order）、Surface 分配",
        "VSYNC 与渲染管线：Choreographer → SurfaceFlinger → HWComposer",
        "进程优先级：前台/可见/服务/后台进程，系统内存不足时按优先级 kill"
    ),
    codeSnippet = """
// Handler 机制核心
val handler = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            MSG_UPDATE_UI -> updateUI(msg.obj as String)
        }
    }
}

// 工作线程发消息
thread {
    val data = fetchFromNetwork()
    handler.sendMessage(Message.obtain(handler, MSG_UPDATE_UI, data))
}

// Binder - AIDL 跨进程通信
// IMyService.aidl
interface IMyService {
    String getData(int id);
}
// Service 端实现 Stub，Client 端调用 Proxy
    """.trimIndent(),
    tips = listOf(
        "systrace / Perfetto 的 Binder 列可以看到跨进程调用耗时",
        "Handler 内部类持有 Activity 引用会导致内存泄漏，用 WeakReference",
        "读源码建议从 AOSP 在线查看：cs.android.com"
    )
)

@Composable
fun AndroidInternalsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "架构设计与前沿技术",
        onBack = onBack
    )
}
