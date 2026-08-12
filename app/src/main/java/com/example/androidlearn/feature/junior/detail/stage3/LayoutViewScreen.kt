package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 布局容器笔记
 * 官方文档：https://developer.android.com/develop/ui/views/layout/declaring-layout
 *
 * ── 1  LinearLayout ───────────────────────────────────────────────────────────
 *
 *  【容器自身属性】
 *  android:orientation         水平(horizontal) / 垂直(vertical)，必填
 *  android:gravity             所有子 View 的默认对齐方式（center / start / end / top | center_vertical 等）
 *  android:baselineAligned     false：关闭基线对齐，提升性能（子 View 含 TextView 时默认 true）
 *  android:divider             分割线 Drawable（配合 showDividers 使用）
 *  android:showDividers        none / beginning / middle / end（分割线显示位置）
 *  android:dividerPadding      分割线两端内边距
 *  android:weightSum           权重总和（默认为所有子 View weight 之和，可手动指定）
 *
 *  【子 View 属性（layout_xxx）】
 *  android:layout_weight       权重，按比例分配剩余空间（设置后对应方向尺寸建议设 0dp）
 *  android:layout_gravity      覆盖父容器 gravity，控制该子 View 自身的对齐方式
 *  android:layout_margin       外边距（可细分 marginStart/End/Top/Bottom）
 *
 *  // 水平均分两列（正确写法：layout_width="0dp"）
 *  <LinearLayout
 *      android:orientation="horizontal"
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content"
 *      android:gravity="center_vertical"
 *      android:divider="@drawable/divider"
 *      android:showDividers="middle">
 *      <TextView
 *          android:layout_width="0dp"
 *          android:layout_height="wrap_content"
 *          android:layout_weight="1"
 *          android:text="左"/>
 *      <Button
 *          android:layout_width="0dp"
 *          android:layout_height="wrap_content"
 *          android:layout_weight="2"
 *          android:text="右（占 2/3）"/>
 *  </LinearLayout>
 *
 *  ⚠️ 嵌套 LinearLayout 会增加测量次数（每层 weight 触发二次测量），深层嵌套时改用 ConstraintLayout
 *
 *
 * ── 2  FrameLayout ────────────────────────────────────────────────────────────
 *
 *  【容器自身属性】
 *  android:foreground          前景 Drawable（叠加在所有子 View 之上，常用于点击水波纹）
 *  android:foregroundGravity   前景 Drawable 的对齐方式
 *  android:measureAllChildren  true：即使子 View 为 GONE 也参与测量（默认 false）
 *
 *  【子 View 属性（layout_xxx）】
 *  android:layout_gravity      子 View 在 FrameLayout 内的位置
 *                              可组合：top|start / bottom|end / center / center_horizontal 等
 *  android:layout_margin       外边距（配合 layout_gravity 精确定位）
 *
 *  // 图片上叠加角标
 *  <FrameLayout
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      android:foreground="?attr/selectableItemBackground">
 *      <ImageView
 *          android:src="@drawable/avatar"
 *          android:layout_width="48dp"
 *          android:layout_height="48dp"/>
 *      <TextView
 *          android:text="99+"
 *          android:layout_gravity="top|end"
 *          android:layout_margin="2dp"
 *          android:background="@drawable/badge_bg"
 *          android:layout_width="wrap_content"
 *          android:layout_height="wrap_content"/>
 *  </FrameLayout>
 *
 *
 * ── 3  RelativeLayout ─────────────────────────────────────────────────────────
 *
 *  · 相对定位，子 View 可相对于父容器或兄弟 View 定位
 *  · 已被 ConstraintLayout 取代，新项目不推荐使用（两次测量，性能差于 ConstraintLayout）
 *
 *  【容器自身属性】
 *  android:gravity             所有子 View 的默认对齐方式
 *
 *  【子 View 相对父容器属性】
 *  android:layout_alignParentTop       = "true"  贴父容器顶部
 *  android:layout_alignParentBottom    = "true"  贴父容器底部
 *  android:layout_alignParentStart     = "true"  贴父容器起始边
 *  android:layout_alignParentEnd       = "true"  贴父容器结束边
 *  android:layout_centerInParent       = "true"  水平+垂直居中
 *  android:layout_centerHorizontal     = "true"  水平居中
 *  android:layout_centerVertical       = "true"  垂直居中
 *
 *  【子 View 相对兄弟 View 属性（值为兄弟 View 的 id）】
 *  android:layout_toStartOf    = "@id/xxx"  在目标 View 的起始侧
 *  android:layout_toEndOf      = "@id/xxx"  在目标 View 的结束侧
 *  android:layout_above        = "@id/xxx"  在目标 View 的上方
 *  android:layout_below        = "@id/xxx"  在目标 View 的下方
 *  android:layout_alignTop     = "@id/xxx"  与目标 View 顶部对齐
 *  android:layout_alignBottom  = "@id/xxx"  与目标 View 底部对齐
 *  android:layout_alignStart   = "@id/xxx"  与目标 View 起始边对齐
 *  android:layout_alignEnd     = "@id/xxx"  与目标 View 结束边对齐
 *  android:layout_alignBaseline= "@id/xxx"  与目标 View 基线对齐
 *
 *
 * ── 4  ConstraintLayout（推荐首选）────────────────────────────────────────────
 *
 * ── 4.1  基础约束 ─────────────────────────────────────────────────────────────
 *
 *  · 扁平化布局，一层解决大多数场景，性能优于多层嵌套
 *  · 水平和垂直方向各需至少一个约束，否则 View 位置为 (0,0)
 *
 *  【位置约束属性（值为 "parent" 或 "@id/xxx"）】
 *  app:layout_constraintTop_toTopOf          顶部对齐目标顶部
 *  app:layout_constraintTop_toBottomOf       顶部对齐目标底部（在目标下方）
 *  app:layout_constraintBottom_toTopOf       底部对齐目标顶部（在目标上方）
 *  app:layout_constraintBottom_toBottomOf    底部对齐目标底部
 *  app:layout_constraintStart_toStartOf      起始边对齐目标起始边
 *  app:layout_constraintStart_toEndOf        起始边对齐目标结束边（在目标右侧）
 *  app:layout_constraintEnd_toStartOf        结束边对齐目标起始边（在目标左侧）
 *  app:layout_constraintEnd_toEndOf          结束边对齐目标结束边
 *  app:layout_constraintBaseline_toBaselineOf 文字基线对齐（TextView 对齐常用）
 *
 *  【尺寸属性】
 *  android:layout_width / height = "0dp"     MATCH_CONSTRAINT：由约束决定尺寸（推荐替代 match_parent）
 *  app:layout_constraintWidth_percent        宽度占父容器百分比（0.0~1.0，需 layout_width="0dp"）
 *  app:layout_constraintHeight_percent       高度占父容器百分比
 *  app:layout_constraintDimensionRatio       宽高比（如 "16:9" 或 "H,16:9"）
 *  app:layout_constraintWidth_min/max        最小/最大宽度约束
 *  app:layout_constraintHeight_min/max       最小/最大高度约束
 *
 *  【偏移属性（两侧都有约束时生效，0.0~1.0）】
 *  app:layout_constraintHorizontal_bias      水平偏移（0=靠左，0.5=居中，1=靠右，默认 0.5）
 *  app:layout_constraintVertical_bias        垂直偏移
 *
 *  【外边距（需先设置对应方向的约束）】
 *  android:layout_marginTop/Bottom/Start/End
 *  app:layout_goneMarginTop/Bottom/Start/End  目标 View 为 GONE 时生效的边距
 *
 *  <androidx.constraintlayout.widget.ConstraintLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent">
 *
 *      <!-- 宽度撑满，顶部距父容器 16dp -->
 *      <TextView
 *          android:id="@+id/tvTitle"
 *          android:layout_width="0dp"
 *          android:layout_height="wrap_content"
 *          android:layout_margin="16dp"
 *          app:layout_constraintTop_toTopOf="parent"
 *          app:layout_constraintStart_toStartOf="parent"
 *          app:layout_constraintEnd_toEndOf="parent"/>
 *
 *      <!-- 宽高比 16:9，水平居中 -->
 *      <ImageView
 *          android:id="@+id/ivBanner"
 *          android:layout_width="0dp"
 *          android:layout_height="0dp"
 *          app:layout_constraintDimensionRatio="16:9"
 *          app:layout_constraintTop_toBottomOf="@id/tvTitle"
 *          app:layout_constraintStart_toStartOf="parent"
 *          app:layout_constraintEnd_toEndOf="parent"/>
 *
 *      <!-- 靠右下角，距边 16dp -->
 *      <Button
 *          android:id="@+id/btnOk"
 *          android:layout_width="wrap_content"
 *          android:layout_height="wrap_content"
 *          app:layout_constraintBottom_toBottomOf="parent"
 *          app:layout_constraintEnd_toEndOf="parent"
 *          android:layout_marginBottom="16dp"
 *          android:layout_marginEnd="16dp"/>
 *
 *  </androidx.constraintlayout.widget.ConstraintLayout>
 *
 *
 * ── 4.2  Guideline / Barrier / Group ─────────────────────────────────────────
 *
 *  · Guideline：辅助线（不可见），按百分比或固定距离划分区域
 *    - orientation="vertical" + layout_constraintGuide_percent="0.5"：垂直居中线
 *    - orientation="horizontal" + layout_constraintGuide_begin="56dp"：距顶 56dp 水平线
 *
 *  · Barrier：跟随多个 View 中最大尺寸的边缘（动态对齐）
 *    - app:barrierDirection="end"：取 referenced ids 中最右边的边缘
 *    - app:constraint_referenced_ids="tvName,tvAge"
 *
 *  · Group：批量控制多个 View 的可见性（VISIBLE / GONE / INVISIBLE）
 *    - app:constraint_referenced_ids="btnOk,btnCancel"
 *    - group.visibility = View.GONE  // 一行代码隐藏多个 View
 *
 *  // Guideline 示例
 *  <androidx.constraintlayout.widget.Guideline
 *      android:id="@+id/guideline"
 *      android:orientation="vertical"
 *      app:layout_constraintGuide_percent="0.4"/>
 *  <TextView
 *      app:layout_constraintEnd_toStartOf="@id/guideline" ... />
 *  <EditText
 *      app:layout_constraintStart_toEndOf="@id/guideline" ... />
 *
 *
 * ── 4.3  Chain（链式约束）────────────────────────────────────────────────────
 *
 *  · 多个 View 相互约束形成链，统一控制分布方式
 *  · 链头（第一个 View）设置 chainStyle：
 *    - spread（默认）：均匀分布，两端有间距
 *    - spread_inside：均匀分布，两端无间距
 *    - packed：紧凑排列，整体居中
 *  · 配合 layout_width="0dp" + layout_weight 实现按比例分配
 *
 *  <Button android:id="@+id/btn1"
 *      app:layout_constraintStart_toStartOf="parent"
 *      app:layout_constraintEnd_toStartOf="@id/btn2"
 *      app:layout_constraintHorizontal_chainStyle="spread_inside" ... />
 *  <Button android:id="@+id/btn2"
 *      app:layout_constraintStart_toEndOf="@id/btn1"
 *      app:layout_constraintEnd_toEndOf="parent" ... />
 *
 *
 * ── 4.4  MotionLayout（动画布局）─────────────────────────────────────────────
 *
 *  · ConstraintLayout 的子类，通过 MotionScene XML 描述动画过渡
 *  · 支持手势驱动（拖拽进度）、关键帧、属性动画
 *  · 适合复杂的 UI 过渡动画（如展开/收起卡片、滑动变形）
 *
 *  <androidx.constraintlayout.motion.widget.MotionLayout
 *      app:layoutDescription="@xml/scene_main" ... >
 *      <!-- 子 View 同 ConstraintLayout -->
 *  </androidx.constraintlayout.motion.widget.MotionLayout>
 *
 *
 * ── 5  布局最佳实践 ───────────────────────────────────────────────────────────
 *
 *  · 减少布局嵌套层级，优先用 ConstraintLayout 扁平化（层级 ≤ 5 层）
 *  · 使用 include / merge 标签复用布局，merge 可减少一层嵌套
 *  · ViewStub：延迟加载不常用的布局（如错误页、空状态页），按需 inflate
 *  · 新项目考虑直接用 Jetpack Compose，彻底告别 XML 布局
 *
 *  // merge 减少嵌套示例
 *  // res/layout/include_header.xml（根节点用 merge）
 *  <merge xmlns:android="http://schemas.android.com/apk/res/android">
 *      <TextView android:id="@+id/tvTitle" ... />
 *      <ImageView android:id="@+id/ivBack" ... />
 *  </merge>
 *  // 使用时
 *  <include layout="@layout/include_header" />
 *
 *  // ViewStub 延迟加载
 *  <ViewStub
 *      android:id="@+id/stubEmpty"
 *      android:layout="@layout/layout_empty_state"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent"/>
 *  // 需要时才 inflate
 *  binding.stubEmpty.inflate()
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1",   "LinearLayout：orientation / gravity / weight / divider"),
    NoteChapter("2",   "FrameLayout：foreground / layout_gravity"),
    NoteChapter("3",   "RelativeLayout：alignParent / center / toEndOf / above"),
    NoteChapter("4",   "ConstraintLayout（推荐首选）"),
    NoteChapter("4.1", "基础约束：位置 / 尺寸 / bias / goneMargin"),
    NoteChapter("4.2", "Guideline / Barrier / Group"),
    NoteChapter("4.3", "Chain：spread / spread_inside / packed"),
    NoteChapter("4.4", "MotionLayout：动画布局"),
    NoteChapter("5",   "布局最佳实践：merge / ViewStub / 层级优化"),
)

@Composable
fun LayoutViewScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "布局容器",
        subtitle = "LinearLayout · FrameLayout · RelativeLayout · ConstraintLayout",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
