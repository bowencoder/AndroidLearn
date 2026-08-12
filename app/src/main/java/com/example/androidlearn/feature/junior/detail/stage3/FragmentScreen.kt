package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * Fragment 与弹窗笔记
 * 官方文档：https://developer.android.com/guide/fragments
 *           https://developer.android.com/develop/ui/views/components/dialogs
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  Fragment 是什么（对比 Activity / View）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  Fragment 是「可复用的 UI 模块」，依附在 Activity 上，有自己的布局和生命周期。
 *
 *  Fragment vs Activity
 *  ---------------------------------------------------------------
 *  维度          Activity                    Fragment
 *  ---------------------------------------------------------------
 *  定义          独立的屏幕单元              屏幕中的一块可复用区域
 *  生命周期      自己管理                    依附 Activity，受其控制
 *  启动方式      startActivity / Intent      FragmentManager 事务
 *  返回键        系统自动处理                需手动加入回退栈
 *  通信          Intent / Result API         setFragmentResult
 *  典型用途      整个页面（登录/详情）       Tab 页 / 侧边栏 / 对话框
 *  ---------------------------------------------------------------
 *
 *  Fragment vs View
 *  ---------------------------------------------------------------
 *  维度          View                        Fragment
 *  ---------------------------------------------------------------
 *  定义          单个 UI 控件                一组 View + 逻辑的组合
 *  生命周期      无                          有完整生命周期
 *  状态保存      需手动处理                  旋转屏幕自动恢复
 *  回退栈        不支持                      支持（addToBackStack）
 *  典型用途      Button / TextView / 自定义  页面级模块（首页/我的）
 *  ---------------------------------------------------------------
 *
 *  一句话总结：
 *  · View     = 控件（画 UI）
 *  · Fragment = 模块（管 UI + 逻辑，可复用，有生命周期）
 *  · Activity = 容器（承载 Fragment，管理页面跳转）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Fragment 生命周期
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  onAttach → onCreate → onCreateView → onViewCreated → onStart → onResume
 *           → onPause  → onStop → onDestroyView → onDestroy → onDetach
 *
 *  · onAttach       Fragment 与 Activity 关联，可获取 requireActivity()
 *  · onCreate       初始化非 UI 数据（arguments 读取、变量初始化），此时视图还未创建
 *  · onCreateView   inflate 布局并返回根 View，不要在这里操作 View（视图刚创建）
 *  · onViewCreated  视图创建完毕，适合绑定 ViewBinding、设置监听器、观察 LiveData
 *  · onStart        Fragment 对用户可见（但还未可交互）
 *  · onResume       Fragment 可见且可交互，开始播放动画/注册传感器等
 *  · onPause        Fragment 失去焦点（另一个 Fragment/Activity 覆盖），停止动画/释放资源
 *  · onStop         Fragment 完全不可见，停止耗时操作
 *  · onDestroyView  视图销毁，Fragment 实例仍存活（回退栈中），必须在此将 ViewBinding 置 null
 *  · onDestroy      Fragment 实例销毁，释放所有资源
 *  · onDetach       Fragment 与 Activity 解除关联
 *
 *  与 Activity 生命周期的联动：
 *  Activity.onStart  → Fragment.onStart
 *  Activity.onResume → Fragment.onResume
 *  Activity.onPause  → Fragment.onPause  （先于 Activity）
 *  Activity.onStop   → Fragment.onStop   （先于 Activity）
 *
 *  两个生命周期 Owner（重要）：
 *  · this（Fragment 本身）    从 onCreate 到 onDestroy
 *  · viewLifecycleOwner       从 onViewCreated 到 onDestroyView
 *  → 观察 LiveData 必须用 viewLifecycleOwner，否则 onDestroyView 后视图已销毁
 *    但回调仍会触发，导致空指针崩溃
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  FragmentManager 事务操作
 * ════════════════════════════════════════════════════════════════════════════
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
 * ════════════════════════════════════════════════════════════════════════════
 *  4  回退栈（Back Stack）
 * ════════════════════════════════════════════════════════════════════════════
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
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  Fragment 间通信
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // setFragmentResult（推荐，Fragment 1.3+）
 *  // 发送方（子 Fragment）
 *  setFragmentResult("requestKey", bundleOf("data" to "hello"))
 *
 *  // 接收方（父 Fragment 或 Activity）
 *  parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
 *      val result = bundle.getString("data")
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  ViewPager2 + Fragment
 * ════════════════════════════════════════════════════════════════════════════
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
 * ════════════════════════════════════════════════════════════════════════════
 *  7  AlertDialog
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  AlertDialog.Builder(context)
 *      .setTitle("确认删除")
 *      .setMessage("删除后无法恢复，是否继续？")
 *      .setPositiveButton("删除") { dialog, _ -> doDelete(); dialog.dismiss() }
 *      .setNegativeButton("取消", null)
 *      .setCancelable(true)
 *      .show()
 *
 *  // 列表选项
 *  AlertDialog.Builder(context)
 *      .setTitle("请选择")
 *      .setItems(arrayOf("选项A", "选项B")) { _, which -> handleSelect(which) }
 *      .show()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  8  DialogFragment（旋转屏幕不消失）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 用 Fragment 实现对话框，屏幕旋转后不消失（普通 AlertDialog 会消失）
 *  · 传参用 arguments（Bundle），不要用构造函数参数（旋转后会丢失）
 *  · 回传结果用 setFragmentResult，不要用接口回调（旋转后引用失效）
 *
 *  class ConfirmDialog : DialogFragment() {
 *      companion object {
 *          fun newInstance(message: String) = ConfirmDialog().apply {
 *              arguments = bundleOf("message" to message)
 *          }
 *      }
 *      override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
 *          val message = arguments?.getString("message") ?: ""
 *          return AlertDialog.Builder(requireContext())
 *              .setTitle("提示").setMessage(message)
 *              .setPositiveButton("确认") { _, _ ->
 *                  setFragmentResult("confirm", bundleOf("result" to true))
 *              }
 *              .setNegativeButton("取消", null).create()
 *      }
 *  }
 *
 *  // 显示
 *  ConfirmDialog.newInstance("确认删除？").show(supportFragmentManager, "confirm_dialog")
 *
 *  // 接收结果
 *  supportFragmentManager.setFragmentResultListener("confirm", this) { _, bundle ->
 *      if (bundle.getBoolean("result")) doDelete()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  9  BottomSheetDialogFragment（底部弹出）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  class ShareBottomSheet : BottomSheetDialogFragment() {
 *      override fun onCreateView(
 *          inflater: LayoutInflater,
 *          container: ViewGroup?,
 *          savedInstanceState: Bundle?
 *      ) = inflater.inflate(R.layout.bottom_sheet_share, container, false)
 *  }
 *
 *  ShareBottomSheet().show(supportFragmentManager, "share")
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  10  Toast & Snackbar
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show()
 *
 *  // Snackbar（推荐替代 Toast）：支持操作按钮，可撤销
 *  Snackbar.make(view, "已删除", Snackbar.LENGTH_LONG)
 *      .setAction("撤销") { undoDelete() }.show()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  11  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  Fragment：
 *  · 用 viewLifecycleOwner 而非 this 观察 LiveData，避免 onDestroyView 后回调
 *  · onDestroyView 中将 ViewBinding 置为 null，防止内存泄漏
 *  · Fragment 间通信优先用 setFragmentResult，避免接口回调（旋转后引用失效）
 *  · 避免在 Fragment 中持有 Activity 的强引用
 *
 *  弹窗：
 *  · 旋转屏幕不消失：用 DialogFragment / BottomSheetDialogFragment，而非直接 show()
 *  · 传参用 arguments（Bundle），不要用构造函数参数（旋转后会丢失）
 *  · 短暂提示优先用 Snackbar（可撤销），Toast 仅用于极简提示
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    // ── Fragment ──────────────────────────────────────────────
    NoteChapter("1",  "Fragment 是什么（对比 Activity / View）"),
    NoteChapter("2",  "Fragment 生命周期"),
    NoteChapter("3",  "FragmentManager 事务操作"),
    NoteChapter("4",  "回退栈（Back Stack）"),
    NoteChapter("5",  "Fragment 间通信"),
    NoteChapter("6",  "ViewPager2 + Fragment"),
    // ── 弹窗 ──────────────────────────────────────────────────
    NoteChapter("7",  "AlertDialog"),
    NoteChapter("8",  "DialogFragment（旋转屏幕不消失）"),
    NoteChapter("9",  "BottomSheetDialogFragment（底部弹出）"),
    NoteChapter("10", "Toast & Snackbar"),
    // ── 综合 ──────────────────────────────────────────────────
    NoteChapter("11", "最佳实践"),
)

@Composable
fun FragmentScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Fragment 与弹窗",
        subtitle = "生命周期 · 事务 · 回退栈 · AlertDialog · BottomSheet · Snackbar",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
