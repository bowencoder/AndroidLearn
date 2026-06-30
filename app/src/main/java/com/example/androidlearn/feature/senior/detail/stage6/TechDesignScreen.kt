package com.example.androidlearn.feature.senior.detail.stage6

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "技术方案与架构评审",
    description = "需求拆解、方案评估、Trade-off 决策、技术文档",
    overview = "高级工程师不只写代码，更要主导技术决策，能在约束条件下做出最优方案，并清晰地对齐团队共识。",
    keyPoints = listOf(
        "需求拆解：从产品需求推导技术需求，识别技术风险和不确定性",
        "方案评估维度：可行性、可维护性、性能、安全、开发成本",
        "Trade-off 决策：没有最好的方案，只有最合适当前阶段的方案",
        "RFC 文档：Request For Comments，描述背景/方案/替代方案/影响",
        "架构评审：Checklist 驱动，覆盖数据流、错误处理、降级策略",
        "技术债务管理：量化债务，制定还债计划，纳入迭代排期"
    ),
    codeSnippet = """
# 技术方案文档模板（RFC）
## 背景与问题
当前列表页首屏加载 P90 耗时 3.2s，用户投诉体验差。

## 方案对比
| 方案 | 优点 | 缺点 |
|------|------|------|
| 预加载 | 实现简单 | 浪费流量 |
| 骨架屏 | 感知体验好 | 需要额外 UI |
| 数据分页 | 根本解决 | 改动较大 |

## 推荐方案
采用 Paging 3 分页 + 骨架屏组合方案...

## 影响范围
- 修改文件：HomeRepository, HomeViewModel, HomeScreen
- 需要 QA 回归：首页、搜索、我的三个页面

## 风险
- 分页边界状态处理复杂，需充分测试
    """.trimIndent(),
    tips = listOf(
        "方案文档先写「为什么」再写「怎么做」，帮助读者理解决策背景",
        "邀请不同背景的人评审，前端、后端、QA 视角互补",
        "决策后记录结论和原因，6 个月后能快速回溯当时的判断"
    )
)

@Composable
fun TechDesignScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF3F51B5),
        stageTitle = "架构设计与前沿技术",
        onBack = onBack
    )
}
