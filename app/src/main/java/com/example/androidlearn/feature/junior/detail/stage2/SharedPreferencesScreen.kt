package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "SharedPreferences",
    description = "轻量级键值对存储，DataStore 替代方案",
    overview = "SharedPreferences 适合存储少量配置数据，DataStore 是 Google 推荐的现代替代品，基于 Flow。",
    keyPoints = listOf(
        "SharedPreferences：getSharedPreferences / getDefaultSharedPreferences",
        "读取：getString / getInt / getBoolean",
        "写入：edit() → putXxx() → apply()（异步）",
        "DataStore：基于 Flow，类型安全，协程友好",
        "加密：EncryptedSharedPreferences 保护敏感数据",
        "不适合大量数据或频繁写入的场景"
    ),
    codeSnippet = """
// DataStore（推荐新项目使用）
val Context.dataStore by preferencesDataStore("settings")
val DARK_MODE = booleanPreferencesKey("dark_mode")

// 读取
val isDark: Flow<Boolean> = context.dataStore.data
    .map { prefs -> prefs[DARK_MODE] ?: false }

// 写入
suspend fun setDarkMode(enabled: Boolean) {
    context.dataStore.edit { it[DARK_MODE] = enabled }
}
    """.trimIndent(),
    tips = listOf(
        "新项目优先使用 DataStore，SP 在主线程 I/O 可能导致 ANR",
        "不要用 SP 存储大量数据或频繁变化的数据",
        "敏感信息用 EncryptedSharedPreferences 加密存储"
    )
)

@Composable
fun SharedPreferencesScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "核心UI与数据组件",
        onBack = onBack
    )
}
