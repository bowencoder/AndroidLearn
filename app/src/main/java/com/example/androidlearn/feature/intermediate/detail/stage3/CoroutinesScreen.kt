package com.example.androidlearn.feature.intermediate.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Kotlin Coroutines",
    description = "suspend、async/await、Flow、Channel",
    overview = "协程是 Kotlin 的并发解决方案，用顺序代码风格编写异步逻辑，比线程更轻量，与 Android 完美集成。",
    keyPoints = listOf(
        "suspend 函数：可暂停的函数，不阻塞线程",
        "CoroutineScope：协程的生命周期容器",
        "Dispatcher：IO（网络/磁盘）、Main（UI）、Default（CPU密集）",
        "async / await：并行执行多个任务",
        "Flow：冷流，顺序发射数据，替代 RxJava",
        "StateFlow / SharedFlow：热流，广播状态或事件"
    ),
    codeSnippet = """
// 顺序执行
viewModelScope.launch {
    val user = withContext(Dispatchers.IO) { api.getUser(1) }
    val orders = withContext(Dispatchers.IO) { api.getOrders(user.id) }
}

// 并行执行
viewModelScope.launch {
    val userDeferred = async(Dispatchers.IO) { api.getUser(1) }
    val configDeferred = async(Dispatchers.IO) { api.getConfig() }
    val user = userDeferred.await()
    val config = configDeferred.await()
}
    """.trimIndent(),
    tips = listOf(
        "网络/IO 操作放在 Dispatchers.IO，UI 更新在 Dispatchers.Main",
        "用 supervisorScope 让子协程失败不影响兄弟协程",
        "Flow 的 catch 操作符处理上游异常"
    )
)

@Composable
fun CoroutinesScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF9C27B0),
        stageTitle = "现代架构体系",
        onBack = onBack
    )
}
