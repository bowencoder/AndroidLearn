package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * ViewModel & StateFlow
 * 官方文档：https://developer.android.com/topic/libraries/architecture/viewmodel
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  ViewModel 核心
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  生命周期特性 ─────────────────────────────────────────────────────────
 *
 *  · 配置变更（旋转屏幕）后存活，不随 Activity 重建而销毁
 *  · onCleared()：ViewModel 真正销毁时调用，用于释放资源
 *  · viewModelScope：ViewModel 专属协程作用域，ViewModel 销毁时自动取消
 *
 * ── 1.2  创建方式 ─────────────────────────────────────────────────────────────
 *
 *  // 委托属性（推荐）
 *  val vm: CounterViewModel by viewModels()
 *
 *  // 带参数（使用 Factory）
 *  val vm: HomeViewModel by viewModels { HomeViewModelFactory(repo) }
 *
 *  // Hilt 注入（最简洁）
 *  @HiltViewModel
 *  class HomeViewModel @Inject constructor(private val repo: ItemRepository) : ViewModel()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  StateFlow vs LiveData
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  StateFlow（推荐） ────────────────────────────────────────────────────
 *
 *  · Kotlin 原生，与协程深度集成
 *  · 始终有初始值，新订阅者立即收到当前值
 *  · collectAsStateWithLifecycle()：Compose 中生命周期感知收集
 *
 *  class CounterViewModel : ViewModel() {
 *      private val _count = MutableStateFlow(0)
 *      val count: StateFlow<Int> = _count.asStateFlow()
 *
 *      fun increment() { _count.update { it + 1 } }
 *  }
 *
 * ── 2.2  LiveData（旧项目） ───────────────────────────────────────────────────
 *
 *  · 生命周期感知，STARTED 状态才分发数据
 *  · Transformations.map / switchMap：LiveData 转换
 *  · 新项目推荐 StateFlow 替代
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 新项目用 StateFlow + collectAsStateWithLifecycle
 *  · 耗时操作放在 viewModelScope.launch { } 中
 *  · ViewModel 中不要持有 Activity/Fragment 引用
 */

val viewModelData = NoteData(
    title = "ViewModel & StateFlow",
    subtitle = "现代架构体系 · 数据驱动 UI，配置变更存活",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "ViewModel 核心"),
        ChapterItem("1.1", "生命周期特性"),
        ChapterItem("1.2", "创建方式"),
        ChapterItem("2",   "StateFlow vs LiveData"),
        ChapterItem("2.1", "StateFlow（推荐）"),
        ChapterItem("2.2", "LiveData（旧项目）"),
        ChapterItem("3",   "最佳实践"),
    )
)
