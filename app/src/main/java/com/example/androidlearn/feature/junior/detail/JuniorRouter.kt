package com.example.androidlearn.feature.junior.detail

import androidx.compose.runtime.Composable
import com.example.androidlearn.feature.junior.detail.stage1.AndroidStudioScreen
import com.example.androidlearn.feature.junior.detail.stage1.DevToolsScreen
import com.example.androidlearn.feature.junior.detail.stage1.JavaBasicsScreen
import com.example.androidlearn.feature.junior.detail.stage1.KotlinSyntaxScreen
import com.example.androidlearn.feature.junior.detail.stage1.ResourceManagementScreen
import com.example.androidlearn.feature.junior.detail.stage2.ActivityLifecycleScreen
import com.example.androidlearn.feature.junior.detail.stage2.BroadcastScreen
import com.example.androidlearn.feature.junior.detail.stage2.ContentProviderScreen
import com.example.androidlearn.feature.junior.detail.stage2.ServiceScreen
import com.example.androidlearn.feature.junior.detail.stage3.AndroidArchScreen
import com.example.androidlearn.feature.junior.detail.stage3.DataStorageScreen
import com.example.androidlearn.feature.junior.detail.stage3.FragmentScreen
import com.example.androidlearn.feature.junior.detail.stage3.LayoutViewScreen
import com.example.androidlearn.feature.junior.detail.stage3.MediaBasicsScreen
import com.example.androidlearn.feature.junior.detail.stage3.NetworkRequestScreen
import com.example.androidlearn.feature.junior.detail.stage3.PermissionScreen
import com.example.androidlearn.feature.junior.detail.stage3.RecyclerViewScreen
import com.example.androidlearn.feature.junior.detail.stage3.ViewScreen
import com.example.androidlearn.feature.shared.NoteChapter

/**
 * 初级工程师 Tab 路由（stageIndex 0, 1, 2）
 *
 *  Stage 0（语言与工具基础）→ stage1/:
 *    0=JavaBasics  1=KotlinSyntax  2=AndroidStudio
 *    3=DevTools    4=ResourceManagement
 *
 *  Stage 1（四大组件）→ stage2/:
 *    0=ActivityLifecycle  1=Service    2=Broadcast  3=ContentProvider
 *
 *  Stage 2（UI 组件与数据基础）→ stage3/:
 *    0=AndroidArch   1=LayoutView    2=View          3=Fragment
 *    4=RecyclerView  5=Permission    6=DataStorage
 *    7=NetworkRequest  8=MediaBasics
 */
@Composable
fun JuniorRouter(
    stageIndex: Int,
    topicIndex: Int,
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    when (stageIndex) {
        // ── Stage 0 : 语言与工具基础 ──────────────────────────
        0 -> when (topicIndex) {
            0 -> JavaBasicsScreen(onBack, onChapterClick)
            1 -> KotlinSyntaxScreen(onBack, onChapterClick)
            2 -> AndroidStudioScreen(onBack, onChapterClick)
            3 -> DevToolsScreen(onBack, onChapterClick)
            4 -> ResourceManagementScreen(onBack, onChapterClick)
        }
        // ── Stage 1 : 四大组件 ────────────────────────────────
        1 -> when (topicIndex) {
            0 -> ActivityLifecycleScreen(onBack, onChapterClick)
            1 -> ServiceScreen(onBack, onChapterClick)
            2 -> BroadcastScreen(onBack, onChapterClick)
            3 -> ContentProviderScreen(onBack, onChapterClick)
        }
        // ── Stage 2 : UI 组件与数据基础 ───────────────────────
        2 -> when (topicIndex) {
            0 -> AndroidArchScreen(onBack, onChapterClick)
            1 -> LayoutViewScreen(onBack, onChapterClick)
            2 -> ViewScreen(onBack, onChapterClick)
            3 -> FragmentScreen(onBack, onChapterClick)
            4 -> RecyclerViewScreen(onBack, onChapterClick)
            5 -> PermissionScreen(onBack, onChapterClick)
            6 -> DataStorageScreen(onBack, onChapterClick)
            7 -> NetworkRequestScreen(onBack, onChapterClick)
            8 -> MediaBasicsScreen(onBack, onChapterClick)
        }
    }
}
