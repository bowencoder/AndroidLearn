package com.example.androidlearn.feature.intermediate.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "MVVM 架构模式",
    description = "Model-View-ViewModel 职责分离",
    overview = "MVVM 是 Android 官方推荐架构，将 UI、业务逻辑和数据层清晰分离，提升可测试性和可维护性。",
    keyPoints = listOf(
        "View：只负责显示 UI 和用户输入（Activity/Fragment/Composable）",
        "ViewModel：持有 UI 状态，处理业务逻辑，不引用 View",
        "Repository：统一数据来源（网络/本地），屏蔽数据细节",
        "单向数据流：UI → Event → ViewModel → State → UI",
        "UiState：用 data class 描述完整的 UI 状态快照",
        "副作用：一次性事件用 Channel/SharedFlow 处理"
    ),
    codeSnippet = """
data class HomeUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val repo: ItemRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        repo.getItems()
            .onSuccess { items -> _uiState.update { it.copy(items = items, isLoading = false) } }
            .onFailure { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
    }
}
    """.trimIndent(),
    tips = listOf(
        "ViewModel 中不要持有 Context，使用 ApplicationContext",
        "用 StateFlow 暴露 UI 状态，Compose 项目比 LiveData 更合适",
        "单向数据流让状态变化可预测、易于测试"
    )
)

@Composable
fun MvvmArchScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF9C27B0),
        stageTitle = "现代架构体系",
        onBack = onBack
    )
}
