package com.example.androidlearn.feature.intermediate.detail.stage4

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "WorkManager",
    description = "后台任务调度，约束条件，链式任务",
    overview = "WorkManager 保证任务执行（即使应用退出或设备重启），支持约束和链式调度，是后台任务的推荐方案。",
    keyPoints = listOf(
        "Worker / CoroutineWorker：定义后台任务",
        "WorkRequest：OneTimeWork（一次性）/ PeriodicWork（定期）",
        "Constraints：网络条件、充电状态、存储空间等约束",
        "链式任务：then() 串行，WorkContinuation 并行合并",
        "观察进度：setProgress() 上报，WorkInfo 监听状态",
        "唯一任务：enqueueUniqueWork 防止重复入队"
    ),
    codeSnippet = """
class UploadWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try { uploadData(); Result.success() }
        catch (e: Exception) { Result.retry() }
    }
}

val request = OneTimeWorkRequestBuilder<UploadWorker>()
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    ).build()

WorkManager.getInstance(context).enqueue(request)
    """.trimIndent(),
    tips = listOf(
        "后台上传/同步用 WorkManager，前台持续任务用 ForegroundService",
        "PeriodicWork 最小间隔 15 分钟",
        "setExpedited() 让任务尽快执行"
    )
)

@Composable
fun WorkManagerScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF9800),
        stageTitle = "进阶开发能力",
        onBack = onBack
    )
}
