package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 组件化 / 模块化
 * 官方文档：https://developer.android.com/topic/modularization
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  0  为什么要模块化  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 编译速度：只重新编译修改的模块，增量编译更快
 *  · 可维护性：业务边界清晰，团队并行开发互不干扰
 *  · 可测试性：每个模块可独立测试，依赖可替换
 *  · 可复用性：core 模块可跨项目复用
 *  · 按需交付：Dynamic Feature Module 减少安装包体积
 *
 *  单模块 vs 多模块：
 *  ┌──────────────┬──────────────────────────┬──────────────────────────┐
 *  │              │        单模块             │        多模块             │
 *  ├──────────────┼──────────────────────────┼──────────────────────────┤
 *  │ 编译速度      │ 全量编译，慢              │ 增量编译，快               │
 *  │ 代码隔离      │ 无隔离，耦合高            │ 模块边界清晰               │
 *  │ 团队协作      │ 容易冲突                  │ 并行开发，减少冲突          │
 *  │ 适用规模      │ 小型项目                  │ 中大型项目                 │
 *  └──────────────┴──────────────────────────┴──────────────────────────┘
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  模块划分策略  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  推荐分层结构  ★ 必学 ─────────────────────────────────────────────────
 *
 *  ┌─────────────────────────────────────────────────────────────┐
 *  │                         :app                                │  ← 壳模块，组装所有模块
 *  ├──────────────┬──────────────┬──────────────┬────────────────┤
 *  │  :feature:   │  :feature:   │  :feature:   │  :feature:     │  ← 业务功能模块
 *  │    home      │    search    │   profile    │    order       │
 *  ├──────────────┴──────────────┴──────────────┴────────────────┤
 *  │  :core:network  :core:database  :core:ui  :core:common      │  ← 基础能力模块
 *  └─────────────────────────────────────────────────────────────┘
 *
 *  · app 模块：壳模块，只负责组装，不含业务逻辑
 *  · feature 模块：按业务功能拆分，只依赖 core，不互相依赖
 *  · core 模块：基础能力，无业务逻辑，可跨 feature 复用
 *
 * ── 1.2  功能模块（feature）─────────────────────────────────────────────────
 *
 *  · feature:home：首页功能（UI + ViewModel + UseCase）
 *  · feature:search：搜索功能
 *  · feature:profile：个人中心
 *  · 每个 feature 模块内部结构：
 *    - ui/：Screen、ViewModel
 *    - domain/：UseCase（可选）
 *    - data/：Repository 实现、数据源
 *
 * ── 1.3  基础模块（core）★ 必学 ───────────────────────────────────────────────
 *
 *  · core:network：Retrofit/OkHttp 封装、统一拦截器、错误处理
 *  · core:database：Room 数据库、DAO 基类
 *  · core:ui：公共 UI 组件、主题、颜色、字体、尺寸
 *  · core:common：工具类、扩展函数、常量
 *  · core:model：数据模型（跨模块共享的实体类）
 *  · core:testing：测试工具类、Fake 实现
 *
 *  // settings.gradle.kts
 *  include(":app")
 *  include(":core:network")
 *  include(":core:database")
 *  include(":core:ui")
 *  include(":core:common")
 *  include(":core:model")
 *  include(":feature:home")
 *  include(":feature:search")
 *  include(":feature:profile")
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  依赖管理  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  Version Catalog（libs.versions.toml）★ 必学 ──────────────────────────
 *
 *  · 统一管理所有依赖版本，避免各模块版本不一致
 *  · 位于项目根目录 gradle/libs.versions.toml
 *
 *  // gradle/libs.versions.toml
 *  [versions]
 *  kotlin = "1.9.0"
 *  compose-bom = "2024.02.00"
 *  hilt = "2.50"
 *  retrofit = "2.9.0"
 *
 *  [libraries]
 *  retrofit-core = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
 *  hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
 *
 *  [plugins]
 *  android-application = { id = "com.android.application", version = "8.2.0" }
 *  kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
 *
 *  // 模块 build.gradle.kts 中使用
 *  dependencies {
 *      implementation(libs.retrofit.core)
 *      implementation(libs.hilt.android)
 *  }
 *
 * ── 2.2  Convention Plugin（约定插件）★ 常用 ──────────────────────────────────
 *
 *  · 将各模块重复的 Gradle 配置抽取为插件，统一管理
 *  · 避免每个模块都写相同的 compileSdk、kotlinOptions 等配置
 *
 *  // build-logic/convention/src/main/kotlin/AndroidFeatureConventionPlugin.kt
 *  class AndroidFeatureConventionPlugin : Plugin<Project> {
 *      override fun apply(target: Project) {
 *          with(target) {
 *              pluginManager.apply("com.android.library")
 *              pluginManager.apply("org.jetbrains.kotlin.android")
 *              pluginManager.apply("dagger.hilt.android.plugin")
 *
 *              extensions.configure<LibraryExtension> {
 *                  compileSdk = 34
 *                  defaultConfig.minSdk = 24
 *              }
 *
 *              dependencies {
 *                  add("implementation", project(":core:ui"))
 *                  add("implementation", project(":core:common"))
 *              }
 *          }
 *      }
 *  }
 *
 *  // feature 模块 build.gradle.kts（极简配置）
 *  plugins {
 *      alias(libs.plugins.androidlearn.android.feature)  // 一行搞定所有配置
 *  }
 *  dependencies {
 *      implementation(project(":core:network"))  // 只写模块特有依赖
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  模块间通信  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  接口依赖倒置（推荐）★ 必学 ──────────────────────────────────────────
 *
 *  · 模块间通过接口依赖，不直接依赖实现类（依赖倒置原则）
 *  · 接口定义在 core 层，实现在 feature 层，通过 Hilt 注入
 *
 *  // core:model 中定义接口
 *  interface UserRepository {
 *      suspend fun getUser(id: String): User
 *  }
 *
 *  // feature:profile 中实现
 *  class UserRepositoryImpl @Inject constructor(
 *      private val api: UserApi
 *  ) : UserRepository {
 *      override suspend fun getUser(id: String) = api.getUser(id)
 *  }
 *
 *  // feature:profile 的 Hilt Module 中绑定
 *  @Module @InstallIn(SingletonComponent::class)
 *  abstract class UserModule {
 *      @Binds abstract fun bindUserRepo(impl: UserRepositoryImpl): UserRepository
 *  }
 *
 *  // feature:home 中注入使用（不依赖 feature:profile）
 *  class HomeViewModel @Inject constructor(
 *      private val userRepo: UserRepository  // 只依赖接口
 *  ) : ViewModel()
 *
 * ── 3.2  Navigation 路由跨模块跳转  ★ 常用 ────────────────────────────────────
 *
 *  · 各 feature 模块暴露自己的 NavGraph，在 app 模块组装
 *  · 通过 deep link 或 route 字符串跨模块导航
 *
 *  // feature:home 暴露路由常量
 *  object HomeNavigation {
 *      const val ROUTE = "home"
 *      fun NavGraphBuilder.homeGraph(navController: NavController) {
 *          composable(ROUTE) { HomeScreen(navController) }
 *      }
 *  }
 *
 *  // app 模块组装所有路由
 *  NavHost(navController, startDestination = "home") {
 *      with(HomeNavigation) { homeGraph(navController) }
 *      with(ProfileNavigation) { profileGraph(navController) }
 *      with(SearchNavigation) { searchGraph(navController) }
 *  }
 *
 * ── 3.3  EventBus / SharedFlow 跨模块事件  ★ 常用 ─────────────────────────────
 *
 *  · 在 core:common 中定义全局事件总线
 *  · 各模块通过 Hilt 注入同一个 SharedFlow 实例
 *
 *  // core:common 中定义
 *  class AppEventBus @Inject constructor() {
 *      private val _events = MutableSharedFlow<AppEvent>()
 *      val events: SharedFlow<AppEvent> = _events.asSharedFlow()
 *      suspend fun emit(event: AppEvent) = _events.emit(event)
 *  }
 *
 *  sealed class AppEvent {
 *      data class UserLoggedOut(val reason: String) : AppEvent()
 *      object TokenExpired : AppEvent()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  Dynamic Feature Module（动态功能模块）★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 按需下载功能，减少安装包初始体积
 *  · 用户触发时才通过 Play Core 下载对应模块
 *  · 适合：地图、AR、高级编辑、不常用的大功能
 *
 *  // 动态模块 build.gradle.kts
 *  plugins { id("com.android.dynamic-feature") }
 *  android {
 *      dynamicFeatures += setOf(":feature:map")
 *  }
 *
 *  // 运行时请求下载
 *  val splitInstallManager = SplitInstallManagerFactory.create(context)
 *  val request = SplitInstallRequest.newBuilder()
 *      .addModule("map")
 *      .build()
 *  splitInstallManager.startInstall(request)
 *      .addOnSuccessListener { /* 下载成功，启动功能 */ }
 *      .addOnFailureListener { /* 下载失败处理 */ }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  模块化迁移路径  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  单模块项目迁移步骤（循序渐进）：
 *
 *  Step 1：抽取 core 层
 *    - 将网络、数据库、工具类移到 core:network / core:database / core:common
 *    - 风险低，不涉及业务逻辑
 *
 *  Step 2：抽取 core:model
 *    - 将跨模块共享的数据模型移到 core:model
 *    - 解决循环依赖问题
 *
 *  Step 3：按业务拆分 feature 模块
 *    - 从最独立的功能开始（如：个人中心、设置页）
 *    - 逐步迁移，保持主干可运行
 *
 *  Step 4：引入 Convention Plugin
 *    - 统一各模块 Gradle 配置
 *    - 减少重复代码
 *
 *  Step 5：引入 Version Catalog
 *    - 统一依赖版本管理
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · feature 模块只依赖 core，不互相依赖（避免循环依赖）
 *  · 用 Version Catalog 统一管理依赖版本
 *  · 用 Convention Plugin 统一各模块 Gradle 配置
 *  · 模块间通信通过接口 + Hilt 注入，不直接依赖实现类
 *  · 从单模块逐步迁移，先抽 core 层，再拆 feature 层
 *  · 每个模块保持单一职责，模块内高内聚、模块间低耦合
 *
 *  ❌ 不应该做：
 *  · feature 模块之间直接互相依赖（会导致循环依赖）
 *  · 在 core 模块中引入业务逻辑（core 应该是纯技术能力）
 *  · 过度拆分（小项目拆太细反而增加维护成本）
 *  · 在 app 模块中写业务逻辑（app 只负责组装）
 */

val modularizationData = NoteData(
    title = "组件化 / 模块化",
    subtitle = "分层架构 · Version Catalog · Convention Plugin · 模块间通信 · 迁移路径",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("0",   "为什么要模块化：编译速度 / 可维护性 / 可测试性  ★ 必学"),
        ChapterItem("1",   "模块划分策略  ★ 必学"),
        ChapterItem("1.1", "推荐分层结构：app / feature / core  ★ 必学"),
        ChapterItem("1.2", "功能模块（feature）：按业务拆分"),
        ChapterItem("1.3", "基础模块（core）：network / database / ui / common  ★ 必学"),
        ChapterItem("2",   "依赖管理  ★ 必学"),
        ChapterItem("2.1", "Version Catalog（libs.versions.toml）统一版本  ★ 必学"),
        ChapterItem("2.2", "Convention Plugin：统一 Gradle 配置  ★ 常用"),
        ChapterItem("3",   "模块间通信  ★ 必学"),
        ChapterItem("3.1", "接口依赖倒置 + Hilt 注入（推荐）  ★ 必学"),
        ChapterItem("3.2", "Navigation 路由跨模块跳转  ★ 常用"),
        ChapterItem("3.3", "EventBus / SharedFlow 跨模块事件  ★ 常用"),
        ChapterItem("4",   "Dynamic Feature Module：按需下载  ★ 常用"),
        ChapterItem("5",   "模块化迁移路径：5 步循序渐进  ★ 常用"),
        ChapterItem("6",   "最佳实践  ★ 必学"),
    )
)
