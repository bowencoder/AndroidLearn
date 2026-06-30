package com.example.androidlearn.feature.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 主题详情数据模型。
 * 每个独立 Screen 文件将自己的 [TopicDetail] 数据内嵌为 private val，
 * 通过 [TopicDetailScaffold] 传入，不依赖任何全局列表。
 *
 * @param title       主题标题
 * @param description 副标题/简短描述
 * @param overview    概述段落
 * @param keyPoints   核心知识点列表
 * @param codeSnippet 示例代码（可选）
 * @param tips        学习小贴士列表
 */
data class TopicDetail(
    val title: String,
    val description: String,
    val overview: String,
    val keyPoints: List<String>,
    val codeSnippet: String? = null,
    val tips: List<String>
)

/**
 * 通用主题详情页脚手架（数据直传版）。
 * 每个独立的 TopicScreen 将 [TopicDetail] 数据直接内嵌在文件中，通过此重载传入，
 * 不再依赖全局 topicDetails 列表的索引查找。
 *
 * @param detail      主题详情数据（直接由调用方提供）
 * @param stageColor  所属阶段主题色（用于 TopAppBar 背景与强调色）
 * @param stageTitle  所属阶段标题（显示在 TopAppBar 副标题）
 * @param onBack      返回回调
 * @param practiceContent 实践区 Composable，用于编写该主题专属的测试代码
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScaffold(
    detail: TopicDetail,
    stageColor: Color,
    stageTitle: String,
    onBack: () -> Unit,
    practiceContent: @Composable () -> Unit = {}
) {
    TopicDetailScaffoldContent(
        detail = detail,
        stageColor = stageColor,
        stageTitle = stageTitle,
        onBack = onBack,
        practiceContent = practiceContent
    )
}

/** 内部实现 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicDetailScaffoldContent(
    detail: TopicDetail,
    stageColor: Color,
    stageTitle: String,
    onBack: () -> Unit,
    practiceContent: @Composable () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = detail.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                        Text(
                            text = stageTitle,
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
                    containerColor = stageColor,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SectionCard(title = "概述", titleColor = stageColor) {
                    Text(
                        text = detail.overview,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
            item {
                SectionCard(title = "核心知识点", titleColor = stageColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        detail.keyPoints.forEachIndexed { i, point ->
                            KeyPointRow(index = i + 1, text = point, color = stageColor)
                        }
                    }
                }
            }
            if (detail.codeSnippet != null) {
                item {
                    SectionCard(title = "示例代码", titleColor = stageColor) {
                        CodeBlock(code = detail.codeSnippet)
                    }
                }
            }
            item {
                SectionCard(title = "学习小贴士", titleColor = stageColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        detail.tips.forEach { tip -> TipRow(text = tip, color = stageColor) }
                    }
                }
            }
            // 实践区：每个独立 Screen 在此插入专属测试代码
            item {
                practiceContent()
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ──────────────────────────────────────────
// 公共 UI 组件（供各 Screen 复用）
// ──────────────────────────────────────────

@Composable
fun SectionCard(
    title: String,
    titleColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(titleColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun KeyPointRow(index: Int, text: String, color: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(color.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$index", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.width(10.dp))
        val colonIdx = text.indexOf('：')
        if (colonIdx >= 0) {
            Column {
                Text(
                    text = text.substring(0, colonIdx),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = text.substring(colonIdx + 1),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        } else {
            Text(text = text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
        }
    }
}

@Composable
fun CodeBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1E1E2E))
    ) {
        Text(
            text = code,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(14.dp),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFFCDD6F4),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun TipRow(text: String, color: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Text(text = "💡", fontSize = 13.sp, modifier = Modifier.padding(top = 1.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
    }
}

/**
 * 实践区卡片模板，供各 Screen 的 PracticeSection 使用。
 */
@Composable
fun PracticeCard(title: String = "实践区", color: Color = Color(0xFF607D8B), content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "✏️ 在此编写测试代码", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
