package com.example.androidlearn.feature.intermediate.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Hilt 依赖注入",
    description = "模块化依赖管理，提升可测试性",
    overview = "Hilt 是基于 Dagger 的 Android 依赖注入框架，自动管理依赖的创建和生命周期，大幅减少样板代码。",
    keyPoints = listOf(
        "@HiltAndroidApp：在 Application 类上标注，初始化 Hilt",
        "@AndroidEntryPoint：Activity/Fragment 注入入口",
        "@Inject constructor：标记构造函数，Hilt 自动创建实例",
        "@Module + @Provides：提供无法修改构造函数的类",
        "@Singleton / @ViewModelScoped：控制依赖的生命周期",
        "@TestInstallIn：测试中替换依赖为 Mock 实现"
    ),
    codeSnippet = """
@HiltAndroidApp
class App : Application()

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .build()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel()
    """.trimIndent(),
    tips = listOf(
        "接口注入用 @Binds 而非 @Provides，性能更好",
        "@Singleton 全局单例，@ViewModelScoped 跟随 ViewModel",
        "Hilt 测试用 @UninstallModules + @TestInstallIn 替换依赖"
    )
)

@Composable
fun HiltScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF9C27B0),
        stageTitle = "现代架构体系",
        onBack = onBack
    )
}
