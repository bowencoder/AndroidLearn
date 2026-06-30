package com.example.androidlearn.feature.intermediate.detail.stage8

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

// ═══════════════════════════════════════════════════════════
//  【Looper / Handler 消息机制】专属学习页
//  stageIndex=7, topicIndex=1
//  阶段颜色：青色 0xFF00BCD4（中级扩展 Stage 7）
// ═══════════════════════════════════════════════════════════

private val detail = TopicDetail(
    title = "Looper / Handler 消息机制",
    description = "MessageQueue、epoll 空闲等待、IdleHandler、子线程 Looper",
    overview = "Android 的主线程消息循环由 Looper + MessageQueue + Handler 构成，是 UI 线程安全和跨线程通信的基础，等价于 iOS 的 RunLoop，但实现更简洁。",
    keyPoints = listOf(
        "Looper.loop()：主线程无限循环，通过 epoll_wait 在无消息时休眠，不占 CPU",
        "MessageQueue.next()：取出下一条消息，支持延迟消息（uptimeMillis）",
        "Handler：发送消息（sendMessage/post）和接收消息（handleMessage）的双重角色",
        "主线程唯一 Looper：ActivityThread.main() 调用 Looper.prepareMainLooper() 创建",
        "IdleHandler：消息队列空闲时执行，适合延迟初始化非关键组件",
        "子线程 Looper：Looper.prepare() + Looper.loop() 构建，HandlerThread 封装版"
    ),
    codeSnippet = """
// Handler 跨线程通信
val mainHandler = Handler(Looper.getMainLooper())

thread {
    val result = fetchDataFromNetwork()   // 子线程执行耗时操作
    mainHandler.post {
        textView.text = result            // 切回主线程更新 UI
    }
}

// IdleHandler：主线程空闲时延迟初始化
Looper.myQueue().addIdleHandler {
    NonCriticalSDK.init(context)
    false  // false = 执行一次后自动移除
}

// 子线程使用 Looper（HandlerThread 封装版）
val workerThread = HandlerThread("bg-worker").apply { start() }
val bgHandler = Handler(workerThread.looper) { msg ->
    when (msg.what) {
        MSG_PROCESS -> processData(msg.obj)
    }
    true
}

// 发送延迟消息
bgHandler.sendEmptyMessageDelayed(MSG_PROCESS, 1000L)

// 使用完毕退出
workerThread.quit()

// Looper.loop() 核心原理（简化版）
// while (true) {
//     val msg = queue.next()   // epoll_wait，无消息时阻塞休眠
//     msg.target.dispatchMessage(msg)
//     msg.recycle()
// }
    """.trimIndent(),
    tips = listOf(
        "Handler 内部类持有 Activity 引用 → 内存泄漏，改用静态类 + WeakReference",
        "主线程 Looper 死循环不耗 CPU，因为 epoll 机制让线程在无消息时进入休眠",
        "Compose/协程时代可用 LaunchedEffect 代替 Handler.post 更新 UI"
    )
)

@Composable
fun LooperHandlerScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "事件与通信机制",
        onBack = onBack
    )
}
