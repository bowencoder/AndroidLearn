package com.example.androidlearn.feature.senior

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
import com.example.androidlearn.feature.junior.seniorRoadmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorScreen(onTopicClick: (stageIndex: Int, topicIndex: Int) -> Unit = { _, _ -> }) {
    val levelColor = Color(0xFFFF5722)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.foundation.layout.Column {
                        Text(
                            text = "高级工程师",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "5 ～ 10 年经验  ·  性能 · 架构 · 前沿技术",
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
                    label = "高级",
                    yearRange = "5 ～ 10 年",
                    description = "具备深度性能优化、架构设计与工程化能力，能主导大型项目技术决策，推动团队工程效能提升。",
                    color = levelColor
                )
            }
            items(seniorRoadmap, key = { it.index }) { stage ->
                StageCard(stage = stage, onTopicClick = onTopicClick)
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}
