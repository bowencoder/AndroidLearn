package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * 单元测试与 UI 测试
 * 官方文档：https://developer.android.com/training/testing
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  单元测试
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  JUnit ────────────────────────────────────────────────────────────────
 *
 *  · assertEquals / assertTrue / assertThrows：常用断言
 *  · @Before / @After：测试前后执行
 *  · @Test：标记测试方法
 *
 *  @Test
 *  fun `increment increases count by 1`() = runTest {
 *      val viewModel = CounterViewModel()
 *      viewModel.increment()
 *      assertEquals(1, viewModel.count.value)
 *  }
 *
 * ── 1.2  MockK（推荐） ────────────────────────────────────────────────────────
 *
 *  · Kotlin 原生 Mock 框架，比 Mockito 更简洁
 *  · mockk<T>()：创建 Mock 对象
 *  · coEvery { }：Mock suspend 函数
 *  · verify { }：验证函数调用
 *
 *  @Test
 *  fun `loadUser calls repository`() = runTest {
 *      val repo = mockk<UserRepository>()
 *      coEvery { repo.getUser(1) } returns User(1, "Alice")
 *
 *      val vm = UserViewModel(repo)
 *      vm.loadUser(1)
 *      assertEquals("Alice", vm.uiState.value.user?.name)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  协程测试
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  ViewModel 测试 ───────────────────────────────────────────────────────
 *
 *  · UnconfinedTestDispatcher：立即执行协程，不需要 advanceUntilIdle
 *  · TestCoroutineScheduler：控制虚拟时间
 *
 *  @get:Rule
 *  val mainDispatcherRule = MainDispatcherRule()
 *
 * ── 2.2  Flow 测试（Turbine） ─────────────────────────────────────────────────
 *
 *  · Turbine 库简化 Flow 断言
 *
 *  viewModel.uiState.test {
 *      val initial = awaitItem()
 *      assertEquals(false, initial.isLoading)
 *      viewModel.load()
 *      val loading = awaitItem()
 *      assertEquals(true, loading.isLoading)
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  Compose UI 测试
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · onNodeWithText("文字").performClick()：查找并点击
 *  · onNodeWithTag("tag").assertIsDisplayed()：断言可见
 *  · composeTestRule.setContent { }：设置测试内容
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  测试覆盖率
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Jacoco：生成覆盖率报告
 *  · 核心业务逻辑覆盖率目标 > 80%
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · 测试命名用 backtick `应该做什么当某条件时`，可读性强
 *  · 优先用 MockK（Kotlin 原生），比 Mockito 更简洁
 *  · 使用 Turbine 库测试 Flow：val item = awaitItem()
 */

val unitTestData = NoteData(
    title = "单元测试与 UI 测试",
    subtitle = "进阶开发能力 · JUnit · MockK · Compose UI Test",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("1",   "单元测试"),
        ChapterItem("1.1", "JUnit"),
        ChapterItem("1.2", "MockK（推荐）"),
        ChapterItem("2",   "协程测试"),
        ChapterItem("2.1", "ViewModel 测试"),
        ChapterItem("2.2", "Flow 测试（Turbine）"),
        ChapterItem("3",   "Compose UI 测试"),
        ChapterItem("4",   "测试覆盖率"),
        ChapterItem("5",   "最佳实践"),
    )
)
