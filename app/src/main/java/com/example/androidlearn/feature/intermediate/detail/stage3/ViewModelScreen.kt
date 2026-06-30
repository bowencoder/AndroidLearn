package com.example.androidlearn.feature.intermediate.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "ViewModel & LiveData",
    description = "数据驱动 UI，配置变更存活",
    overview = "ViewModel 在配置变更后存活，LiveData 是生命周期感知的可观察数据容器，是数据驱动 UI 的核心。",
    keyPoints = listOf(
        "ViewModel：配置变更后保留，onCleared() 释放资源",
        "viewModelScope：ViewModel 专属协程，ViewModel 销毁时自动取消",
        "LiveData：生命周期感知，STARTED 状态才分发数据",
        "MutableLiveData / MutableStateFlow：可修改的数据容器",
        "Transformations.map / switchMap：LiveData 转换",
        "StateFlow（推荐）：Kotlin 原生，替代 LiveData"
    ),
    codeSnippet = """
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() { _count.update { it + 1 } }
}

// Compose 中使用
@Composable
fun CounterScreen(vm: CounterViewModel = viewModel()) {
    val count by vm.count.collectAsStateWithLifecycle()
    Button(onClick = vm::increment) {
        Text("点击次数：${'$'}count")
    }
}
    """.trimIndent(),
    tips = listOf(
        "新项目用 StateFlow + collectAsStateWithLifecycle",
        "耗时操作放在 viewModelScope.launch { } 中",
        "使用 by viewModels() 委托属性创建 ViewModel"
    )
)

@Composable
fun ViewModelScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF9C27B0),
        stageTitle = "现代架构体系",
        onBack = onBack
    )
}
