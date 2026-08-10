package com.example.androidlearn.feature.intermediate.detail.stage3

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * Hilt 依赖注入
 * 官方文档：https://developer.android.com/training/dependency-injection/hilt-android
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  核心注解
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  入口注解 ─────────────────────────────────────────────────────────────
 *
 *  · @HiltAndroidApp：在 Application 类上标注，初始化 Hilt 组件树
 *  · @AndroidEntryPoint：Activity / Fragment / Service 的注入入口
 *  · @HiltViewModel：ViewModel 注入入口，配合 by viewModels() 使用
 *
 * ── 1.2  提供依赖 ─────────────────────────────────────────────────────────────
 *
 *  · @Inject constructor：标记构造函数，Hilt 自动创建实例
 *  · @Module + @Provides：提供无法修改构造函数的类（第三方库）
 *  · @Binds：将接口绑定到实现类，比 @Provides 性能更好
 *
 *  @Module
 *  @InstallIn(SingletonComponent::class)
 *  object NetworkModule {
 *      @Provides @Singleton
 *      fun provideRetrofit(): Retrofit = Retrofit.Builder()
 *          .baseUrl("https://api.example.com/")
 *          .build()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  作用域管理
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  常用 Scope ───────────────────────────────────────────────────────────
 *
 *  · @Singleton：全局单例，跟随 Application 生命周期
 *  · @ActivityScoped：跟随 Activity 生命周期
 *  · @ViewModelScoped：跟随 ViewModel 生命周期
 *  · @FragmentScoped：跟随 Fragment 生命周期
 *
 * ── 2.2  ViewModel 注入示例 ───────────────────────────────────────────────────
 *
 *  @HiltViewModel
 *  class HomeViewModel @Inject constructor(
 *      private val repository: ItemRepository
 *  ) : ViewModel()
 *
 *  // Activity 中使用
 *  @AndroidEntryPoint
 *  class HomeActivity : AppCompatActivity() {
 *      private val vm: HomeViewModel by viewModels()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  测试替换
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · @UninstallModules：移除生产模块
 *  · @TestInstallIn：安装测试模块替换依赖
 *
 *  @UninstallModules(NetworkModule::class)
 *  @HiltAndroidTest
 *  class HomeViewModelTest {
 *      @BindValue val fakeRepo: ItemRepository = FakeItemRepository()
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 接口注入用 @Binds 而非 @Provides，性能更好
 *  · @Singleton 全局单例，@ViewModelScoped 跟随 ViewModel
 *  · Hilt 测试用 @UninstallModules + @TestInstallIn 替换依赖
 */

val hiltData = NoteData(
    title = "Hilt 依赖注入",
    subtitle = "现代架构体系 · 模块化依赖管理，提升可测试性",
    color = Color.parseColor("#9C27B0"),
    chapters = listOf(
        ChapterItem("1",   "核心注解"),
        ChapterItem("1.1", "入口注解"),
        ChapterItem("1.2", "提供依赖"),
        ChapterItem("2",   "作用域管理"),
        ChapterItem("2.1", "常用 Scope"),
        ChapterItem("2.2", "ViewModel 注入示例"),
        ChapterItem("3",   "测试替换"),
        ChapterItem("4",   "最佳实践"),
    )
)
