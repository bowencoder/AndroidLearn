package com.example.androidlearn.feature.intermediate.detail.stage5

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "App Widget 与 Shortcut",
    description = "桌面小组件 · RemoteViews · Glance · 动态快捷方式",
    overview = "App Widget 是运行在桌面 Launcher 上的轻量级 UI 组件，让用户无需进入 App 即可查看关键信息或执行简单操作。",
    keyPoints = listOf(
        "AppWidgetProvider：继承 BroadcastReceiver，处理小组件更新/删除等事件",
        "RemoteViews：跨进程 UI 描述对象，只支持部分 View 类型（TextView/ImageView/Button 等）",
        "AppWidgetProviderInfo：XML 配置小组件尺寸、更新频率、预览图",
        "Glance（Jetpack）：用 Compose 语法编写 Widget，自动转为 RemoteViews",
        "动态快捷方式（Dynamic Shortcuts）：ShortcutManagerCompat.pushDynamicShortcut()",
        "固定快捷方式（Pinned Shortcuts）：requestPinShortcut() 需用户确认"
    ),
    codeSnippet = """
// 传统 AppWidgetProvider
class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_weather)
            views.setTextViewText(R.id.tv_temp, "25°C")
            views.setOnClickPendingIntent(R.id.btn_refresh,
                PendingIntent.getBroadcast(context, 0, Intent(ACTION_REFRESH), FLAG_IMMUTABLE))
            manager.updateAppWidget(id, views)
        }
    }
}

// Glance（Compose 风格 Widget）
class MyWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
        Column {
            Text("Hello Glance!")
            Button("刷新", onClick = actionRunCallback<RefreshAction>())
        }
    }
}
    """.trimIndent(),
    tips = listOf(
        "Widget 更新频率最低 30 分钟，频繁更新请用 AlarmManager 或 WorkManager",
        "Glance 是未来趋势，但部分旧设备兼容性需测试",
        "Widget 尺寸在 Android 12+ 可动态响应（flexible sizing）"
    )
)

@Composable
fun AppWidgetScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF00BCD4),
        stageTitle = "多媒体与系统能力",
        onBack = onBack
    )
}
