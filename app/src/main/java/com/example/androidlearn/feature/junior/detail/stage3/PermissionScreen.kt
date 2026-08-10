package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 权限申请基础笔记
 * 官方文档：https://developer.android.com/guide/topics/permissions/overview
 *
 * ── 1  权限分类 ───────────────────────────────────────────────────────────────
 *
 *  普通权限（Normal）：
 *  · 在 AndroidManifest.xml 声明即自动授予，无需运行时申请
 *  · 示例：INTERNET、ACCESS_NETWORK_STATE、VIBRATE、RECEIVE_BOOT_COMPLETED
 *
 *  危险权限（Dangerous）：
 *  · 涉及用户隐私，Android 6.0（API 23）起必须运行时申请
 *  · 常见：CAMERA、RECORD_AUDIO、READ_CONTACTS、ACCESS_FINE_LOCATION、CALL_PHONE
 *
 *  特殊权限（Special）：
 *  · 需跳转系统设置页由用户手动开启
 *  · MANAGE_EXTERNAL_STORAGE（Android 11+）、SYSTEM_ALERT_WINDOW、WRITE_SETTINGS
 *
 *  // AndroidManifest.xml 声明（所有权限都需要声明）
 *  <uses-permission android:name="android.permission.CAMERA" />
 *
 *
 * ── 2  运行时权限申请（传统方式）─────────────────────────────────────────────
 *
 *  val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
 *      PackageManager.PERMISSION_GRANTED
 *
 *  ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
 *
 *  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
 *      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
 *      if (requestCode == REQUEST_CODE) {
 *          if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) openCamera()
 *          else showDeniedMessage()
 *      }
 *  }
 *
 *
 * ── 3  ActivityResultContracts（推荐现代方式）──────────────────────────────────
 *
 *  // 单个权限
 *  private val cameraLauncher = registerForActivityResult(
 *      ActivityResultContracts.RequestPermission()
 *  ) { isGranted ->
 *      if (isGranted) openCamera() else showDeniedMessage()
 *  }
 *
 *  fun requestCamera() {
 *      val permission = Manifest.permission.CAMERA
 *      when {
 *          ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED -> openCamera()
 *          shouldShowRequestPermissionRationale(permission) -> showRationaleDialog()
 *          else -> cameraLauncher.launch(permission)
 *      }
 *  }
 *
 *  // 多个权限
 *  private val multiLauncher = registerForActivityResult(
 *      ActivityResultContracts.RequestMultiplePermissions()
 *  ) { permissions ->
 *      val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
 *      val audioGranted  = permissions[Manifest.permission.RECORD_AUDIO] ?: false
 *  }
 *  multiLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
 *
 *
 * ── 4  Compose 中申请权限 ─────────────────────────────────────────────────────
 *
 *  val launcher = rememberLauncherForActivityResult(
 *      ActivityResultContracts.RequestPermission()
 *  ) { isGranted ->
 *      if (isGranted) openCamera() else showDeniedMessage()
 *  }
 *
 *  Button(onClick = {
 *      val permission = Manifest.permission.CAMERA
 *      if (ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED) {
 *          openCamera()
 *      } else {
 *          launcher.launch(permission)
 *      }
 *  }) { Text("打开相机") }
 *
 *  // 推荐使用 Accompanist Permissions 库（更简洁）
 *  val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
 *  if (cameraPermissionState.status.isGranted) {
 *      CameraContent()
 *  } else {
 *      Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
 *          Text("申请相机权限")
 *      }
 *  }
 *
 *
 * ── 5  永久拒绝处理 ───────────────────────────────────────────────────────────
 *
 *  · 用户拒绝两次后，系统不再弹出权限对话框
 *  · shouldShowRequestPermissionRationale() 返回 false 且未授权 → 已永久拒绝
 *  · 需引导用户去系统设置页手动开启
 *
 *  fun handlePermanentlyDenied() {
 *      AlertDialog.Builder(this)
 *          .setTitle("需要相机权限")
 *          .setMessage("请在设置中手动开启相机权限")
 *          .setPositiveButton("去设置") { _, _ ->
 *              val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
 *                  data = Uri.fromParts("package", packageName, null)
 *              }
 *              startActivity(intent)
 *          }
 *          .setNegativeButton("取消", null).show()
 *  }
 *
 *
 * ── 6  特殊权限申请 ───────────────────────────────────────────────────────────
 *
 *  // MANAGE_EXTERNAL_STORAGE（Android 11+，访问所有文件）
 *  if (!Environment.isExternalStorageManager()) {
 *      startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
 *          data = Uri.fromParts("package", packageName, null)
 *      })
 *  }
 *
 *  // SYSTEM_ALERT_WINDOW（悬浮窗）
 *  if (!Settings.canDrawOverlays(this)) {
 *      startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
 *          data = Uri.fromParts("package", packageName, null)
 *      })
 *  }
 *
 *
 * ── 7  各版本权限变化 ─────────────────────────────────────────────────────────
 *
 *  · Android 6.0（API 23）：引入运行时危险权限
 *  · Android 10（API 29）：后台位置权限 ACCESS_BACKGROUND_LOCATION 独立申请
 *  · Android 11（API 30）：一次性权限（位置/麦克风/相机）；MANAGE_EXTERNAL_STORAGE
 *  · Android 12（API 31）：位置权限分精确（FINE）/ 模糊（COARSE），优先请求模糊
 *  · Android 13（API 33）：细化媒体权限（READ_MEDIA_IMAGES/VIDEO/AUDIO）
 *                           通知权限 POST_NOTIFICATIONS 变为危险权限
 *
 *
 * ── 8  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 按需申请：只在真正需要时才申请权限，不要在启动时批量申请
 *  · 解释用途：shouldShowRequestPermissionRationale() 为 true 时先向用户说明原因
 *  · 优雅降级：权限被拒时提供替代方案，而非直接崩溃或功能不可用
 *  · 永久拒绝：引导用户去设置页，不要反复弹窗骚扰
 *  · 最小权限：优先请求模糊位置而非精确位置，降低用户顾虑
 *  · 使用 ActivityResultContracts 替代 onRequestPermissionsResult（已废弃）
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1", "权限分类"),
    NoteChapter("2", "运行时权限申请（传统方式）"),
    NoteChapter("3", "ActivityResultContracts（推荐现代方式）"),
    NoteChapter("4", "Compose 中申请权限"),
    NoteChapter("5", "永久拒绝处理"),
    NoteChapter("6", "特殊权限申请"),
    NoteChapter("7", "各版本权限变化"),
    NoteChapter("8", "最佳实践"),
)

@Composable
fun PermissionScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "权限申请基础",
        subtitle = "普通/危险/特殊权限 · 运行时申请 · 永久拒绝",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
