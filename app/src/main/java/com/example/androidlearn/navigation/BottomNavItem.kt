package com.example.androidlearn.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val sublabel: String,
    val icon: ImageVector
) {
    object Junior : BottomNavItem(
        route = "junior",
        label = "初级",
        sublabel = "1～3 年",
        icon = Icons.Filled.Person
    )

    object Intermediate : BottomNavItem(
        route = "intermediate",
        label = "中级",
        sublabel = "3～5 年",
        icon = Icons.Filled.Build
    )

    object Senior : BottomNavItem(
        route = "senior",
        label = "高级",
        sublabel = "5～10 年",
        icon = Icons.Filled.Star
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Junior,
    BottomNavItem.Intermediate,
    BottomNavItem.Senior
)
