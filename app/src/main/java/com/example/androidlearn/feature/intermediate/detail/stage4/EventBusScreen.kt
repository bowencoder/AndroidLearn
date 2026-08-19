package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * EventBus 事件总线
 * 官方文档：https://greenrobot.org/eventbus/
 * GitHub：https://github.com/greenrobot/EventBus
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心概念
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  三个角色 ─────────────────────────────────────────────────────────
 *
 *  · Event（事件）：普通 POJO 类，承载数据
 *  · Publisher（发布者）：调用 EventBus.getDefault().post(event) 发送事件
 *  · Subscriber（订阅者）：注册到 EventBus，用 @Subscribe 注解方法接收事件
 *
 *  · 核心优势：组件间完全解耦，发布者和订阅者互不持有引用
 *  · 适用场景：Activity ↔ Fragment 通信、跨层级数据传递、全局广播
 *
 *
 * ── 1.2  基本使用流程 ─────────────────────────────────────────────────────
 *
 *  // ① 定义事件类（普通 data class）
 *  data class LoginEvent(val userId: String, val success: Boolean)
 *  data class NetworkChangeEvent(val isConnected: Boolean)
 *
 *  // ② 订阅者注册与注销（Activity / Fragment）
 *  override fun onStart() {
 *      super.onStart()
 *      EventBus.getDefault().register(this)    // 注册
 *  }
 *
 *  override fun onStop() {
 *      super.onStop()
 *      EventBus.getDefault().unregister(this)  // 注销（必须！否则内存泄漏）
 *  }
 *
 *  // ③ 接收事件（@Subscribe 注解方法，参数类型即事件类型）
 *  @Subscribe(threadMode = ThreadMode.MAIN)
 *  fun onLoginEvent(event: LoginEvent) {
 *      if (event.success) showWelcome(event.userId)
 *  }
 *
 *  // ④ 发布事件（任意位置）
 *  EventBus.getDefault().post(LoginEvent(userId = "123", success = true))
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  ThreadMode（线程模式）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · POSTING（默认）：在发布事件的线程中执行，同步调用，开销最小
 *    - 适合：不切换线程、执行极快的操作
 *    - 注意：若在主线程发布，订阅方法也在主线程执行，不能做耗时操作
 *
 *  · MAIN：在主线程（UI 线程）执行
 *    - 若发布线程是主线程：同步调用（与 POSTING 相同）
 *    - 若发布线程是子线程：通过 Handler 切换到主线程
 *    - 适合：更新 UI
 *
 *  · MAIN_ORDERED：在主线程执行，但始终通过 Handler 排队（异步）
 *    - 与 MAIN 区别：即使发布线程是主线程，也不会立即执行，而是排队
 *    - 适合：需要严格保证执行顺序的 UI 更新
 *
 *  · BACKGROUND：在后台线程执行
 *    - 若发布线程是子线程：直接在该线程执行
 *    - 若发布线程是主线程：在 EventBus 的后台线程池中执行
 *    - 适合：轻量级 IO 操作，不适合耗时任务（会阻塞后台线程）
 *
 *  · ASYNC：始终在独立的异步线程执行（线程池）
 *    - 无论发布线程是什么，都在新线程中执行
 *    - 适合：耗时操作（网络请求、数据库操作）
 *    - 注意：并发执行，需注意线程安全
 *
 *  // 示例：不同线程模式
 *  @Subscribe(threadMode = ThreadMode.MAIN)
 *  fun onEvent(event: UpdateUiEvent) { textView.text = event.text }
 *
 *  @Subscribe(threadMode = ThreadMode.ASYNC)
 *  fun onEvent(event: UploadEvent) { uploadFile(event.filePath) }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  粘性事件（Sticky Event）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 普通事件：订阅者注册前发布的事件，订阅者收不到
 *  · 粘性事件：EventBus 缓存最后一个粘性事件，新订阅者注册后立即收到
 *  · 适用场景：登录状态、网络状态等"全局状态"的初始化同步
 *
 *  // 发布粘性事件
 *  EventBus.getDefault().postSticky(NetworkChangeEvent(isConnected = true))
 *
 *  // 接收粘性事件（sticky = true）
 *  @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
 *  fun onNetworkChange(event: NetworkChangeEvent) {
 *      updateNetworkStatus(event.isConnected)
 *  }
 *
 *  // 手动移除粘性事件（不再需要时清理）
 *  EventBus.getDefault().removeStickyEvent(NetworkChangeEvent::class.java)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  事件优先级
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · priority 值越大，优先级越高（默认 0）
 *  · 高优先级订阅者先收到事件
 *  · 仅在相同 ThreadMode 的订阅者之间有效
 *
 *  @Subscribe(threadMode = ThreadMode.MAIN, priority = 10)
 *  fun onHighPriorityEvent(event: MyEvent) {
 *      // 先于 priority = 0 的订阅者执行
 *      // 可调用 EventBus.getDefault().cancelEventDelivery(event) 阻止继续传递
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  EventBus 配置与索引加速
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 默认使用反射查找 @Subscribe 方法，有一定性能开销
 *  · EventBus 注解处理器（APT）可在编译期生成订阅者索引，避免运行时反射
 *
 *  // build.gradle.kts 添加注解处理器
 *  kapt("org.greenrobot:eventbus-annotation-processor:3.3.1")
 *
 *  // Application 中初始化（使用索引）
 *  EventBus.builder()
 *      .addIndex(MyEventBusIndex())   // 编译期生成的索引类
 *      .installDefaultEventBus()
 *
 *  // 自定义配置
 *  EventBus.builder()
 *      .logNoSubscriberMessages(false)   // 关闭无订阅者日志
 *      .sendNoSubscriberEvent(false)     // 无订阅者时不发送 NoSubscriberEvent
 *      .throwSubscriberException(true)   // 订阅者异常时抛出（调试用）
 *      .installDefaultEventBus()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践与注意事项
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 推荐做法：
 *  · 事件类用 data class，字段不可变（val）
 *  · 在 onStart/onStop 或 onCreate/onDestroy 中成对注册/注销
 *  · 事件命名语义化（LoginSuccessEvent、CartUpdateEvent）
 *  · 复杂场景用 sticky 事件传递初始状态
 *
 *  ⚠️ 注意事项：
 *  · 必须注销（unregister），否则内存泄漏
 *  · 不要在 @Subscribe 方法中做耗时操作（除非 ASYNC 模式）
 *  · 避免滥用：EventBus 适合跨层级通信，同层级优先用接口回调
 *  · 事件链过长会导致调试困难（"事件满天飞"问题）
 *
 *  EventBus vs 其他方案：
 *  ┌──────────────────┬──────────────────────┬──────────────────────────────┐
 *  │                  │      EventBus         │    LiveData / SharedFlow     │
 *  ├──────────────────┼──────────────────────┼──────────────────────────────┤
 *  │ 生命周期感知     │ 无（需手动注销）      │ 有（自动感知）               │
 *  │ 线程切换         │ ThreadMode 注解       │ observeOn / Dispatchers      │
 *  │ 粘性事件         │ 原生支持              │ LiveData 天然粘性            │
 *  │ 类型安全         │ 运行时（反射）        │ 编译期泛型                   │
 *  │ 适用场景         │ 跨模块全局事件        │ ViewModel → View 状态驱动    │
 *  └──────────────────┴──────────────────────┴──────────────────────────────┘
 *
 *  · 新项目推荐 SharedFlow（Kotlin 协程）替代 EventBus
 *  · 旧项目 EventBus 仍广泛使用，了解其原理和坑点很重要
 */

val eventBusData = NoteData(
    title = "EventBus 事件总线",
    subtitle = "发布订阅 · ThreadMode · 粘性事件 · 优先级 · 索引加速",
    color = Color.parseColor("#4CAF50"),
    chapters = listOf(
        ChapterItem("1",   "核心概念"),
        ChapterItem("1.1", "三个角色：Event / Publisher / Subscriber"),
        ChapterItem("1.2", "基本使用：定义事件 / 注册注销 / @Subscribe / post"),
        ChapterItem("2",   "ThreadMode（线程模式）"),
        ChapterItem("2.1", "POSTING / MAIN / MAIN_ORDERED / BACKGROUND / ASYNC"),
        ChapterItem("3",   "粘性事件（Sticky Event）"),
        ChapterItem("3.1", "postSticky / sticky=true / removeStickyEvent"),
        ChapterItem("4",   "事件优先级：priority / cancelEventDelivery"),
        ChapterItem("5",   "配置与索引加速"),
        ChapterItem("5.1", "APT 编译期索引 / EventBus.builder() 自定义配置"),
        ChapterItem("6",   "最佳实践与注意事项"),
        ChapterItem("6.1", "EventBus vs LiveData / SharedFlow 方案对比"),
    )
)
