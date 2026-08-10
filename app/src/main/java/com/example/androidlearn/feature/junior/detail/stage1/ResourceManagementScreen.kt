package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Android 资源管理笔记
 * 官方文档：https://developer.android.com/guide/topics/resources/providing-resources
 *
 * ── 1  资源目录结构 ───────────────────────────────────────────────────────────
 *
 *  · values/    字符串、颜色、尺寸、样式（strings.xml / colors.xml / dimens.xml）
 *  · drawable/  图形资源（VectorDrawable、PNG、Shape、Selector）
 *  · mipmap/    应用图标专用，不同密度各一套
 *  · layout/    界面布局 XML（View 系统）
 *  · raw/       原始文件（音频、视频），通过 R.raw.xxx 访问
 *  · font/      字体文件（Android 8+）
 *
 *  // 代码访问
 *  getString(R.string.app_name)
 *  ContextCompat.getColor(context, R.color.primary)
 *  // XML 访问
 *  android:text="@string/app_name"
 *  android:textColor="@color/primary"
 *
 *
 * ── 2  strings.xml ────────────────────────────────────────────────────────────
 *
 *  <resources>
 *      <string name="app_name">Android Learn</string>
 *      <string name="greeting">你好，%1$s！</string>   <!-- 格式化占位符 -->
 *  </resources>
 *
 *  getString(R.string.greeting, "Alice")   // "你好，Alice！"
 *
 *  · 所有用户可见文字放 strings.xml，方便国际化
 *  · 单引号需转义：it\'s；HTML 标签支持：<b>加粗</b>
 *
 *
 * ── 3  colors.xml & dimens.xml ───────────────────────────────────────────────
 *
 *  <!-- colors.xml -->
 *  <color name="primary">#FF6200EE</color>
 *
 *  <!-- dimens.xml -->
 *  <dimen name="spacing_medium">16dp</dimen>
 *  <dimen name="text_body">14sp</dimen>
 *
 *  · dp：与屏幕密度无关，用于布局尺寸
 *  · sp：在 dp 基础上受字体大小设置影响，用于文字大小
 *  · 深色模式：res/values-night/colors.xml 同名覆盖
 *
 *
 * ── 4  drawable 资源 ──────────────────────────────────────────────────────────
 *
 *  · VectorDrawable（推荐）：基于 SVG，任意分辨率无损，只需一份文件
 *  · 位图（PNG/WebP）：按密度分目录 mdpi / hdpi / xhdpi / xxhdpi / xxxhdpi
 *  · Shape：纯 XML 绘制圆角矩形、描边等，无需图片文件
 *  · Selector：按状态（pressed / focused / normal）切换不同 drawable
 *  · mipmap vs drawable：图标放 mipmap，其他图片放 drawable
 *
 *
 * ── 5  资源限定符 ─────────────────────────────────────────────────────────────
 *
 *  目录名后缀决定在哪种配置下使用：
 *  · values-zh-rCN/   简体中文（i18n 多语言）
 *  · layout-land/     横屏布局
 *  · layout-sw600dp/  最小宽度 600dp（平板）
 *  · values-night/    深色模式
 *  · values-v26/      Android 8.0+
 *
 *  // Compose 中访问资源
 *  Text(stringResource(R.string.app_name))
 *  Image(painter = painterResource(R.drawable.ic_logo), contentDescription = null)
 *  val spacing = dimensionResource(R.dimen.spacing_medium)
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1", "资源目录结构"),
    NoteChapter("2", "strings.xml"),
    NoteChapter("3", "colors.xml & dimens.xml"),
    NoteChapter("4", "drawable 资源"),
    NoteChapter("5", "资源限定符"),
)

@Composable
fun ResourceManagementScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "资源管理",
        subtitle = "strings · colors · drawable · 限定符",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
