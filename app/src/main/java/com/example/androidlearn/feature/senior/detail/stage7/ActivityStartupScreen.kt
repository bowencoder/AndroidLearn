package com.example.androidlearn.feature.senior.detail.stage7

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Activity 启动全链路",
    description = "AMS / ATMS 调度，进程创建，生命周期回调时序",
    overview = "Activity 启动涉及 Launcher、AMS/ATMS、Zygote、目标 App 进程多方协作，理解全链路才能精准优化冷启动。",
    keyPoints = listOf(
        "Launcher → AMS/ATMS：startActivity 经 Binder 发送给 ActivityTaskManagerService",
        "ATMS：检查权限、处理 Task 栈、决定是否需要新进程",
        "Zygote fork：若目标进程不存在，Socket 通知 Zygote fork 新进程",
        "ActivityThread.main()：新进程入口，初始化 Looper / Application / Activity",
        "H 类消息：LAUNCH_ACTIVITY 消息触发 performLaunchActivity，调用 onCreate",
        "Window 挂载：setContentView → DecorView → ViewRootImpl → 第一帧渲染"
    ),
    codeSnippet = """
// 冷启动优化 - 用 ReportFullyDrawn 精准标记首屏完成
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 数据加载完成后才算首屏
        viewModel.uiState.observe(this) { state ->
            if (!state.isLoading) {
                // 通知系统首屏已绘制完成，用于 Perfetto 分析
                reportFullyDrawn()
            }
        }
    }
}

// 在 Manifest 中声明 exported + 启动主题避免白屏
// android:theme="@style/Theme.App.SplashScreen"
    """.trimIndent(),
    tips = listOf(
        "冷启动链路：Launcher click → AMS → Zygote → Application.onCreate → Activity.onCreate → 首帧",
        "Zygote 预加载了常用类和资源，fork 比直接创建进程快很多",
        "用 adb shell am start -W 测量真实冷启动耗时"
    )
)

@Composable
fun ActivityStartupScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF795548),
        stageTitle = "底层原理与面试重点",
        onBack = onBack
    )
}
