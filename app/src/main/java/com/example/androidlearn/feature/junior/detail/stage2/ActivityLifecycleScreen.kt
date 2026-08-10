package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Activity、Intent 与页面跳转笔记
 * 官方文档：https://developer.android.com/guide/components/activities/activity-lifecycle
 *           https://developer.android.com/guide/components/intents-filters
 *
 * ── 1  生命周期回调顺序 ───────────────────────────────────────────────────────
 *
 *  onCreate → onStart → onResume → [运行中] → onPause → onStop → onDestroy
 *
 *  · onCreate：初始化 UI、绑定数据，整个生命周期只执行一次
 *  · onResume：进入前台可交互；每次回到前台都会调用
 *  · onPause：失去焦点；不要做耗时操作，会阻塞下一个 Activity 启动
 *  · onStop：完全不可见；释放重量级资源
 *  · onDestroy：被销毁；释放所有资源、取消协程
 *
 *  class MainActivity : AppCompatActivity() {
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          setContentView(R.layout.activity_main)
 *      }
 *      override fun onResume() { super.onResume(); /* 恢复相机、传感器 */ }
 *      override fun onPause()  { super.onPause();  /* 暂停动画、释放相机 */ }
 *  }
 *
 *
 * ── 2  状态保存与恢复 ─────────────────────────────────────────────────────────
 *
 *  触发时机：屏幕旋转、按 Home 切后台（系统可能回收）
 *  注意：用户主动按返回键退出不会触发
 *
 *  // 保存
 *  override fun onSaveInstanceState(outState: Bundle) {
 *      super.onSaveInstanceState(outState)
 *      outState.putString("key", value)
 *  }
 *  // 恢复
 *  override fun onCreate(savedInstanceState: Bundle?) {
 *      super.onCreate(savedInstanceState)
 *      val value = savedInstanceState?.getString("key")
 *  }
 *
 *  · ViewModel：配置变更（旋转）时不销毁，适合保存业务数据（推荐）
 *  · Bundle：适合保存少量 UI 状态（滚动位置、输入框文字）
 *
 *
 * ── 3  Activity 启动模式（launchMode）────────────────────────────────────────
 *
 *  · standard（默认）：每次启动都创建新实例
 *  · singleTop：目标已在栈顶则复用，调用 onNewIntent()；适合通知点击、搜索页
 *  · singleTask：栈中只有一个实例，清除其上方所有 Activity；适合主页
 *  · singleInstance：独占一个任务栈；适合来电界面
 *
 *  // AndroidManifest.xml
 *  <activity android:name=".HomeActivity" android:launchMode="singleTask"/>
 *
 *  // singleTop/singleTask 复用时调用
 *  override fun onNewIntent(intent: Intent) {
 *      super.onNewIntent(intent)
 *      setIntent(intent)
 *  }
 *
 *
 * ── 4  显式 Intent 与数据传递 ─────────────────────────────────────────────────
 *
 *  · 显式：明确指定目标组件类名，用于应用内跳转
 *  · 隐式：只声明 Action/Data，由系统匹配合适组件
 *
 *  // 显式跳转 + 传参
 *  val intent = Intent(this, DetailActivity::class.java)
 *  intent.putExtra("userId", 42)
 *  intent.putExtra("userName", "Alice")
 *  startActivity(intent)
 *
 *  // 接收（目标 Activity）
 *  val userId = intent.getIntExtra("userId", -1)
 *  val userName = intent.getStringExtra("userName")
 *
 *  // 传递对象：@Parcelize 注解自动实现 Parcelable（性能优于 Serializable）
 *  @Parcelize data class User(val id: Int, val name: String) : Parcelable
 *  intent.putExtra("user", User(1, "Alice"))
 *
 *
 * ── 5  隐式 Intent（常用系统 Action）────────────────────────────────────────
 *
 *  Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"))  // 打开网页
 *  Intent(Intent.ACTION_DIAL, Uri.parse("tel:10086"))            // 打开拨号盘
 *  Intent(Intent.ACTION_SEND).apply {                            // 分享文本
 *      type = "text/plain"
 *      putExtra(Intent.EXTRA_TEXT, "分享内容")
 *  }
 *
 *  · Android 11+ 需在 Manifest 声明 queries 块，否则 resolveActivity 返回 null
 *
 *
 * ── 6  ActivityResultLauncher（新 API）────────────────────────────────────────
 *
 *  · startActivityForResult + onActivityResult 已废弃，用 registerForActivityResult 替代
 *
 *  val launcher = registerForActivityResult(
 *      ActivityResultContracts.StartActivityForResult()
 *  ) { result ->
 *      if (result.resultCode == Activity.RESULT_OK) {
 *          val data = result.data?.getStringExtra("key")
 *      }
 *  }
 *  launcher.launch(Intent(this, PickerActivity::class.java))
 *
 *  // 目标 Activity 返回结果
 *  setResult(Activity.RESULT_OK, Intent().putExtra("key", "value"))
 *  finish()
 *
 *
 * ── 7  Intent Flags 与任务栈 ──────────────────────────────────────────────────
 *
 *  · NEW_TASK：在新任务中启动（从非 Activity 上下文启动必须加）
 *  · CLEAR_TOP：清除目标 Activity 上方所有页面
 *  · SINGLE_TOP：目标已在栈顶则复用，调用 onNewIntent
 *  · CLEAR_TASK：清空整个任务栈（配合 NEW_TASK，常用于登出跳首页）
 *
 *  // 登出后跳首页，清空所有 Activity
 *  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
 *
 *
 * ── 8  Deep Link & Jetpack Navigation ────────────────────────────────────────
 *
 *  Deep Link：通过 URI 从外部直接打开应用特定页面
 *  · 自定义 Scheme：myapp://detail/42
 *  · App Links：https://example.com/detail/42（需服务器验证）
 *  · Manifest 配置：intent-filter 加 action(VIEW) + category(BROWSABLE) + data
 *
 *  Compose Navigation（替代 Intent 管理 Fragment/Composable 跳转）：
 *  NavHost(navController, startDestination = "home") {
 *      composable("home") { HomeScreen(navController) }
 *      composable("detail/{id}") { backStackEntry ->
 *          DetailScreen(backStackEntry.arguments?.getString("id"))
 *      }
 *  }
 *  navController.navigate("detail/42")
 *
 *
 * ── 9  Lifecycle 组件（Jetpack）──────────────────────────────────────────────
 *
 *  · 让其他组件感知 Activity 生命周期，自动注册/注销，避免内存泄漏
 *
 *  // 实现 DefaultLifecycleObserver
 *  class LocationManager : DefaultLifecycleObserver {
 *      override fun onResume(owner: LifecycleOwner) = startLocationUpdates()
 *      override fun onPause(owner: LifecycleOwner)  = stopLocationUpdates()
 *  }
 *  lifecycle.addObserver(LocationManager())
 *
 *  // 协程：在 STARTED 时收集，onStop 自动取消
 *  lifecycleScope.launch {
 *      repeatOnLifecycle(Lifecycle.State.STARTED) {
 *          viewModel.uiState.collect { updateUI(it) }
 *      }
 *  }
 *
 *
 * ── 10  最佳实践 ──────────────────────────────────────────────────────────────
 *
 *  · onResume/onPause 对称操作（注册/注销、开始/停止）
 *  · 业务逻辑放 ViewModel，不要放 Activity
 *  · 协程用 lifecycleScope，随 Activity 销毁自动取消
 *  · 不要持有静态 Context 引用，会导致内存泄漏
 *  · 配置变更优先用 ViewModel 解决，而非 configChanges
 *  · 传递复杂对象用 @Parcelize，避免 Serializable 性能损耗
 *  · 用 ActivityResultLauncher 替代已废弃的 startActivityForResult
 */

private val Blue = Color(0xFF2196F3)

private val chapters = listOf(
    NoteChapter("1", "生命周期回调顺序"),
    NoteChapter("2", "状态保存与恢复"),
    NoteChapter("3", "Activity 启动模式（launchMode）"),
    NoteChapter("4", "显式 Intent 与数据传递"),
    NoteChapter("5", "隐式 Intent（常用系统 Action）"),
    NoteChapter("6", "ActivityResultLauncher（新 API）"),
    NoteChapter("7", "Intent Flags 与任务栈"),
    NoteChapter("8", "Deep Link & Jetpack Navigation"),
    NoteChapter("9", "Lifecycle 组件（Jetpack）"),
    NoteChapter("10", "最佳实践"),
)

@Composable
fun ActivityLifecycleScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Activity 与 Intent",
        subtitle = "生命周期 · 启动模式 · Intent · 数据传递 · Deep Link",
        color = Blue,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
