package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Android 应用架构入口笔记（传统 View 体系）
 * 官方文档：https://developer.android.com/guide/components/activities/intro-activities
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  Application
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  Application 的作用 ───────────────────────────────────────────────────
 *
 *  · 应用进程启动时最先创建，全局唯一，生命周期与进程相同
 *  · 适合做：全局初始化（日志、崩溃监控、网络库）、全局 Context 持有
 *  · 不适合做：存储大量数据（进程被杀后丢失）、耗时初始化（会延迟首屏）
 *
 *
 * ── 1.2  自定义 Application ───────────────────────────────────────────────────
 *
 *  // 1. 继承 Application
 *  class MyApp : Application() {
 *      override fun onCreate() {
 *          super.onCreate()
 *          initCrashReporter()   // 崩溃监控
 *          initNetworkClient()   // 网络库
 *          initImageLoader()     // 图片库
 *      }
 *
 *      override fun onLowMemory() {
 *          super.onLowMemory()
 *          imageCache.clear()    // 释放缓存
 *      }
 *  }
 *
 *  // 2. AndroidManifest.xml 注册（必须）
 *  <application android:name=".MyApp" ...>
 *
 *  // 3. 获取 Application 实例
 *  val app = context.applicationContext as MyApp
 *
 *
 * ── 1.3  Application 生命周期 ─────────────────────────────────────────────────
 *
 *  进程启动 → Application.onCreate()
 *           → Activity / Service / BroadcastReceiver 创建
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
 *  2  Activity 导航
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  Activity 返回栈（Back Stack）────────────────────────────────────────
 *
 *  · 每次 startActivity 将新 Activity 压栈，按返回键弹栈
 *  · 启动模式（launchMode）控制压栈行为：
 *    - standard（默认）：每次都创建新实例
 *    - singleTop：栈顶已有则复用（onNewIntent），否则新建
 *    - singleTask：整个栈中只有一个实例，已有则弹出其上所有 Activity
 *    - singleInstance：独占一个任务栈
 *
 *  // 跳转并传参
 *  val intent = Intent(this, DetailActivity::class.java).apply {
 *      putExtra("id", 42)
 *      putExtra("title", "详情页")
 *  }
 *  startActivity(intent)
 *
 *  // 目标 Activity 接收参数
 *  val id = intent.getIntExtra("id", 0)
 *  val title = intent.getStringExtra("title") ?: ""
 *
 *  // 返回并回传结果（ActivityResultLauncher，替代已废弃的 startActivityForResult）
 *  val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
 *      if (result.resultCode == Activity.RESULT_OK) {
 *          val data = result.data?.getStringExtra("result")
 *      }
 *  }
 *  launcher.launch(Intent(this, EditActivity::class.java))
 *
 *  // 目标 Activity 回传结果
 *  val resultIntent = Intent().putExtra("result", "已保存")
 *  setResult(Activity.RESULT_OK, resultIntent)
 *  finish()
 *
 *
 * ── 2.2  常用 Intent Flag ─────────────────────────────────────────────────────
 *
 *  FLAG_ACTIVITY_NEW_TASK          在新任务栈中启动（从非 Activity Context 启动时必须加）
 *  FLAG_ACTIVITY_CLEAR_TOP         清除目标 Activity 之上的所有 Activity
 *  FLAG_ACTIVITY_SINGLE_TOP        等同于 launchMode=singleTop
 *  FLAG_ACTIVITY_NO_HISTORY        不加入返回栈（finish 后不可返回）
 *  FLAG_ACTIVITY_CLEAR_TASK        清空整个任务栈（配合 NEW_TASK 使用，常用于登出后跳首页）
 *
 *  // 登出后跳转到登录页，清空所有 Activity
 *  val intent = Intent(this, LoginActivity::class.java).apply {
 *      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
 *  }
 *  startActivity(intent)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Fragment 导航
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  Fragment 事务与返回栈 ────────────────────────────────────────────────
 *
 *  // 替换 Fragment（加入返回栈，按返回键可 pop）
 *  supportFragmentManager.commit {
 *      replace(R.id.container, DetailFragment.newInstance(id))
 *      addToBackStack("detail")
 *      setCustomAnimations(
 *          R.anim.slide_in_right, R.anim.slide_out_left,   // 进入/退出
 *          R.anim.slide_in_left,  R.anim.slide_out_right   // pop 进入/退出
 *      )
 *  }
 *
 *  // 手动 pop
 *  supportFragmentManager.popBackStack()
 *  supportFragmentManager.popBackStack("detail", FragmentManager.POP_BACK_STACK_INCLUSIVE)
 *
 *  // 传参（推荐用 arguments，不要用构造函数参数）
 *  companion object {
 *      fun newInstance(id: Int) = DetailFragment().apply {
 *          arguments = Bundle().apply { putInt("id", id) }
 *      }
 *  }
 *  // Fragment 内读取
 *  val id = arguments?.getInt("id") ?: 0
 *
 *
 * ── 3.2  Fragment 间通信 ──────────────────────────────────────────────────────
 *
 *  // 方式一：通过宿主 Activity 中转（接口回调）
 *  interface OnItemSelectedListener {
 *      fun onItemSelected(id: Int)
 *  }
 *  // Fragment 中
 *  (activity as? OnItemSelectedListener)?.onItemSelected(id)
 *
 *  // 方式二：FragmentResult API（推荐，Fragment 间解耦）
 *  // 发送方
 *  parentFragmentManager.setFragmentResult("requestKey", bundleOf("id" to 42))
 *  // 接收方
 *  parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
 *      val id = bundle.getInt("id")
 *  }
 *
 *  // 方式三：共享 ViewModel（同一 Activity 下的 Fragment 共享）
 *  val sharedVm: SharedViewModel by activityViewModels()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  底部导航（BottomNavigationView）
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 4.1  XML 布局 ─────────────────────────────────────────────────────────────
 *
 *  // res/menu/bottom_nav_menu.xml
 *  <menu xmlns:android="http://schemas.android.com/apk/res/android">
 *      <item android:id="@+id/nav_home"
 *            android:title="首页"
 *            android:icon="@drawable/ic_home"/>
 *      <item android:id="@+id/nav_search"
 *            android:title="搜索"
 *            android:icon="@drawable/ic_search"/>
 *      <item android:id="@+id/nav_profile"
 *            android:title="我的"
 *            android:icon="@drawable/ic_person"/>
 *  </menu>
 *
 *  // Activity 布局
 *  <LinearLayout android:orientation="vertical" ...>
 *      <FrameLayout
 *          android:id="@+id/container"
 *          android:layout_width="match_parent"
 *          android:layout_height="0dp"
 *          android:layout_weight="1"/>
 *      <com.google.android.material.bottomnavigation.BottomNavigationView
 *          android:id="@+id/bottomNav"
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content"
 *          app:menu="@menu/bottom_nav_menu"/>
 *  </LinearLayout>
 *
 *
 * ── 4.2  代码绑定 ─────────────────────────────────────────────────────────────
 *
 *  // 手动切换 Fragment
 *  binding.bottomNav.setOnItemSelectedListener { item ->
 *      val fragment = when (item.itemId) {
 *          R.id.nav_home    -> HomeFragment()
 *          R.id.nav_search  -> SearchFragment()
 *          R.id.nav_profile -> ProfileFragment()
 *          else -> return@setOnItemSelectedListener false
 *      }
 *      supportFragmentManager.commit {
 *          replace(R.id.container, fragment)
 *      }
 *      true
 *  }
 *  // 默认选中首页
 *  binding.bottomNav.selectedItemId = R.id.nav_home
 *
 *  // 配合 NavController 自动管理（需引入 navigation-fragment）
 *  val navController = findNavController(R.id.nav_host_fragment)
 *  binding.bottomNav.setupWithNavController(navController)
 *
 *
 * ── 4.3  Tab 状态保留 ─────────────────────────────────────────────────────────
 *
 *  · 手动切换 Fragment 时，用 show/hide 替代 replace，保留各 Tab 的状态
 *
 *  private val fragments = arrayOfNulls<Fragment>(3)
 *
 *  private fun switchTab(index: Int) {
 *      val fm = supportFragmentManager
 *      fm.commit {
 *          fragments.forEachIndexed { i, f ->
 *              if (f != null) { if (i == index) show(f) else hide(f) }
 *          }
 *          if (fragments[index] == null) {
 *              val newFrag = when (index) {
 *                  0 -> HomeFragment()
 *                  1 -> SearchFragment()
 *                  else -> ProfileFragment()
 *              }
 *              fragments[index] = newFrag
 *              add(R.id.container, newFrag)
 *          }
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  完整 App 框架（Toolbar + BottomNavigationView）
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 5.1  布局文件（activity_main.xml）────────────────────────────────────────
 *
 *  <!-- 整体结构：LinearLayout 垂直排列，Toolbar 顶部，FrameLayout 内容区（weight=1），BottomNavigationView 底部 -->
 *  <LinearLayout
 *      xmlns:android="http://schemas.android.com/apk/res/android"
 *      xmlns:app="http://schemas.android.com/apk/res-auto"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent"
 *      android:orientation="vertical">
 *
 *      <!-- 顶部：Toolbar -->
 *      <androidx.appcompat.widget.Toolbar
 *          android:id="@+id/toolbar"
 *          android:layout_width="match_parent"
 *          android:layout_height="?attr/actionBarSize"
 *          android:background="?attr/colorPrimary"
 *          android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
 *          app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
 *          app:title="首页"/>
 *
 *      <!-- 内容区：Fragment 容器，weight=1 撑满剩余空间 -->
 *      <FrameLayout
 *          android:id="@+id/container"
 *          android:layout_width="match_parent"
 *          android:layout_height="0dp"
 *          android:layout_weight="1"/>
 *
 *      <!-- 底部：BottomNavigationView -->
 *      <com.google.android.material.bottomnavigation.BottomNavigationView
 *          android:id="@+id/bottomNav"
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content"
 *          app:menu="@menu/bottom_nav_menu"/>
 *
 *  </LinearLayout>
 *
 *
 * ── 5.2  布局与 Activity 的映射关系 ──────────────────────────────────────────
 *
 *  · 布局文件命名规则：activity_xxx.xml → 对应 XxxActivity
 *    - activity_main.xml  ←→  MainActivity
 *    - activity_detail.xml ←→ DetailActivity
 *    - fragment_home.xml  ←→  HomeFragment（Fragment 用 fragment_ 前缀）
 *
 *  · 映射方式一：setContentView（传统方式）
 *    override fun onCreate(...) {
 *        super.onCreate(savedInstanceState)
 *        setContentView(R.layout.activity_main)   // 直接传布局资源 ID
 *        val toolbar = findViewById<Toolbar>(R.id.toolbar)  // 再 findViewById 获取 View
 *    }
 *
 *  · 映射方式二：ViewBinding（推荐）
 *    // 编译期根据 activity_main.xml 自动生成 ActivityMainBinding 类
 *    // 命名规则：文件名去掉下划线、每段首字母大写 + "Binding"
 *    //   activity_main.xml → ActivityMainBinding
 *    //   fragment_home.xml → FragmentHomeBinding
 *    override fun onCreate(...) {
 *        super.onCreate(savedInstanceState)
 *        binding = ActivityMainBinding.inflate(layoutInflater)  // inflate 生成 View 树
 *        setContentView(binding.root)   // binding.root 就是布局的根 View（LinearLayout）
 *        // 之后直接用 binding.toolbar、binding.container、binding.bottomNav 访问子 View
 *    }
 *
 *  · AndroidManifest.xml 中注册 Activity（必须，否则无法启动）
 *    <activity android:name=".MainActivity"
 *              android:exported="true">
 *        <intent-filter>
 *            <action android:name="android.intent.action.MAIN"/>
 *            <category android:name="android.intent.category.LAUNCHER"/>  <!-- 入口 Activity -->
 *        </intent-filter>
 *    </activity>
 *
 *
 * ── 5.3  MainActivity 骨架代码 ────────────────────────────────────────────────
 *
 *  class MainActivity : AppCompatActivity() {
 *
 *      private lateinit var binding: ActivityMainBinding
 *
 *      // 缓存各 Tab 的 Fragment 实例，避免重建
 *      private val tabFragments = arrayOfNulls<Fragment>(3)
 *      private var currentTabIndex = 0
 *
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          binding = ActivityMainBinding.inflate(layoutInflater)
 *          setContentView(binding.root)
 *
 *          setupToolbar()
 *          setupBottomNav()
 *
 *          // 恢复状态或默认显示首页
 *          if (savedInstanceState == null) switchTab(0)
 *      }
 *
 *      private fun setupToolbar() {
 *          setSupportActionBar(binding.toolbar)
 *          // 支持返回按钮（子页面用）
 *          supportActionBar?.setDisplayHomeAsUpEnabled(false)
 *      }
 *
 *      private fun setupBottomNav() {
 *          binding.bottomNav.setOnItemSelectedListener { item ->
 *              val index = when (item.itemId) {
 *                  R.id.nav_home    -> 0
 *                  R.id.nav_search  -> 1
 *                  R.id.nav_profile -> 2
 *                  else -> return@setOnItemSelectedListener false
 *              }
 *              switchTab(index)
 *              true
 *          }
 *      }
 *
 *      // 用 show/hide 切换 Tab，保留各 Tab 状态
 *      private fun switchTab(index: Int) {
 *          if (index == currentTabIndex && tabFragments[index] != null) return
 *          currentTabIndex = index
 *
 *          supportFragmentManager.commit {
 *              // 隐藏所有已存在的 Fragment
 *              tabFragments.filterNotNull().forEach { hide(it) }
 *              // 显示或创建目标 Fragment
 *              if (tabFragments[index] == null) {
 *                  val newFrag = when (index) {
 *                      0    -> HomeFragment()
 *                      1    -> SearchFragment()
 *                      else -> ProfileFragment()
 *                  }
 *                  tabFragments[index] = newFrag
 *                  add(R.id.container, newFrag)
 *              } else {
 *                  show(tabFragments[index]!!)
 *              }
 *          }
 *
 *          // 同步更新 Toolbar 标题
 *          val title = when (index) {
 *              0    -> "首页"
 *              1    -> "搜索"
 *              else -> "我的"
 *          }
 *          supportActionBar?.title = title
 *      }
 *
 *      // 子页面（二级页面）压栈时显示返回按钮
 *      fun pushFragment(fragment: Fragment, title: String) {
 *          supportFragmentManager.commit {
 *              replace(R.id.container, fragment)
 *              addToBackStack(null)
 *          }
 *          supportActionBar?.title = title
 *          supportActionBar?.setDisplayHomeAsUpEnabled(true)
 *      }
 *
 *      override fun onOptionsItemSelected(item: MenuItem): Boolean {
 *          if (item.itemId == android.R.id.home) {
 *              onBackPressedDispatcher.onBackPressed()
 *              return true
 *          }
 *          return super.onOptionsItemSelected(item)
 *      }
 *
 *      // 返回栈变化时同步 Toolbar 返回按钮状态
 *      override fun onBackPressed() {
 *          if (supportFragmentManager.backStackEntryCount > 0) {
 *              supportFragmentManager.popBackStack()
 *              val hasBack = supportFragmentManager.backStackEntryCount > 1
 *              supportActionBar?.setDisplayHomeAsUpEnabled(hasBack)
 *              if (!hasBack) {
 *                  // 恢复当前 Tab 标题
 *                  supportActionBar?.title = when (currentTabIndex) {
 *                      0 -> "首页"; 1 -> "搜索"; else -> "我的"
 *                  }
 *              }
 *          } else {
 *              super.onBackPressed()
 *          }
 *      }
 *  }
 *
 *
 * ── 5.4  各 Tab Fragment 骨架 ─────────────────────────────────────────────────
 *
 *  // 每个 Tab 对应一个 Fragment，内部可再嵌套子 Fragment 或跳转二级页面
 *  class HomeFragment : Fragment(R.layout.fragment_home) {
 *
 *      private var _binding: FragmentHomeBinding? = null
 *      private val binding get() = _binding!!
 *
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          super.onViewCreated(view, savedInstanceState)
 *          _binding = FragmentHomeBinding.bind(view)
 *
 *          binding.btnDetail.setOnClickListener {
 *              // 跳转二级页面（通过宿主 Activity）
 *              (activity as? MainActivity)?.pushFragment(DetailFragment(), "详情")
 *          }
 *      }
 *
 *      override fun onDestroyView() {
 *          super.onDestroyView()
 *          _binding = null
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Application.onCreate() 只做必要初始化，耗时操作放到子线程或懒加载
 *  · Activity 间传参用 Intent.putExtra，复杂对象实现 Parcelable（比 Serializable 快）
 *  · Fragment 传参用 arguments Bundle，不要用构造函数参数（系统重建时会丢失）
 *  · Tab 切换用 show/hide 而非 replace，避免 Fragment 重建导致状态丢失
 *  · Fragment 间通信优先用 FragmentResult API，避免直接持有对方引用
 *  · 二级页面压栈时同步更新 Toolbar 标题和返回按钮，返回时恢复 Tab 标题
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1",   "Application"),
    NoteChapter("1.1", "Application 的作用与适用场景"),
    NoteChapter("1.2", "自定义 Application：onCreate / onLowMemory"),
    NoteChapter("1.3", "Application 生命周期：onTrimMemory"),
    NoteChapter("2",   "Activity 导航"),
    NoteChapter("2.1", "Activity 返回栈：startActivity / ActivityResultLauncher"),
    NoteChapter("2.2", "常用 Intent Flag：CLEAR_TOP / CLEAR_TASK / NEW_TASK"),
    NoteChapter("3",   "Fragment 导航"),
    NoteChapter("3.1", "Fragment 事务与返回栈：replace / addToBackStack / pop"),
    NoteChapter("3.2", "Fragment 间通信：接口回调 / FragmentResult / 共享 ViewModel"),
    NoteChapter("4",   "底部导航（BottomNavigationView）"),
    NoteChapter("4.1", "XML 布局：menu / BottomNavigationView"),
    NoteChapter("4.2", "代码绑定：setOnItemSelectedListener / setupWithNavController"),
    NoteChapter("4.3", "Tab 状态保留：show/hide 替代 replace"),
    NoteChapter("5",   "完整 App 框架（Toolbar + BottomNavigationView）"),
    NoteChapter("5.1", "布局文件：LinearLayout / Toolbar / FrameLayout(weight=1) / BottomNavigationView"),
    NoteChapter("5.2", "布局与 Activity 的映射：setContentView / ViewBinding / Manifest 注册"),
    NoteChapter("5.3", "MainActivity 骨架：setupToolbar / switchTab / pushFragment"),
    NoteChapter("5.4", "各 Tab Fragment 骨架：show/hide 保留状态 / 二级页面跳转"),
    NoteChapter("6",   "最佳实践"),
)

@Composable
fun AndroidArchScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Android 架构入口",
        subtitle = "Application · Activity 导航 · Fragment 导航 · 底部导航 · 完整框架",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
