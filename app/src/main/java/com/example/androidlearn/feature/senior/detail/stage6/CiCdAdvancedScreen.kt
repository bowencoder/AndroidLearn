package com.example.androidlearn.feature.senior.detail.stage6

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "CI/CD 与发布工程",
    description = "GitHub Actions，Fastlane，多渠道打包，Play Store API",
    overview = "完善的 CI/CD 流水线让发布从「手工操作」变为「一键自动化」，是工程效能的核心基础设施。",
    keyPoints = listOf(
        "流水线阶段：代码检查 → 单元测试 → 集成测试 → 打包 → 分发 → 上线",
        "GitHub Actions：YAML 定义 workflow，matrix 并行多 Android 版本测试",
        "Fastlane：Ruby 工具链，supply 上传 Play Store，截图自动化",
        "多渠道打包：productFlavors 定义渠道，Walle / VasDolly 写入渠道信息",
        "Firebase App Distribution：测试包快速分发，替代蒲公英",
        "灰度发布：Play Store 分阶段发布（1% → 10% → 全量）"
    ),
    codeSnippet = """
# .github/workflows/release.yml
name: Release Pipeline
on:
  push:
    tags: ['v*']

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./gradlew testReleaseUnitTest lint

  build-and-deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Sign & Build AAB
        env:
          KEYSTORE_B64: ${'$'}{{ secrets.KEYSTORE_B64 }}
          KEY_ALIAS: ${'$'}{{ secrets.KEY_ALIAS }}
          KEY_PASS: ${'$'}{{ secrets.KEY_PASS }}
        run: |
          echo ${'$'}KEYSTORE_B64 | base64 -d > keystore.jks
          ./gradlew bundleRelease
      - name: Upload to Play Store
        uses: r0adkll/upload-google-play@v1
        with:
          serviceAccountJsonPlainText: ${'$'}{{ secrets.PLAY_JSON }}
          packageName: com.example.app
          releaseFiles: app/build/outputs/bundle/release/*.aab
          track: internal
    """.trimIndent(),
    tips = listOf(
        "Keystore 用 Base64 编码存入 GitHub Secrets，CI 中解码使用",
        "Release 流水线触发条件建议绑定 Git tag，而非 branch push",
        "集成 Slack/钉钉通知，流水线成功/失败实时推送给团队"
    )
)

@Composable
fun CiCdAdvancedScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "架构设计与前沿技术",
        onBack = onBack
    )
}
