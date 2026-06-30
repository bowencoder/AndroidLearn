package com.example.androidlearn.feature.senior.detail.stage12

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "组件化架构设计",
    description = "路由框架（ARouter），模块间通信，组件独立运行，依赖治理",
    overview = "组件化是将单一 App 拆分为多个业务模块（Module）的架构方案。每个模块可独立开发、测试、编译，通过路由框架实现模块间通信，彻底解耦业务边界，提升大型团队的协作效率。",
    keyPoints = listOf(
        "分层架构：app壳 > 业务模块 > 功能模块 > 基础模块，依赖方向单向向下",
        "ARouter 路由：@Route 标注目标页面，ARouter.getInstance().build('/module/page').navigation()",
        "模块间通信：接口下沉 + 实现上移，通过服务发现（IProvider）实现解耦调用",
        "组件独立运行：每个模块可配置 isModule=true 单独打包成 App 调试",
        "资源隔离：各模块 res 文件名加前缀（如 `home_`, `user_`）防止资源命名冲突",
        "Gradle 依赖治理：Convention Plugin 统一版本管理，避免各模块版本不一致"
    ),
    codeSnippet = """
// ARouter 路由跳转
// 目标页面声明
@Route(path = "/user/profile")
class UserProfileActivity : AppCompatActivity() { ... }

// 路由调用（跨模块）
ARouter.getInstance()
    .build("/user/profile")
    .withString("userId", "123")
    .navigation()

// IProvider 服务通信
// 接口定义（下沉到公共模块）
interface IUserService : IProvider {
    fun getUserName(): String
}

// 实现（在 user 模块）
@Route(path = "/service/user")
class UserServiceImpl : IUserService {
    override fun getUserName() = "Alice"
    override fun init(context: Context?) {}
}

// 调用（在其他模块）
val userService = ARouter.getInstance()
    .navigation(IUserService::class.java)
val name = userService?.getUserName()

// 组件独立运行配置（build.gradle.kts）
val isModule = project.properties["isModule"]?.toString()?.toBoolean() ?: false
if (isModule) {
    plugins.apply("com.android.application")
} else {
    plugins.apply("com.android.library")
}
    """.trimIndent(),
    tips = listOf(
        "模块间不能直接 import 对方的类，只能通过接口 + ARouter 服务发现调用",
        "ARouter 需要在每个使用路由的模块的 build.gradle 中配置 kapt，不能漏",
        "大型项目推荐结合 Gradle Convention Plugin 统一所有模块的编译配置，减少重复代码"
    )
)

@Composable
fun ComponentizationScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF8BC34A),
        stageTitle = "插件化与热修复",
        onBack = onBack
    )
}
