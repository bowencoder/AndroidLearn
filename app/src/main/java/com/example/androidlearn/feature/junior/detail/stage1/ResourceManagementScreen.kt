package com.example.androidlearn.feature.junior.detail.stage1

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
 * Android 资源管理笔记
 * 官方文档：https://developer.android.com/guide/topics/resources/providing-resources
 *
 * ── 1  资源目录结构 ───────────────────────────────────────────────────────────
 *
 *  res/ 目录下的标准子目录：
 *  · values/       字符串、颜色、尺寸、样式等 XML 资源
 *  · drawable/     图形资源（VectorDrawable、PNG、Shape、Selector 等）
 *  · mipmap/       应用图标（不同密度各一套，不随屏幕密度缩放）
 *  · layout/       界面布局 XML（View 系统）
 *  · anim/         补间动画（Tween Animation）
 *  · animator/     属性动画（Property Animation）
 *  · menu/         菜单资源
 *  · raw/          原始文件（音频、视频等），通过 R.raw.xxx 访问
 *  · font/         字体文件（TTF/OTF），Android 8+ 支持
 *  · xml/          任意 XML 文件（如 network_security_config.xml）
 *
 *  访问方式：
 *  // 代码中
 *  val str = getString(R.string.app_name)
 *  val color = ContextCompat.getColor(context, R.color.primary)
 *  val drawable = ContextCompat.getDrawable(context, R.drawable.ic_logo)
 *
 *  // XML 中
 *  android:text="@string/app_name"
 *  android:textColor="@color/primary"
 *  android:src="@drawable/ic_logo"
 *
 *
 * ── 2  strings.xml（字符串资源）────────────────────────────────────────────
 *
 *  基本用法：
 *  <resources>
 *      <string name="app_name">Android Learn</string>
 *      <string name="welcome">欢迎使用</string>
 *  </resources>
 *
 *  格式化字符串（占位符）：
 *  <string name="greeting">你好，%1$s！今天是第 %2$d 天。</string>
 *  // 代码中
 *  getString(R.string.greeting, "Alice", 7)  // "你好，Alice！今天是第 7 天。"
 *
 *  复数支持（Plurals）：
 *  <plurals name="item_count">
 *      <item quantity="one">%d 个项目</item>
 *      <item quantity="other">%d 个项目</item>
 *  </plurals>
 *  // 代码中
 *  resources.getQuantityString(R.plurals.item_count, count, count)
 *
 *  字符串数组：
 *  <string-array name="planets">
 *      <item>水星</item>
 *      <item>金星</item>
 *      <item>地球</item>
 *  </string-array>
 *  // 代码中
 *  resources.getStringArray(R.array.planets)
 *
 *  注意事项：
 *  · 所有用户可见文字必须放入 strings.xml，方便国际化
 *  · 单引号需转义：it\'s → it's；或用双引号包裹："it's"
 *  · HTML 标签支持：<b>加粗</b>、<i>斜体</i>、<u>下划线</u>
 *
 *
 * ── 3  colors.xml & themes.xml（颜色与主题）──────────────────────────────
 *
 *  colors.xml 定义颜色：
 *  <resources>
 *      <color name="primary">#FF6200EE</color>
 *      <color name="primary_variant">#FF3700B3</color>
 *      <color name="on_primary">#FFFFFFFF</color>
 *      <color name="background">#FFFFFFFF</color>
 *      <color name="surface">#FFFFFFFF</color>
 *  </resources>
 *
 *  Material 3 主题（themes.xml）：
 *  <style name="Theme.App" parent="Theme.Material3.DayNight">
 *      <item name="colorPrimary">@color/primary</item>
 *      <item name="colorOnPrimary">@color/on_primary</item>
 *      <item name="android:statusBarColor">@color/primary</item>
 *  </style>
 *
 *  深色模式适配：
 *  · res/values/colors.xml         → 浅色模式颜色
 *  · res/values-night/colors.xml   → 深色模式颜色（同名覆盖）
 *
 *  Compose 中使用主题色：
 *  MaterialTheme.colorScheme.primary
 *  MaterialTheme.colorScheme.background
 *  MaterialTheme.colorScheme.onSurface
 *
 *
 * ── 4  dimens.xml（尺寸资源）────────────────────────────────────────────
 *
 *  定义尺寸：
 *  <resources>
 *      <dimen name="spacing_small">8dp</dimen>
 *      <dimen name="spacing_medium">16dp</dimen>
 *      <dimen name="spacing_large">24dp</dimen>
 *      <dimen name="text_body">14sp</dimen>
 *      <dimen name="text_title">20sp</dimen>
 *      <dimen name="card_corner_radius">12dp</dimen>
 *  </resources>
 *
 *  代码中使用：
 *  val spacing = resources.getDimension(R.dimen.spacing_medium)  // 返回 px
 *  val spacingDp = resources.getDimensionPixelSize(R.dimen.spacing_medium)
 *
 *  dp vs sp：
 *  · dp（density-independent pixel）：与屏幕密度无关，用于布局尺寸
 *  · sp（scale-independent pixel）：在 dp 基础上还受字体大小设置影响，用于文字大小
 *  · px = dp × (dpi / 160)；1dp ≈ 1px（在 160dpi 屏幕上）
 *
 *
 * ── 5  drawable 资源 ─────────────────────────────────────────────────────
 *
 *  VectorDrawable（矢量图，推荐）：
 *  · 基于 SVG，任意分辨率无损缩放，只需一份文件
 *  · Android Studio → File → New → Vector Asset 导入 SVG 或 Material Icon
 *  · 文件放在 res/drawable/ 目录（无需分辨率子目录）
 *
 *  <vector xmlns:android="http://schemas.android.com/apk/res/android"
 *      android:width="24dp" android:height="24dp"
 *      android:viewportWidth="24" android:viewportHeight="24">
 *      <path android:fillColor="#FF000000"
 *          android:pathData="M12,2L2,7l10,5 10,-5-10,-5z"/>
 *  </vector>
 *
 *  位图资源（PNG/WebP）：
 *  · 按屏幕密度分目录存放：
 *    drawable-mdpi/    → 1x（160dpi）
 *    drawable-hdpi/    → 1.5x（240dpi）
 *    drawable-xhdpi/   → 2x（320dpi）
 *    drawable-xxhdpi/  → 3x（480dpi）
 *    drawable-xxxhdpi/ → 4x（640dpi）
 *  · 推荐使用 WebP 格式（比 PNG 小 25-34%）
 *
 *  Shape Drawable（纯 XML 绘制形状）：
 *  <shape xmlns:android="http://schemas.android.com/apk/res/android"
 *      android:shape="rectangle">
 *      <corners android:radius="8dp"/>
 *      <solid android:color="#FF6200EE"/>
 *      <stroke android:width="1dp" android:color="#FF000000"/>
 *  </shape>
 *
 *  Selector Drawable（状态选择器）：
 *  <selector xmlns:android="http://schemas.android.com/apk/res/android">
 *      <item android:state_pressed="true" android:drawable="@color/pressed"/>
 *      <item android:state_focused="true" android:drawable="@color/focused"/>
 *      <item android:drawable="@color/normal"/>
 *  </selector>
 *
 *  点九图（NinePatch .9.png）：
 *  · 可拉伸的位图，指定哪些区域可拉伸、哪些区域固定
 *  · 用于按钮背景、气泡等需要自适应大小的场景
 *
 *
 * ── 6  mipmap（应用图标）────────────────────────────────────────────────
 *
 *  与 drawable 的区别：
 *  · mipmap 专门用于应用图标，系统启动器会使用比当前密度更高分辨率的图标
 *  · drawable 中的图标会随屏幕密度缩放，mipmap 不会被缩放优化
 *  · 规则：应用图标放 mipmap，其他图片资源放 drawable
 *
 *  自适应图标（Adaptive Icon，Android 8+）：
 *  · 由前景层（foreground）+ 背景层（background）组成
 *  · 系统可对图标应用不同形状（圆形、方形、圆角矩形等）
 *
 *  res/mipmap-anydpi-v26/ic_launcher.xml：
 *  <adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
 *      <background android:drawable="@drawable/ic_launcher_background"/>
 *      <foreground android:drawable="@drawable/ic_launcher_foreground"/>
 *  </adaptive-icon>
 *
 *
 * ── 7  资源限定符（Configuration Qualifiers）────────────────────────────
 *
 *  常用限定符（目录名后缀）：
 *  · 语言/地区：values-zh-rCN（简体中文）、values-en（英文）
 *  · 屏幕方向：layout-land（横屏）、layout-port（竖屏）
 *  · 屏幕尺寸：layout-sw600dp（最小宽度 600dp，平板）
 *  · 夜间模式：values-night（深色模式）
 *  · API 版本：values-v26（Android 8.0+）
 *  · 屏幕密度：drawable-xhdpi、drawable-xxhdpi
 *
 *  多语言国际化（i18n）：
 *  · res/values/strings.xml         → 默认（英文）
 *  · res/values-zh-rCN/strings.xml  → 简体中文
 *  · res/values-zh-rTW/strings.xml  → 繁体中文
 *  · 系统语言切换后，App 自动加载对应语言资源
 *
 *  平板适配示例：
 *  · res/layout/activity_main.xml          → 手机布局（单栏）
 *  · res/layout-sw600dp/activity_main.xml  → 平板布局（双栏）
 *
 *  资源匹配优先级（从高到低）：
 *  语言 > 布局方向 > 最小宽度 > 屏幕尺寸 > 屏幕方向 > 屏幕密度 > API 版本
 *
 *
 * ── 8  资源访问最佳实践 ──────────────────────────────────────────────────
 *
 *  避免硬编码：
 *  · 颜色、尺寸、字符串都不要硬编码在代码或布局中
 *  · 统一在 res/values/ 中定义，修改主题只需改一处
 *
 *  Compose 中的资源访问：
 *  // 字符串
 *  Text(stringResource(R.string.app_name))
 *  Text(stringResource(R.string.greeting, "Alice"))
 *
 *  // 颜色
 *  val color = colorResource(R.color.primary)
 *
 *  // 尺寸
 *  val spacing = dimensionResource(R.dimen.spacing_medium)
 *
 *  // Painter（图片）
 *  Image(painter = painterResource(R.drawable.ic_logo), contentDescription = null)
 *
 *  资源压缩（shrinkResources）：
 *  // app/build.gradle.kts
 *  buildTypes {
 *      release {
 *          isShrinkResources = true   // 移除未使用的资源
 *          isMinifyEnabled = true     // 代码混淆（配合使用）
 *      }
 *  }
 */

private val Green = Color(0xFF4CAF50)

private data class ResChapter(val num: String, val title: String)

private val chapters = listOf(
    ResChapter("1", "资源目录结构"),
    ResChapter("2", "strings.xml（字符串资源）"),
    ResChapter("3", "colors.xml & themes.xml（颜色与主题）"),
    ResChapter("4", "dimens.xml（尺寸资源）"),
    ResChapter("5", "drawable 资源"),
    ResChapter("6", "mipmap（应用图标）"),
    ResChapter("7", "资源限定符（Configuration Qualifiers）"),
    ResChapter("8", "资源访问最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceManagementScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("资源管理", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "strings · colors · drawable · 限定符",
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
                    containerColor = Green,
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
            items(chapters.size) { i -> ChapterRowRes(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowRes(chapter: ResChapter) {
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
                color = Green.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
