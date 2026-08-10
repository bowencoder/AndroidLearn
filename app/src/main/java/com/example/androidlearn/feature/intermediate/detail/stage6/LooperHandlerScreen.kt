package com.example.androidlearn.feature.intermediate.detail.stage6

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Looper / Handler 消息机制
 * 官方文档：https://developer.android.com/reference/android/os/Looper
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心组件
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Looper ───────────────────────────────────────────────────────────────
 *
 *  · 主线程唯一 Looper：ActivityThread.main() 调用 Looper.prepareMainLooper() 创建
 *  · Looper.loop()：主线程无限循环，通过 epoll_wait 在无消息时休眠，不占 CPU
 *  · 子线程 Looper：Looper.prepare() + Looper.loop() 构建，HandlerThread 封装版
 *
 * ── 1.2  MessageQueue ─────────────────────────────────────────────────────────
 *
 *  · MessageQueue.next()：取出下一条消息，支持延迟消息（uptimeMillis）
 *  · 内部使用 epoll 机制，无消息时阻塞休眠，有消息时唤醒
 *
 * ── 1.3  Handler ──────────────────────────────────────────────────────────────
 *
 *  · 发送消息（sendMessage / post）和接收消息（handleMessage）的双重角色
 *  · 与 Looper 绑定，默认绑定当前线程的 Looper
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  跨线程通信示例
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // Handler 跨线程通信
 *  val mainHandler = Handler(Looper.getMainLooper())
 *
 *  thread {
 *      val result = fetchDataFromNetwork()   // 子线程执行耗时操作
 *      mainHandler.post {
 *          textView.text = result            // 切回主线程更新 UI
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  IdleHandler
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 消息队列空闲时执行，适合延迟初始化非关键组件
 *  · 返回 false = 执行一次后自动移除；返回 true = 每次空闲都执行
 *
 *  // IdleHandler：主线程空闲时延迟初始化
 *  Looper.myQueue().addIdleHandler {
 *      NonCriticalSDK.init(context)
 *      false  // 执行一次后自动移除
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  HandlerThread
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 封装了 Looper 的子线程，适合串行后台任务
 *
 *  val workerThread = HandlerThread("bg-worker").apply { start() }
 *  val bgHandler = Handler(workerThread.looper) { msg ->
 *      when (msg.what) {
 *          MSG_PROCESS -> processData(msg.obj)
 *      }
 *      true
 *  }
 *
 *  bgHandler.sendEmptyMessageDelayed(MSG_PROCESS, 1000L)
 *  workerThread.quit()   // 使用完毕退出
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Looper.loop() 核心原理
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // 简化版
 *  while (true) {
 *      val msg = queue.next()        // epoll_wait，无消息时阻塞休眠
 *      msg.target.dispatchMessage(msg)
 *      msg.recycle()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Handler 内部类持有 Activity 引用 → 内存泄漏，改用静态类 + WeakReference
 *  · 主线程 Looper 死循环不耗 CPU，因为 epoll 机制让线程在无消息时进入休眠
 *  · Compose / 协程时代可用 LaunchedEffect 代替 Handler.post 更新 UI
 */

val looperHandlerData = NoteData(
    title = "Looper / Handler 消息机制",
    subtitle = "事件机制与动态编程 · MessageQueue · epoll · IdleHandler",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "核心组件"),
        ChapterItem("1.1", "Looper"),
        ChapterItem("1.2", "MessageQueue"),
        ChapterItem("1.3", "Handler"),
        ChapterItem("2",   "跨线程通信示例"),
        ChapterItem("3",   "IdleHandler"),
        ChapterItem("4",   "HandlerThread"),
        ChapterItem("5",   "Looper.loop() 核心原理"),
        ChapterItem("6",   "最佳实践"),
    )
)
