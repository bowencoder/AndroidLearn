package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Android 应用架构入口笔记
 * 官方文档：https://developer.android.com/guide/components/activities/intro-activities
 *           https://developer.android.com/guide/navigation
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  Application
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Application 的作用 ───────────────────────────────────────────────────
 *
 *  · 应用进程启动时最先创建，全局唯一，生命周期与进程相同
 *  · 适合：全局初始化（日志、崩溃监控、网络库）、全局状态存储
 *
 *
 * ── 1.2  自定义 Application ───────────────────────────────────────────────────
 *
 *  // 1. 继承 Application
 *  class MyApp : Application() {
 *      override fun onCreate() {
 *          super.onCreate()
 *          // 全局初始化
 *          initCrashReporter()
 *          initNetworkClient()
 *          initImageLoader()
 *      }
 *
 *      override fun onLowMemory() {
 *          super.onLowMemory()
 *          // 释放缓存
 *          imageCache.clear()
 *      }
 *  }
 *
 *  // 2. AndroidManifest.xml 注册（必须）
 *  <application android:name=".MyApp" ...>
 *
 *  // 3. 获取 Application 实例
 *  val app = context.applicationContext as MyApp
 *
 *  // Hilt 项目：改用 @HiltAndroidApp
 *  @HiltAndroidApp
 *  class MyApp : Application()
 *
 *
 * ── 1.3  Application 生命周期 ─────────────────────────────────────────────────
 *
 *  进程启动 → Application.onCreate()
 *           → Activity/Service/BroadcastReceiver 创建
 *           → ...（正常运行）
 *           → 系统内存不足 → onLowMemory() / onTrimMemory(level)
 *           → 进程被杀死（无 onDestroy 回调）
 *
 *  · onTrimMemory(level)：比 onLowMemory 更细粒度，推荐使用
 *    - TRIM_MEMORY_UI_HIDDEN：App 进入后台，释放 UI 缓存
 *    - TRIM_MEMORY_RUNNING_LOW：系统内存紧张
 *    - TRIM_MEMORY_COMPLETE：进程即将被杀
 *
 *  override fun onTrimMemory(level: Int) {
 *      super.onTrimMemory(level)
 *      if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
 *          imageCache.trimToSize(imageCache.size() / 2)
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  导航容器
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  Activity 返回栈（Back Stack）────────────────────────────────────────
 *
 *  // 跳转并传参
 *  val intent = Intent(this, DetailActivity::class.java)
 *  intent.putExtra("id", 42)
 *  startActivity(intent)
 *
 *  // 返回并回传结果（新 API）：
 *  val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
 *      if (result.resultCode == Activity.RESULT_OK) {
 *          val data = result.data?.getStringExtra("result")
 *      }
 *  }
 *  launcher.launch(Intent(this, DetailActivity::class.java))
 *
 *
 * ── 2.2  Jetpack Navigation（推荐）────────────────────────────────────────────
 *
 *  · 单 Activity 多 Fragment/Composable 架构，统一管理导航
 *  · 支持返回栈、动画、深链接、参数传递
 *
 *  // 依赖：implementation("androidx.navigation:navigation-compose:2.7.7")
 *
 *  @Composable
 *  fun AppNavHost() {
 *      val navController = rememberNavController()
 *      NavHost(navController, startDestination = "home") {
 *          composable("home") {
 *              HomeScreen(onNavigateToDetail = { id ->
 *                  navController.navigate("detail/$id")
 *              })
 *          }
 *          composable(
 *              route = "detail/{id}",
 *              arguments = listOf(navArgument("id") { type = NavType.IntType })
 *          ) { backStackEntry ->
 *              val id = backStackEntry.arguments?.getInt("id") ?: 0
 *              DetailScreen(id = id, onBack = { navController.popBackStack() })
 *          }
 *      }
 *  }
 *
 *  // 导航操作
 *  navController.navigate("detail/42")          // push
 *  navController.popBackStack()                 // pop
 *  navController.navigate("home") {             // 清栈跳首页
 *      popUpTo("home") { inclusive = true }
 *  }
 *
 *
 * ── 2.3  Fragment 返回栈（View 体系）─────────────────────────────────────────
 *
 *  // Fragment 事务
 *  supportFragmentManager.commit {
 *      replace(R.id.container, DetailFragment.newInstance(id))
 *      addToBackStack("detail")   // 加入返回栈，按返回键可 pop
 *      setCustomAnimations(
 *          R.anim.slide_in_right, R.anim.slide_out_left,
 *          R.anim.slide_in_left,  R.anim.slide_out_right
 *      )
 *  }
 *
 *  // 手动 pop
 *  supportFragmentManager.popBackStack()
 *  supportFragmentManager.popBackStack("detail", FragmentManager.POP_BACK_STACK_INCLUSIVE)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  底部导航
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  Compose 底部导航（推荐）─────────────────────────────────────────────
 *
 *  sealed class BottomTab(val route: String, val label: String, val icon: ImageVector) {
 *      object Home    : BottomTab("home",    "首页",   Icons.Default.Home)
 *      object Search  : BottomTab("search",  "搜索",   Icons.Default.Search)
 *      object Profile : BottomTab("profile", "我的",   Icons.Default.Person)
 *  }
 *
 *  @Composable
 *  fun MainScreen() {
 *      val navController = rememberNavController()
 *      val currentRoute by navController.currentBackStackEntryAsState()
 *
 *      Scaffold(
 *          bottomBar = {
 *              NavigationBar {
 *                  tabs.forEach { tab ->
 *                      NavigationBarItem(
 *                          selected = currentRoute?.destination?.route == tab.route,
 *                          onClick = {
 *                              navController.navigate(tab.route) {
 *                                  popUpTo(navController.graph.startDestinationId) { saveState = true }
 *                                  launchSingleTop = true
 *                                  restoreState = true
 *                              }
 *                          },
 *                          icon = { Icon(tab.icon, contentDescription = tab.label) },
 *                          label = { Text(tab.label) }
 *                      )
 *                  }
 *              }
 *          }
 *      ) { padding ->
 *          NavHost(navController, startDestination = "home",
 *              modifier = Modifier.padding(padding)) {
 *              composable("home")    { HomeScreen() }
 *              composable("search")  { SearchScreen() }
 *              composable("profile") { ProfileScreen() }
 *          }
 *      }
 *  }
 *
 *
 * ── 3.2  Tab 状态保存（saveState / restoreState）─────────────────────────────
 *
 *  · 切换 Tab 时需要在 navigate 时显式配置 saveState / restoreState
 *
 *  navController.navigate(tab.route) {
 *      popUpTo(navController.graph.startDestinationId) {
 *          saveState = true      // 离开时保存当前 Tab 状态
 *      }
 *      launchSingleTop = true    // 避免重复创建同一目的地
 *      restoreState = true       // 切回时恢复之前的状态
 *  }
 *
 *
 * ── 3.3  View 体系底部导航（BottomNavigationView）────────────────────────────
 *
 *  // res/menu/bottom_nav_menu.xml
 *  <menu>
 *      <item android:id="@+id/nav_home"    android:title="首页"   android:icon="@drawable/ic_home"/>
 *      <item android:id="@+id/nav_search"  android:title="搜索"   android:icon="@drawable/ic_search"/>
 *      <item android:id="@+id/nav_profile" android:title="我的"   android:icon="@drawable/ic_person"/>
 *  </menu>
 *
 *  // Activity 中绑定
 *  val navController = findNavController(R.id.nav_host_fragment)
 *  binding.bottomNav.setupWithNavController(navController)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Application.onCreate() 只做必要的全局初始化，避免耗时操作（会延迟首屏）
 *  · 新项目推荐单 Activity + Compose Navigation，避免多 Activity 管理复杂度
 *  · 底部导航切换 Tab 时配置 saveState/restoreState，保留各 Tab 的导航状态
 *  · 使用 Hilt 注入依赖，不要在 Application 中存储全局单例
 *  · 深链接（Deep Link）在 NavHost 中统一配置，不要在 Activity 中手动解析
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    // ── 一级：Application ─────────────────────────────────
    NoteChapter("1",   "Application"),
    NoteChapter("1.1", "Application 的作用"),
    NoteChapter("1.2", "自定义 Application"),
    NoteChapter("1.3", "Application 生命周期"),
    // ── 一级：导航容器 ────────────────────────────────────
    NoteChapter("2",   "导航容器"),
    NoteChapter("2.1", "Activity 返回栈（Back Stack）"),
    NoteChapter("2.2", "Jetpack Navigation（推荐）"),
    NoteChapter("2.3", "Fragment 返回栈（View 体系）"),
    // ── 一级：底部导航 ────────────────────────────────────
    NoteChapter("3",   "底部导航"),
    NoteChapter("3.1", "Compose 底部导航（推荐）"),
    NoteChapter("3.2", "Tab 状态保存（saveState / restoreState）"),
    NoteChapter("3.3", "View 体系底部导航（BottomNavigationView）"),
    // ── 一级：最佳实践 ────────────────────────────────────
    NoteChapter("4",   "最佳实践"),
)

@Composable
fun AndroidArchScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Android 架构入口",
        subtitle = "Application · Navigation · 底部导航",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
