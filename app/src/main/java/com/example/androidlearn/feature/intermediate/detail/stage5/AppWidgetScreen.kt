package com.example.androidlearn.feature.intermediate.detail.stage5

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * App Widget 与 Shortcut
 * 官方文档：https://developer.android.com/guide/topics/appwidgets
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  App Widget 基础
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  AppWidgetProvider ────────────────────────────────────────────────────
 *
 *  · 继承 BroadcastReceiver，处理小组件更新/删除等事件
 *  · onUpdate()：定期更新小组件内容
 *  · onEnabled() / onDisabled()：首次添加/最后一个删除时调用
 *
 *  class WeatherWidget : AppWidgetProvider() {
 *      override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
 *          ids.forEach { id ->
 *              val views = RemoteViews(context.packageName, R.layout.widget_weather)
 *              views.setTextViewText(R.id.tv_temp, "25°C")
 *              views.setOnClickPendingIntent(R.id.btn_refresh,
 *                  PendingIntent.getBroadcast(context, 0, Intent(ACTION_REFRESH), FLAG_IMMUTABLE))
 *              manager.updateAppWidget(id, views)
 *          }
 *      }
 *  }
 *
 * ── 1.2  RemoteViews ──────────────────────────────────────────────────────────
 *
 *  · 跨进程 UI 描述对象，只支持部分 View 类型
 *  · 支持：TextView / ImageView / Button / ProgressBar / ListView 等
 *  · 不支持：自定义 View / RecyclerView
 *
 * ── 1.3  AppWidgetProviderInfo ────────────────────────────────────────────────
 *
 *  · XML 配置小组件尺寸、更新频率、预览图
 *
 *  <appwidget-provider
 *      android:minWidth="250dp"
 *      android:minHeight="110dp"
 *      android:updatePeriodMillis="1800000"
 *      android:previewImage="@drawable/widget_preview"
 *      android:initialLayout="@layout/widget_weather" />
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Glance（Compose 风格 Widget）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 用 Compose 语法编写 Widget，自动转为 RemoteViews
 *  · 是未来趋势，但部分旧设备兼容性需测试
 *
 *  class MyWidget : GlanceAppWidget() {
 *      @Composable
 *      override fun Content() {
 *          Column {
 *              Text("Hello Glance!")
 *              Button("刷新", onClick = actionRunCallback<RefreshAction>())
 *          }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  快捷方式（Shortcut）
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  动态快捷方式 ─────────────────────────────────────────────────────────
 *
 *  · ShortcutManagerCompat.pushDynamicShortcut()
 *  · 最多 5 个动态快捷方式
 *
 * ── 3.2  固定快捷方式 ─────────────────────────────────────────────────────────
 *
 *  · requestPinShortcut() 需用户确认
 *  · 固定到桌面后不会随 App 更新而消失
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Widget 更新频率最低 30 分钟，频繁更新请用 AlarmManager 或 WorkManager
 *  · Glance 是未来趋势，但部分旧设备兼容性需测试
 *  · Widget 尺寸在 Android 12+ 可动态响应（flexible sizing）
 */

val appWidgetData = NoteData(
    title = "App Widget 与 Shortcut",
    subtitle = "多媒体与系统能力 · 桌面小组件 · RemoteViews · Glance",
    color = Color.parseColor("#00BCD4"),
    chapters = listOf(
        ChapterItem("1",   "App Widget 基础"),
        ChapterItem("1.1", "AppWidgetProvider"),
        ChapterItem("1.2", "RemoteViews"),
        ChapterItem("1.3", "AppWidgetProviderInfo"),
        ChapterItem("2",   "Glance（Compose 风格 Widget）"),
        ChapterItem("3",   "快捷方式（Shortcut）"),
        ChapterItem("3.1", "动态快捷方式"),
        ChapterItem("3.2", "固定快捷方式"),
        ChapterItem("4",   "最佳实践"),
    )
)
