package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "开发工具：Git / Gradle / ADB",
    description = "Git 版本控制、Gradle 构建配置、ADB 调试命令",
    overview = "掌握 Git、Gradle 和 ADB 是 Android 开发者的基础工程能力。Git 管理代码历史，Gradle 驱动项目构建，ADB 连接真机/模拟器进行调试操作。",
    keyPoints = listOf(
        "Git 核心命令：clone/add/commit/push/pull，分支管理（branch/checkout/merge/rebase）",
        "Git Flow：feature/develop/release/hotfix 分支模型，适合团队协作",
        "Gradle 构建：Project 与 Module 结构，dependencies/buildTypes/productFlavors 配置",
        "libs.versions.toml：Version Catalog 统一管理依赖版本，推荐用于多模块项目",
        "ADB 常用命令：adb devices / install / shell / logcat / pull / push / forward",
        "ADB 调试技巧：am start 启动 Activity，pm list packages 查包名，dumpsys 查系统状态"
    ),
    codeSnippet = """
# Git 常用操作
git clone https://github.com/example/repo.git
git checkout -b feature/login        # 新建并切换分支
git add .                            # 暂存所有修改
git commit -m "feat: 添加登录功能"
git push origin feature/login

git merge develop                    # 合并 develop 到当前分支
git rebase develop                   # 变基（保持线性历史）
git stash / git stash pop            # 临时保存/恢复工作区

# Gradle 常用命令
./gradlew assembleDebug              # 构建 Debug APK
./gradlew :app:dependencies          # 查看依赖树
./gradlew clean                      # 清理构建缓存

# ADB 常用命令
adb devices                          # 查看连接设备
adb install app-debug.apk            # 安装 APK
adb shell am start -n com.example/.MainActivity  # 启动 Activity
adb logcat -s "MyTag"                # 过滤日志
adb shell dumpsys activity top       # 查看栈顶 Activity
adb pull /sdcard/test.log ./         # 从设备拉取文件
    """.trimIndent(),
    tips = listOf(
        "Git commit message 推荐 Conventional Commits 规范：feat/fix/docs/refactor/chore",
        "Gradle 构建慢时，开启 --parallel 并行构建和 --build-cache 缓存",
        "用 adb logcat --pid=$(adb shell pidof -s com.example.app) 只看自己 App 的日志"
    )
)

@Composable
fun DevToolsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
