package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "ContentProvider 数据共享",
    description = "URI 设计，增删改查，FileProvider，跨进程数据访问",
    overview = "ContentProvider 是 Android 四大组件之一，提供统一的数据共享接口，允许不同应用之间安全地访问数据。系统通讯录、媒体库等均通过 ContentProvider 对外暴露数据。",
    keyPoints = listOf(
        "URI 格式：content://authority/path/id，authority 在 Manifest 中声明唯一标识",
        "CRUD 接口：query/insert/update/delete，Cursor 遍历查询结果",
        "ContentResolver：调用方通过 context.contentResolver 访问 ContentProvider",
        "权限控制：readPermission/writePermission，exported=true 才能跨进程访问",
        "FileProvider：安全地在应用间共享文件（替代 file:// URI），Android 7+ 必须使用",
        "常用系统 Provider：MediaStore（图片/视频）、ContactsContract（通讯录）"
    ),
    codeSnippet = """
// 自定义 ContentProvider
class BookProvider : ContentProvider() {
    private lateinit var db: SQLiteDatabase

    override fun onCreate(): Boolean {
        db = BookDbHelper(context!!).writableDatabase
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return db.query("books", projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = db.insert("books", null, values)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun getType(uri: Uri): String = "vnd.android.cursor.dir/books"
    override fun delete(uri: Uri, s: String?, a: Array<String>?): Int = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, a: Array<String>?): Int = 0
}

// ContentResolver 访问数据
val cursor = contentResolver.query(
    Uri.parse("content://com.example.provider/books"),
    arrayOf("title", "author"), null, null, null
)
cursor?.use {
    while (it.moveToNext()) {
        val title = it.getString(it.getColumnIndexOrThrow("title"))
    }
}

// FileProvider 共享文件（Android 7+）
val file = File(cacheDir, "photo.jpg")
val uri = FileProvider.getUriForFile(this, "${'$'}{packageName}.provider", file)
val intent = Intent(Intent.ACTION_VIEW).apply {
    setDataAndType(uri, "image/jpeg")
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
    """.trimIndent(),
    tips = listOf(
        "访问通讯录、媒体库等系统 ContentProvider 需要在 Manifest 申请对应权限",
        "FileProvider 需要在 Manifest 声明 provider 并提供 file_paths.xml 配置文件",
        "自定义 ContentProvider 在多线程访问时需要自行保证线程安全"
    )
)

@Composable
fun ContentProviderScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF2196F3),
        stageTitle = "四大组件与核心 UI",
        onBack = onBack
    )
}
