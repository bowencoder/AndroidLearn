package com.example.androidlearn.feature.intermediate.detail.stage4

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "CI/CD 流程",
    description = "Gradle 脚本、自动化构建发布流水线",
    overview = "CI/CD 自动化构建、测试、打包和发布，提高交付效率，减少人为错误，是团队工程化的重要实践。",
    keyPoints = listOf(
        "GitHub Actions / GitLab CI：编写 YAML 配置流水线",
        "Gradle 任务：assembleDebug / assembleRelease / test / lint",
        "签名配置：通过环境变量安全传入 Keystore",
        "自动化测试：PR 触发单元测试，失败则阻止合并",
        "发布：Fastlane supply / Google Play API 自动上传",
        "代码质量：集成 Lint / Detekt / Spotless 检查"
    ),
    codeSnippet = """
# .github/workflows/android.yml
name: Android CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '17' }
      - name: Run Tests
        run: ./gradlew test
      - name: Build Release
        run: ./gradlew assembleRelease
      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: release-apk
          path: app/build/outputs/apk/release/
    """.trimIndent(),
    tips = listOf(
        "Keystore 密码用 GitHub Secrets 存储，不提交到代码库",
        "每次 PR 必须通过所有测试和 Lint 检查才能合并",
        "使用 Gradle Build Cache 加速 CI 编译"
    )
)

@Composable
fun CiCdScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFFFF9800),
        stageTitle = "进阶开发能力",
        onBack = onBack
    )
}
