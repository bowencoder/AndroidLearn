package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * WorkManager 后台任务调度
 * 官方文档：https://developer.android.com/topic/libraries/architecture/workmanager
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  0  为什么用 WorkManager  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · WorkManager 是 Android 官方推荐的后台任务调度库
 *  · 保证任务执行：即使 App 退出、设备重启，任务也会在条件满足时执行
 *  · 适用场景：日志上传、数据同步、图片压缩、定期备份等"延迟可靠"任务
 *
 *  后台任务方案选型：
 *  ┌──────────────────────┬──────────────────────┬──────────────────────┐
 *  │       方案            │       适用场景        │       特点            │
 *  ├──────────────────────┼──────────────────────┼──────────────────────┤
 *  │ WorkManager          │ 延迟、可靠、有约束     │ 保证执行，支持重启恢复  │
 *  │ ForegroundService    │ 持续进行中的任务       │ 需要通知栏，用户可见   │
 *  │ coroutine/thread     │ 即时、短暂的后台操作   │ App 退出即停止         │
 *  │ AlarmManager         │ 精确定时触发           │ 不保证网络/充电条件    │
 *  └──────────────────────┴──────────────────────┴──────────────────────┘
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心概念  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Worker 类型  ★ 必学 ──────────────────────────────────────────────────
 *
 *  · Worker：同步执行，在 WorkManager 管理的后台线程中运行
 *  · CoroutineWorker（推荐）：支持 suspend 函数，与协程无缝集成
 *  · RxWorker：适合 RxJava 项目
 *
 *  // CoroutineWorker（推荐写法）
 *  class UploadWorker(
 *      ctx: Context,
 *      params: WorkerParameters
 *  ) : CoroutineWorker(ctx, params) {
 *
 *      override suspend fun doWork(): Result {
 *          return try {
 *              val data = inputData.getString("file_path") ?: return Result.failure()
 *              uploadFile(data)
 *              Result.success()
 *          } catch (e: Exception) {
 *              if (runAttemptCount < 3) Result.retry()  // 最多重试 3 次
 *              else Result.failure()
 *          }
 *      }
 *  }
 *
 *  · doWork() 返回值：
 *    - Result.success()：任务成功，可携带输出数据
 *    - Result.failure()：任务失败，不再重试
 *    - Result.retry()：任务失败，按退避策略重试
 *
 * ── 1.2  WorkRequest 类型  ★ 必学 ────────────────────────────────────────────
 *
 *  · OneTimeWorkRequest：一次性任务
 *  · PeriodicWorkRequest：定期任务（最小间隔 15 分钟，系统限制）
 *
 *  // 一次性任务（携带输入数据）
 *  val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
 *      .setInputData(workDataOf("file_path" to "/sdcard/photo.jpg"))
 *      .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
 *      .addTag("upload")
 *      .build()
 *
 *  // 定期任务（每天同步一次）
 *  val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.DAYS)
 *      .setConstraints(
 *          Constraints.Builder()
 *              .setRequiredNetworkType(NetworkType.CONNECTED)
 *              .build()
 *      ).build()
 *
 *  WorkManager.getInstance(context).enqueue(uploadRequest)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  约束条件（Constraints）  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 只有满足约束条件时，任务才会执行
 *  · 条件不满足时任务等待，满足后自动触发
 *
 *  val constraints = Constraints.Builder()
 *      .setRequiredNetworkType(NetworkType.CONNECTED)   // 需要网络
 *      .setRequiresCharging(true)                       // 需要充电
 *      .setRequiresBatteryNotLow(true)                  // 电量不低
 *      .setRequiresStorageNotLow(true)                  // 存储空间不低
 *      .setRequiresDeviceIdle(true)                     // 设备空闲（API 23+）
 *      .build()
 *
 *  · NetworkType 枚举：
 *    - NOT_REQUIRED：无网络要求
 *    - CONNECTED：任意网络（WiFi 或移动数据）
 *    - UNMETERED：仅 WiFi（不计费网络）
 *    - NOT_ROAMING：非漫游网络
 *    - METERED：计费网络（移动数据）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  链式任务  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  串行链式任务 ─────────────────────────────────────────────────────────
 *
 *  · then()：前一个任务成功后才执行下一个
 *  · 前一个任务的输出数据自动作为下一个任务的输入
 *
 *  WorkManager.getInstance(context)
 *      .beginWith(downloadWork)    // 下载
 *      .then(compressWork)         // 压缩
 *      .then(uploadWork)           // 上传
 *      .enqueue()
 *
 * ── 3.2  并行合并任务  ★ 常用 ─────────────────────────────────────────────────
 *
 *  · WorkContinuation.combine()：多个并行任务全部完成后，执行合并任务
 *
 *  val filter1 = OneTimeWorkRequestBuilder<FilterWorker>().build()
 *  val filter2 = OneTimeWorkRequestBuilder<FilterWorker>().build()
 *  val compress = OneTimeWorkRequestBuilder<CompressWorker>().build()
 *
 *  WorkContinuation.combine(
 *      listOf(
 *          WorkManager.getInstance(context).beginWith(filter1),
 *          WorkManager.getInstance(context).beginWith(filter2)
 *      )
 *  ).then(compress).enqueue()  // filter1 和 filter2 并行，都完成后执行 compress
 *
 * ── 3.3  任务间数据传递 ───────────────────────────────────────────────────────
 *
 *  // 上游 Worker 输出数据
 *  class DownloadWorker(...) : CoroutineWorker(...) {
 *      override suspend fun doWork(): Result {
 *          val filePath = download()
 *          return Result.success(workDataOf("file_path" to filePath))
 *      }
 *  }
 *
 *  // 下游 Worker 读取输入数据
 *  class CompressWorker(...) : CoroutineWorker(...) {
 *      override suspend fun doWork(): Result {
 *          val filePath = inputData.getString("file_path") ?: return Result.failure()
 *          compress(filePath)
 *          return Result.success()
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  进度上报  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · setProgress()：在 Worker 中上报进度（挂起函数）
 *  · getWorkInfoByIdLiveData / getWorkInfoByIdFlow：观察任务状态和进度
 *
 *  // Worker 中上报进度
 *  class UploadWorker(...) : CoroutineWorker(...) {
 *      override suspend fun doWork(): Result {
 *          for (i in 0..100 step 10) {
 *              setProgress(workDataOf("progress" to i))
 *              delay(500)
 *          }
 *          return Result.success()
 *      }
 *  }
 *
 *  // ViewModel 中观察（Flow 方式，推荐）
 *  WorkManager.getInstance(context)
 *      .getWorkInfoByIdFlow(request.id)
 *      .collect { info ->
 *          when (info?.state) {
 *              WorkInfo.State.RUNNING   -> {
 *                  val progress = info.progress.getInt("progress", 0)
 *                  updateProgressBar(progress)
 *              }
 *              WorkInfo.State.SUCCEEDED -> showSuccess()
 *              WorkInfo.State.FAILED    -> showError()
 *              else -> {}
 *          }
 *      }
 *
 *  · WorkInfo.State 枚举：
 *    ENQUEUED → RUNNING → SUCCEEDED / FAILED / CANCELLED
 *    BLOCKED（等待前置任务完成）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  唯一任务  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · enqueueUniqueWork：防止同名任务重复入队（一次性任务）
 *  · enqueueUniquePeriodicWork：防止同名定期任务重复（定期任务）
 *
 *  · ExistingWorkPolicy（一次性任务）：
 *    - KEEP：保留已有任务，忽略新任务
 *    - REPLACE：取消已有任务，用新任务替换
 *    - APPEND：追加到已有任务链末尾
 *    - APPEND_OR_REPLACE：追加，若已有任务失败则替换
 *
 *  · ExistingPeriodicWorkPolicy（定期任务）：
 *    - KEEP：保留已有定期任务
 *    - REPLACE：替换已有定期任务（重置计时）
 *    - UPDATE：更新参数但保留计时（API 31+）
 *
 *  // 确保同一时间只有一个上传任务
 *  WorkManager.getInstance(context).enqueueUniqueWork(
 *      "unique_upload",
 *      ExistingWorkPolicy.KEEP,
 *      uploadRequest
 *  )
 *
 *  // 确保只有一个定期同步任务
 *  WorkManager.getInstance(context).enqueueUniquePeriodicWork(
 *      "daily_sync",
 *      ExistingPeriodicWorkPolicy.KEEP,
 *      syncRequest
 *  )
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  加急任务（Expedited Work）  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · setExpedited()：让任务尽快执行，绕过部分系统限制（API 31+）
 *  · 需要在 Worker 中实现 getForegroundInfo() 提供通知（低版本兼容）
 *
 *  val expeditedRequest = OneTimeWorkRequestBuilder<UploadWorker>()
 *      .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
 *      .build()
 *
 *  // Worker 中实现（API < 31 的兼容）
 *  override suspend fun getForegroundInfo(): ForegroundInfo {
 *      return ForegroundInfo(
 *          NOTIFICATION_ID,
 *          createNotification()  // 创建前台通知
 *      )
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  取消与查询任务  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  val wm = WorkManager.getInstance(context)
 *
 *  // 按 ID 取消
 *  wm.cancelWorkById(request.id)
 *
 *  // 按 Tag 取消（批量）
 *  wm.cancelAllWorkByTag("upload")
 *
 *  // 取消唯一任务
 *  wm.cancelUniqueWork("unique_upload")
 *
 *  // 取消所有任务（慎用）
 *  wm.cancelAllWork()
 *
 *  // 按 Tag 查询状态
 *  wm.getWorkInfosByTagFlow("upload").collect { infos -> /* ... */ }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  8  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · 优先使用 CoroutineWorker，与协程生态无缝集成
 *  · 用 enqueueUniqueWork 防止任务重复入队
 *  · 网络相关任务加 NetworkType.CONNECTED 约束
 *  · 用 Tag 管理同类任务，方便批量取消/查询
 *  · 用 getWorkInfoByIdFlow 观察任务状态（替代 LiveData）
 *  · 重试逻辑用 Result.retry() + runAttemptCount 控制最大次数
 *
 *  ❌ 不应该做：
 *  · 不要用 WorkManager 执行需要精确定时的任务（用 AlarmManager）
 *  · 不要用 WorkManager 执行需要持续运行的任务（用 ForegroundService）
 *  · PeriodicWork 最小间隔 15 分钟，不要期望更短的间隔
 *  · 不要在 doWork() 中做 UI 操作（在 Main 线程更新 UI）
 */

val workManagerData = NoteData(
    title = "WorkManager",
    subtitle = "后台任务调度 · CoroutineWorker · 约束 · 链式 · 唯一任务 · 进度上报",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("0",   "为什么用 WorkManager：方案选型对比  ★ 必学"),
        ChapterItem("1",   "核心概念  ★ 必学"),
        ChapterItem("1.1", "Worker 类型：CoroutineWorker / Result  ★ 必学"),
        ChapterItem("1.2", "WorkRequest 类型：OneTime / Periodic  ★ 必学"),
        ChapterItem("2",   "约束条件（Constraints）：网络/充电/电量  ★ 常用"),
        ChapterItem("3",   "链式任务  ★ 常用"),
        ChapterItem("3.1", "串行链式：then()"),
        ChapterItem("3.2", "并行合并：WorkContinuation.combine()  ★ 常用"),
        ChapterItem("3.3", "任务间数据传递：inputData / outputData"),
        ChapterItem("4",   "进度上报：setProgress / getWorkInfoByIdFlow  ★ 常用"),
        ChapterItem("5",   "唯一任务：enqueueUniqueWork / ExistingWorkPolicy  ★ 常用"),
        ChapterItem("6",   "加急任务：setExpedited / getForegroundInfo  ★ 常用"),
        ChapterItem("7",   "取消与查询：cancelWorkById / cancelAllWorkByTag  ★ 常用"),
        ChapterItem("8",   "最佳实践  ★ 必学"),
    )
)
