package com.example.androidlearn.feature.junior.detail.stage2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 布局与 View 笔记
 * 官方文档：https://developer.android.com/develop/ui/views/layout/declaring-layout
 *
 * ── 1  常用布局容器 ───────────────────────────────────────────────────────────
 *
 *  LinearLayout：线性排列（水平/垂直），weight 按比例分配剩余空间
 *  <LinearLayout
 *      android:orientation="horizontal"
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content">
 *      <TextView android:layout_weight="1" ... />
 *      <Button  android:layout_weight="1" ... />
 *  </LinearLayout>
 *
 *  FrameLayout：层叠布局，子 View 默认左上角叠放；常用于 Fragment 容器
 *  <FrameLayout android:id="@+id/container" ... />
 *
 *  RelativeLayout：相对定位，已被 ConstraintLayout 取代，不推荐新项目使用
 *
 *
 * ── 2  ConstraintLayout（推荐首选）────────────────────────────────────────────
 *
 *  · 扁平化布局，一层解决大多数场景，性能优于多层嵌套
 *  · 约束方向：Start/End/Top/Bottom，必须在水平和垂直方向各有至少一个约束
 *
 *  <androidx.constraintlayout.widget.ConstraintLayout>
 *      <TextView
 *          android:id="@+id/tvTitle"
 *          app:layout_constraintTop_toTopOf="parent"
 *          app:layout_constraintStart_toStartOf="parent"
 *          android:text="标题" />
 *      <Button
 *          android:id="@+id/btnOk"
 *          app:layout_constraintTop_toBottomOf="@id/tvTitle"
 *          app:layout_constraintEnd_toEndOf="parent"
 *          android:text="确认" />
 *  </androidx.constraintlayout.widget.ConstraintLayout>
 *
 *  · Guideline：辅助线，按百分比或固定距离划分区域
 *  · Barrier：跟随多个 View 中最大尺寸的边缘
 *  · Group：批量控制多个 View 的可见性
 *  · Chain：水平/垂直方向均匀分布（spread / packed / spread_inside）
 *
 *
 * ── 3  RecyclerView ───────────────────────────────────────────────────────────
 *
 *  · 高性能列表，替代 ListView；通过 ViewHolder 复用 View
 *
 *  // Adapter
 *  class MyAdapter(private val items: List<String>) :
 *      RecyclerView.Adapter<MyAdapter.VH>() {
 *
 *      class VH(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
 *
 *      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
 *          val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
 *          return VH(binding)
 *      }
 *
 *      override fun onBindViewHolder(holder: VH, position: Int) {
 *          holder.binding.tvName.text = items[position]
 *      }
 *
 *      override fun getItemCount() = items.size
 *  }
 *
 *  // 设置
 *  recyclerView.layoutManager = LinearLayoutManager(this)
 *  recyclerView.adapter = MyAdapter(dataList)
 *
 *  · DiffUtil：高效计算列表差异，只刷新变化的 Item
 *  · ListAdapter：内置 DiffUtil，推荐替代普通 Adapter
 *  · GridLayoutManager：网格布局；StaggeredGridLayoutManager：瀑布流
 *
 *
 * ── 4  ViewBinding ────────────────────────────────────────────────────────────
 *
 *  · 类型安全地访问 View，替代 findViewById，编译期检查，不会 NPE
 *
 *  // build.gradle.kts 开启
 *  android {
 *      buildFeatures { viewBinding = true }
 *  }
 *
 *  // Activity 中使用
 *  private lateinit var binding: ActivityMainBinding
 *  override fun onCreate(savedInstanceState: Bundle?) {
 *      super.onCreate(savedInstanceState)
 *      binding = ActivityMainBinding.inflate(layoutInflater)
 *      setContentView(binding.root)
 *      binding.tvTitle.text = "Hello"
 *      binding.btnOk.setOnClickListener { /* ... */ }
 *  }
 *
 *  // Fragment 中使用（注意在 onDestroyView 置 null）
 *  private var _binding: FragmentHomeBinding? = null
 *  private val binding get() = _binding!!
 *  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
 *      _binding = FragmentHomeBinding.inflate(inflater, container, false)
 *      return binding.root
 *  }
 *  override fun onDestroyView() { super.onDestroyView(); _binding = null }
 *
 *
 * ── 5  常用基础控件 ───────────────────────────────────────────────────────────
 *
 *  TextView：
 *  · android:ellipsize="end" + android:maxLines="2"：超出省略
 *  · android:autoLink="web|phone"：自动识别链接
 *  · Spannable：富文本（颜色、大小、点击）
 *
 *  EditText：
 *  · android:inputType="textPassword"：密码输入
 *  · android:imeOptions="actionDone"：键盘右下角按钮
 *  · TextWatcher：监听文字变化
 *
 *  ImageView：
 *  · android:scaleType="centerCrop"：裁剪填充
 *  · 加载网络图片用 Glide / Coil（不要在主线程 decode Bitmap）
 *
 *  Button / MaterialButton：
 *  · MaterialButton 支持 icon、cornerRadius、strokeColor 等 Material 属性
 *
 *
 * ── 6  自定义 View ────────────────────────────────────────────────────────────
 *
 *  class CircleView @JvmOverloads constructor(
 *      context: Context,
 *      attrs: AttributeSet? = null,
 *      defStyleAttr: Int = 0
 *  ) : View(context, attrs, defStyleAttr) {
 *
 *      private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
 *          color = Color.BLUE
 *          style = Paint.Style.FILL
 *      }
 *
 *      override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
 *          val size = resolveSize(200, widthMeasureSpec)
 *          setMeasuredDimension(size, size)
 *      }
 *
 *      override fun onDraw(canvas: Canvas) {
 *          val cx = width / 2f
 *          canvas.drawCircle(cx, cx, cx, paint)
 *      }
 *  }
 *
 *  · onMeasure：确定 View 尺寸
 *  · onLayout：确定子 View 位置（ViewGroup 重写）
 *  · onDraw：绘制内容；避免在此创建对象（GC 压力）
 *  · invalidate()：请求重绘；requestLayout()：请求重新测量+布局
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · 减少布局嵌套层级，优先用 ConstraintLayout 扁平化
 *  · 使用 ViewBinding 替代 findViewById，避免类型错误和 NPE
 *  · RecyclerView 配合 ListAdapter + DiffUtil，避免 notifyDataSetChanged()
 *  · 图片加载用 Glide/Coil，不要手动管理 Bitmap 内存
 *  · 自定义 View 的 onDraw 中不要 new 对象，提前在成员变量初始化
 *  · 新项目考虑直接用 Jetpack Compose，彻底告别 XML 布局
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1", "常用布局容器"),
    NoteChapter("2", "ConstraintLayout（推荐首选）"),
    NoteChapter("3", "RecyclerView"),
    NoteChapter("4", "ViewBinding"),
    NoteChapter("5", "常用基础控件"),
    NoteChapter("6", "自定义 View"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun LayoutViewScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "布局与 View",
        subtitle = "ConstraintLayout · RecyclerView · ViewBinding",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
