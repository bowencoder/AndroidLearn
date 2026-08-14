package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Kotlin 协程与 Flow（中级深度）
 * 官方文档：https://kotlinlang.org/docs/coroutines-overview.html
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  协程基础  ★ 必学
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
 * ── 1.2  启动方式  ★ 必学 ──────────────────────────────────────────────────────
 *
 *  // launch：不关心返回值（fire-and-forget），返回 Job
 *  viewModelScope.launch {
 *      val user = withContext(Dispatchers.IO) { api.getUser(1) }
 *      _uiState.value = UiState.Success(user)
 *  }
 *
 *  // async/await：并行执行，获取返回值，返回 Deferred<T>
 *  viewModelScope.launch {
 *      val userDeferred   = async(Dispatchers.IO) { api.getUser(1) }
 *      val configDeferred = async(Dispatchers.IO) { api.getConfig() }
 *      val user   = userDeferred.await()
 *      val config = configDeferred.await()
 *      // 两个请求并行，总耗时 = max(t1, t2)
 *  }
 *
 *  // runBlocking：阻塞当前线程（仅用于测试/main 函数，生产代码禁用）
 *  fun main() = runBlocking {
 *      delay(1000)
 *      println("Hello Coroutines")
 *  }
 *
 *  // withContext：切换线程执行，有返回值（挂起函数，不新建协程）
 *  suspend fun fetchUser(): User = withContext(Dispatchers.IO) {
 *      api.getUser(1)  // 在 IO 线程执行，结果返回给调用方线程
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
 * ── 1.4  delay vs Thread.sleep  ★ 必学 ────────────────────────────────────────
 *
 *  · delay()：挂起当前协程，不阻塞线程，其他协程可继续运行
 *  · Thread.sleep()：阻塞整个线程，协程中禁止使用
 *
 *  // ✅ 正确：协程中用 delay
 *  launch { delay(1000); doWork() }
 *
 *  // ❌ 错误：阻塞线程，影响同线程其他协程
 *  launch { Thread.sleep(1000); doWork() }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  结构化并发  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  父子关系与取消传播 ───────────────────────────────────────────────────
 *
 *  · 父协程取消 → 所有子协程取消（结构化并发核心）
 *  · 子协程失败 → 父协程取消 → 兄弟协程取消（默认行为）
 *  · viewModelScope：ViewModel.onCleared() 时自动取消，防止内存泄漏
 *  · lifecycleScope：Activity/Fragment 销毁时自动取消
 *
 *  viewModelScope.launch {
 *      val child1 = launch { doWork1() }
 *      val child2 = launch { doWork2() }
 *      // viewModelScope 取消时，child1 和 child2 也会被取消
 *  }
 *
 * ── 2.2  supervisorScope  ★ 常用 ──────────────────────────────────────────────
 *
 *  · 子协程失败不影响兄弟协程（与默认 coroutineScope 的区别）
 *  · SupervisorJob：创建 supervisorScope 的 Job
 *  · 适用场景：多个独立任务并行，某个失败不应中断其他任务
 *
 *  supervisorScope {
 *      val a = async { riskyTask() }   // a 失败不影响 b
 *      val b = async { safeTask() }
 *      try { a.await() } catch (e: Exception) { /* 处理 a 的失败 */ }
 *      println(b.await())  // b 仍可正常获取结果
 *  }
 *
 * ── 2.3  协程取消与 CancellationException  ★ 必学 ──────────────────────────────
 *
 *  · job.cancel() 发送取消信号，协程在下一个挂起点响应
 *  · isActive 检查协程是否仍在运行（CPU 密集型任务中手动检查）
 *  · CancellationException 是正常取消，不应被 catch 后吞掉
 *  · ensureActive()：若已取消则抛出 CancellationException（比 isActive 更简洁）
 *
 *  val job = launch {
 *      repeat(1000) { i ->
 *          ensureActive()          // 若已取消则立即抛出
 *          doHeavyWork(i)
 *      }
 *  }
 *  delay(500)
 *  job.cancelAndJoin()  // 取消并等待完成
 *
 *  // ❌ 错误：吞掉 CancellationException 会破坏结构化并发
 *  try { delay(1000) } catch (e: Exception) { /* 不要这样写！ */ }
 *
 *  // ✅ 正确：只捕获业务异常
 *  try { delay(1000) } catch (e: IOException) { handleError(e) }
 *
 * ── 2.4  coroutineScope vs supervisorScope 对比  ★ 常用 ────────────────────────
 *
 *  ┌─────────────────────┬──────────────────────┬──────────────────────┐
 *  │                     │   coroutineScope     │   supervisorScope    │
 *  ├─────────────────────┼──────────────────────┼──────────────────────┤
 *  │ 子协程失败           │ 取消所有兄弟协程      │ 不影响兄弟协程        │
 *  │ 父协程取消           │ 取消所有子协程        │ 取消所有子协程        │
 *  │ 适用场景             │ 所有子任务必须成功    │ 子任务可独立失败      │
 *  └─────────────────────┴──────────────────────┴──────────────────────┘
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Flow  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  冷流 Flow  ★ 必学 ────────────────────────────────────────────────────
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
 *  // collect 在 Main 线程
 *  viewModelScope.launch {
 *      getItems().collect { items -> _uiState.value = UiState.Success(items) }
 *  }
 *
 * ── 3.2  热流 StateFlow / SharedFlow  ★ 必学 ──────────────────────────────────
 *
 *  · StateFlow：始终有值，新订阅者立即收到当前值，适合 UI 状态
 *    - 相当于 LiveData 的协程版，但必须有初始值
 *    - 连续相同值不会重复发射（distinctUntilChanged 语义）
 *  · SharedFlow：可配置缓存，适合一次性事件（导航、Toast）
 *    - replay=0：新订阅者不收到历史值（默认）
 *    - replay=1：新订阅者收到最近一条
 *
 *  // ViewModel 中
 *  private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
 *  val uiState: StateFlow<UiState> = _uiState.asStateFlow()
 *
 *  // 一次性事件（导航、Toast）
 *  private val _events = MutableSharedFlow<UiEvent>()
 *  val events: SharedFlow<UiEvent> = _events.asSharedFlow()
 *
 *  // 发送事件
 *  viewModelScope.launch { _events.emit(UiEvent.NavigateToDetail(id)) }
 *
 *  // View 层收集（感知生命周期，推荐）
 *  lifecycleScope.launch {
 *      repeatOnLifecycle(Lifecycle.State.STARTED) {
 *          viewModel.uiState.collect { state -> render(state) }
 *      }
 *  }
 *
 * ── 3.3  Flow 操作符  ★ 常用 ──────────────────────────────────────────────────
 *
 *  转换类：
 *  · map：一对一转换值
 *  · filter：过滤不满足条件的值
 *  · take(n)：只取前 n 个值后取消
 *  · transform：灵活转换，可 emit 多个值
 *
 *  合并类：
 *  · combine：合并多个 Flow 的最新值（任一更新都触发）
 *  · zip：一对一配对合并（等待两个 Flow 各发一个值）
 *  · merge：合并多个 Flow 为一个（先到先得）
 *
 *  切换类：
 *  · flatMapLatest：切换到最新 Flow，取消旧的（搜索场景）★ 常用
 *  · flatMapConcat：顺序执行，等上一个完成再处理下一个
 *  · flatMapMerge：并发执行所有 Flow
 *
 *  时间类：
 *  · debounce(ms)：防抖，延迟指定时间后才发射（搜索框输入）★ 常用
 *  · throttleFirst：节流，指定时间内只取第一个值
 *  · sample(ms)：定时采样最新值
 *
 *  去重/缓冲：
 *  · distinctUntilChanged：过滤连续重复值 ★ 常用
 *  · buffer：缓冲发射，解耦生产者和消费者速度
 *  · conflate：只保留最新值，跳过中间值（UI 渲染场景）
 *
 *  // 搜索防抖示例（经典用法）
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
 * ── 3.4  Flow 收集方式  ★ 必学 ────────────────────────────────────────────────
 *
 *  · collect：最基础的终止操作符，挂起直到 Flow 完成
 *  · collectLatest：新值到来时取消上一次处理（处理耗时场景）
 *  · toList / toSet：收集为集合（有限 Flow）
 *  · first / firstOrNull：只取第一个值
 *  · launchIn(scope)：在指定 Scope 中启动收集（配合 onEach 使用）
 *
 *  // launchIn 写法（等价于 scope.launch { flow.collect { } }）
 *  viewModel.uiState
 *      .onEach { state -> render(state) }
 *      .launchIn(lifecycleScope)
 *
 *  // collectLatest：新数据到来时取消上一次处理
 *  viewModel.searchResults
 *      .collectLatest { results ->
 *          delay(100)          // 若新值到来，此处会被取消
 *          updateUi(results)
 *      }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  异常处理  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 4.1  try/catch 与 CoroutineExceptionHandler ───────────────────────────────
 *
 *  · try/catch 在 suspend 函数中正常使用（推荐方式）
 *  · CoroutineExceptionHandler：捕获未处理的协程异常（仅对 launch 有效）
 *  · async 的异常在 await() 时才抛出，需在 await() 处 try/catch
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
 *  // CoroutineExceptionHandler（全局兜底，不能替代 try/catch）
 *  val handler = CoroutineExceptionHandler { _, throwable ->
 *      Log.e("TAG", "Unhandled exception", throwable)
 *  }
 *  viewModelScope.launch(handler) { riskyWork() }
 *
 *  // async 异常处理
 *  viewModelScope.launch {
 *      val deferred = async { riskyTask() }
 *      try {
 *          val result = deferred.await()  // 异常在这里抛出
 *      } catch (e: Exception) { handleError(e) }
 *  }
 *
 * ── 4.2  Flow 异常处理  ★ 常用 ────────────────────────────────────────────────
 *
 *  · catch 操作符：处理上游异常，可 emit 默认值（只能捕获上游异常）
 *  · onCompletion：Flow 完成（正常/异常）时执行，类似 finally
 *  · retry / retryWhen：自动重试（网络请求场景）
 *
 *  getItems()
 *      .retry(3) { e -> e is IOException }  // 网络异常最多重试 3 次
 *      .catch { e ->
 *          Log.e("TAG", "Flow error", e)
 *          emit(emptyList())  // 发射默认值，让 UI 正常显示
 *      }
 *      .onCompletion { cause ->
 *          hideLoading()
 *          if (cause != null) Log.e("TAG", "Flow completed with error", cause)
 *      }
 *      .collect { items -> updateUi(items) }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Channel  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 5.1  Channel 基础 ─────────────────────────────────────────────────────────
 *
 *  · Channel：协程间通信的管道，类似 BlockingQueue 但非阻塞
 *  · send()：发送数据（挂起直到有接收方）
 *  · receive()：接收数据（挂起直到有数据）
 *  · Channel 类型：
 *    - RENDEZVOUS（默认）：无缓冲，send/receive 必须同时就绪
 *    - BUFFERED：有缓冲（默认 64），缓冲满才挂起
 *    - UNLIMITED：无限缓冲（慎用，可能 OOM）
 *    - CONFLATED：只保留最新值
 *
 *  val channel = Channel<Int>(Channel.BUFFERED)
 *
 *  // 生产者
 *  launch { repeat(5) { channel.send(it) }; channel.close() }
 *
 *  // 消费者
 *  launch { for (value in channel) println(value) }
 *
 * ── 5.2  Channel vs Flow 选择 ─────────────────────────────────────────────────
 *
 *  ┌──────────────┬──────────────────────────┬──────────────────────────┐
 *  │              │         Flow             │         Channel          │
 *  ├──────────────┼──────────────────────────┼──────────────────────────┤
 *  │ 类型          │ 冷流（按需执行）           │ 热流（独立运行）           │
 *  │ 消费者        │ 每个 collect 独立执行      │ 多消费者共享数据           │
 *  │ 适用场景      │ 数据转换管道、UI 状态      │ 协程间通信、任务队列        │
 *  │ 背压          │ 自动（挂起生产者）         │ 手动（缓冲区控制）          │
 *  └──────────────┴──────────────────────────┴──────────────────────────┘
 *
 *  · 优先使用 Flow（更安全、操作符丰富）
 *  · Channel 适合：多个协程竞争消费同一数据源（如任务队列）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  Android 中的实战模式  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 6.1  Repository 层封装 ────────────────────────────────────────────────────
 *
 *  · Repository 的 suspend 函数由 ViewModel 在 viewModelScope 中调用
 *  · Repository 内部用 withContext(Dispatchers.IO) 切换线程
 *  · 不要在 Repository 中启动新协程（由调用方控制生命周期）
 *
 *  class UserRepository(private val api: UserApi, private val db: UserDao) {
 *      // suspend 函数，不自己启动协程
 *      suspend fun getUser(id: Int): User = withContext(Dispatchers.IO) {
 *          try {
 *              val user = api.getUser(id)
 *              db.insert(user)
 *              user
 *          } catch (e: IOException) {
 *              db.getUser(id) ?: throw e  // 网络失败时返回缓存
 *          }
 *      }
 *
 *      // Flow 版本：持续观察数据库变化
 *      fun observeUser(id: Int): Flow<User> = db.observeUser(id)
 *          .flowOn(Dispatchers.IO)
 *  }
 *
 * ── 6.2  ViewModel 层调用  ★ 必学 ────────────────────────────────────────────
 *
 *  class UserViewModel(private val repo: UserRepository) : ViewModel() {
 *
 *      private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
 *      val uiState: StateFlow<UiState> = _uiState.asStateFlow()
 *
 *      init {
 *          // 启动时自动加载
 *          viewModelScope.launch { loadUser(1) }
 *      }
 *
 *      fun loadUser(id: Int) {
 *          viewModelScope.launch {
 *              _uiState.value = UiState.Loading
 *              try {
 *                  val user = repo.getUser(id)
 *                  _uiState.value = UiState.Success(user)
 *              } catch (e: Exception) {
 *                  _uiState.value = UiState.Error(e.message ?: "Error")
 *              }
 *          }
 *      }
 *
 *      // 观察 Flow（stateIn 将冷流转为热流）
 *      val user: StateFlow<User?> = repo.observeUser(1)
 *          .stateIn(
 *              scope = viewModelScope,
 *              started = SharingStarted.WhileSubscribed(5000),  // 5s 无订阅者则停止
 *              initialValue = null
 *          )
 *  }
 *
 * ── 6.3  View 层收集（生命周期安全）  ★ 必学 ──────────────────────────────────
 *
 *  · repeatOnLifecycle(STARTED)：页面不可见时暂停收集，可见时恢复
 *  · 避免在 onStart/onResume 中直接 launch（会重复订阅）
 *
 *  // Fragment 中（推荐写法）
 *  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *      viewLifecycleOwner.lifecycleScope.launch {
 *          viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
 *              viewModel.uiState.collect { state ->
 *                  when (state) {
 *                      is UiState.Loading  -> showLoading()
 *                      is UiState.Success  -> showData(state.data)
 *                      is UiState.Error    -> showError(state.message)
 *                  }
 *              }
 *          }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · 网络/IO 操作放在 Dispatchers.IO，UI 更新在 Dispatchers.Main
 *  · 用 supervisorScope 让子协程失败不影响兄弟协程
 *  · Flow 的 catch 操作符处理上游异常
 *  · 使用 flowOn 而非 withContext 切换 Flow 的执行线程
 *  · 使用 repeatOnLifecycle(STARTED) 在 View 层安全收集 Flow
 *  · stateIn 将冷流转为热流，避免重复请求
 *  · Repository 只暴露 suspend 函数和 Flow，不自己启动协程
 *
 *  ❌ 不应该做：
 *  · 在协程中使用 Thread.sleep()（用 delay() 替代）
 *  · 在 catch 中吞掉 CancellationException（会破坏结构化并发）
 *  · 在 ViewModel 中直接持有 Activity/Fragment 引用（内存泄漏）
 *  · 在 GlobalScope 中启动协程（无法自动取消，生命周期不受控）
 *  · 在 Repository 中启动协程（调用方无法控制生命周期）
 */

val coroutinesData = NoteData(
    title = "Kotlin 协程与 Flow",
    subtitle = "suspend · Dispatcher · 结构化并发 · Flow · Channel · 实战模式",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "协程基础  ★ 必学"),
        ChapterItem("1.1", "核心概念：suspend / Scope / Job / Dispatcher"),
        ChapterItem("1.2", "启动方式：launch / async / withContext  ★ 必学"),
        ChapterItem("1.3", "CoroutineStart 启动模式"),
        ChapterItem("1.4", "delay vs Thread.sleep  ★ 必学"),
        ChapterItem("2",   "结构化并发  ★ 必学"),
        ChapterItem("2.1", "父子关系与取消传播"),
        ChapterItem("2.2", "supervisorScope：子协程失败互不影响  ★ 常用"),
        ChapterItem("2.3", "协程取消与 CancellationException  ★ 必学"),
        ChapterItem("2.4", "coroutineScope vs supervisorScope 对比  ★ 常用"),
        ChapterItem("3",   "Flow  ★ 必学"),
        ChapterItem("3.1", "冷流 Flow：flow { } / emit / flowOn  ★ 必学"),
        ChapterItem("3.2", "热流 StateFlow / SharedFlow  ★ 必学"),
        ChapterItem("3.3", "Flow 操作符：map/filter/combine/flatMapLatest/debounce  ★ 常用"),
        ChapterItem("3.4", "Flow 收集方式：collect / collectLatest / launchIn  ★ 必学"),
        ChapterItem("4",   "异常处理  ★ 必学"),
        ChapterItem("4.1", "try/catch 与 CoroutineExceptionHandler"),
        ChapterItem("4.2", "Flow 异常处理：catch / onCompletion / retry  ★ 常用"),
        ChapterItem("5",   "Channel  ★ 常用"),
        ChapterItem("5.1", "Channel 基础：send / receive / 缓冲类型"),
        ChapterItem("5.2", "Channel vs Flow 选择"),
        ChapterItem("6",   "Android 实战模式  ★ 必学"),
        ChapterItem("6.1", "Repository 层封装：suspend 函数 + Flow"),
        ChapterItem("6.2", "ViewModel 层调用：stateIn / viewModelScope  ★ 必学"),
        ChapterItem("6.3", "View 层收集：repeatOnLifecycle 生命周期安全  ★ 必学"),
        ChapterItem("7",   "最佳实践  ★ 必学"),
    )
)
