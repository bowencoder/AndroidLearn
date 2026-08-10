package com.example.androidlearn.feature.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 笔记详情页通用章节数据模型。
 *
 * @param num   章节编号，如 "1"、"2"
 * @param title 章节标题
 */
data class NoteChapter(val num: String, val title: String)

/**
 * 笔记详情页通用脚手架。
 * 所有 junior 阶段的 Screen 文件均通过此组件渲染，
 * 只需传入标题、副标题、主题色、章节列表和返回回调。
 *
 * @param title          页面主标题（显示在 TopAppBar）
 * @param subtitle       页面副标题（显示在 TopAppBar 主标题下方）
 * @param color          主题色（TopAppBar 背景色 & 章节编号强调色）
 * @param chapters       章节列表
 * @param onBack         返回回调
 * @param onChapterClick 章节行点击回调，默认空实现
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScaffold(
    title: String,
    subtitle: String,
    color: Color,
    chapters: List<NoteChapter>,
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters) { chapter ->
                NoteChapterRow(
                    chapter = chapter,
                    color = color,
                    onClick = { onChapterClick(chapter) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

/**
 * 笔记详情页通用章节行组件。
 * 左侧显示带主题色背景的章节编号徽章，右侧显示章节标题。
 *
 * @param chapter 章节数据
 * @param color   主题色（编号徽章背景色 & 文字色）
 * @param onClick 点击回调，默认空实现
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteChapterRow(
    chapter: NoteChapter,
    color: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = color.copy(alpha = 0.12f)
            ) {
                Text(
                    text = chapter.num,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = chapter.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
