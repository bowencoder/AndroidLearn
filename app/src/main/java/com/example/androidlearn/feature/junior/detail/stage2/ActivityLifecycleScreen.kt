package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

private data class LifecycleChapter(val num: String, val title: String)

private val chapters = listOf(
    LifecycleChapter("1", "生命周期回调顺序"),
    LifecycleChapter("2", "状态保存与恢复"),
    LifecycleChapter("3", "Activity 启动模式（launchMode）"),
    LifecycleChapter("4", "Lifecycle 组件（Jetpack）"),
    LifecycleChapter("5", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLifecycleScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Activity 与生命周期", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "onCreate · onResume · 状态保存 · 启动模式",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters.size) { i -> ChapterRowLifecycle(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowLifecycle(chapter: LifecycleChapter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Blue.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
