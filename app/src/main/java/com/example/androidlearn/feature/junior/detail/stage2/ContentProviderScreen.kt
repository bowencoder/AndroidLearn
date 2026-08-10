package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * ContentProvider 数据共享笔记
 * 官方文档：https://developer.android.com/guide/topics/providers/content-providers
 *
 * ── 1  核心概念 ───────────────────────────────────────────────────────────────
 *
 *  · ContentProvider：四大组件之一，提供统一的跨应用数据共享接口
 *  · URI 格式：content://authority/path/id
 *    - authority：在 Manifest 中声明的唯一标识（通常用包名）
 *    - path：数据表名，id：具体记录
 *  · ContentResolver：调用方通过 context.contentResolver 访问 Provider
 *  · 权限控制：exported=true 才能跨进程；readPermission / writePermission 细粒度控制
 *
 *
 * ── 2  自定义 ContentProvider ─────────────────────────────────────────────────
 *
 *  class BookProvider : ContentProvider() {
 *      override fun onCreate(): Boolean { /* 初始化数据库 */ return true }
 *
 *      override fun query(uri: Uri, projection: Array<String>?, selection: String?,
 *                         selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
 *          return db.query("books", projection, selection, selectionArgs, null, null, sortOrder)
 *      }
 *
 *      override fun insert(uri: Uri, values: ContentValues?): Uri? {
 *          val id = db.insert("books", null, values)
 *          return ContentUris.withAppendedId(uri, id)
 *      }
 *
 *      override fun getType(uri: Uri) = "vnd.android.cursor.dir/books"
 *      override fun delete(uri: Uri, s: String?, a: Array<String>?) = 0
 *      override fun update(uri: Uri, v: ContentValues?, s: String?, a: Array<String>?) = 0
 *  }
 *
 *  // AndroidManifest.xml 声明
 *  <provider android:name=".BookProvider"
 *      android:authorities="com.example.provider"
 *      android:exported="true"/>
 *
 *
 * ── 3  ContentResolver 访问数据 ───────────────────────────────────────────────
 *
 *  val cursor = contentResolver.query(
 *      Uri.parse("content://com.example.provider/books"),
 *      arrayOf("title", "author"), null, null, null
 *  )
 *  cursor?.use {
 *      while (it.moveToNext()) {
 *          val title = it.getString(it.getColumnIndexOrThrow("title"))
 *      }
 *  }
 *
 *  // 插入
 *  val values = ContentValues().apply { put("title", "Kotlin"); put("author", "JetBrains") }
 *  contentResolver.insert(Uri.parse("content://com.example.provider/books"), values)
 *
 *
 * ── 4  FileProvider（文件共享，Android 7+）────────────────────────────────────
 *
 *  · Android 7+ 禁止直接传递 file:// URI，必须用 FileProvider 转换为 content:// URI
 *
 *  // Manifest 声明
 *  <provider android:name="androidx.core.content.FileProvider"
 *      android:authorities="${applicationId}.provider"
 *      android:exported="false" android:grantUriPermissions="true">
 *      <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
 *          android:resource="@xml/file_paths"/>
 *  </provider>
 *
 *  // res/xml/file_paths.xml
 *  <paths><cache-path name="cache" path="."/></paths>
 *
 *  // 代码中获取 URI
 *  val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
 *  val intent = Intent(Intent.ACTION_VIEW).apply {
 *      setDataAndType(uri, "image/jpeg")
 *      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
 *  }
 *
 *
 * ── 5  常用系统 ContentProvider ──────────────────────────────────────────────
 *
 *  · MediaStore：访问图片/视频/音频（需 READ_MEDIA_IMAGES 等权限）
 *  · ContactsContract：读写通讯录（需 READ_CONTACTS / WRITE_CONTACTS 权限）
 *  · CallLog：通话记录（需 READ_CALL_LOG 权限）
 *  · Settings：系统设置（部分需要 WRITE_SETTINGS 权限）
 *
 *  // 查询相册图片
 *  val cursor = contentResolver.query(
 *      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
 *      arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME),
 *      null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC"
 *  )
 */

private val Blue = Color(0xFF2196F3)

private val chapters = listOf(
    NoteChapter("1", "核心概念"),
    NoteChapter("2", "自定义 ContentProvider"),
    NoteChapter("3", "ContentResolver 访问数据"),
    NoteChapter("4", "FileProvider（文件共享，Android 7+）"),
    NoteChapter("5", "常用系统 ContentProvider"),
)

@Composable
fun ContentProviderScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "ContentProvider 数据共享",
        subtitle = "URI · CRUD · FileProvider · 系统 Provider",
        color = Blue,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
