package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "DataStore 数据持久化",
    description = "Preferences DataStore · Proto DataStore · 替代 SharedPreferences",
    overview = "Jetpack DataStore 是官方推荐的键值对持久化方案，基于 Kotlin 协程和 Flow，解决了 SharedPreferences 的线程安全问题和同步 I/O 问题。",
    keyPoints = listOf(
        "Preferences DataStore：无模板类型安全的键值存储，类似 SharedPreferences",
        "Proto DataStore：基于 Protocol Buffers，强类型，支持复杂数据结构",
        "异步 API：基于 Flow，数据变化自动通知，无需手动监听",
        "线程安全：所有 I/O 操作在 Dispatchers.IO 执行，不阻塞主线程",
        "事务性更新：通过 edit {} 块保证原子性写入",
        "迁移：提供 SharedPreferencesMigration 从旧数据迁移"
    ),
    codeSnippet = """
// 定义 key
val COUNTER_KEY = intPreferencesKey("counter")

// 写入
suspend fun increment(context: Context) {
    context.dataStore.edit { prefs ->
        prefs[COUNTER_KEY] = (prefs[COUNTER_KEY] ?: 0) + 1
    }
}

// 读取（Flow）
val counterFlow: Flow<Int> = context.dataStore.data
    .map { prefs -> prefs[COUNTER_KEY] ?: 0 }

// ViewModel 中收集
val counter by counterFlow.collectAsState(initial = 0)
    """.trimIndent(),
    tips = listOf(
        "SharedPreferences 已被标记为过时，新项目直接使用 DataStore",
        "Preferences DataStore 对于简单配置足够，Proto DataStore 适合复杂业务数据",
        "在 Hilt 中注入 DataStore 实例，避免全局单例问题"
    )
)

@Composable
fun DataStoreScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF009688),
        stageTitle = "UI 组件与数据基础",
        onBack = onBack
    )
}
