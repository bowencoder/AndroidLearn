package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 多线程与并发
 * 官方文档：https://developer.android.com/guide/background/threading
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  线程基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Thread 与 Runnable ───────────────────────────────────────────────
 *
 *  · Thread：Java 线程的直接封装，继承 Thread 或传入 Runnable
 *  · Android 主线程（UI 线程）：负责 UI 渲染和事件分发，不能执行耗时操作
 *  · 子线程：执行耗时任务（网络、IO、计算），不能直接更新 UI
 *
 *  // 方式一：继承 Thread
 *  class MyThread : Thread() {
 *      override fun run() { /* 耗时操作 */ }
 *  }
 *  MyThread().start()
 *
 *  // 方式二：Runnable（推荐，解耦线程与任务）
 *  Thread {
 *      val result = fetchData()
 *      runOnUiThread { textView.text = result }   // 切回主线程更新 UI
 *  }.start()
 *
 *  // 方式三：Kotlin 协程（现代推荐，见协程章节）
 *
 *
 * ── 1.2  线程状态 ─────────────────────────────────────────────────────────
 *
 *  NEW → RUNNABLE → BLOCKED / WAITING / TIMED_WAITING → TERMINATED
 *
 *  · NEW：创建但未 start()
 *  · RUNNABLE：就绪或正在运行（JVM 层面合并）
 *  · BLOCKED：等待 synchronized 锁
 *  · WAITING：调用 wait() / join() / LockSupport.park()，无限等待
 *  · TIMED_WAITING：sleep(n) / wait(n) / join(n)，有超时等待
 *  · TERMINATED：run() 执行完毕或抛出异常
 *
 *
 * ── 1.3  线程优先级与守护线程 ─────────────────────────────────────────────
 *
 *  · 优先级：1（MIN）~ 10（MAX），默认 5（NORM）
 *    thread.priority = Thread.MAX_PRIORITY   // 仅建议，不保证
 *  · 守护线程（Daemon）：JVM 中所有非守护线程结束后自动退出
 *    thread.isDaemon = true   // 必须在 start() 前设置
 *    GC 线程是典型的守护线程
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  线程同步
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  synchronized 关键字 ──────────────────────────────────────────────
 *
 *  · 对象锁：锁住 this 或指定对象
 *  · 类锁：锁住 Class 对象（静态方法 / synchronized(Xxx::class.java)）
 *  · 可重入：同一线程可多次获取同一把锁，不会死锁
 *
 *  // 同步方法
 *  @Synchronized
 *  fun increment() { count++ }
 *
 *  // 同步代码块（锁粒度更细，性能更好）
 *  fun increment() {
 *      synchronized(lock) { count++ }
 *  }
 *
 *  // 静态同步（类锁）
 *  companion object {
 *      @Synchronized
 *      fun getInstance(): Singleton { ... }
 *  }
 *
 *
 * ── 2.2  volatile 关键字 ──────────────────────────────────────────────────
 *
 *  · 保证可见性：写操作立即刷新到主内存，读操作从主内存读取
 *  · 禁止指令重排序（happens-before 语义）
 *  · 不保证原子性：count++ 仍然线程不安全（需用 AtomicInteger）
 *
 *  @Volatile
 *  private var running = true
 *
 *  // 典型用法：双重检查锁（DCL）单例
 *  class Singleton private constructor() {
 *      companion object {
 *          @Volatile
 *          private var instance: Singleton? = null
 *
 *          fun getInstance() = instance ?: synchronized(this) {
 *              instance ?: Singleton().also { instance = it }
 *          }
 *      }
 *  }
 *
 *
 * ── 2.3  ReentrantLock ────────────────────────────────────────────────────
 *
 *  · 比 synchronized 更灵活：可中断、可超时、可公平锁
 *  · 必须手动 unlock()，推荐 try-finally 保证释放
 *
 *  private val lock = ReentrantLock()
 *
 *  fun increment() {
 *      lock.lock()
 *      try {
 *          count++
 *      } finally {
 *          lock.unlock()   // 必须在 finally 中释放
 *      }
 *  }
 *
 *  // 可中断锁（等待时可被 interrupt()）
 *  lock.lockInterruptibly()
 *
 *  // 超时锁（等待最多 500ms）
 *  if (lock.tryLock(500, TimeUnit.MILLISECONDS)) { ... }
 *
 *  // 公平锁（按等待顺序获取，默认非公平）
 *  val fairLock = ReentrantLock(true)
 *
 *
 * ── 2.4  wait / notify / notifyAll ───────────────────────────────────────
 *
 *  · 必须在 synchronized 块内调用，否则抛 IllegalMonitorStateException
 *  · wait()：释放锁并进入 WAITING，等待 notify() 唤醒
 *  · notify()：随机唤醒一个等待线程
 *  · notifyAll()：唤醒所有等待线程（推荐，避免信号丢失）
 *
 *  // 生产者-消费者模型
 *  synchronized(queue) {
 *      while (queue.isEmpty()) queue.wait()   // 用 while 防止虚假唤醒
 *      val item = queue.poll()
 *      queue.notifyAll()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  原子类与并发工具
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  原子类（java.util.concurrent.atomic）────────────────────────────
 *
 *  · 基于 CAS（Compare-And-Swap）实现无锁线程安全操作
 *  · AtomicInteger / AtomicLong / AtomicBoolean / AtomicReference
 *
 *  val counter = AtomicInteger(0)
 *  counter.incrementAndGet()          // 原子自增，返回新值
 *  counter.getAndAdd(5)               // 原子加 5，返回旧值
 *  counter.compareAndSet(5, 10)       // CAS：期望值 5，更新为 10
 *
 *  // AtomicReference 保护对象引用
 *  val ref = AtomicReference<String>("old")
 *  ref.compareAndSet("old", "new")
 *
 *
 * ── 3.2  CountDownLatch ───────────────────────────────────────────────────
 *
 *  · 一次性倒计时门闩：等待 N 个任务全部完成后继续
 *  · countDown()：计数 -1；await()：阻塞直到计数为 0
 *
 *  val latch = CountDownLatch(3)
 *
 *  repeat(3) { i ->
 *      Thread {
 *          doWork(i)
 *          latch.countDown()   // 每个任务完成后 -1
 *      }.start()
 *  }
 *
 *  latch.await()   // 主线程等待 3 个任务全部完成
 *  println("所有任务完成")
 *
 *
 * ── 3.3  CyclicBarrier ────────────────────────────────────────────────────
 *
 *  · 循环屏障：N 个线程互相等待，全部到达后同时继续（可重用）
 *  · 与 CountDownLatch 区别：可重置复用，线程互相等待而非等待外部
 *
 *  val barrier = CyclicBarrier(3) { println("所有线程到达屏障，继续执行") }
 *
 *  repeat(3) {
 *      Thread {
 *          prepare()
 *          barrier.await()   // 等待其他线程到达
 *          execute()
 *      }.start()
 *  }
 *
 *
 * ── 3.4  Semaphore（信号量）──────────────────────────────────────────────
 *
 *  · 控制同时访问某资源的线程数量（限流）
 *  · acquire()：获取许可（无许可时阻塞）；release()：释放许可
 *
 *  val semaphore = Semaphore(3)   // 最多 3 个线程同时访问
 *
 *  fun accessResource() {
 *      semaphore.acquire()
 *      try {
 *          useResource()
 *      } finally {
 *          semaphore.release()
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  线程池（ThreadPoolExecutor）
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 4.1  核心参数 ─────────────────────────────────────────────────────────
 *
 *  ThreadPoolExecutor(
 *      corePoolSize,      // 核心线程数（常驻，不会被回收）
 *      maximumPoolSize,   // 最大线程数
 *      keepAliveTime,     // 非核心线程空闲存活时间
 *      unit,              // 时间单位
 *      workQueue,         // 任务队列
 *      threadFactory,     // 线程工厂（可自定义线程名）
 *      handler            // 拒绝策略
 *  )
 *
 *  · 任务提交流程：
 *    ① 核心线程未满 → 创建核心线程执行
 *    ② 核心线程已满 → 放入任务队列
 *    ③ 队列已满 → 创建非核心线程（不超过 maximumPoolSize）
 *    ④ 线程数达上限且队列满 → 执行拒绝策略
 *
 *
 * ── 4.2  常用线程池（Executors 工厂）─────────────────────────────────────
 *
 *  · newFixedThreadPool(n)：固定 n 个核心线程，队列无界（OOM 风险）
 *  · newCachedThreadPool()：核心线程 0，最大 Integer.MAX_VALUE，60s 回收（OOM 风险）
 *  · newSingleThreadExecutor()：单线程，保证任务顺序执行
 *  · newScheduledThreadPool(n)：支持定时 / 周期任务
 *
 *  ⚠️ 生产环境推荐手动创建 ThreadPoolExecutor，明确各参数，避免 OOM
 *
 *  // 推荐写法
 *  val executor = ThreadPoolExecutor(
 *      4, 8, 60L, TimeUnit.SECONDS,
 *      LinkedBlockingQueue(128),
 *      Executors.defaultThreadFactory(),
 *      ThreadPoolExecutor.CallerRunsPolicy()   // 拒绝时由调用线程执行
 *  )
 *
 *
 * ── 4.3  拒绝策略 ─────────────────────────────────────────────────────────
 *
 *  · AbortPolicy（默认）：抛出 RejectedExecutionException
 *  · CallerRunsPolicy：由提交任务的线程直接执行（降速，不丢任务）
 *  · DiscardPolicy：静默丢弃新任务
 *  · DiscardOldestPolicy：丢弃队列头部最旧的任务，重新提交新任务
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  并发集合
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ConcurrentHashMap：分段锁（JDK 8+ 用 CAS + synchronized），高并发 Map
 *    - 不允许 null key/value（区别于 HashMap）
 *    - putIfAbsent / computeIfAbsent 原子操作
 *
 *  · CopyOnWriteArrayList：写时复制，读无锁，适合读多写少场景
 *    - 写操作复制整个数组，开销大，不适合频繁写
 *
 *  · LinkedBlockingQueue / ArrayBlockingQueue：阻塞队列，线程池任务队列
 *    - put()：队列满时阻塞；offer()：队列满时返回 false
 *    - take()：队列空时阻塞；poll()：队列空时返回 null
 *
 *  · Collections.synchronizedList()：包装普通 List，粗粒度锁，性能差
 *    - 迭代时仍需手动 synchronized，推荐用 CopyOnWriteArrayList 替代
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  Android 线程切换
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Handler + Looper：Android 消息机制，子线程切回主线程的底层实现
 *  · runOnUiThread { }：Activity 内快捷切回主线程
 *  · View.post { }：View 关联的主线程 Handler 发送消息
 *  · Kotlin 协程（推荐）：withContext(Dispatchers.Main) 切换调度器
 *
 *  // Handler 切回主线程（传统方式）
 *  private val mainHandler = Handler(Looper.getMainLooper())
 *
 *  Thread {
 *      val data = fetchFromNetwork()
 *      mainHandler.post { textView.text = data }
 *  }.start()
 *
 *  // 协程方式（现代推荐）
 *  viewModelScope.launch {
 *      val data = withContext(Dispatchers.IO) { fetchFromNetwork() }
 *      // 自动切回主线程
 *      _uiState.value = data
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  常见并发问题
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 竞态条件（Race Condition）：多线程同时读写共享变量，结果不确定
 *    → 解决：synchronized / ReentrantLock / 原子类
 *
 *  · 死锁（Deadlock）：两个线程互相持有对方需要的锁，永久阻塞
 *    → 预防：固定加锁顺序、使用 tryLock 超时、减少锁粒度
 *
 *  · 内存泄漏：线程持有 Activity/Context 引用，Activity 销毁后线程仍在运行
 *    → 解决：使用弱引用、在 onDestroy 中中断线程、改用协程 + viewModelScope
 *
 *  · 主线程 ANR：在主线程执行耗时操作（网络、IO、大量计算）
 *    → 解决：所有耗时操作移到子线程，通过 Handler / 协程回调主线程
 */

val threadConcurrencyData = NoteData(
    title = "多线程与并发",
    subtitle = "Thread · 同步 · 原子类 · 线程池 · 并发集合 · 线程切换",
    color = Color.parseColor("#F44336"),
    chapters = listOf(
        ChapterItem("1",   "线程基础"),
        ChapterItem("1.1", "Thread 与 Runnable：继承 / Runnable / 协程三种方式"),
        ChapterItem("1.2", "线程状态：NEW / RUNNABLE / BLOCKED / WAITING / TERMINATED"),
        ChapterItem("1.3", "线程优先级与守护线程（Daemon）"),
        ChapterItem("2",   "线程同步"),
        ChapterItem("2.1", "synchronized：对象锁 / 类锁 / 可重入 / 同步代码块"),
        ChapterItem("2.2", "volatile：可见性 / 禁止重排序 / DCL 单例"),
        ChapterItem("2.3", "ReentrantLock：可中断 / 超时 / 公平锁 / try-finally"),
        ChapterItem("2.4", "wait / notify / notifyAll：生产者-消费者模型"),
        ChapterItem("3",   "原子类与并发工具"),
        ChapterItem("3.1", "原子类：AtomicInteger / CAS / compareAndSet"),
        ChapterItem("3.2", "CountDownLatch：等待 N 个任务全部完成"),
        ChapterItem("3.3", "CyclicBarrier：N 个线程互相等待后同时继续"),
        ChapterItem("3.4", "Semaphore：限制同时访问资源的线程数"),
        ChapterItem("4",   "线程池（ThreadPoolExecutor）"),
        ChapterItem("4.1", "核心参数：corePoolSize / 任务提交流程"),
        ChapterItem("4.2", "常用线程池：Fixed / Cached / Single / Scheduled"),
        ChapterItem("4.3", "拒绝策略：Abort / CallerRuns / Discard / DiscardOldest"),
        ChapterItem("5",   "并发集合"),
        ChapterItem("5.1", "ConcurrentHashMap / CopyOnWriteArrayList / BlockingQueue"),
        ChapterItem("6",   "Android 线程切换"),
        ChapterItem("6.1", "Handler / runOnUiThread / View.post / 协程 withContext"),
        ChapterItem("7",   "常见并发问题"),
        ChapterItem("7.1", "竞态条件 / 死锁 / 内存泄漏 / ANR 预防"),
    )
)
