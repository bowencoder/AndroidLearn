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
 * Intent 与页面跳转笔记
 * 官方文档：https://developer.android.com/guide/components/intents-filters
 *
 * ── 1  Intent 基础概念 ────────────────────────────────────────────────────────
 *
 *  Intent 是什么：
 *  · Android 组件间通信的消息对象，可以启动 Activity、Service、发送广播
 *  · 携带操作描述（Action）、数据（Data/Extras）、目标组件信息
 *
 *  两种类型：
 *  · 显式 Intent（Explicit）：明确指定目标组件类名，用于应用内跳转
 *  · 隐式 Intent（Implicit）：只声明 Action/Category/Data，由系统匹配合适组件
 *
 *  Intent 的组成：
 *  · Component：目标组件（显式 Intent 必填）
 *  · Action：要执行的操作，如 ACTION_VIEW / ACTION_SEND
 *  · Data：操作的 URI 数据，如 tel:10086 / https://example.com
 *  · Category：附加信息，如 CATEGORY_LAUNCHER / CATEGORY_BROWSABLE
 *  · Extras：键值对附加数据（Bundle）
 *  · Flags：控制 Activity 启动行为和任务栈
 *
 *  // 显式 Intent
 *  val intent = Intent(this, DetailActivity::class.java)
 *  startActivity(intent)
 *
 *  // 隐式 Intent
 *  val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"))
 *  startActivity(intent)
 *
 *
 * ── 2  显式 Intent 与数据传递 ─────────────────────────────────────────────────
 *
 *  基本跳转：
 *  val intent = Intent(this, DetailActivity::class.java)
 *  startActivity(intent)
 *
 *  传递基本类型数据（putExtra）：
 *  intent.putExtra("userId", 42)
 *  intent.putExtra("userName", "Alice")
 *  intent.putExtra("isVip", true)
 *  intent.putExtra("score", 99.5f)
 *
 *  接收数据（在目标 Activity 中）：
 *  val userId = intent.getIntExtra("userId", -1)       // 第二个参数是默认值
 *  val userName = intent.getStringExtra("userName")
 *  val isVip = intent.getBooleanExtra("isVip", false)
 *
 *  传递 Bundle（批量传递）：
 *  val bundle = Bundle().apply {
 *      putInt("userId", 42)
 *      putString("userName", "Alice")
 *  }
 *  intent.putExtras(bundle)
 *
 *  传递 Parcelable 对象（推荐，性能优于 Serializable）：
 *  // 数据类实现 Parcelable（Kotlin 用 @Parcelize 注解自动生成）
 *  @Parcelize
 *  data class User(val id: Int, val name: String) : Parcelable
 *
 *  intent.putExtra("user", User(1, "Alice"))
 *  val user = intent.getParcelableExtra<User>("user")  // 接收
 *
 *
 * ── 3  隐式 Intent ────────────────────────────────────────────────────────────
 *
 *  常用系统 Action：
 *  // 打开网页
 *  Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"))
 *
 *  // 拨打电话（需 CALL_PHONE 权限）
 *  Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"))
 *
 *  // 打开拨号盘（无需权限）
 *  Intent(Intent.ACTION_DIAL, Uri.parse("tel:10086"))
 *
 *  // 发送短信
 *  Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:10086")).apply {
 *      putExtra("sms_body", "短信内容")
 *  }
 *
 *  // 分享文本
 *  Intent(Intent.ACTION_SEND).apply {
 *      type = "text/plain"
 *      putExtra(Intent.EXTRA_TEXT, "分享内容")
 *  }
 *
 *  // 选择图片
 *  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
 *
 *  安全检查（Android 11+ 需要在 Manifest 声明 queries 块）：
 *  // 发送前检查是否有应用可处理
 *  if (intent.resolveActivity(packageManager) != null) {
 *      startActivity(intent)
 *  }
 *
 *  // AndroidManifest.xml 中声明查询意图（queries 块）
 *  // queries > intent > action(VIEW) + data(scheme=https)
 *
 *
 * ── 4  Intent Filter（接收隐式 Intent）────────────────────────────────────────
 *
 *  在 AndroidManifest.xml 中声明：
 *  // activity(.ShareActivity) > intent-filter:
 *  //   action(SEND) + category(DEFAULT) + data(mimeType=text/plain)
 *
 *  匹配规则（三者都要满足）：
 *  · Action：Intent 的 action 必须与 filter 中某个 action 匹配
 *  · Category：Intent 的所有 category 都必须在 filter 中声明
 *    （隐式 Intent 默认带 CATEGORY_DEFAULT，filter 必须声明它）
 *  · Data：URI scheme/host/path 和 MIME type 都要匹配
 *
 *  在 Activity 中接收分享内容：
 *  if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
 *      val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
 *  }
 *
 *
 * ── 5  ActivityResultLauncher（新 API）────────────────────────────────────────
 *
 *  背景：
 *  · startActivityForResult + onActivityResult 已废弃
 *  · 新 API：registerForActivityResult，类型安全，可在任意位置注册
 *
 *  基本用法：
 *  // 1. 注册（在 onCreate 之前，或作为成员变量）
 *  val launcher = registerForActivityResult(
 *      ActivityResultContracts.StartActivityForResult()
 *  ) { result ->
 *      if (result.resultCode == Activity.RESULT_OK) {
 *          val data = result.data?.getStringExtra("key")
 *          // 处理返回数据
 *      }
 *  }
 *
 *  // 2. 启动
 *  val intent = Intent(this, PickerActivity::class.java)
 *  launcher.launch(intent)
 *
 *  // 3. 在目标 Activity 中返回结果
 *  val resultIntent = Intent().putExtra("key", "value")
 *  setResult(Activity.RESULT_OK, resultIntent)
 *  finish()
 *
 *  内置 Contract（无需手写 Intent）：
 *  // 选择图片（MIME 类型传 "image/" + "*"，即 image 通配符）
 *  val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
 *      uri?.let { imageView.setImageURI(it) }
 *  }
 *  pickImage.launch("image/jpeg")  // 或 "image/png"，通配写法见注释
 *
 *  // 拍照
 *  val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
 *      if (success) { }  // success=true 时照片已写入 photoUri
 *  }
 *  takePicture.launch(photoUri)
 *
 *  // 请求权限
 *  val requestPermission = registerForActivityResult(
 *      ActivityResultContracts.RequestPermission()
 *  ) { granted -> if (granted) { } }  // 权限已授予，执行后续逻辑
 *  requestPermission.launch(Manifest.permission.CAMERA)
 *
 *
 * ── 6  Intent Flags 与任务栈 ──────────────────────────────────────────────────
 *
 *  任务栈（Task Back Stack）：
 *  · Activity 按启动顺序压栈，返回键弹出栈顶
 *  · 同一 Task 内的 Activity 共享同一个任务栈
 *
 *  常用 Flags：
 *  · FLAG_ACTIVITY_NEW_TASK：在新任务中启动（从非 Activity 上下文启动必须加）
 *  · FLAG_ACTIVITY_CLEAR_TOP：如果目标 Activity 已在栈中，清除其上方所有 Activity
 *  · FLAG_ACTIVITY_SINGLE_TOP：如果目标 Activity 已在栈顶，不新建实例（调用 onNewIntent）
 *  · FLAG_ACTIVITY_NO_HISTORY：Activity 不保留在栈中（finish 后不可返回）
 *  · FLAG_ACTIVITY_CLEAR_TASK：清空整个任务栈（配合 NEW_TASK 使用，常用于登出后跳首页）
 *
 *  // 登出后跳转到首页，清空所有 Activity
 *  val intent = Intent(this, MainActivity::class.java).apply {
 *      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
 *  }
 *  startActivity(intent)
 *
 *  // 回到已有的 Activity 并清除其上方的页面
 *  val intent = Intent(this, HomeActivity::class.java).apply {
 *      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
 *  }
 *  startActivity(intent)
 *
 *
 * ── 7  Deep Link（深链接）────────────────────────────────────────────────────
 *
 *  作用：
 *  · 通过 URI 从外部（浏览器、通知、其他 App）直接打开应用特定页面
 *  · 两种形式：自定义 Scheme（myapp://detail/42）和 App Links（https://example.com/detail/42）
 *
 *  配置 AndroidManifest.xml（activity > intent-filter autoVerify=true）：
 *  //   action(VIEW) + category(DEFAULT) + category(BROWSABLE)
 *  //   data(scheme=myapp, host=detail)          ← 自定义 Scheme
 *  //   data(scheme=https, host=example.com, pathPrefix=/detail)  ← App Links
 *
 *  在 Activity 中处理 Deep Link：
 *  override fun onCreate(savedInstanceState: Bundle?) {
 *      super.onCreate(savedInstanceState)
 *      val uri = intent.data
 *      if (uri != null) {
 *          val id = uri.lastPathSegment          // 获取路径最后一段
 *          val param = uri.getQueryParameter("tab")  // 获取查询参数
 *      }
 *  }
 *
 *  App Links 验证：
 *  · 需要在服务器 /.well-known/assetlinks.json 放置验证文件
 *  · 验证通过后，点击链接直接打开 App，不弹选择框
 *
 *  // ADB 测试 Deep Link
 *  adb shell am start -W -a android.intent.action.VIEW \
 *      -d "myapp://detail/42" com.example.app
 *
 *
 * ── 8  Jetpack Navigation 中的跳转 ───────────────────────────────────────────
 *
 *  与传统 Intent 的区别：
 *  · Navigation Component 管理 Fragment/Composable 间的跳转，不依赖 Intent
 *  · 类型安全的参数传递（Safe Args / NavType）
 *  · 统一处理返回栈、动画、Deep Link
 *
 *  Compose Navigation 基本用法：
 *  // 定义路由
 *  NavHost(navController, startDestination = "home") {
 *      composable("home") { HomeScreen(navController) }
 *      composable("detail/{id}") { backStackEntry ->
 *          val id = backStackEntry.arguments?.getString("id")
 *          DetailScreen(id)
 *      }
 *  }
 *
 *  // 跳转并传参
 *  navController.navigate("detail/42")
 *
 *  // 返回并传递结果（通过 SavedStateHandle）
 *  // 在目标页面
 *  navController.previousBackStackEntry
 *      ?.savedStateHandle?.set("result", "value")
 *  navController.popBackStack()
 *
 *  // 在来源页面观察结果
 *  val result = navController.currentBackStackEntry
 *      ?.savedStateHandle?.getLiveData<String>("result")
 *
 *  Navigation Deep Link（Compose）：
 *  composable(
 *      "detail/{id}",
 *      deepLinks = listOf(navDeepLink { uriPattern = "myapp://detail/{id}" })
 *  ) { ... }
 */

private val Green = Color(0xFF4CAF50)

private data class IntentChapter(val num: String, val title: String)

private val chapters = listOf(
    IntentChapter("1", "Intent 基础概念"),
    IntentChapter("2", "显式 Intent 与数据传递"),
    IntentChapter("3", "隐式 Intent"),
    IntentChapter("4", "Intent Filter（接收隐式 Intent）"),
    IntentChapter("5", "ActivityResultLauncher（新 API）"),
    IntentChapter("6", "Intent Flags 与任务栈"),
    IntentChapter("7", "Deep Link（深链接）"),
    IntentChapter("8", "Jetpack Navigation 中的跳转"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentNavigationScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Intent 与页面跳转", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "显式/隐式 · 数据传递 · Deep Link · Navigation",
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
            items(chapters.size) { i -> ChapterRowIntent(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowIntent(chapter: IntentChapter) {
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
