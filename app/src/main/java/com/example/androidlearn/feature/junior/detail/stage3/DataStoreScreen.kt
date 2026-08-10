package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * DataStore 数据持久化笔记
 * 官方文档：https://developer.android.com/topic/libraries/architecture/datastore
 *
 * ── 1  DataStore 简介与优势 ───────────────────────────────────────────────────
 *
 *  · Jetpack 官方推荐的键值对持久化方案，替代 SharedPreferences
 *  · 基于 Kotlin 协程和 Flow，完全异步，不阻塞主线程
 *  · 两种实现：Preferences DataStore（键值对）和 Proto DataStore（强类型）
 *
 *  对比 SharedPreferences：
 *  ┌─────────────────────┬──────────────────┬──────────────────┐
 *  │ 特性                │ SharedPreferences│ DataStore        │
 *  ├─────────────────────┼──────────────────┼──────────────────┤
 *  │ 线程安全            │ 否               │ 是               │
 *  │ 主线程 I/O          │ 可能阻塞         │ 不阻塞           │
 *  │ 类型安全            │ 否（字符串 key） │ 是（类型化 key） │
 *  │ 响应式              │ 需手动监听       │ Flow 自动推送    │
 *  │ 错误处理            │ 无               │ catch 异常       │
 *  └─────────────────────┴──────────────────┴──────────────────┘
 *
 *  // 依赖（build.gradle.kts）
 *  implementation("androidx.datastore:datastore-preferences:1.1.1")
 *  // Proto DataStore 额外依赖
 *  implementation("androidx.datastore:datastore:1.1.1")
 *
 *
 * ── 2  Preferences DataStore 基础用法 ────────────────────────────────────────
 *
 *  // 创建 DataStore（顶层属性，保证单例）
 *  val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
 *
 *  // 定义类型安全的 Key
 *  val DARK_MODE_KEY  = booleanPreferencesKey("dark_mode")
 *  val USER_NAME_KEY  = stringPreferencesKey("user_name")
 *  val FONT_SIZE_KEY  = intPreferencesKey("font_size")
 *  val VOLUME_KEY     = floatPreferencesKey("volume")
 *  val COUNTER_KEY    = longPreferencesKey("counter")
 *
 *  // 读取（返回 Flow，数据变化自动推送）
 *  val isDarkFlow: Flow<Boolean> = context.dataStore.data
 *      .map { prefs -> prefs[DARK_MODE_KEY] ?: false }
 *
 *  // 读取并处理异常
 *  val safeFlow: Flow<Boolean> = context.dataStore.data
 *      .catch { e ->
 *          if (e is IOException) emit(emptyPreferences())
 *          else throw e
 *      }
 *      .map { prefs -> prefs[DARK_MODE_KEY] ?: false }
 *
 *  // 写入（挂起函数，事务性原子写入）
 *  suspend fun setDarkMode(enabled: Boolean) {
 *      context.dataStore.edit { prefs ->
 *          prefs[DARK_MODE_KEY] = enabled
 *      }
 *  }
 *
 *  // 删除单个 key
 *  suspend fun clearUserName() {
 *      context.dataStore.edit { prefs ->
 *          prefs.remove(USER_NAME_KEY)
 *      }
 *  }
 *
 *  // 清空所有数据
 *  suspend fun clearAll() {
 *      context.dataStore.edit { it.clear() }
 *  }
 *
 *
 * ── 3  在 ViewModel 中使用 ────────────────────────────────────────────────────
 *
 *  class SettingsViewModel(
 *      private val dataStore: DataStore<Preferences>
 *  ) : ViewModel() {
 *
 *      // 将 Flow 转为 StateFlow 供 UI 收集
 *      val isDark: StateFlow<Boolean> = dataStore.data
 *          .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
 *          .map { it[DARK_MODE_KEY] ?: false }
 *          .stateIn(
 *              scope = viewModelScope,
 *              started = SharingStarted.WhileSubscribed(5_000),
 *              initialValue = false
 *          )
 *
 *      val userName: StateFlow<String> = dataStore.data
 *          .map { it[USER_NAME_KEY] ?: "" }
 *          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
 *
 *      fun setDarkMode(enabled: Boolean) {
 *          viewModelScope.launch {
 *              dataStore.edit { it[DARK_MODE_KEY] = enabled }
 *          }
 *      }
 *
 *      fun setUserName(name: String) {
 *          viewModelScope.launch {
 *              dataStore.edit { it[USER_NAME_KEY] = name }
 *          }
 *      }
 *  }
 *
 *  // Compose UI 中收集
 *  val isDark by viewModel.isDark.collectAsStateWithLifecycle()
 *
 *
 * ── 4  Proto DataStore（强类型）──────────────────────────────────────────────
 *
 *  · 使用 Protocol Buffers 定义数据结构，完全类型安全，适合复杂配置
 *
 *  // 1. 定义 .proto 文件（src/main/proto/user_prefs.proto）
 *  // syntax = "proto3";
 *  // option java_package = "com.example.app";
 *  // message UserPreferences {
 *  //     bool dark_mode = 1;
 *  //     string user_name = 2;
 *  //     int32 font_size = 3;
 *  // }
 *
 *  // 2. 实现 Serializer
 *  object UserPreferencesSerializer : Serializer<UserPreferences> {
 *      override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
 *
 *      override suspend fun readFrom(input: InputStream): UserPreferences =
 *          try { UserPreferences.parseFrom(input) }
 *          catch (e: InvalidProtocolBufferException) { throw CorruptionException("无法读取 proto", e) }
 *
 *      override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
 *          t.writeTo(output)
 *  }
 *
 *  // 3. 创建 DataStore
 *  val Context.protoDataStore by dataStore(
 *      fileName = "user_prefs.pb",
 *      serializer = UserPreferencesSerializer
 *  )
 *
 *  // 4. 读写
 *  val darkModeFlow: Flow<Boolean> = context.protoDataStore.data
 *      .map { it.darkMode }
 *
 *  suspend fun setDarkMode(enabled: Boolean) {
 *      context.protoDataStore.updateData { prefs ->
 *          prefs.toBuilder().setDarkMode(enabled).build()
 *      }
 *  }
 *
 *
 * ── 5  从 SharedPreferences 迁移 ─────────────────────────────────────────────
 *
 *  · 提供 SharedPreferencesMigration，一次性自动迁移旧数据
 *
 *  val Context.dataStore by preferencesDataStore(
 *      name = "settings",
 *      produceMigrations = { context ->
 *          listOf(
 *              SharedPreferencesMigration(
 *                  context = context,
 *                  sharedPreferencesName = "old_prefs",
 *                  keysToMigrate = setOf("dark_mode", "user_name")  // 不传则迁移全部
 *              )
 *          )
 *      }
 *  )
 *  // 迁移完成后旧 SP 文件会被自动删除
 *
 *
 * ── 6  Hilt 注入 DataStore ────────────────────────────────────────────────────
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)
 *  object DataStoreModule {
 *
 *      @Provides
 *      @Singleton
 *      fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
 *          PreferenceDataStoreFactory.create(
 *              produceFile = { context.preferencesDataStoreFile("settings") }
 *          )
 *  }
 *
 *  // ViewModel 中注入
 *  @HiltViewModel
 *  class SettingsViewModel @Inject constructor(
 *      private val dataStore: DataStore<Preferences>
 *  ) : ViewModel() { ... }
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 新项目直接用 DataStore，不要再用 SharedPreferences
 *  · 用 Hilt 注入 DataStore 实例，避免全局单例
 *  · 在 ViewModel 中操作，通过 StateFlow 暴露给 UI
 *  · 读取时加 catch 处理 IOException，防止数据损坏崩溃
 *  · 简单键值对用 Preferences DataStore，复杂结构用 Proto DataStore
 *  · 大量结构化数据改用 Room，DataStore 不适合列表/关联查询
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1", "DataStore 简介与优势"),
    NoteChapter("2", "Preferences DataStore 基础用法"),
    NoteChapter("3", "在 ViewModel 中使用"),
    NoteChapter("4", "Proto DataStore（强类型）"),
    NoteChapter("5", "从 SharedPreferences 迁移"),
    NoteChapter("6", "Hilt 注入 DataStore"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun DataStoreScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "DataStore 数据持久化",
        subtitle = "Preferences · Proto · 替代 SharedPreferences",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
