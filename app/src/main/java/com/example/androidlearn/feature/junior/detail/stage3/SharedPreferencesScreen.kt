package com.example.androidlearn.feature.junior.detail.stage3

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
 * SharedPreferences & DataStore 笔记
 * 官方文档：https://developer.android.com/topic/libraries/architecture/datastore
 *
 * ── 1  SharedPreferences 基础用法 ─────────────────────────────────────────────
 *
 *  · 轻量级键值对存储，数据持久化到 XML 文件
 *  · 适合：用户偏好设置、少量配置项
 *  · 不适合：大量数据、频繁写入、复杂结构
 *
 *  // 获取实例
 *  val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
 *  // 或使用默认文件（包名_preferences.xml）
 *  val prefs = PreferenceManager.getDefaultSharedPreferences(context)
 *
 *  // 读取
 *  val name    = prefs.getString("name", "默认值")
 *  val age     = prefs.getInt("age", 0)
 *  val isDark  = prefs.getBoolean("dark_mode", false)
 *  val score   = prefs.getFloat("score", 0f)
 *  val count   = prefs.getLong("count", 0L)
 *
 *  // 写入
 *  prefs.edit()
 *      .putString("name", "张三")
 *      .putInt("age", 25)
 *      .putBoolean("dark_mode", true)
 *      .apply()    // 异步写入（推荐）
 *      // .commit() // 同步写入，返回 Boolean，主线程慎用
 *
 *  // 删除
 *  prefs.edit().remove("name").apply()
 *  prefs.edit().clear().apply()   // 清空所有
 *
 *  // 监听变化
 *  val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
 *      if (key == "dark_mode") updateTheme(prefs.getBoolean(key, false))
 *  }
 *  prefs.registerOnSharedPreferenceChangeListener(listener)
 *  // 注意：需持有 listener 强引用，否则会被 GC 回收
 *
 *
 * ── 2  SharedPreferences 的问题 ───────────────────────────────────────────────
 *
 *  · 主线程 I/O：getSharedPreferences() 首次调用会在主线程读取文件，可能导致 ANR
 *  · apply() 虽然异步写入，但在 Activity.onStop 时会等待写入完成，仍可能卡顿
 *  · 类型不安全：key 是字符串，容易拼写错误
 *  · 不支持 Flow/协程，无法响应式监听
 *  · 多进程不安全（MODE_MULTI_PROCESS 已废弃）
 *
 *
 * ── 3  DataStore Preferences（推荐替代 SP）────────────────────────────────────
 *
 *  · 基于 Kotlin Flow，异步非阻塞，协程友好
 *  · 类型安全的 Key，编译期检查
 *  · 不会在主线程做 I/O
 *
 *  // 依赖
 *  // implementation "androidx.datastore:datastore-preferences:1.0.0"
 *
 *  // 创建 DataStore（顶层属性，单例）
 *  val Context.dataStore by preferencesDataStore(name = "settings")
 *
 *  // 定义 Key
 *  val DARK_MODE_KEY  = booleanPreferencesKey("dark_mode")
 *  val USER_NAME_KEY  = stringPreferencesKey("user_name")
 *  val FONT_SIZE_KEY  = intPreferencesKey("font_size")
 *
 *  // 读取（Flow，在协程中收集）
 *  val isDarkFlow: Flow<Boolean> = context.dataStore.data
 *      .map { prefs -> prefs[DARK_MODE_KEY] ?: false }
 *
 *  // 写入（挂起函数）
 *  suspend fun setDarkMode(enabled: Boolean) {
 *      context.dataStore.edit { prefs ->
 *          prefs[DARK_MODE_KEY] = enabled
 *      }
 *  }
 *
 *  // 在 ViewModel 中使用
 *  class SettingsViewModel(private val context: Context) : ViewModel() {
 *      val isDark: StateFlow<Boolean> = context.dataStore.data
 *          .map { it[DARK_MODE_KEY] ?: false }
 *          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
 *
 *      fun toggleDarkMode(enabled: Boolean) {
 *          viewModelScope.launch {
 *              context.dataStore.edit { it[DARK_MODE_KEY] = enabled }
 *          }
 *      }
 *  }
 *
 *
 * ── 4  DataStore Proto（强类型，适合复杂结构）─────────────────────────────────
 *
 *  · 使用 Protocol Buffers 定义数据结构，完全类型安全
 *  · 适合：复杂配置对象、需要版本迁移的数据
 *
 *  // 定义 .proto 文件
 *  // syntax = "proto3";
 *  // message UserPreferences {
 *  //     bool dark_mode = 1;
 *  //     string user_name = 2;
 *  //     int32 font_size = 3;
 *  // }
 *
 *  // 实现 Serializer
 *  object UserPreferencesSerializer : Serializer<UserPreferences> {
 *      override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
 *      override suspend fun readFrom(input: InputStream): UserPreferences =
 *          UserPreferences.parseFrom(input)
 *      override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
 *          t.writeTo(output)
 *  }
 *
 *  val Context.protoDataStore by dataStore(
 *      fileName = "user_prefs.pb",
 *      serializer = UserPreferencesSerializer
 *  )
 *
 *
 * ── 5  EncryptedSharedPreferences（敏感数据加密）─────────────────────────────
 *
 *  · 对 key 和 value 都进行加密，适合存储 Token、密码等敏感信息
 *  · 基于 Android Keystore，密钥由系统管理
 *
 *  // 依赖
 *  // implementation "androidx.security:security-crypto:1.1.0-alpha06"
 *
 *  val masterKey = MasterKey.Builder(context)
 *      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
 *      .build()
 *
 *  val encryptedPrefs = EncryptedSharedPreferences.create(
 *      context,
 *      "secret_prefs",
 *      masterKey,
 *      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
 *      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
 *  )
 *
 *  // 用法与普通 SP 完全相同
 *  encryptedPrefs.edit().putString("token", "Bearer xxx").apply()
 *  val token = encryptedPrefs.getString("token", null)
 *
 *
 * ── 6  存储方案选型 ───────────────────────────────────────────────────────────
 *
 *  SharedPreferences：
 *  · 维护旧项目；简单配置项；不需要响应式
 *
 *  DataStore Preferences：
 *  · 新项目替代 SP；需要 Flow 响应式；键值对结构
 *
 *  DataStore Proto：
 *  · 复杂配置对象；需要强类型保证；需要版本迁移
 *
 *  EncryptedSharedPreferences：
 *  · Token、密码等敏感数据；安全要求高的场景
 *
 *  Room Database：
 *  · 大量结构化数据；需要查询/排序/关联；列表数据
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 新项目优先使用 DataStore，SP 在主线程 I/O 可能导致 ANR
 *  · 不要用 SP/DataStore 存储大量数据，改用 Room
 *  · 敏感信息（Token/密码）用 EncryptedSharedPreferences 加密
 *  · DataStore 操作放在 ViewModel 中，通过 StateFlow 暴露给 UI
 *  · SP 的 apply() 优于 commit()，避免主线程阻塞
 *  · 注册 OnSharedPreferenceChangeListener 时持有强引用，防止被 GC
 */

private val Blue = Color(0xFF2196F3)

private data class SpChapter(val num: String, val title: String)

private val chapters = listOf(
    SpChapter("1", "SharedPreferences 基础用法"),
    SpChapter("2", "SharedPreferences 的问题"),
    SpChapter("3", "DataStore Preferences（推荐替代 SP）"),
    SpChapter("4", "DataStore Proto（强类型）"),
    SpChapter("5", "EncryptedSharedPreferences（敏感数据加密）"),
    SpChapter("6", "存储方案选型"),
    SpChapter("7", "最佳实践"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedPreferencesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("SharedPreferences", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            "键值对存储 · DataStore · 加密存储",
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
            items(chapters.size) { i -> ChapterRowSp(chapters[i]) }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ChapterRowSp(chapter: SpChapter) {
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
