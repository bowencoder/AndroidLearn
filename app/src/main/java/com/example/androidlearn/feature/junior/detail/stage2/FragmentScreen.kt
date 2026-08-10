package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Fragment 笔记
 * 官方文档：https://developer.android.com/guide/fragments
 *
 * ── 1  Fragment 生命周期 ──────────────────────────────────────────────────────
 *
 *  onAttach → onCreate → onCreateView → onViewCreated → onStart → onResume
 *           → onPause → onStop → onDestroyView → onDestroy → onDetach
 *
 *  · onAttach：Fragment 与 Activity 关联，可获取 requireActivity()
 *  · onCreateView：创建并返回 Fragment 的视图（View）
 *  · onViewCreated：视图创建完毕，适合初始化 View 引用、设置监听器
 *  · onDestroyView：视图销毁（Fragment 仍存活），应在此清空 View 引用
 *  · onDetach：Fragment 与 Activity 解除关联
 *
 *  注意：Fragment 有两个生命周期 Owner：
 *  · this（Fragment 本身）：从 onCreate 到 onDestroy
 *  · viewLifecycleOwner：从 onCreateView 到 onDestroyView（推荐用于 Observer）
 *
 *
 * ── 2  FragmentManager 事务操作 ───────────────────────────────────────────────
 *
 *  // 添加 Fragment（不移除原有）
 *  supportFragmentManager.beginTransaction()
 *      .add(R.id.container, MyFragment(), "tag")
 *      .commit()
 *
 *  // 替换 Fragment（移除容器中已有的）
 *  supportFragmentManager.beginTransaction()
 *      .replace(R.id.container, MyFragment())
 *      .addToBackStack("name")   // 加入回退栈，按返回键可弹出
 *      .commit()
 *
 *  // 移除 Fragment
 *  val fragment = supportFragmentManager.findFragmentByTag("tag")
 *  fragment?.let {
 *      supportFragmentManager.beginTransaction().remove(it).commit()
 *  }
 *
 *  · commit()：异步提交，在下一帧执行
 *  · commitNow()：同步提交，不能加入回退栈
 *  · commitAllowingStateLoss()：允许状态丢失，避免 onSaveInstanceState 后崩溃
 *
 *
 * ── 3  回退栈（Back Stack）────────────────────────────────────────────────────
 *
 *  // 加入回退栈
 *  .addToBackStack(null)   // null 或自定义名称
 *
 *  // 弹出回退栈（回到上一个 Fragment）
 *  supportFragmentManager.popBackStack()
 *
 *  // 弹出到指定名称的事务
 *  supportFragmentManager.popBackStack("name", FragmentManager.POP_BACK_STACK_INCLUSIVE)
 *
 *  // 监听回退栈变化
 *  supportFragmentManager.addOnBackStackChangedListener {
 *      val count = supportFragmentManager.backStackEntryCount
 *  }
 *
 *
 * ── 4  Fragment 间通信 ────────────────────────────────────────────────────────
 *
 *  方式一：共享 ViewModel（推荐）
 *  // 两个 Fragment 共享同一个 ViewModel（以 Activity 为作用域）
 *  val viewModel: SharedViewModel by activityViewModels()
 *
 *  方式二：setFragmentResult（Jetpack，Fragment 1.3+）
 *  // 发送方（子 Fragment）
 *  setFragmentResult("requestKey", bundleOf("data" to "hello"))
 *
 *  // 接收方（父 Fragment 或 Activity）
 *  parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
 *      val result = bundle.getString("data")
 *  }
 *
 *  方式三：接口回调（传统方式，耦合度高，不推荐）
 *  interface OnDataPass { fun onDataPass(data: String) }
 *  // Fragment 中：(requireActivity() as OnDataPass).onDataPass("value")
 *
 *
 * ── 5  DialogFragment ─────────────────────────────────────────────────────────
 *
 *  · 用 Fragment 实现对话框，屏幕旋转后不消失（普通 AlertDialog 会消失）
 *
 *  class MyDialog : DialogFragment() {
 *      override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
 *          return AlertDialog.Builder(requireContext())
 *              .setTitle("提示")
 *              .setMessage("确认删除？")
 *              .setPositiveButton("确认") { _, _ -> /* 处理 */ }
 *              .setNegativeButton("取消", null)
 *              .create()
 *      }
 *  }
 *
 *  // 显示对话框
 *  MyDialog().show(supportFragmentManager, "myDialog")
 *
 *
 * ── 6  ViewPager2 + Fragment ──────────────────────────────────────────────────
 *
 *  // Adapter
 *  class MyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
 *      override fun getItemCount() = 3
 *      override fun createFragment(position: Int): Fragment = when (position) {
 *          0 -> HomeFragment()
 *          1 -> ProfileFragment()
 *          else -> SettingsFragment()
 *      }
 *  }
 *
 *  // 绑定 TabLayout
 *  viewPager.adapter = MyPagerAdapter(this)
 *  TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
 *      tab.text = listOf("首页", "我的", "设置")[pos]
 *  }.attach()
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 用 viewLifecycleOwner 而非 this 观察 LiveData，避免 onDestroyView 后回调
 *  · onDestroyView 中将 View Binding 置为 null，防止内存泄漏
 *  · 优先用 ViewModel 共享数据，而非接口回调
 *  · Compose 时代可用 NavHost 代替 FragmentManager，更简洁
 *  · 避免在 Fragment 中持有 Activity 的强引用
 *  · 使用 by viewModels() / by activityViewModels() 委托创建 ViewModel
 */

private val Blue = Color(0xFF2196F3)

private val chapters = listOf(
    NoteChapter("1", "Fragment 生命周期"),
    NoteChapter("2", "FragmentManager 事务操作"),
    NoteChapter("3", "回退栈（Back Stack）"),
    NoteChapter("4", "Fragment 间通信"),
    NoteChapter("5", "DialogFragment"),
    NoteChapter("6", "ViewPager2 + Fragment"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun FragmentScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Fragment",
        subtitle = "生命周期 · 事务 · 回退栈 · 通信",
        color = Blue,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
