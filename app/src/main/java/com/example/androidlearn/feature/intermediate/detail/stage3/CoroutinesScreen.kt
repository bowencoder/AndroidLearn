package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Kotlin 协程与 Flow（中级深度）
 * 官方文档：https://kotlinlang.org/docs/coroutines-overview.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  协程基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  核心概念 ─────────────────────────────────────────────────────────────
 *
 *  · suspend 函数：可暂停的函数，不阻塞线程，只能在协程或其他 suspend 函数中调用
 *  · CoroutineScope：协程的生命周期容器，所有协程都在 Scope 内启动
 *  · Job：协程的句柄，可用于取消、等待协程完成
 *  · Dispatcher：调度器，决定协程运行在哪个线程
 *    - Dispatchers.IO：网络/磁盘 I/O 操作（线程池最多 64 个线程）
 *    - Dispatchers.Main：UI 线程操作（Android 主线程）
 *    - Dispatchers.Default：CPU 密集型计算（线程数 = CPU 核心数）
 *    - Dispatchers.Unconfined：不限定线程（测试用，生产慎用）
 *
 * ── 1.2  启动方式 ─────────────────────────────────────────────────────────────
 *
 *  // launch：不关心返回值（fire-and-forget）
 *  viewModelScope.launch {
 *      val user = withContext(Dispatchers.IO) { api.getUser(1) }
 *      _uiState.value = UiState.Success(user)
 *  }
 *
 *  // async/await：并行执行，获取返回值
 *  viewModelScope.launch {
 *      val userDeferred   = async(Dispatchers.IO) { api.getUser(1) }
 *      val configDeferred = async(Dispatchers.IO) { api.getConfig() }
 *      val user   = userDeferred.await()
 *      val config = configDeferred.await()
 *  }
 *
 *  // runBlocking：阻塞当前线程（仅用于测试/main 函数）
 *  fun main() = runBlocking {
 *      delay(1000)
 *      println("Hello Coroutines")
 *  }
 *
 * ── 1.3  CoroutineStart 启动模式 ──────────────────────────────────────────────
 *
 *  · DEFAULT：立即调度执行（默认）
 *  · LAZY：调用 start() 或 await() 时才启动
 *  · ATOMIC：立即调度，但在第一个挂起点前不可取消
 *  · UNDISPATCHED：在当前线程立即执行，直到第一个挂起点
 *
 *  val job = launch(start = CoroutineStart.LAZY) { doWork() }
 *  job.start()  // 手动启动
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  结构化并发
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  父子关系与取消传播 ───────────────────────────────────────────────────
 *
 *  · 父协程取消 → 所有子协程取消（结构化并发核心）
 *  · 子协程失败 → 父协程取消 → 兄弟协程取消（默认行为）
 *  · viewModelScope：ViewModel.onCleared() 时自动取消
 *  · lifecycleScope：Activity/Fragment 销毁时自动取消
 *
 *  viewModelScope.launch {
 *      val child1 = launch { doWork1() }
 *      val child2 = launch { doWork2() }
 *      // viewModelScope 取消时，child1 和 child2 也会被取消
 *  }
 *
 * ── 2.2  supervisorScope ──────────────────────────────────────────────────────
 *
 *  · 子协程失败不影响兄弟协程（与默认 coroutineScope 的区别）
 *  · SupervisorJob：创建 supervisorScope 的 Job
 *
 *  supervisorScope {
 *      val a = async { riskyTask() }   // a 失败不影响 b
 *      val b = async { safeTask() }
 *      try { a.await() } catch (e: Exception) { /* 处理 a 的失败 */ }
 *      println(b.await())  // b 仍可正常获取结果
 *  }
 *
 * ── 2.3  协程取消与 CancellationException ─────────────────────────────────────
 *
 *  · job.cancel() 发送取消信号，协程在下一个挂起点响应
 *  · isActive 检查协程是否仍在运行（CPU 密集型任务中手动检查）
 *  · CancellationException 是正常取消，不应被 catch 后吞掉
 *
 *  val job = launch {
 *      repeat(1000) { i ->
 *          if (!isActive) return@launch  // 手动检查取消
 *          doHeavyWork(i)
 *      }
 *  }
 *  delay(500)
 *  job.cancelAndJoin()  // 取消并等待完成
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Flow
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  冷流 Flow ────────────────────────────────────────────────────────────
 *
 *  · 冷流：每次 collect 都重新执行，顺序发射数据
 *  · flow { } 构建器；emit() 发射值；flowOn() 切换执行线程
 *  · 替代 RxJava，与协程无缝集成
 *
 *  fun getItems(): Flow<List<Item>> = flow {
 *      emit(db.getItems())          // 先发本地缓存
 *      emit(api.getItems())         // 再发网络最新数据
 *  }.flowOn(Dispatchers.IO)         // 在 IO 线程执行（不影响 collect 所在线程）
 *
 * ── 3.2  热流 StateFlow / SharedFlow ─────────────────────────────────────────
 *
 *  · StateFlow：始终有值，新订阅者立即收到当前值，适合 UI 状态
 *  · SharedFlow：可配置缓存，适合一次性事件（导航、Toast）
 *
 *  // ViewModel 中
 *  private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
 *  val uiState: StateFlow<UiState> = _uiState.asStateFlow()
 *
 *  // 一次性事件
 *  private val _events = MutableSharedFlow<UiEvent>()
 *  val events: SharedFlow<UiEvent> = _events.asSharedFlow()
 *
 *  // Compose 中收集
 *  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 *
 * ── 3.3  Flow 操作符 ──────────────────────────────────────────────────────────
 *
 *  · map / filter / take / drop：转换与过滤
 *  · combine：合并多个 Flow 的最新值
 *  · flatMapLatest：切换到最新 Flow，取消旧的（搜索场景）
 *  · debounce：防抖，延迟指定时间后才发射（搜索框输入）
 *  · distinctUntilChanged：过滤连续重复值
 *  · buffer：缓冲发射，解耦生产者和消费者速度
 *  · conflate：只保留最新值，跳过中间值（UI 渲染场景）
 *
 *  // 搜索防抖示例
 *  searchQuery
 *      .debounce(300)
 *      .distinctUntilChanged()
 *      .flatMapLatest { query -> searchApi(query) }
 *      .catch { e -> emit(emptyList()) }
 *      .collect { results -> updateUi(results) }
 *
 *  // combine 合并多个状态
 *  combine(userFlow, settingsFlow) { user, settings ->
 *      UiState(user = user, settings = settings)
 *  }.collect { state -> render(state) }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  异常处理
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 4.1  try/catch 与 CoroutineExceptionHandler ───────────────────────────────
 *
 *  · try/catch 在 suspend 函数中正常使用
 *  · CoroutineExceptionHandler：捕获未处理的协程异常（仅对 launch 有效，async 需 await 时捕获）
 *
 *  // try/catch（推荐方式）
 *  viewModelScope.launch {
 *      try {
 *          val data = fetchData()
 *          _uiState.value = UiState.Success(data)
 *      } catch (e: IOException) {
 *          _uiState.value = UiState.Error(e.message ?: "Unknown error")
 *      }
 *  }
 *
 *  // CoroutineExceptionHandler（全局兜底）
 *  val handler = CoroutineExceptionHandler { _, throwable ->
 *      Log.e("TAG", "Unhandled exception", throwable)
 *  }
 *  viewModelScope.launch(handler) { riskyWork() }
 *
 * ── 4.2  Flow 异常处理 ────────────────────────────────────────────────────────
 *
 *  · catch 操作符：处理上游异常，可 emit 默认值
 *  · onCompletion：Flow 完成（正常/异常）时执行
 *
 *  getItems()
 *      .catch { e ->
 *          Log.e("TAG", "Flow error", e)
 *          emit(emptyList())  // 发射默认值
 *      }
 *      .onCompletion { cause -> if (cause != null) hideLoading() }
 *      .collect { items -> updateUi(items) }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 网络/IO 操作放在 Dispatchers.IO，UI 更新在 Dispatchers.Main
 *  · 用 supervisorScope 让子协程失败不影响兄弟协程
 *  · Flow 的 catch 操作符处理上游异常
 *  · 使用 flowOn 而非 withContext 切换 Flow 的执行线程
 *  · 避免在 ViewModel 中直接持有 Activity/Fragment 引用（内存泄漏）
 *  · 使用 collectAsStateWithLifecycle 代替 collectAsState（感知生命周期）
 *  · 不要在 catch 中吞掉 CancellationException（会破坏结构化并发）
 */

