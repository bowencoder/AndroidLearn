package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Activity 与生命周期",
    description = "onCreate/onStart/onResume/onPause 等回调",
    overview = "Activity 是 Android 应用的基本 UI 单元，理解生命周期是避免内存泄漏和状态丢失的关键。",
    keyPoints = listOf(
        "onCreate：初始化 UI、绑定数据，只执行一次",
        "onStart / onStop：Activity 可见/不可见时调用",
        "onResume / onPause：Activity 获得/失去焦点",
        "onDestroy：Activity 被销毁，释放资源",
        "onSaveInstanceState：保存临时状态，防止配置变更丢失",
        "configChanges：横竖屏切换会重新创建 Activity"
    ),
    codeSnippet = """
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        // 恢复动画、注册监听器
    }

    override fun onPause() {
        super.onPause()
        // 暂停动画、释放相机等
    }
}
    """.trimIndent(),
    tips = listOf(
        "不要在 onPause 做耗时操作，会影响下一个 Activity 启动速度",
        "使用 ViewModel 保存 UI 状态，比 onSaveInstanceState 更可靠",
        "使用 Lifecycle-aware 组件自动管理生命周期"
    )
)

@Composable
fun ActivityLifecycleScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "四大组件",
        onBack = onBack
    )
}
