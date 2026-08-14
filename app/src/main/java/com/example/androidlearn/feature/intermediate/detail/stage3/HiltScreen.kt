package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Hilt 依赖注入
 * 官方文档：https://developer.android.com/training/dependency-injection/hilt-android
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  0  依赖注入（DI）基础概念  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 依赖注入：将对象的依赖从外部传入，而非在内部自己创建
 *  · 好处：解耦、可测试（替换 Fake 实现）、可维护
 *
 *  // ❌ 不用 DI：内部创建，无法替换
 *  class UserViewModel {
 *      private val repo = UserRepository(RetrofitClient.api, AppDatabase.dao)
 *  }
 *
 *  // ✅ 用 DI：外部注入，测试时可传 FakeRepository
 *  class UserViewModel @Inject constructor(
 *      private val repo: UserRepository
 *  ) : ViewModel()
 *
 *  · Hilt 是 Google 官方推荐的 Android DI 框架，基于 Dagger2，简化了大量样板代码
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心注解  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  入口注解  ★ 必学 ──────────────────────────────────────────────────────
 *
 *  · @HiltAndroidApp：在 Application 类上标注，触发 Hilt 代码生成，初始化组件树
 *  · @AndroidEntryPoint：Activity / Fragment / View / Service / BroadcastReceiver 的注入入口
 *  · @HiltViewModel：ViewModel 注入入口，配合 by viewModels() 使用
 *
 *  // Application
 *  @HiltAndroidApp
 *  class MyApp : Application()
 *
 *  // Activity
 *  @AndroidEntryPoint
 *  class MainActivity : AppCompatActivity() {
 *      @Inject lateinit var analytics: AnalyticsService  // 字段注入
 *      private val vm: HomeViewModel by viewModels()
 *  }
 *
 *  // Fragment（宿主 Activity 也必须加 @AndroidEntryPoint）
 *  @AndroidEntryPoint
 *  class HomeFragment : Fragment() {
 *      private val vm: HomeViewModel by viewModels()
 *  }
 *
 * ── 1.2  @Inject constructor：构造函数注入  ★ 必学 ────────────────────────────
 *
 *  · 最简单的提供方式，Hilt 自动创建实例并注入依赖
 *  · 适用于自己编写的类
 *
 *  class UserRepository @Inject constructor(
 *      private val api: UserApi,       // Hilt 自动注入
 *      private val dao: UserDao        // Hilt 自动注入
 *  ) {
 *      suspend fun getUser(id: Int) = api.getUser(id)
 *  }
 *
 * ── 1.3  @Module + @Provides：模块提供  ★ 必学 ────────────────────────────────
 *
 *  · 用于无法修改构造函数的类（第三方库：Retrofit、OkHttp、Room 等）
 *  · @InstallIn：指定模块安装到哪个组件（决定生命周期）
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)   // 全局单例
 *  object NetworkModule {
 *
 *      @Provides
 *      @Singleton
 *      fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
 *          .addInterceptor(HttpLoggingInterceptor())
 *          .build()
 *
 *      @Provides
 *      @Singleton
 *      fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
 *          Retrofit.Builder()
 *              .baseUrl("https://api.example.com/")
 *              .client(okHttpClient)          // Hilt 自动传入上面提供的 OkHttpClient
 *              .addConverterFactory(GsonConverterFactory.create())
 *              .build()
 *
 *      @Provides
 *      @Singleton
 *      fun provideUserApi(retrofit: Retrofit): UserApi =
 *          retrofit.create(UserApi::class.java)
 *  }
 *
 * ── 1.4  @Binds：接口绑定实现  ★ 常用 ────────────────────────────────────────
 *
 *  · 将接口绑定到具体实现类，比 @Provides 性能更好（编译期处理，无运行时反射）
 *  · 必须在 abstract class Module 中使用（不能是 object）
 *  · 函数必须是 abstract，参数是实现类，返回值是接口
 *
 *  interface UserRepository {
 *      suspend fun getUser(id: Int): User
 *  }
 *
 *  class UserRepositoryImpl @Inject constructor(
 *      private val api: UserApi
 *  ) : UserRepository {
 *      override suspend fun getUser(id: Int) = api.getUser(id)
 *  }
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)
 *  abstract class RepositoryModule {
 *      @Binds
 *      @Singleton
 *      abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
 *      // 注入 UserRepository 时，Hilt 自动提供 UserRepositoryImpl
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  作用域（Scope）管理  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  Hilt 组件与 Scope 对应关系  ★ 必学 ──────────────────────────────────
 *
 *  ┌──────────────────────────┬──────────────────────────┬──────────────────────────┐
 *  │       Hilt 组件           │         Scope 注解        │       生命周期            │
 *  ├──────────────────────────┼──────────────────────────┼──────────────────────────┤
 *  │ SingletonComponent       │ @Singleton               │ Application 整个生命周期   │
 *  │ ActivityRetainedComponent│ @ActivityRetainedScoped  │ 配置变更后存活（同 VM）     │
 *  │ ViewModelComponent       │ @ViewModelScoped         │ ViewModel 生命周期         │
 *  │ ActivityComponent        │ @ActivityScoped          │ Activity 生命周期          │
 *  │ FragmentComponent        │ @FragmentScoped          │ Fragment 生命周期          │
 *  │ ViewComponent            │ @ViewScoped              │ View 生命周期              │
 *  │ ServiceComponent         │ @ServiceScoped           │ Service 生命周期           │
 *  └──────────────────────────┴──────────────────────────┴──────────────────────────┘
 *
 *  · 不加 Scope 注解：每次注入都创建新实例
 *  · 加 Scope 注解：在对应组件范围内复用同一实例
 *
 * ── 2.2  常用 Scope 示例  ★ 必学 ─────────────────────────────────────────────
 *
 *  // @Singleton：全局单例（Retrofit、数据库等）
 *  @Provides @Singleton
 *  fun provideDatabase(app: Application): AppDatabase =
 *      Room.databaseBuilder(app, AppDatabase::class.java, "app.db").build()
 *
 *  // @ViewModelScoped：ViewModel 内单例（同一 ViewModel 中多处注入同一实例）
 *  @ViewModelScoped
 *  class UserUseCase @Inject constructor(private val repo: UserRepository)
 *
 *  // @ActivityScoped：Activity 内单例
 *  @ActivityScoped
 *  class Navigator @Inject constructor()
 *
 * ── 2.3  ViewModel 注入完整示例  ★ 必学 ──────────────────────────────────────
 *
 *  @HiltViewModel
 *  class HomeViewModel @Inject constructor(
 *      private val userRepo: UserRepository,
 *      private val settingsRepo: SettingsRepository,
 *      savedStateHandle: SavedStateHandle   // 可直接注入，获取导航参数
 *  ) : ViewModel() {
 *      val userId: String = savedStateHandle.get<String>("userId") ?: ""
 *  }
 *
 *  // Activity 中
 *  @AndroidEntryPoint
 *  class HomeActivity : AppCompatActivity() {
 *      private val vm: HomeViewModel by viewModels()
 *  }
 *
 *  // Fragment 中（共享 Activity 的 ViewModel）
 *  @AndroidEntryPoint
 *  class HomeFragment : Fragment() {
 *      private val vm: HomeViewModel by activityViewModels()  // 共享 Activity 的 VM
 *      private val ownVm: DetailViewModel by viewModels()     // Fragment 自己的 VM
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  限定符（Qualifier）  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 3.1  @Qualifier：区分同类型的不同实现 ────────────────────────────────────
 *
 *  · 当同一类型有多个实现时，用 @Qualifier 区分
 *  · 内置：@ApplicationContext / @ActivityContext
 *
 *  // 自定义 Qualifier
 *  @Qualifier
 *  @Retention(AnnotationRetention.BINARY)
 *  annotation class AuthInterceptorOkHttpClient
 *
 *  @Qualifier
 *  @Retention(AnnotationRetention.BINARY)
 *  annotation class OtherInterceptorOkHttpClient
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)
 *  object NetworkModule {
 *      @AuthInterceptorOkHttpClient
 *      @Provides
 *      fun provideAuthOkHttpClient(): OkHttpClient =
 *          OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
 *
 *      @OtherInterceptorOkHttpClient
 *      @Provides
 *      fun provideOtherOkHttpClient(): OkHttpClient =
 *          OkHttpClient.Builder().addInterceptor(OtherInterceptor()).build()
 *  }
 *
 *  // 注入时指定使用哪个
 *  class UserRepository @Inject constructor(
 *      @AuthInterceptorOkHttpClient private val okHttpClient: OkHttpClient
 *  )
 *
 * ── 3.2  @ApplicationContext / @ActivityContext  ★ 常用 ───────────────────────
 *
 *  · Hilt 内置，无需自定义
 *
 *  class AnalyticsAdapter @Inject constructor(
 *      @ApplicationContext private val context: Context  // Application Context
 *  )
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  EntryPoint：非 Android 组件中获取依赖  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 用于无法使用 @AndroidEntryPoint 的场景（ContentProvider、自定义 View 等）
 *  · 通过 EntryPointAccessors 手动获取依赖
 *
 *  @EntryPoint
 *  @InstallIn(SingletonComponent::class)
 *  interface AnalyticsEntryPoint {
 *      fun analyticsService(): AnalyticsService
 *  }
 *
 *  // 在 ContentProvider 中使用
 *  class MyContentProvider : ContentProvider() {
 *      override fun query(...): Cursor? {
 *          val entryPoint = EntryPointAccessors.fromApplication(
 *              context!!.applicationContext,
 *              AnalyticsEntryPoint::class.java
 *          )
 *          val analytics = entryPoint.analyticsService()
 *          // ...
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  测试替换  ★ 常用
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 5.1  @UninstallModules + @TestInstallIn ───────────────────────────────────
 *
 *  · @UninstallModules：移除生产模块
 *  · @TestInstallIn：安装测试模块替换依赖
 *
 *  // 测试用 Fake 模块
 *  @Module
 *  @TestInstallIn(components = [SingletonComponent::class], replaces = [NetworkModule::class])
 *  object FakeNetworkModule {
 *      @Provides @Singleton
 *      fun provideUserApi(): UserApi = FakeUserApi()
 *  }
 *
 *  @HiltAndroidTest
 *  class HomeViewModelTest {
 *      @get:Rule val hiltRule = HiltAndroidRule(this)
 *      @Inject lateinit var repo: UserRepository
 *
 *      @Before fun setUp() { hiltRule.inject() }
 *  }
 *
 * ── 5.2  @BindValue：快速替换单个依赖  ★ 常用 ────────────────────────────────
 *
 *  · 无需创建整个 Module，直接在测试类中替换单个依赖
 *
 *  @HiltAndroidTest
 *  class HomeViewModelTest {
 *      @BindValue val fakeRepo: UserRepository = FakeUserRepository()
 *      // Hilt 会用 fakeRepo 替换所有注入 UserRepository 的地方
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  Hilt + Room + Retrofit 完整配置示例  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)
 *  object AppModule {
 *
 *      // Room 数据库
 *      @Provides @Singleton
 *      fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
 *          Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
 *              .fallbackToDestructiveMigration()
 *              .build()
 *
 *      // DAO（从数据库获取，无需 @Singleton，数据库本身是单例）
 *      @Provides
 *      fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
 *
 *      // Retrofit
 *      @Provides @Singleton
 *      fun provideRetrofit(): Retrofit = Retrofit.Builder()
 *          .baseUrl(BuildConfig.BASE_URL)
 *          .addConverterFactory(GsonConverterFactory.create())
 *          .build()
 *
 *      // API Service
 *      @Provides @Singleton
 *      fun provideUserApi(retrofit: Retrofit): UserApi =
 *          retrofit.create(UserApi::class.java)
 *  }
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)
 *  abstract class RepositoryModule {
 *      @Binds @Singleton
 *      abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  最佳实践  ★ 必学
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ✅ 应该做：
 *  · 自己的类用 @Inject constructor，第三方库用 @Module + @Provides
 *  · 接口绑定用 @Binds（abstract class Module），比 @Provides 性能更好
 *  · 合理使用 Scope：全局资源用 @Singleton，ViewModel 内共享用 @ViewModelScoped
 *  · 用 @ApplicationContext 注入 Context，避免 Activity 泄漏
 *  · 测试时用 @BindValue 或 @TestInstallIn 替换依赖，不要 mock 整个类
 *
 *  ❌ 不应该做：
 *  · 所有依赖都加 @Singleton（浪费内存，应按生命周期选择合适 Scope）
 *  · 在 @Provides 方法中注入 Activity Context（应用级 Module 用 @ApplicationContext）
 *  · 在 ViewModel 中注入 Activity/Fragment（会导致内存泄漏）
 *  · 用 @Provides 绑定接口（应用 @Binds，更高效）
 *
 *  · @Provides vs @Binds 选择：
 *    - 有具体逻辑（Builder 模式、条件判断）→ @Provides
 *    - 纯接口绑定实现 → @Binds（性能更好）
 */

val hiltData = NoteData(
    title = "Hilt 依赖注入1",
    subtitle = "@Inject · @Module · @Binds · Scope · Qualifier · 测试替换",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("0",   "依赖注入基础概念  ★ 必学"),
        ChapterItem("1",   "核心注解  ★ 必学"),
        ChapterItem("1.1", "入口注解：@HiltAndroidApp / @AndroidEntryPoint / @HiltViewModel  ★ 必学"),
        ChapterItem("1.2", "@Inject constructor：构造函数注入  ★ 必学"),
        ChapterItem("1.3", "@Module + @Provides：第三方库依赖提供  ★ 必学"),
        ChapterItem("1.4", "@Binds：接口绑定实现（比 @Provides 更高效）  ★ 常用"),
        ChapterItem("2",   "作用域（Scope）管理  ★ 必学"),
        ChapterItem("2.1", "Hilt 组件与 Scope 对应关系表  ★ 必学"),
        ChapterItem("2.2", "常用 Scope 示例：@Singleton / @ViewModelScoped  ★ 必学"),
        ChapterItem("2.3", "ViewModel 注入完整示例（含 SavedStateHandle）  ★ 必学"),
        ChapterItem("3",   "限定符（Qualifier）  ★ 常用"),
        ChapterItem("3.1", "@Qualifier：区分同类型的不同实现"),
        ChapterItem("3.2", "@ApplicationContext / @ActivityContext  ★ 常用"),
        ChapterItem("4",   "EntryPoint：非 Android 组件中获取依赖  ★ 常用"),
        ChapterItem("5",   "测试替换  ★ 常用"),
        ChapterItem("5.1", "@UninstallModules + @TestInstallIn"),
        ChapterItem("5.2", "@BindValue：快速替换单个依赖  ★ 常用"),
        ChapterItem("6",   "Hilt + Room + Retrofit 完整配置示例  ★ 必学"),
        ChapterItem("7",   "最佳实践  ★ 必学"),
    )
)
