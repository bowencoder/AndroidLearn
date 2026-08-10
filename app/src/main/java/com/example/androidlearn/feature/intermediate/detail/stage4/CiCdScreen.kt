package com.example.androidlearn.feature.intermediate.detail.stage4

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

/*
 * CI/CD 流程
 * 官方文档：https://developer.android.com/studio/build
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  CI 持续集成
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 1.1  GitHub Actions ───────────────────────────────────────────────────────
 *
 *  # .github/workflows/android.yml
 *  name: Android CI
 *  on: [push, pull_request]
 *  jobs:
 *    build:
 *      runs-on: ubuntu-latest
 *      steps:
 *        - uses: actions/checkout@v4
 *        - uses: actions/setup-java@v4
 *          with: { java-version: '17' }
 *        - name: Run Tests
 *          run: ./gradlew test
 *        - name: Build Release
 *          run: ./gradlew assembleRelease
 *        - name: Upload APK
 *          uses: actions/upload-artifact@v4
 *          with:
 *            name: release-apk
 *            path: app/build/outputs/apk/release/
 *
 * ── 1.2  Gradle 常用任务 ──────────────────────────────────────────────────────
 *
 *  · assembleDebug / assembleRelease：打包 APK
 *  · test：运行单元测试
 *  · lint：代码质量检查
 *  · connectedAndroidTest：运行 UI 测试（需要设备）
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  CD 持续交付
 * ════════════════════════════════════════════════════════════════════════════
 *
 * ── 2.1  签名配置 ─────────────────────────────────────────────────────────────
 *
 *  · Keystore 密码用 GitHub Secrets 存储，不提交到代码库
 *
 *  android {
 *      signingConfigs {
 *          release {
 *              storeFile = file(System.getenv("KEYSTORE_PATH"))
 *              storePassword = System.getenv("KEYSTORE_PASSWORD")
 *              keyAlias = System.getenv("KEY_ALIAS")
 *              keyPassword = System.getenv("KEY_PASSWORD")
 *          }
 *      }
 *  }
 *
 * ── 2.2  自动发布 ─────────────────────────────────────────────────────────────
 *
 *  · Fastlane supply：上传 APK 到 Google Play
 *  · Google Play API：自动化发布到内测/正式轨道
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  代码质量门禁
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Lint：Android 官方静态分析
 *  · Detekt：Kotlin 代码风格检查
 *  · Spotless：代码格式化（ktlint）
 *  · 每次 PR 必须通过所有检查才能合并
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Keystore 密码用 GitHub Secrets 存储，不提交到代码库
 *  · 每次 PR 必须通过所有测试和 Lint 检查才能合并
 *  · 使用 Gradle Build Cache 加速 CI 编译
 */

val ciCdData = NoteData(
    title = "CI/CD 流程",
    subtitle = "进阶开发能力 · Gradle 脚本 · 自动化构建发布",
    color = Color.parseColor("#FF9800"),
    chapters = listOf(
        ChapterItem("1",   "CI 持续集成"),
        ChapterItem("1.1", "GitHub Actions"),
        ChapterItem("1.2", "Gradle 常用任务"),
        ChapterItem("2",   "CD 持续交付"),
        ChapterItem("2.1", "签名配置"),
        ChapterItem("2.2", "自动发布"),
        ChapterItem("3",   "代码质量门禁"),
        ChapterItem("4",   "最佳实践"),
    )
)
