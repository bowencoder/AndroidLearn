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
import com.example.androidlearn.feature.junior.detail.stage2.DialogScreen
import com.example.androidlearn.feature.junior.detail.stage2.FragmentScreen
import com.example.androidlearn.feature.junior.detail.stage2.LayoutViewScreen
import com.example.androidlearn.feature.junior.detail.stage2.PermissionScreen
import com.example.androidlearn.feature.junior.detail.stage2.ServiceScreen
import com.example.androidlearn.feature.junior.detail.stage3.DataStoreScreen
import com.example.androidlearn.feature.junior.detail.stage3.ImageLoadingScreen
import com.example.androidlearn.feature.junior.detail.stage3.MediaBasicsScreen
import com.example.androidlearn.feature.junior.detail.stage3.NetworkRequestScreen
import com.example.androidlearn.feature.junior.detail.stage3.RecyclerViewScreen
import com.example.androidlearn.feature.junior.detail.stage3.RoomDatabaseScreen
import com.example.androidlearn.feature.junior.detail.stage3.SharedPreferencesScreen

/**
 * 初级工程师 Tab 路由（stageIndex 0, 1, 2）
 *
 *  Stage 0（语言与工具基础）→ stage1/:
 *    0=JavaBasics  1=KotlinSyntax  2=AndroidStudio
 *    3=DevTools    4=IntentNavigation  5=ResourceManagement
 *
 *  Stage 1（四大组件）→ stage2/:
 *    0=ActivityLifecycle  1=Fragment   2=Dialog
 *    3=Service            4=Broadcast  5=ContentProvider
 *    6=LayoutView         7=Permission
 *
 *  Stage 2（UI 组件与数据基础）→ stage3/:
 *    0=RecyclerView  1=RoomDatabase      2=SharedPreferences
 *    3=DataStore     4=NetworkRequest    5=ImageLoading
 *    6=MediaBasics
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
            1 -> FragmentScreen(onBack)
            2 -> DialogScreen(onBack)
            3 -> ServiceScreen(onBack)
            4 -> BroadcastScreen(onBack)
            5 -> ContentProviderScreen(onBack)
            6 -> LayoutViewScreen(onBack)
            7 -> PermissionScreen(onBack)
        }
        // ── Stage 2 : UI 组件与数据基础 ───────────────────────
        2 -> when (topicIndex) {
            0 -> RecyclerViewScreen(onBack)
            1 -> RoomDatabaseScreen(onBack)
            2 -> SharedPreferencesScreen(onBack)
            3 -> DataStoreScreen(onBack)
            4 -> NetworkRequestScreen(onBack)
            5 -> ImageLoadingScreen(onBack)
            6 -> MediaBasicsScreen(onBack)
        }
    }
}
