package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * MVVM 架构模式
 * 官方文档：https://developer.android.com/topic/architecture
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心概念
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  三层职责 ─────────────────────────────────────────────────────────────
 *
 *  · View：只负责显示 UI 和用户输入（Activity / Fragment / Composable）
 *  · ViewModel：持有 UI 状态，处理业务逻辑，不引用 View
 *  · Repository：统一数据来源（网络/本地），屏蔽数据细节
 *
 * ── 1.2  单向数据流 ───────────────────────────────────────────────────────────
 *
 *  UI → Event → ViewModel → State → UI
 *
 *  · UiState：用 data class 描述完整的 UI 状态快照
 *  · 副作用：一次性事件用 Channel / SharedFlow 处理
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  代码示例
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  UiState 定义 ─────────────────────────────────────────────────────────
 *
 *  data class HomeUiState(
 *      val items: List<Item> = emptyList(),
 *      val isLoading: Boolean = false,
 *      val error: String? = null
 *  )
 *
 * ── 2.2  ViewModel 实现 ───────────────────────────────────────────────────────
 *
 *  class HomeViewModel(private val repo: ItemRepository) : ViewModel() {
 *      private val _uiState = MutableStateFlow(HomeUiState())
 *      val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
 *
 *      fun load() = viewModelScope.launch {
 *          _uiState.update { it.copy(isLoading = true) }
 *          repo.getItems()
 *              .onSuccess { items ->
 *                  _uiState.update { it.copy(items = items, isLoading = false) }
 *              }
 *              .onFailure { e ->
 *                  _uiState.update { it.copy(error = e.message, isLoading = false) }
 *              }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ViewModel 中不要持有 Context，使用 ApplicationContext
 *  · 用 StateFlow 暴露 UI 状态，Compose 项目比 LiveData 更合适
 *  · 单向数据流让状态变化可预测、易于测试
 */

val mvvmArchData = NoteData(
    title = "MVVM 架构模式",
    subtitle = "现代架构体系 · Model-View-ViewModel",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "核心概念"),
        ChapterItem("1.1", "三层职责"),
        ChapterItem("1.2", "单向数据流"),
        ChapterItem("2",   "代码示例"),
        ChapterItem("2.1", "UiState 定义"),
        ChapterItem("2.2", "ViewModel 实现"),
        ChapterItem("3",   "最佳实践"),
    )
)
