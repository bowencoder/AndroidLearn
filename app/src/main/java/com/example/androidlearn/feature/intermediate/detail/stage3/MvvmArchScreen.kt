package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * MVVM 架构模式
 * 官方文档：https://developer.android.com/topic/architecture
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  MVVM 核心概念
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  三层职责 ─────────────────────────────────────────────────────────────
 *
 *  · View（视图层）：只负责显示 UI 和转发用户输入（Activity / Fragment / Composable）
 *    - 观察 ViewModel 暴露的 UiState，驱动界面渲染
 *    - 将用户操作（点击、输入）转换为 Event 发送给 ViewModel
 *    - 不包含任何业务逻辑，不直接访问数据源
 *
 *  · ViewModel（视图模型层）：持有 UI 状态，处理业务逻辑，不引用 View
 *    - 通过 StateFlow / LiveData 向 View 暴露不可变状态
 *    - 调用 Model 层（Repository）获取数据，转换为 UiState
 *    - 生命周期比 Activity/Fragment 长，配置变更后存活
 *
 *  · Model（模型层）：数据与业务逻辑的核心，ViewModel 的数据来源
 *    - Repository：统一数据入口，决策使用网络数据还是本地缓存
 *    - DataSource：具体数据获取实现（RemoteDataSource / LocalDataSource）
 *    - Domain Model：业务实体类（与网络 DTO、数据库 Entity 分离）
 *
 *
 * ── 1.2  UiState 设计原则 ─────────────────────────────────────────────────────
 *
 *  · 用 data class 描述完整快照，避免多个独立 StateFlow 导致状态不一致
 *  · 包含所有 UI 需要的字段：数据列表、加载状态、错误信息
 *  · 使用 sealed class 表达互斥状态（Loading / Success / Error）
 *
 *  // 方式一：data class 快照（推荐，适合字段较多的页面）
 *  data class HomeUiState(
 *      val items: List<Item> = emptyList(),
 *      val isLoading: Boolean = false,
 *      val error: String? = null,
 *      val isRefreshing: Boolean = false
 *  )
 *
 *  // 方式二：sealed class（适合状态互斥明确的场景）
 *  sealed class HomeUiState {
 *      object Loading : HomeUiState()
 *      data class Success(val items: List<Item>) : HomeUiState()
 *      data class Error(val message: String) : HomeUiState()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  单向数据流（UDF）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · UDF 是通用架构原则，并非 MVVM 专属（MVI、Redux 同样遵循）
 *  · Google 官方推荐在 Android MVVM 中结合 UDF，让状态变化更可预测、易测试
 *  · 核心约定：View 只发送 Event，ViewModel 持有并更新 State，View 被动观察
 *
 *
 * ── 2.1  核心三要素 ───────────────────────────────────────────────────────────
 *
 *  · State（状态）：描述 UI 当前应该显示什么，由 ViewModel 持有并暴露
 *  · Event（事件）：用户操作或系统触发（点击、输入、网络回调），由 View 发送给 ViewModel
 *  · SideEffect（副作用）：一次性事件（导航、Toast、弹窗），不属于持久状态
 *    - 用 Channel（推荐）或 SharedFlow 发送，View 消费后不重复触发
 *
 *  Event → ViewModel → State → View → Event（循环）
 *
 *  ┌─────────────────────────────────────────────────────────┐
 *  │  View（视图层）                                         │
 *  │   ├── 观察 uiState（StateFlow）→ 渲染 UI               │
 *  │   └── 用户操作 → 调用 ViewModel.onXxx()                │
 *  └─────────────────────────────────────────────────────────┘
 *           ↑ State                        ↓ Event
 *  ┌─────────────────────────────────────────────────────────┐
 *  │  ViewModel（视图模型层）                                │
 *  │   ├── _uiState: MutableStateFlow<UiState>              │
 *  │   └── 处理 Event → 调用 Model → 更新 _uiState          │
 *  └─────────────────────────────────────────────────────────┘
 *           ↑ Result                       ↓ Request
 *  ┌─────────────────────────────────────────────────────────┐
 *  │  Model（模型层）                                        │
 *  │   ├── Repository（数据入口，协调网络与缓存）            │
 *  │   ├── RemoteDataSource（Retrofit / Ktor）               │
 *  │   └── LocalDataSource（Room / DataStore）               │
 *  └─────────────────────────────────────────────────────────┘
 *
 *
 * ── 2.2  与 MVI 的关系 ────────────────────────────────────────────────────────
 *
 *  · MVI（Model-View-Intent）是对 UDF 更严格的落地：
 *    - Intent（意图）= Event，所有用户操作封装为 sealed class
 *    - Model = State，唯一不可变状态
 *    - View 只渲染 State，不持有任何逻辑
 *  · MVVM + UDF 与 MVI 思想相近，区别在于 MVI 对 Intent 的封装更严格
 *  · Android 中 MVVM + UDF 更常见，MVI 多见于 Compose 项目
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  ViewModel & StateFlow
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  生命周期特性 ─────────────────────────────────────────────────────────
 *
 *  · 配置变更（旋转屏幕）后存活，不随 Activity 重建而销毁
 *  · onCleared()：ViewModel 真正销毁时调用，用于释放资源
 *  · viewModelScope：ViewModel 专属协程作用域，ViewModel 销毁时自动取消
 *
 *
 * ── 3.2  创建方式 ─────────────────────────────────────────────────────────────
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
 *  // Fragment 共享 Activity 的 ViewModel
 *  val sharedVm: SharedViewModel by activityViewModels()
 *
 *
 * ── 3.3  StateFlow vs LiveData ────────────────────────────────────────────────
 *
 *  · StateFlow（推荐）：Kotlin 原生，与协程深度集成
 *    - 始终有初始值，新订阅者立即收到当前值
 *    - collectAsStateWithLifecycle()：Compose 中生命周期感知收集
 *
 *  class CounterViewModel : ViewModel() {
 *      private val _count = MutableStateFlow(0)
 *      val count: StateFlow<Int> = _count.asStateFlow()
 *
 *      fun increment() { _count.update { it + 1 } }
 *  }
 *
 *  · LiveData（旧项目）：生命周期感知，STARTED 状态才分发数据
 *    - Transformations.map / switchMap：LiveData 转换
 *    - 新项目推荐 StateFlow 替代
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · ViewModel 中不要持有 Context（如需要用 ApplicationContext，继承 AndroidViewModel）
 *  · 用 StateFlow 暴露 UI 状态，Compose 项目比 LiveData 更合适
 *  · 耗时操作放在 viewModelScope.launch { } 中
 *  · 副作用（导航、Toast）用 Channel 而非 StateFlow，避免重复触发
 *  · Repository 定义接口，便于单元测试时 Mock
 *  · UiState 用 data class，通过 copy() 更新，保持不可变性
 *  · 在 Compose 中用 collectAsStateWithLifecycle() 替代 collectAsState()，节省资源
 *  · 避免在 ViewModel 中直接操作 UI（不要持有 View 引用）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  MVP 架构模式
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 5.1  三层职责 ─────────────────────────────────────────────────────────────
 *
 *  · View（视图层）：Activity / Fragment，只负责 UI 渲染，实现 View 接口
 *    - 持有 Presenter 引用，将用户操作委托给 Presenter 处理
 *    - 实现 IView 接口，供 Presenter 回调更新 UI
 *    - 不包含任何业务逻辑
 *
 *  · Presenter（展示层）：业务逻辑核心，连接 View 与 Model
 *    - 持有 View 接口引用（弱引用，防止内存泄漏）
 *    - 调用 Model 获取数据，处理后通过 View 接口回调更新 UI
 *    - 不依赖 Android 框架，便于纯 JVM 单元测试
 *
 *  · Model（模型层）：数据获取与业务规则（同 MVVM 的 Model 层）
 *    - Repository + DataSource，与 MVVM 中的 Model 层结构相同
 *
 *
 * ── 5.2  数据流向 ─────────────────────────────────────────────────────────────
 *
 *  View ──(用户操作)──→ Presenter ──(请求数据)──→ Model
 *  View ←─(回调更新)── Presenter ←─(返回结果)── Model
 *
 *  · View 与 Model 完全解耦，所有交互都经过 Presenter 中转
 *  · View 和 Presenter 通过接口通信，双向依赖（区别于 MVVM + UDF 的单向流动）
 *
 *
 * ── 5.3  代码示例 ─────────────────────────────────────────────────────────────
 *
 *  // ① View 接口
 *  interface IHomeView {
 *      fun showLoading()
 *      fun hideLoading()
 *      fun showItems(items: List<Item>)
 *      fun showError(message: String)
 *  }
 *
 *  // ② Presenter
 *  class HomePresenter(private val repo: ItemRepository) {
 *
 *      // 弱引用持有 View，防止 Activity 泄漏
 *      private var view: WeakReference<IHomeView>? = null
 *
 *      fun attachView(v: IHomeView) { view = WeakReference(v) }
 *      fun detachView() { view = null }
 *
 *      fun loadItems() {
 *          view?.get()?.showLoading()
 *          CoroutineScope(Dispatchers.Main).launch {
 *              runCatching { repo.getItems() }
 *                  .onSuccess { items ->
 *                      view?.get()?.hideLoading()
 *                      view?.get()?.showItems(items)
 *                  }
 *                  .onFailure { e ->
 *                      view?.get()?.hideLoading()
 *                      view?.get()?.showError(e.message ?: "未知错误")
 *                  }
 *          }
 *      }
 *  }
 *
 *  // ③ View 实现（Activity）
 *  class HomeActivity : AppCompatActivity(), IHomeView {
 *
 *      private val presenter = HomePresenter(ItemRepositoryImpl())
 *
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          presenter.attachView(this)
 *          presenter.loadItems()
 *      }
 *
 *      override fun onDestroy() {
 *          super.onDestroy()
 *          presenter.detachView()   // 解绑，防止内存泄漏
 *      }
 *
 *      override fun showLoading() { binding.progressBar.isVisible = true }
 *      override fun hideLoading() { binding.progressBar.isVisible = false }
 *      override fun showItems(items: List<Item>) { adapter.submitList(items) }
 *      override fun showError(message: String) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
 *  }
 *
 *
 * ── 5.4  MVP vs MVVM 对比 ─────────────────────────────────────────────────────
 *
 *  ┌──────────────┬──────────────────────────────┬──────────────────────────────┐
 *  │              │           MVP                │           MVVM               │
 *  ├──────────────┼──────────────────────────────┼──────────────────────────────┤
 *  │ View 与逻辑  │ 通过接口双向通信             │ 单向数据流，View 观察状态    │
 *  │ 配置变更     │ Presenter 需手动处理重建      │ ViewModel 自动存活           │
 *  │ 测试难度     │ Presenter 易于单元测试        │ ViewModel 同样易于测试       │
 *  │ 内存泄漏风险 │ 需弱引用持有 View            │ ViewModel 不持有 View，无风险│
 *  │ 代码量       │ 接口模板代码较多             │ 相对简洁                     │
 *  │ 适用场景     │ 旧项目维护、View 体系        │ 新项目首选，Compose 深度集成 │
 *  └──────────────┴──────────────────────────────┴──────────────────────────────┘
 *
 *  · MVP 在 Android 中已逐渐被 MVVM 取代，新项目推荐 MVVM + ViewModel + StateFlow
 *  · 旧项目若已使用 MVP，可逐步迁移：先引入 ViewModel 替换 Presenter，再引入 StateFlow
 */

