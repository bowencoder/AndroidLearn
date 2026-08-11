package com.example.androidlearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidlearn.navigation.BottomNavItem
import com.example.androidlearn.navigation.bottomNavItems
import com.example.androidlearn.feature.junior.JuniorScreen
import com.example.androidlearn.feature.intermediate.IntermediateScreen
import com.example.androidlearn.feature.senior.SeniorScreen
import com.example.androidlearn.feature.junior.detail.JuniorRouter
import com.example.androidlearn.feature.intermediate.detail.NoteDetailActivity
import com.example.androidlearn.feature.senior.detail.SeniorNoteDetailActivity
import com.example.androidlearn.ui.theme.AndroidLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLearnTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 在详情页（topic_detail）时隐藏底部 Tab
    val showBottomBar = currentDestination?.route?.startsWith("topic_detail") != true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Junior.route,
            modifier = Modifier.padding(
                bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            // ── 初级工程师 Tab ─────────────────────────────
            composable(BottomNavItem.Junior.route) {
                JuniorScreen(
                    onTopicClick = { stageIndex, topicIndex ->
                        navController.navigate("topic_detail/$stageIndex/$topicIndex")
                    }
                )
            }
            // ── 中级工程师 Tab ─────────────────────────────
            composable(BottomNavItem.Intermediate.route) {
                val context = LocalContext.current
                IntermediateScreen(
                    onTopicClick = { stageIndex, topicIndex ->
                        NoteDetailActivity.start(context, stageIndex, topicIndex)
                    }
                )
            }
            // ── 高级工程师 Tab ─────────────────────────────
            composable(BottomNavItem.Senior.route) {
                val context = LocalContext.current
                SeniorScreen(
                    onTopicClick = { stageIndex, topicIndex ->
                        SeniorNoteDetailActivity.start(context, stageIndex, topicIndex)
                    }
                )
            }
            // ── 主题详情页（初级专用）─────────────────────────
            composable(
                route = "topic_detail/{stageIndex}/{topicIndex}",
                arguments = listOf(
                    navArgument("stageIndex") { type = NavType.IntType },
                    navArgument("topicIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val stageIndex = backStackEntry.arguments?.getInt("stageIndex") ?: 0
                val topicIndex = backStackEntry.arguments?.getInt("topicIndex") ?: 0
                JuniorRouter(stageIndex, topicIndex, onBack = { navController.popBackStack() })
            }
        }
    }
}
