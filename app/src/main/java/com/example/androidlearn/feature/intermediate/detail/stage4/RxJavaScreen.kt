package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * RxJava 响应式编程
 * 官方文档：https://github.com/ReactiveX/RxJava
 * RxAndroid：https://github.com/ReactiveX/RxAndroid
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心概念
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Observable / Observer 模型 ──────────────────────────────────────
 *
 *  · Observable（被观察者）：数据源，负责发射数据
 *  · Observer（观察者）：订阅 Observable，接收数据
 *  · Subscription / Disposable：订阅关系，可用于取消订阅防止内存泄漏
 *
 *  · 三种通知：
 *    - onNext(T)：发射一个数据项
 *    - onError(Throwable)：发生错误，序列终止
 *    - onComplete()：序列正常结束
 *
 *  // 基本用法
 *  Observable.just("Hello", "RxJava")
 *      .subscribe(
 *          { item -> Log.d(TAG, item) },    // onNext
 *          { e -> Log.e(TAG, e.message) },  // onError
 *          { Log.d(TAG, "完成") }            // onComplete
 *      )
 *
 *
 * ── 1.2  Observable 类型 ──────────────────────────────────────────────────
 *
 *  · Observable<T>：0~N 个数据，支持背压（RxJava 2+ 用 Flowable 处理背压）
 *  · Single<T>：只发射 1 个数据或错误（onSuccess / onError）
 *  · Maybe<T>：0 或 1 个数据（onSuccess / onComplete / onError）
 *  · Completable：只关心完成或错误，不发射数据（onComplete / onError）
 *  · Flowable<T>：支持背压（Backpressure）的 Observable，适合大量数据流
 *
 *  // Single：网络请求典型场景
 *  Single.fromCallable { api.getUser(userId) }
 *      .subscribeOn(Schedulers.io())
 *      .observeOn(AndroidSchedulers.mainThread())
 *      .subscribe({ user -> showUser(user) }, { e -> showError(e) })
 *
 *  // Completable：只关心成功/失败
 *  Completable.fromAction { db.deleteAll() }
 *      .subscribeOn(Schedulers.io())
 *      .observeOn(AndroidSchedulers.mainThread())
 *      .subscribe({ showSuccess() }, { e -> showError(e) })
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  调度器（Schedulers）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · subscribeOn()：指定 Observable 在哪个线程执行（只有第一次调用生效）
 *  · observeOn()：指定下游操作符和 Observer 在哪个线程执行（可多次切换）
 *
 *  常用调度器：
 *  · Schedulers.io()：IO 密集型（网络、文件），线程池可动态扩展
 *  · Schedulers.computation()：CPU 密集型（计算），线程数 = CPU 核心数
 *  · Schedulers.newThread()：每次创建新线程（不推荐，开销大）
 *  · Schedulers.single()：单线程顺序执行
 *  · AndroidSchedulers.mainThread()：Android 主线程（RxAndroid 提供）
 *
 *  // 典型：IO 线程请求，主线程更新 UI
 *  Observable.fromCallable { fetchData() }
 *      .subscribeOn(Schedulers.io())          // 在 IO 线程执行 fetchData
 *      .observeOn(AndroidSchedulers.mainThread())  // 切回主线程
 *      .subscribe { data -> updateUi(data) }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  常用操作符
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  变换操作符 ───────────────────────────────────────────────────────
 *
 *  · map：一对一转换数据类型
 *    Observable.just(1, 2, 3).map { it * 2 }  // 2, 4, 6
 *
 *  · flatMap：一对多，将每个数据转换为 Observable 并合并（无序）
 *    Observable.just(1, 2, 3)
 *        .flatMap { id -> api.getUser(id).toObservable() }
 *
 *  · concatMap：与 flatMap 类似，但保证顺序（串行）
 *
 *  · switchMap：只保留最新的 Observable，旧的自动取消（搜索防抖常用）
 *
 *
 * ── 3.2  过滤操作符 ───────────────────────────────────────────────────────
 *
 *  · filter：过滤不满足条件的数据
 *    Observable.range(1, 10).filter { it % 2 == 0 }  // 2,4,6,8,10
 *
 *  · take(n)：只取前 n 个数据
 *  · skip(n)：跳过前 n 个数据
 *  · distinct()：去重
 *  · debounce(timeout)：防抖，只发射最后一个（搜索框输入常用）
 *  · throttleFirst(timeout)：节流，只发射第一个（防重复点击常用）
 *
 *  // 搜索防抖（300ms 内只触发最后一次）
 *  RxTextView.textChanges(searchView)
 *      .debounce(300, TimeUnit.MILLISECONDS)
 *      .switchMap { query -> api.search(query).toObservable() }
 *      .observeOn(AndroidSchedulers.mainThread())
 *      .subscribe { results -> showResults(results) }
 *
 *
 * ── 3.3  组合操作符 ───────────────────────────────────────────────────────
 *
 *  · zip：将多个 Observable 的数据按顺序配对合并
 *    Observable.zip(obs1, obs2) { a, b -> Pair(a, b) }
 *
 *  · merge：合并多个 Observable，数据交错发射（无序）
 *  · concat：串联多个 Observable，前一个完成后再订阅下一个（有序）
 *  · combineLatest：任意一个 Observable 发射数据时，取所有最新值合并
 *
 *  // 并行请求两个接口，等两个都完成后合并结果
 *  Observable.zip(
 *      api.getUserInfo(id).subscribeOn(Schedulers.io()),
 *      api.getUserOrders(id).subscribeOn(Schedulers.io()),
 *      { user, orders -> UserDetail(user, orders) }
 *  ).observeOn(AndroidSchedulers.mainThread())
 *   .subscribe { detail -> showDetail(detail) }
 *
 *
 * ── 3.4  错误处理操作符 ───────────────────────────────────────────────────
 *
 *  · onErrorReturn：发生错误时返回默认值，序列正常结束
 *    .onErrorReturn { e -> emptyList() }
 *
 *  · onErrorResumeNext：发生错误时切换到另一个 Observable
 *    .onErrorResumeNext { e -> Observable.just(cachedData) }
 *
 *  · retry(n)：发生错误时重试 n 次
 *    .retry(3)
 *
 *  · retryWhen：自定义重试逻辑（如指数退避）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  生命周期管理（防内存泄漏）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 订阅返回 Disposable，Activity/Fragment 销毁时必须 dispose()
 *  · CompositeDisposable：统一管理多个 Disposable
 *
 *  // 推荐写法
 *  private val disposables = CompositeDisposable()
 *
 *  fun loadData() {
 *      val d = api.getData()
 *          .subscribeOn(Schedulers.io())
 *          .observeOn(AndroidSchedulers.mainThread())
 *          .subscribe({ showData(it) }, { showError(it) })
 *      disposables.add(d)
 *  }
 *
 *  override fun onDestroy() {
 *      super.onDestroy()
 *      disposables.clear()   // 取消所有订阅，防止内存泄漏
 *  }
 *
 *  // ViewModel 中（配合 RxLifecycle 或手动管理）
 *  override fun onCleared() {
 *      super.onCleared()
 *      disposables.clear()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Subject（既是 Observable 又是 Observer）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · PublishSubject：只发射订阅后的数据（不重放历史）
 *  · BehaviorSubject：发射最近一个数据 + 订阅后的数据（有初始值）
 *  · ReplaySubject：重放所有历史数据给新订阅者
 *  · AsyncSubject：只发射最后一个数据（onComplete 后）
 *
 *  // PublishSubject 作为事件总线（简单场景）
 *  val subject = PublishSubject.create<String>()
 *  subject.subscribe { event -> handleEvent(event) }
 *  subject.onNext("click")   // 发射事件
 *
 *  // BehaviorSubject 保存最新状态（类似 StateFlow）
 *  val state = BehaviorSubject.createDefault("loading")
 *  state.onNext("success")
 *  state.value   // 获取当前值
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  RxJava vs Kotlin 协程
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ┌──────────────────┬──────────────────────────┬──────────────────────────┐
 *  │                  │        RxJava             │      Kotlin 协程          │
 *  ├──────────────────┼──────────────────────────┼──────────────────────────┤
 *  │ 语言             │ Java/Kotlin 均可           │ Kotlin 专属               │
 *  │ 学习曲线         │ 操作符多，曲线陡           │ 相对平缓                  │
 *  │ 背压支持         │ Flowable 原生支持          │ Flow 支持                 │
 *  │ 操作符丰富度     │ 极丰富（100+）             │ 较少但够用                │
 *  │ 线程切换         │ subscribeOn/observeOn     │ withContext/Dispatcher    │
 *  │ 错误处理         │ onErrorReturn/retry       │ try-catch / catch{}       │
 *  │ Android 集成     │ RxAndroid 扩展             │ viewModelScope 原生支持   │
 *  │ 新项目推荐       │ 旧项目维护                 │ 新项目首选                │
 *  └──────────────────┴──────────────────────────┴──────────────────────────┘
 *
 *  · 新项目推荐 Kotlin 协程 + Flow，RxJava 在旧项目中仍大量使用
 *  · 两者可共存：rxjava3-coroutines-interop 库提供互转扩展
 */

val rxJavaData = NoteData(
    title = "RxJava 响应式编程",
    subtitle = "Observable · 调度器 · 操作符 · 生命周期管理 · Subject",
    color = Color.parseColor("#B71C1C"),
    chapters = listOf(
        ChapterItem("1",   "核心概念"),
        ChapterItem("1.1", "Observable / Observer 模型：onNext / onError / onComplete"),
        ChapterItem("1.2", "Observable 类型：Single / Maybe / Completable / Flowable"),
        ChapterItem("2",   "调度器（Schedulers）"),
        ChapterItem("2.1", "subscribeOn / observeOn：io / computation / mainThread"),
        ChapterItem("3",   "常用操作符"),
        ChapterItem("3.1", "变换：map / flatMap / concatMap / switchMap"),
        ChapterItem("3.2", "过滤：filter / debounce / throttleFirst / distinct"),
        ChapterItem("3.3", "组合：zip / merge / concat / combineLatest"),
        ChapterItem("3.4", "错误处理：onErrorReturn / retry / retryWhen"),
        ChapterItem("4",   "生命周期管理（防内存泄漏）"),
        ChapterItem("4.1", "Disposable / CompositeDisposable / onDestroy 清理"),
        ChapterItem("5",   "Subject"),
        ChapterItem("5.1", "PublishSubject / BehaviorSubject / ReplaySubject / AsyncSubject"),
        ChapterItem("6",   "RxJava vs Kotlin 协程：新旧项目选型对比"),
    )
)
