package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Kotlin 协程与 Flow
 * 官方文档：https://kotlinlang.org/docs/coroutines-overview.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  协程基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  核心概念 ─────────────────────────────────────────────────────────────
 *
 *  · suspend 函数：可暂停的函数，不阻塞线程
 *  · CoroutineScope：协程的生命周期容器
 *  · Dispatcher：调度器，决定协程运行在哪个线程
 *    - Dispatchers.IO：网络/磁盘 I/O 操作
 *    - Dispatchers.Main：UI 线程操作
 *    - Dispatchers.Default：CPU 密集型计算
 *
 * ── 1.2  启动方式 ─────────────────────────────────────────────────────────────
 *
 *  // 顺序执行
 *  viewModelScope.launch {
 *      val user = withContext(Dispatchers.IO) { api.getUser(1) }
 *      val orders = withContext(Dispatchers.IO) { api.getOrders(user.id) }
 *  }
 *
 *  // 并行执行
 *  viewModelScope.launch {
 *      val userDeferred = async(Dispatchers.IO) { api.getUser(1) }
 *      val configDeferred = async(Dispatchers.IO) { api.getConfig() }
 *      val user = userDeferred.await()
 *      val config = configDeferred.await()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Flow
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  冷流 Flow ────────────────────────────────────────────────────────────
 *
 *  · 冷流：订阅时才开始执行，顺序发射数据
 *  · 替代 RxJava，与协程无缝集成
 *
 *  fun getItems(): Flow<List<Item>> = flow {
 *      emit(db.getItems())          // 本地缓存
 *      emit(api.getItems())         // 网络最新数据
 *  }.flowOn(Dispatchers.IO)
 *
 * ── 2.2  热流 StateFlow / SharedFlow ─────────────────────────────────────────
 *
 *  · StateFlow：始终有值，新订阅者立即收到当前值，适合 UI 状态
 *  · SharedFlow：可配置缓存，适合一次性事件（导航、Toast）
 *
 *  // 一次性事件
 *  private val _events = MutableSharedFlow<UiEvent>()
 *  val events: SharedFlow<UiEvent> = _events.asSharedFlow()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 网络/IO 操作放在 Dispatchers.IO，UI 更新在 Dispatchers.Main
 *  · 用 supervisorScope 让子协程失败不影响兄弟协程
 *  · Flow 的 catch 操作符处理上游异常
 *  · 使用 flowOn 而非 withContext 切换 Flow 的执行线程
 */

val coroutinesData = NoteData(
    title = "Kotlin 协程与 Flow",
    subtitle = "现代架构体系 · suspend · async/await · Flow",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "协程基础"),
        ChapterItem("1.1", "核心概念"),
        ChapterItem("1.2", "启动方式"),
        ChapterItem("2",   "Flow"),
        ChapterItem("2.1", "冷流 Flow"),
        ChapterItem("2.2", "热流 StateFlow / SharedFlow"),
        ChapterItem("3",   "最佳实践"),
    )
)