val mvvmArchData = NoteData(
    title = "MVVM & ViewModel",
    subtitle = "现代架构体系 · MVVM · ViewModel · StateFlow · UDF · MVP 对比",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "MVVM 核心概念"),
        ChapterItem("1.1", "三层职责：View / ViewModel / Model（Repository）"),
        ChapterItem("1.2", "UiState 设计：data class 快照 vs sealed class"),
        ChapterItem("2",   "单向数据流（UDF）"),
        ChapterItem("2.1", "核心三要素：State / Event / SideEffect"),
        ChapterItem("2.2", "与 MVI 的关系：Intent 封装 / 严格程度对比"),
        ChapterItem("3",   "ViewModel & StateFlow"),
        ChapterItem("3.1", "生命周期特性：配置变更存活 / onCleared / viewModelScope"),
        ChapterItem("3.2", "创建方式：viewModels / Factory / Hilt / activityViewModels"),
        ChapterItem("3.3", "StateFlow vs LiveData：collectAsStateWithLifecycle"),
        ChapterItem("4",   "最佳实践"),
        ChapterItem("5",   "MVP 架构模式"),
        ChapterItem("5.1", "三层职责：View（IView 接口）/ Presenter / Model"),
        ChapterItem("5.2", "数据流向：View ↔ Presenter ↔ Model（双向回调）"),
        ChapterItem("5.3", "代码示例：IView 接口 / Presenter 弱引用 / Activity 实现"),
        ChapterItem("5.4", "MVP vs MVVM 对比：配置变更 / 内存泄漏 / 测试 / 适用场景"),
    )
)
