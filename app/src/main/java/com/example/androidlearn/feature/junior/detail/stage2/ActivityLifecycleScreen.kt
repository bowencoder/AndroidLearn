package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Activity 与生命周期笔记
 * 官方文档：https://developer.android.com/guide/components/activities/activity-lifecycle
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
 * ── 4  Lifecycle 组件（Jetpack）──────────────────────────────────────────────
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
 * ── 5  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · onResume/onPause 对称操作（注册/注销、开始/停止）
 *  · 业务逻辑放 ViewModel，不要放 Activity
 *  · 协程用 lifecycleScope，随 Activity 销毁自动取消
 *  · 不要持有静态 Context 引用，会导致内存泄漏
 *  · 配置变更优先用 ViewModel 解决，而非 configChanges
 */

private val Blue = Color(0xFF2196F3)

private val chapters = listOf(
    NoteChapter("1", "生命周期回调顺序"),
    NoteChapter("2", "状态保存与恢复"),
    NoteChapter("3", "Activity 启动模式（launchMode）"),
    NoteChapter("4", "Lifecycle 组件（Jetpack）"),
    NoteChapter("5", "最佳实践"),
)

@Composable
fun ActivityLifecycleScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Activity 与生命周期",
        subtitle = "onCreate · onResume · 状态保存 · 启动模式",
        color = Blue,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
