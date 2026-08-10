package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * WorkManager 后台任务调度
 * 官方文档：https://developer.android.com/topic/libraries/architecture/workmanager
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心概念
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Worker 类型 ──────────────────────────────────────────────────────────
 *
 *  · Worker：同步执行，在 WorkManager 管理的线程中运行
 *  · CoroutineWorker（推荐）：支持 suspend 函数，与协程集成
 *
 *  class UploadWorker(ctx: Context, params: WorkerParameters) :
 *      CoroutineWorker(ctx, params) {
 *      override suspend fun doWork(): Result {
 *          return try { uploadData(); Result.success() }
 *          catch (e: Exception) { Result.retry() }
 *      }
 *  }
 *
 * ── 1.2  WorkRequest 类型 ─────────────────────────────────────────────────────
 *
 *  · OneTimeWorkRequest：一次性任务
 *  · PeriodicWorkRequest：定期任务（最小间隔 15 分钟）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  约束条件
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 网络条件：NetworkType.CONNECTED / UNMETERED
 *  · 充电状态：requiresCharging(true)
 *  · 存储空间：requiresStorageNotLow(true)
 *  · 电量：requiresBatteryNotLow(true)
 *
 *  val request = OneTimeWorkRequestBuilder<UploadWorker>()
 *      .setConstraints(
 *          Constraints.Builder()
 *              .setRequiredNetworkType(NetworkType.CONNECTED)
 *              .build()
 *      ).build()
 *
 *  WorkManager.getInstance(context).enqueue(request)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  链式任务与进度
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  链式任务 ─────────────────────────────────────────────────────────────
 *
 *  · then()：串行执行
 *  · WorkContinuation：并行合并
 *
 *  WorkManager.getInstance(context)
 *      .beginWith(downloadWork)
 *      .then(processWork)
 *      .then(uploadWork)
 *      .enqueue()
 *
 * ── 3.2  进度上报 ─────────────────────────────────────────────────────────────
 *
 *  · setProgress()：在 Worker 中上报进度
 *  · WorkInfo：监听任务状态和进度
 *
 *  WorkManager.getInstance(context)
 *      .getWorkInfoByIdLiveData(request.id)
 *      .observe(this) { info ->
 *          val progress = info.progress.getInt("progress", 0)
 *      }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  唯一任务
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · enqueueUniqueWork：防止重复入队
 *  · ExistingWorkPolicy.KEEP：保留已有任务
 *  · ExistingWorkPolicy.REPLACE：替换已有任务
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 后台上传/同步用 WorkManager，前台持续任务用 ForegroundService
 *  · PeriodicWork 最小间隔 15 分钟
 *  · setExpedited() 让任务尽快执行
 */

val workManagerData = NoteData(
    title = "WorkManager",
    subtitle = "进阶开发能力 · 后台任务调度 · 约束条件 · 链式任务",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("1",   "核心概念"),
        ChapterItem("1.1", "Worker 类型"),
        ChapterItem("1.2", "WorkRequest 类型"),
        ChapterItem("2",   "约束条件"),
        ChapterItem("3",   "链式任务与进度"),
        ChapterItem("3.1", "链式任务"),
        ChapterItem("3.2", "进度上报"),
        ChapterItem("4",   "唯一任务"),
        ChapterItem("5",   "最佳实践"),
    )
)
