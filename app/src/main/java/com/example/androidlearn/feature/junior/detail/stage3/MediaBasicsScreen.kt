package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 文件与多媒体基础笔记
 * 官方文档：https://developer.android.com/training/data-storage
 *
 * ── 1  存储分区概览 ───────────────────────────────────────────────────────────
 *
 *  Internal Storage（内部存储，应用私有）：
 *  · 路径：/data/data/<packageName>/files/
 *  · 无需权限，其他 App 无法访问，卸载后删除
 *  · context.filesDir / context.cacheDir
 *
 *  External Storage（外部存储）：
 *  · 应用专属目录（无需权限）：context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
 *  · 公共媒体目录（需权限）：通过 MediaStore 访问
 *
 *  分区存储（Scoped Storage，Android 10+）：
 *  · 禁止直接通过路径访问其他 App 的文件
 *  · 访问媒体文件用 MediaStore；访问任意文件用 SAF（Storage Access Framework）
 *  · MANAGE_EXTERNAL_STORAGE：访问所有文件（需特殊申请，应用商店审核严格）
 *
 *
 * ── 2  文件读写 ───────────────────────────────────────────────────────────────
 *
 *  // 写入内部存储
 *  val file = File(context.filesDir, "note.txt")
 *  file.writeText("Hello, Android!")
 *  // 或追加写入
 *  file.appendText("\n新增内容")
 *
 *  // 读取内部存储
 *  val content = file.readText()
 *
 *  // 使用 BufferedReader/Writer（大文件推荐）
 *  file.bufferedWriter().use { writer ->
 *      writer.write("第一行")
 *      writer.newLine()
 *      writer.write("第二行")
 *  }
 *  file.bufferedReader().useLines { lines ->
 *      lines.forEach { println(it) }
 *  }
 *
 *  // 写入应用专属外部存储（无需权限）
 *  val externalFile = File(
 *      context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
 *      "report.txt"
 *  )
 *  externalFile.writeText("报告内容")
 *
 *  // Assets 文件读取（只读，打包在 APK 中）
 *  context.assets.open("config.json").bufferedReader().use { it.readText() }
 *
 *
 * ── 3  MediaPlayer 音频播放 ───────────────────────────────────────────────────
 *
 *  生命周期：Idle → Initialized → Prepared → Started/Paused/Stopped → End
 *
 *  // 播放 res/raw 中的音频
 *  val player = MediaPlayer.create(context, R.raw.music)
 *  player.start()
 *  player.pause()
 *  player.seekTo(5000)   // 跳转到 5 秒
 *
 *  // 播放网络/本地 URI
 *  val player = MediaPlayer().apply {
 *      setDataSource(context, uri)
 *      setAudioAttributes(
 *          AudioAttributes.Builder()
 *              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
 *              .setUsage(AudioAttributes.USAGE_MEDIA)
 *              .build()
 *      )
 *      setOnPreparedListener { it.start() }
 *      setOnCompletionListener { it.release() }
 *      setOnErrorListener { _, what, extra -> true }
 *      prepareAsync()   // 异步准备，不阻塞主线程
 *  }
 *
 *  // 必须释放资源（在 onDestroy 或 onStop 中）
 *  player.stop()
 *  player.release()
 *
 *  // 循环播放
 *  player.isLooping = true
 *
 *
 * ── 4  相机拍照 ───────────────────────────────────────────────────────────────
 *
 *  // AndroidManifest.xml 配置 FileProvider
 *  // <provider
 *  //     android:name="androidx.core.content.FileProvider"
 *  //     android:authorities="${applicationId}.provider"
 *  //     android:exported="false"
 *  //     android:grantUriPermissions="true">
 *  //     <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
 *  //         android:resource="@xml/file_paths" />
 *  // </provider>
 *
 *  // res/xml/file_paths.xml
 *  // <paths>
 *  //     <external-cache-path name="camera" path="." />
 *  // </paths>
 *
 *  // Compose 中拍照
 *  var photoUri by remember { mutableStateOf<Uri?>(null) }
 *
 *  val takePicture = rememberLauncherForActivityResult(
 *      ActivityResultContracts.TakePicture()
 *  ) { success ->
 *      if (success) {
 *          // photoUri 中已有拍摄的图片
 *      }
 *  }
 *
 *  fun launchCamera() {
 *      val tempFile = File.createTempFile("photo_", ".jpg", context.externalCacheDir)
 *      photoUri = FileProvider.getUriForFile(
 *          context, "${context.packageName}.provider", tempFile
 *      )
 *      takePicture.launch(photoUri!!)
 *  }
 *
 *  // 从相册选图
 *  val pickImage = rememberLauncherForActivityResult(
 *      ActivityResultContracts.PickVisualMedia()
 *  ) { uri -> uri?.let { /* 使用选中的图片 */ } }
 *
 *  pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
 *
 *
 * ── 5  MediaStore 访问媒体库 ──────────────────────────────────────────────────
 *
 *  · Android 10+ 访问公共媒体文件的标准方式
 *  · 权限：READ_MEDIA_IMAGES / READ_MEDIA_VIDEO / READ_MEDIA_AUDIO（Android 13+）
 *          READ_EXTERNAL_STORAGE（Android 12 及以下）
 *
 *  // 查询图片列表
 *  val projection = arrayOf(
 *      MediaStore.Images.Media._ID,
 *      MediaStore.Images.Media.DISPLAY_NAME,
 *      MediaStore.Images.Media.DATE_ADDED
 *  )
 *  val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
 *
 *  contentResolver.query(
 *      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
 *      projection, null, null, sortOrder
 *  )?.use { cursor ->
 *      val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
 *      val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
 *      while (cursor.moveToNext()) {
 *          val id = cursor.getLong(idCol)
 *          val name = cursor.getString(nameCol)
 *          val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
 *          // 用 Coil/Glide 加载 uri
 *      }
 *  }
 *
 *  // 保存图片到媒体库（Android 10+）
 *  val values = ContentValues().apply {
 *      put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
 *      put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
 *      put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
 *      put(MediaStore.Images.Media.IS_PENDING, 1)
 *  }
 *  val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
 *  contentResolver.openOutputStream(uri)?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
 *  values.put(MediaStore.Images.Media.IS_PENDING, 0)
 *  contentResolver.update(uri, values, null, null)
 *
 *
 * ── 6  SAF（Storage Access Framework）────────────────────────────────────────
 *
 *  · 让用户通过系统文件选择器选择文件，无需申请存储权限
 *
 *  // 选择单个文件
 *  val openDocument = rememberLauncherForActivityResult(
 *      ActivityResultContracts.OpenDocument()
 *  ) { uri ->
 *      uri?.let {
 *          contentResolver.openInputStream(it)?.use { stream ->
 *              val content = stream.bufferedReader().readText()
 *          }
 *      }
 *  }
 *  openDocument.launch(arrayOf("text/plain", "application/pdf"))
 *
 *  // 创建文件
 *  val createDocument = rememberLauncherForActivityResult(
 *      ActivityResultContracts.CreateDocument("text/plain")
 *  ) { uri ->
 *      uri?.let {
 *          contentResolver.openOutputStream(it)?.use { stream ->
 *              stream.write("文件内容".toByteArray())
 *          }
 *      }
 *  }
 *  createDocument.launch("新建文档.txt")
 *
 *  // 持久化 URI 权限（跨重启保留访问权限）
 *  contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · Android 10+ 用 MediaStore 访问媒体文件，不要直接操作文件路径
 *  · 相机拍照必须通过 FileProvider 提供 URI，否则 Android 7+ 抛异常
 *  · MediaPlayer 使用完毕必须调用 release()，防止资源泄漏
 *  · 文件 I/O 放在协程 Dispatchers.IO 中，不要在主线程操作
 *  · 选择文件优先用 SAF（无需权限），不要申请 MANAGE_EXTERNAL_STORAGE
 *  · 大文件读写用 BufferedReader/Writer，避免一次性加载到内存
 *  · 保存图片到媒体库用 IS_PENDING 标记，写入完成后再置 0
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1", "存储分区概览"),
    NoteChapter("2", "文件读写"),
    NoteChapter("3", "MediaPlayer 音频播放"),
    NoteChapter("4", "相机拍照"),
    NoteChapter("5", "MediaStore 访问媒体库"),
    NoteChapter("6", "SAF（Storage Access Framework）"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun MediaBasicsScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "文件与多媒体基础",
        subtitle = "文件读写 · MediaPlayer · 相机 · MediaStore · SAF",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
