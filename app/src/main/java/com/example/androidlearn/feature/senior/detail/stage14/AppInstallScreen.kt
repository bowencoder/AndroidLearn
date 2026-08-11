package com.example.androidlearn.feature.senior.detail.stage14

import android.graphics.Color
import com.example.androidlearn.feature.intermediate.detail.ChapterItem
import com.example.androidlearn.feature.intermediate.detail.NoteData

val appInstallData = NoteData(
    title = "应用安装过程",
    subtitle = "PMS 解析 APK、dex2oat 预编译、数据目录创建与多用户安装机制",
    color = Color.parseColor("#3F51B5"),
    chapters = listOf(
        ChapterItem("1",   "安装入口"),
        ChapterItem("1.1", "PackageInstaller（用户触发）或 adb install（通过 adbd → installd）"),
        ChapterItem("2",   "PMS 职责"),
        ChapterItem("2.1", "解析 AndroidManifest.xml、校验签名、分配 UID、注册 Package 信息到数据库"),
        ChapterItem("3",   "dex2oat"),
        ChapterItem("3.1", "将 DEX 字节码 AOT 编译为本地机器码（.oat/.art 文件），存放在 /data/dalvik-cache/"),
        ChapterItem("4",   "文件布局"),
        ChapterItem("4.1", "APK 本体 → /data/app/包名/；用户数据 → /data/data/包名/（shared_prefs、databases）"),
        ChapterItem("5",   "多用户安装"),
        ChapterItem("5.1", "Android 支持多用户，/data/user/0/包名 是 user 0 的数据目录（0 号用户为主用户）"),
        ChapterItem("6",   "广播通知"),
        ChapterItem("6.1", "安装完成后 PMS 发送 ACTION_PACKAGE_ADDED 广播，Launcher 监听此广播刷新图标"),
    )
)
