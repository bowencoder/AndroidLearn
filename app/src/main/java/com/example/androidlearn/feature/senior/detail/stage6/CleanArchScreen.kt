package com.example.androidlearn.feature.senior.detail.stage6

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Clean Architecture 设计",
    description = "分层架构，用例 UseCase，依赖规则，可测试性",
    overview = "Clean Architecture 将系统分为 Presentation / Domain / Data 三层，依赖只能从外向内，Domain 层不依赖任何框架，天然可测试。",
    keyPoints = listOf(
        "三层结构：Presentation（UI/ViewModel）→ Domain（UseCase/Model）→ Data（Repository/Source）",
        "依赖规则：内层不知道外层，Domain 层零框架依赖",
        "UseCase（Interactor）：封装单一业务用例，ViewModel 调用 UseCase",
        "Repository 接口：Domain 层定义接口，Data 层提供实现",
        "数据映射：Dto → Entity → DomainModel 各层有独立模型",
        "测试优势：Domain 层纯 Kotlin，JUnit 直接测试，无 Android 依赖"
    ),
    codeSnippet = """
// Domain 层 - 零框架依赖
data class User(val id: Int, val name: String) // Domain Model

interface UserRepository {  // Domain 层定义接口
    suspend fun getUser(id: Int): Result<User>
}

class GetUserUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(id: Int) = repo.getUser(id)
}

// Presentation 层
class UserViewModel(private val getUser: GetUserUseCase) : ViewModel() {
    fun load(id: Int) = viewModelScope.launch {
        val result = getUser(id)  // 调用 UseCase
        // 更新 UI 状态
    }
}

// Data 层
class UserRepositoryImpl(
    private val api: UserApi,
    private val dao: UserDao
) : UserRepository {
    override suspend fun getUser(id: Int) = runCatching {
        api.fetchUser(id).toDomainModel()
    }
}
    """.trimIndent(),
    tips = listOf(
        "UseCase 只做一件事，命名用动词：GetUser、UpdateProfile、DeleteOrder",
        "Domain 层 build.gradle 只有 kotlin() 插件，不引入 Android 依赖",
        "过度设计陷阱：小项目不必强制 Clean Arch，按需引入复杂度"
    )
)

@Composable
fun CleanArchScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "架构设计与前沿技术",
        onBack = onBack
    )
}
