package com.example.androidlearn.feature.junior.detail.stage2

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

private data class ProviderChapter(val num: String, val title: String)

private val chapters = listOf(
    ProviderChapter("1", "核心概念"),
    ProviderChapter("2", "自定义 ContentProvider"),
    ProviderChapter("3", "ContentResolver 访问数据"),
    ProviderChapter("4", "FileProvider（文件共享，Android 7+）"),
    ProviderChapter("5", "常用系统 ContentProvider"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentProviderScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ContentProvider 数据共享", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "URI · CRUD · FileProvider · 系统 Provider",
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
                    containerColor = Blue,
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
            items(chapters.size) { i -> ChapterRowProvider(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowProvider(chapter: ProviderChapter) {
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
                color = Blue.copy(alpha = 0.12f)
            ) {
                Text(
                    chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(chapter.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
