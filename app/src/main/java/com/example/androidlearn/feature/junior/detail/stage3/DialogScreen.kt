package com.example.androidlearn.feature.junior.detail.stage3

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
 * ── 2  Material3 AlertDialog（Compose）───────────────────────────────────────
 *
 *  var showDialog by remember { mutableStateOf(false) }
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
 *
 * ── 3  DialogFragment（旋转屏幕不消失）────────────────────────────────────────
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
 *  ConfirmDialog.newInstance("确认删除？").show(supportFragmentManager, "confirm_dialog")
 *  supportFragmentManager.setFragmentResultListener("confirm", this) { _, bundle ->
 *      if (bundle.getBoolean("result")) doDelete()
 *  }
 *
 *
 * ── 4  BottomSheetDialog（底部弹出）──────────────────────────────────────────
 *
 *  // Compose：ModalBottomSheet（Material3）
 *  var showSheet by remember { mutableStateOf(false) }
 *  if (showSheet) {
 *      ModalBottomSheet(onDismissRequest = { showSheet = false }) {
 *          Column(modifier = Modifier.padding(16.dp)) {
 *              Text("分享到", style = MaterialTheme.typography.titleMedium)
 *          }
 *      }
 *  }
 *
 *  // View 体系：BottomSheetDialogFragment（推荐，旋转不消失）
 *  class ShareBottomSheet : BottomSheetDialogFragment() {
 *      override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
 *          inflater.inflate(R.layout.bottom_sheet_share, container, false)
 *  }
 *  ShareBottomSheet().show(supportFragmentManager, "share")
 *
 *
 * ── 5  PopupWindow（悬浮弹出）────────────────────────────────────────────────
 *
 *  val popup = PopupWindow(context).apply {
 *      contentView = layoutInflater.inflate(R.layout.popup_layout, null)
 *      width = ViewGroup.LayoutParams.WRAP_CONTENT
 *      height = ViewGroup.LayoutParams.WRAP_CONTENT
 *      isOutsideTouchable = true
 *      isFocusable = true
 *  }
 *  popup.showAsDropDown(anchorView, 0, 8)
 *
 *  // Compose 中用 Popup
 *  Popup(alignment = Alignment.TopCenter) {
 *      Card { Text("气泡提示", modifier = Modifier.padding(8.dp)) }
 *  }
 *
 *
 * ── 6  Toast & Snackbar ───────────────────────────────────────────────────────
 *
 *  Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show()
 *
 *  // Snackbar（推荐替代 Toast）：支持操作按钮，可撤销
 *  Snackbar.make(view, "已删除", Snackbar.LENGTH_LONG)
 *      .setAction("撤销") { undoDelete() }.show()
 *
 *  // Compose 中的 Snackbar
 *  val snackbarHostState = remember { SnackbarHostState() }
 *  scope.launch {
 *      val result = snackbarHostState.showSnackbar("已删除", actionLabel = "撤销")
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
 */

private val Teal = Color(0xFF009688)

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
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
