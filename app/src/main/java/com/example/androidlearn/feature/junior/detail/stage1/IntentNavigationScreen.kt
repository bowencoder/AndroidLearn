package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Intent 与页面跳转笔记
 * 官方文档：https://developer.android.com/guide/components/intents-filters
 *
 * ── 1  显式 Intent 与数据传递 ─────────────────────────────────────────────────
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
 * ── 2  隐式 Intent（常用系统 Action）────────────────────────────────────────
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
 * ── 3  ActivityResultLauncher（新 API）────────────────────────────────────────
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
 * ── 4  Intent Flags 与任务栈 ──────────────────────────────────────────────────
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
 * ── 5  Deep Link & Jetpack Navigation ────────────────────────────────────────
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
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1", "显式 Intent 与数据传递"),
    NoteChapter("2", "隐式 Intent（常用系统 Action）"),
    NoteChapter("3", "ActivityResultLauncher（新 API）"),
    NoteChapter("4", "Intent Flags 与任务栈"),
    NoteChapter("5", "Deep Link & Jetpack Navigation"),
)

@Composable
fun IntentNavigationScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Intent 与页面跳转",
        subtitle = "显式/隐式 · 数据传递 · Deep Link · Navigation",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