val coroutinesData = NoteData(
    title = "Kotlin 协程与 Flow",
    subtitle = "suspend · Dispatcher · 结构化并发 · Flow · 异常处理",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "协程基础"),
        ChapterItem("1.1", "核心概念：suspend / Scope / Job / Dispatcher"),
        ChapterItem("1.2", "启动方式：launch / async / runBlocking"),
        ChapterItem("1.3", "CoroutineStart 启动模式"),
        ChapterItem("2",   "结构化并发"),
        ChapterItem("2.1", "父子关系与取消传播"),
        ChapterItem("2.2", "supervisorScope：子协程失败互不影响"),
        ChapterItem("2.3", "协程取消与 CancellationException"),
        ChapterItem("3",   "Flow"),
        ChapterItem("3.1", "冷流 Flow：flow { } / emit / flowOn"),
        ChapterItem("3.2", "热流 StateFlow / SharedFlow"),
        ChapterItem("3.3", "Flow 操作符：map/filter/combine/flatMapLatest/debounce"),
        ChapterItem("4",   "异常处理"),
        ChapterItem("4.1", "try/catch 与 CoroutineExceptionHandler"),
        ChapterItem("4.2", "Flow 异常处理：catch / onCompletion"),
        ChapterItem("5",   "最佳实践"),
    )
)
