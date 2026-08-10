package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 弹窗（Dialog）笔记
 * 官方文档：https://developer.android.com/develop/ui/views/components/dialogs
 *
 * ── 1  AlertDialog（最常用）──────────────────────────────────────────────────
 *
 *  · 系统风格对话框，支持标题、内容、最多三个按钮（Positive/Negative/Neutral）
 *
 *  // 基础用法
 *  AlertDialog.Builder(context)
 *      .setTitle("确认删除")
 *      .setMessage("删除后无法恢复，是否继续？")
 *      .setPositiveButton("删除") { dialog, _ ->
 *          doDelete()
 *          dialog.dismiss()
 *      }
 *      .setNegativeButton("取消", null)
 *      .setNeutralButton("稍后再说") { _, _ -> /* 中立操作 */ }
 *      .setCancelable(true)   // 点击外部可关闭，默认 true
 *      .show()
 *
 *  // 列表选项
 *  val items = arrayOf("选项A", "选项B", "选项C")
 *  AlertDialog.Builder(context)
 *      .setTitle("请选择")
 *      .setItems(items) { _, which -> handleSelect(which) }
 *      .show()
 *
 *  // 单选列表
 *  AlertDialog.Builder(context)
 *      .setSingleChoiceItems(items, checkedIndex) { _, which -> selectedIndex = which }
 *      .setPositiveButton("确认") { _, _ -> handleConfirm(selectedIndex) }
 *      .show()
 *
 *  // 多选列表
 *  val checked = booleanArrayOf(true, false, true)
 *  AlertDialog.Builder(context)
 *      .setMultiChoiceItems(items, checked) { _, which, isChecked -> checked[which] = isChecked }
 *      .setPositiveButton("确认") { _, _ -> handleMultiSelect(checked) }
 *      .show()
 *
 *
 * ── 2  Material3 AlertDialog（Compose）───────────────────────────────────────
 *
 *  var showDialog by remember { mutableStateOf(false) }
 *
 *  if (showDialog) {
 *      AlertDialog(
 *          onDismissRequest = { showDialog = false },
 *          title = { Text("确认删除") },
 *          text = { Text("删除后无法恢复，是否继续？") },
 *          confirmButton = {
 *              TextButton(onClick = { doDelete(); showDialog = false }) {
 *                  Text("删除", color = MaterialTheme.colorScheme.error)
 *              }
 *          },
 *          dismissButton = {
 *              TextButton(onClick = { showDialog = false }) { Text("取消") }
 *          }
 *      )
 *  }
 *
 *  // 自定义内容的 Dialog（Compose）
 *  Dialog(onDismissRequest = { showDialog = false }) {
 *      Card(shape = RoundedCornerShape(16.dp)) {
 *          Column(modifier = Modifier.padding(24.dp)) {
 *              Text("自定义标题", style = MaterialTheme.typography.titleLarge)
 *              Spacer(Modifier.height(16.dp))
 *              // 任意自定义内容
 *              TextField(value = input, onValueChange = { input = it })
 *              Spacer(Modifier.height(16.dp))
 *              Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
 *                  TextButton(onClick = { showDialog = false }) { Text("取消") }
 *                  Button(onClick = { handleConfirm(input); showDialog = false }) { Text("确认") }
 *              }
 *          }
 *      }
 *  }
 *
 *
 * ── 3  DialogFragment（旋转屏幕不消失）────────────────────────────────────────
 *
 *  · 用 Fragment 实现对话框，配置变更（旋转）后不消失，推荐替代直接 show AlertDialog
 *
 *  class ConfirmDialog : DialogFragment() {
 *
 *      // 通过 companion object 传参，避免直接构造函数传参
 *      companion object {
 *          fun newInstance(message: String) = ConfirmDialog().apply {
 *              arguments = bundleOf("message" to message)
 *          }
 *      }
 *
 *      override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
 *          val message = arguments?.getString("message") ?: ""
 *          return AlertDialog.Builder(requireContext())
 *              .setTitle("提示")
 *              .setMessage(message)
 *              .setPositiveButton("确认") { _, _ ->
 *                  // 通过 setFragmentResult 回传结果
 *                  setFragmentResult("confirm", bundleOf("result" to true))
 *              }
 *              .setNegativeButton("取消", null)
 *              .create()
 *      }
 *  }
 *
 *  // 显示
 *  ConfirmDialog.newInstance("确认删除？")
 *      .show(supportFragmentManager, "confirm_dialog")
 *
 *  // 接收结果
 *  supportFragmentManager.setFragmentResultListener("confirm", this) { _, bundle ->
 *      if (bundle.getBoolean("result")) doDelete()
 *  }
 *
 *
 * ── 4  BottomSheetDialog（底部弹出）──────────────────────────────────────────
 *
 *  · 从底部滑出的对话框，适合操作菜单、筛选条件、分享面板
 *
 *  // View 体系
 *  val bottomSheet = BottomSheetDialog(context)
 *  val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
 *  bottomSheet.setContentView(view)
 *  bottomSheet.show()
 *
 *  // BottomSheetDialogFragment（推荐，旋转不消失）
 *  class ShareBottomSheet : BottomSheetDialogFragment() {
 *      override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
 *          inflater.inflate(R.layout.bottom_sheet_share, container, false)
 *  }
 *  ShareBottomSheet().show(supportFragmentManager, "share")
 *
 *  // Compose：ModalBottomSheet（Material3）
 *  var showSheet by remember { mutableStateOf(false) }
 *  if (showSheet) {
 *      ModalBottomSheet(onDismissRequest = { showSheet = false }) {
 *          Column(modifier = Modifier.padding(16.dp)) {
 *              Text("分享到", style = MaterialTheme.typography.titleMedium)
 *              // 分享选项列表...
 *          }
 *      }
 *  }
 *
 *
 * ── 5  PopupWindow（悬浮弹出）────────────────────────────────────────────────
 *
 *  · 可定位到任意位置的浮层，适合气泡提示、下拉菜单
 *
 *  val popup = PopupWindow(context).apply {
 *      contentView = layoutInflater.inflate(R.layout.popup_layout, null)
 *      width = ViewGroup.LayoutParams.WRAP_CONTENT
 *      height = ViewGroup.LayoutParams.WRAP_CONTENT
 *      isOutsideTouchable = true   // 点击外部关闭
 *      isFocusable = true
 *      setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
 *  }
 *
 *  // 显示在 anchorView 下方
 *  popup.showAsDropDown(anchorView, 0, 8)
 *  // 显示在屏幕指定位置
 *  popup.showAtLocation(rootView, Gravity.CENTER, 0, 0)
 *
 *  // Compose 中用 Popup
 *  Popup(alignment = Alignment.TopCenter) {
 *      Card { Text("气泡提示", modifier = Modifier.padding(8.dp)) }
 *  }
 *
 *
 * ── 6  Toast & Snackbar ───────────────────────────────────────────────────────
 *
 *  // Toast：短暂提示，不可交互（Android 11+ 自定义 Toast 已废弃）
 *  Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show()
 *
 *  // Snackbar（推荐替代 Toast）：支持操作按钮，可撤销
 *  Snackbar.make(view, "已删除", Snackbar.LENGTH_LONG)
 *      .setAction("撤销") { undoDelete() }
 *      .show()
 *
 *  // Compose 中的 Snackbar
 *  val snackbarHostState = remember { SnackbarHostState() }
 *  Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { ... }
 *
 *  // 触发
 *  scope.launch {
 *      val result = snackbarHostState.showSnackbar(
 *          message = "已删除",
 *          actionLabel = "撤销",
 *          duration = SnackbarDuration.Long
 *      )
 *      if (result == SnackbarResult.ActionPerformed) undoDelete()
 *  }
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 旋转屏幕不消失：用 DialogFragment / BottomSheetDialogFragment，而非直接 show()
 *  · 传参用 arguments（Bundle），不要用构造函数参数（旋转后会丢失）
 *  · 回传结果用 setFragmentResult，不要用接口回调（旋转后引用失效）
 *  · Compose 中用状态控制弹窗显示，避免命令式 show/dismiss
 *  · 短暂提示优先用 Snackbar（可撤销），Toast 仅用于极简提示
 *  · 不要在 Dialog 中做耗时操作，应先 dismiss 再异步处理
 *  · 避免弹窗嵌套弹窗，影响用户体验
 */

private val Blue = Color(0xFF2196F3)

private val chapters = listOf(
    NoteChapter("1", "AlertDialog（最常用）"),
    NoteChapter("2", "Material3 AlertDialog（Compose）"),
    NoteChapter("3", "DialogFragment（旋转屏幕不消失）"),
    NoteChapter("4", "BottomSheetDialog（底部弹出）"),
    NoteChapter("5", "PopupWindow（悬浮弹出）"),
    NoteChapter("6", "Toast & Snackbar"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun DialogScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "弹窗",
        subtitle = "AlertDialog · BottomSheet · PopupWindow · Snackbar",
        color = Blue,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
