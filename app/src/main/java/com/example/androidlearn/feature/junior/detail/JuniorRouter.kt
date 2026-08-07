package com.example.androidlearn.feature.junior.detail

import androidx.compose.runtime.Composable
import com.example.androidlearn.feature.junior.detail.stage1.AndroidStudioScreen
import com.example.androidlearn.feature.junior.detail.stage1.DevToolsScreen
import com.example.androidlearn.feature.junior.detail.stage1.IntentNavigationScreen
import com.example.androidlearn.feature.junior.detail.stage1.JavaBasicsScreen
import com.example.androidlearn.feature.junior.detail.stage1.KotlinSyntaxScreen
import com.example.androidlearn.feature.junior.detail.stage1.ResourceManagementScreen
import com.example.androidlearn.feature.junior.detail.stage2.ActivityLifecycleScreen
import com.example.androidlearn.feature.junior.detail.stage2.BroadcastScreen
import com.example.androidlearn.feature.junior.detail.stage2.ContentProviderScreen
import com.example.androidlearn.feature.junior.detail.stage2.LayoutViewScreen
import com.example.androidlearn.feature.junior.detail.stage2.PermissionScreen
import com.example.androidlearn.feature.junior.detail.stage2.ServiceScreen
import com.example.androidlearn.feature.junior.detail.stage3.DataStoreScreen
import com.example.androidlearn.feature.junior.detail.stage3.ImageLoadingScreen
import com.example.androidlearn.feature.junior.detail.stage3.MediaBasicsScreen
import com.example.androidlearn.feature.junior.detail.stage3.NetworkRequestScreen
import com.example.androidlearn.feature.junior.detail.stage3.RecyclerViewScreen
import com.example.androidlearn.feature.junior.detail.stage3.RoomDatabaseScreen

/**
 * 初级工程师 Tab 路由（stageIndex 0, 1, 2）
 *
 *  Stage 0（语言与工具基础）→ stage1/:
 *    0=JavaBasics  1=KotlinSyntax  2=AndroidStudio
 *    3=DevTools    4=IntentNavigation  5=ResourceManagement
 *
 *  Stage 1（四大组件）→ stage2/:
 *    0=ActivityLifecycle  1=Service  2=Broadcast
 *    3=ContentProvider    4=LayoutView  5=Permission
 *
 *  Stage 2（UI 组件与数据基础）→ stage3/:
 *    0=RecyclerView  1=RoomDatabase  2=NetworkRequest
 *    3=ImageLoading  4=DataStore     5=MediaBasics
 */
@Composable
fun JuniorRouter(stageIndex: Int, topicIndex: Int, onBack: () -> Unit) {
    when (stageIndex) {
        // ── Stage 0 : 语言与工具基础 ──────────────────────────
        0 -> when (topicIndex) {
            0 -> JavaBasicsScreen(onBack)
            1 -> KotlinSyntaxScreen(onBack)
            2 -> AndroidStudioScreen(onBack)
            3 -> DevToolsScreen(onBack)
            4 -> IntentNavigationScreen(onBack)
            5 -> ResourceManagementScreen(onBack)
        }
        // ── Stage 1 : 四大组件 ────────────────────────────────
        1 -> when (topicIndex) {
            0 -> ActivityLifecycleScreen(onBack)
            1 -> ServiceScreen(onBack)
            2 -> BroadcastScreen(onBack)
            3 -> ContentProviderScreen(onBack)
            4 -> LayoutViewScreen(onBack)
            5 -> PermissionScreen(onBack)
        }
        // ── Stage 2 : UI 组件与数据基础 ───────────────────────
        2 -> when (topicIndex) {
            0 -> RecyclerViewScreen(onBack)
            1 -> RoomDatabaseScreen(onBack)
            2 -> NetworkRequestScreen(onBack)
            3 -> ImageLoadingScreen(onBack)
            4 -> DataStoreScreen(onBack)
            5 -> MediaBasicsScreen(onBack)
        }
    }
}
