package com.example.androidlearn.feature.intermediate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidlearn.feature.junior.LevelBadge
import com.example.androidlearn.feature.junior.StageCard
import com.example.androidlearn.feature.junior.intermediateRoadmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntermediateScreen(onTopicClick: (stageIndex: Int, topicIndex: Int) -> Unit = { _, _ -> }) {
    val levelColor = Color(0xFF9C27B0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.foundation.layout.Column {
                        Text(
                            text = "中级工程师",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "3 ～ 5 年经验  ·  构建现代化应用架构",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = levelColor,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                LevelBadge(
                    label = "中级",
                    yearRange = "3 ～ 5 年",
                    description = "掌握 MVVM、协程、Compose 等现代架构体系，能主导模块设计与技术选型，具备一定的性能优化能力。",
                    color = levelColor
                )
            }
            items(intermediateRoadmap, key = { it.index }) { stage ->
                StageCard(stage = stage, onTopicClick = onTopicClick)
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}
