package com.example.androidlearn.feature.intermediate.detail.stage4

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "单元测试",
    description = "JUnit、Mockito、Compose UI Test",
    overview = "自动化测试保障代码质量，单元测试验证业务逻辑，UI 测试验证用户交互，是高质量代码的保障。",
    keyPoints = listOf(
        "JUnit 4/5：断言 assertEquals / assertTrue / assertThrows",
        "Mockito / MockK：Mock 依赖，验证函数调用",
        "ViewModel 测试：TestCoroutineDispatcher / UnconfinedTestDispatcher",
        "Flow 测试：Turbine 库简化 Flow 断言",
        "Compose UI Test：onNodeWithText().performClick()",
        "测试覆盖率：Jacoco 生成报告，核心逻辑 > 80%"
    ),
    codeSnippet = """
@Test
fun `increment increases count by 1`() = runTest {
    val viewModel = CounterViewModel()
    viewModel.increment()
    assertEquals(1, viewModel.count.value)
}

// MockK 示例
@Test
fun `loadUser calls repository`() = runTest {
    val repo = mockk<UserRepository>()
    coEvery { repo.getUser(1) } returns User(1, "Alice")

    val vm = UserViewModel(repo)
    vm.loadUser(1)
    assertEquals("Alice", vm.uiState.value.user?.name)
}
    """.trimIndent(),
    tips = listOf(
        "测试命名用 backtick `应该做什么当某条件时`，可读性强",
        "优先用 MockK（Kotlin 原生），比 Mockito 更简洁",
        "使用 Turbine 库测试 Flow：val item = awaitItem()"
    )
)

@Composable
fun UnitTestScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF9800),
        stageTitle = "进阶开发能力",
        onBack = onBack
    )
}
