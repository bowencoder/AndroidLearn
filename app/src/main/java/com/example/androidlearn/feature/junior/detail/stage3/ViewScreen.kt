package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 常用 View 与自定义 View 笔记
 * 官方文档：https://developer.android.com/develop/ui/views/components
 *
 * ── 1  ViewBinding ────────────────────────────────────────────────────────────
 *
 *  · 编译期为每个 XML 布局生成对应的 Binding 类，彻底消除 findViewById 和 NullPointerException
 *  · 相比 DataBinding 更轻量（不支持双向绑定，但编译更快）
 *
 *  // build.gradle 启用
 *  android {
 *      buildFeatures { viewBinding = true }
 *  }
 *
 *  // Activity 中使用
 *  class MainActivity : AppCompatActivity() {
 *      private lateinit var binding: ActivityMainBinding
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          binding = ActivityMainBinding.inflate(layoutInflater)
 *          setContentView(binding.root)
 *          binding.tvTitle.text = "Hello ViewBinding"
 *          binding.btnSubmit.setOnClickListener { /* ... */ }
 *      }
 *  }
 *
 *  // Fragment 中使用（⚠️ onDestroyView 中必须置空，避免内存泄漏）
 *  class HomeFragment : Fragment(R.layout.fragment_home) {
 *      private var _binding: FragmentHomeBinding? = null
 *      private val binding get() = _binding!!
 *
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          super.onViewCreated(view, savedInstanceState)
 *          _binding = FragmentHomeBinding.bind(view)
 *      }
 *
 *      override fun onDestroyView() {
 *          super.onDestroyView()
 *          _binding = null   // Fragment 生命周期比 View 长，必须置空
 *      }
 *  }
 *
 *  // RecyclerView ViewHolder 中使用
 *  class MyViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)
 *
 *  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
 *      val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
 *      return MyViewHolder(binding)
 *  }
 *
 *  // 排除某个布局不生成 Binding
 *  <LinearLayout tools:viewBindingIgnore="true" ... />
 *
 *
 * ── 2  常用基础控件 ───────────────────────────────────────────────────────────
 *
 * ── 2.1  TextView ─────────────────────────────────────────────────────────────
 *
 *  android:text                    显示文本（或 @string/xxx）
 *  android:textSize                字体大小（推荐 sp 单位）
 *  android:textColor               字体颜色
 *  android:textStyle               bold / italic / bold|italic
 *  android:fontFamily              字体族（如 @font/roboto_medium）
 *  android:gravity                 文字在 TextView 内的对齐方式
 *  android:maxLines                最大行数
 *  android:ellipsize               超出截断方式：start / middle / end / marquee
 *  android:lineSpacingMultiplier   行间距倍数（如 1.2）
 *  android:letterSpacing           字间距（em 单位，如 0.05）
 *  android:drawableStart/End/Top   文字旁边的 Drawable 图标
 *  android:drawablePadding         Drawable 与文字的间距
 *  android:autoLink                自动识别链接：web / email / phone / all
 *  android:includeFontPadding      false：去除字体上下额外内边距（紧凑布局时用）
 *
 *  // 富文本（SpannableString）
 *  val span = SpannableString("点击这里查看详情")
 *  span.setSpan(ForegroundColorSpan(Color.BLUE), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
 *  span.setSpan(UnderlineSpan(), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
 *  span.setSpan(StyleSpan(Typeface.BOLD), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
 *  tvContent.text = span
 *
 *  // HTML 文本
 *  tvContent.text = HtmlCompat.fromHtml("<b>粗体</b> <i>斜体</i>", HtmlCompat.FROM_HTML_MODE_LEGACY)
 *
 *
 * ── 2.2  EditText ─────────────────────────────────────────────────────────────
 *
 *  android:hint                    占位提示文字
 *  android:inputType               text / textPassword / number / phone / textEmailAddress 等
 *  android:imeOptions              actionDone / actionSearch / actionNext / actionSend
 *  android:maxLength               最大输入字符数
 *  android:selectAllOnFocus        true：获取焦点时全选文字
 *  android:digits                  限制可输入的字符集（如 "0123456789."）
 *
 *  // 监听输入
 *  editText.addTextChangedListener { text -> /* 实时监听 */ }
 *  editText.setOnEditorActionListener { _, actionId, _ ->
 *      if (actionId == EditorInfo.IME_ACTION_DONE) { /* 处理完成 */ true } else false
 *  }
 *
 *
 * ── 2.3  Button / ImageButton ────────────────────────────────────────────────
 *
 *  android:text                    按钮文字
 *  android:enabled                 是否可点击（false 时显示禁用样式）
 *  android:background              背景（可设置 selector drawable 实现按压效果）
 *  android:onClick                 XML 中直接绑定方法名（不推荐，用代码设置）
 *
 *  // 设置点击事件
 *  button.setOnClickListener { /* 处理点击 */ }
 *
 *  // ImageButton：只显示图标的按钮
 *  <ImageButton
 *      android:src="@drawable/ic_back"
 *      android:background="?attr/selectableItemBackgroundBorderless"
 *      android:contentDescription="返回"
 *      android:layout_width="48dp"
 *      android:layout_height="48dp"/>
 *
 *  // 防止重复点击（节流）
 *  var lastClickTime = 0L
 *  button.setOnClickListener {
 *      val now = System.currentTimeMillis()
 *      if (now - lastClickTime > 500) { lastClickTime = now; /* 处理 */ }
 *  }
 *
 *
 * ── 2.4  ImageView ────────────────────────────────────────────────────────────
 *
 *  android:src / app:srcCompat     图片资源（srcCompat 支持 VectorDrawable）
 *  android:scaleType               缩放方式：
 *                                  - fitCenter（默认）：等比缩放居中
 *                                  - centerCrop：等比裁剪填满（头像/封面常用）
 *                                  - centerInside：等比缩放，不超出边界
 *                                  - fitXY：拉伸填满（会变形）
 *  android:adjustViewBounds        true：根据图片比例自动调整 ImageView 尺寸
 *  android:tint / app:tint         着色（图标变色常用）
 *  android:contentDescription      无障碍描述（必填，否则 Lint 警告）
 *
 *  // Glide 加载网络图片
 *  Glide.with(context)
 *      .load(url)
 *      .placeholder(R.drawable.ic_placeholder)
 *      .error(R.drawable.ic_error)
 *      .centerCrop()
 *      .into(imageView)
 *
 *  // Coil（Kotlin 友好，支持 Compose）
 *  imageView.load(url) {
 *      crossfade(true)
 *      placeholder(R.drawable.ic_placeholder)
 *      transformations(CircleCropTransformation())
 *  }
 *
 *
 * ── 3  自定义 View ────────────────────────────────────────────────────────────
 *
 * ── 3.1  绘制三大流程：onMeasure / onLayout / onDraw ─────────────────────────
 *
 *  · measure → layout → draw
 *  · invalidate()：只触发 draw（内容变化时用）
 *  · requestLayout()：触发三者（尺寸/位置变化时用，代价更大）
 *
 *  onMeasure(widthMeasureSpec, heightMeasureSpec)
 *  · MeasureSpec 包含 mode + size：
 *    - EXACTLY：父容器指定精确尺寸（match_parent 或固定 dp）
 *    - AT_MOST：父容器给出最大尺寸（wrap_content）
 *    - UNSPECIFIED：父容器不限制（ScrollView 内的子 View）
 *  · 必须调用 setMeasuredDimension(width, height)
 *
 *  onLayout(changed, left, top, right, bottom)
 *  · 仅 ViewGroup 需要重写，调用 child.layout(l, t, r, b) 摆放子 View
 *
 *  onDraw(canvas: Canvas)
 *  · 使用 Paint + Canvas API 绘制
 *  · ⚠️ 禁止在 onDraw 中创建对象（Paint/Path/RectF 在构造函数中初始化）
 *
 *  // 自定义圆形进度条
 *  class CircleProgressView @JvmOverloads constructor(
 *      context: Context, attrs: AttributeSet? = null
 *  ) : View(context, attrs) {
 *
 *      private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
 *          style = Paint.Style.STROKE
 *          strokeWidth = 8f
 *          strokeCap = Paint.Cap.ROUND
 *      }
 *      var progress: Float = 0f
 *          set(value) { field = value.coerceIn(0f, 100f); invalidate() }
 *
 *      override fun onDraw(canvas: Canvas) {
 *          val cx = width / 2f; val cy = height / 2f
 *          val radius = minOf(cx, cy) - paint.strokeWidth
 *          paint.color = Color.LTGRAY
 *          canvas.drawCircle(cx, cy, radius, paint)
 *          paint.color = Color.BLUE
 *          val oval = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
 *          canvas.drawArc(oval, -90f, 360f * progress / 100f, false, paint)
 *      }
 *  }
 *
 *
 * ── 3.2  自定义属性（attrs.xml）──────────────────────────────────────────────
 *
 *  // res/values/attrs.xml
 *  <declare-styleable name="CircleProgressView">
 *      <attr name="progressColor" format="color"/>
 *      <attr name="trackColor"    format="color"/>
 *      <attr name="strokeWidth"   format="dimension"/>
 *      <attr name="maxProgress"   format="integer"/>
 *  </declare-styleable>
 *
 *  // 构造函数中读取
 *  init {
 *      context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView).use { ta ->
 *          progressColor = ta.getColor(R.styleable.CircleProgressView_progressColor, Color.BLUE)
 *          strokeWidth   = ta.getDimension(R.styleable.CircleProgressView_strokeWidth, 8f)
 *      }
 *  }
 *
 *  // XML 中使用
 *  <com.example.CircleProgressView
 *      app:progressColor="@color/primary"
 *      app:strokeWidth="6dp"
 *      android:layout_width="80dp"
 *      android:layout_height="80dp"/>
 *
 *
 * ── 3.3  触摸事件处理 ─────────────────────────────────────────────────────────
 *
 *  · 事件分发链：Activity → Window → DecorView → ViewGroup → View
 *  · dispatchTouchEvent：分发（返回 true = 消费）
 *  · onInterceptTouchEvent：ViewGroup 专有，拦截（返回 true = 子 View 收不到）
 *  · onTouchEvent：处理（返回 true = 消费）
 *
 *  // 处理拖拽
 *  private var lastX = 0f; private var lastY = 0f
 *  override fun onTouchEvent(event: MotionEvent): Boolean {
 *      when (event.action) {
 *          MotionEvent.ACTION_DOWN -> { lastX = event.x; lastY = event.y }
 *          MotionEvent.ACTION_MOVE -> {
 *              translationX += event.x - lastX
 *              translationY += event.y - lastY
 *          }
 *      }
 *      return true
 *  }
 *
 *  // GestureDetector 简化手势
 *  private val detector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
 *      override fun onSingleTapUp(e: MotionEvent) = true.also { performClick() }
 *      override fun onDoubleTap(e: MotionEvent) = true.also { /* 双击 */ }
 *  })
 *  override fun onTouchEvent(event: MotionEvent) = detector.onTouchEvent(event)
 *
 *
 * ── 3.4  ViewGroup 自定义布局 ─────────────────────────────────────────────────
 *
 *  // 流式布局（FlowLayout）核心逻辑
 *  class FlowLayout @JvmOverloads constructor(
 *      context: Context, attrs: AttributeSet? = null
 *  ) : ViewGroup(context, attrs) {
 *
 *      override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
 *          val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
 *          var lineWidth = 0; var lineHeight = 0; var totalHeight = 0
 *          for (i in 0 until childCount) {
 *              val child = getChildAt(i)
 *              measureChild(child, widthMeasureSpec, heightMeasureSpec)
 *              if (lineWidth + child.measuredWidth > maxWidth) {
 *                  totalHeight += lineHeight; lineWidth = 0; lineHeight = 0
 *              }
 *              lineWidth += child.measuredWidth
 *              lineHeight = maxOf(lineHeight, child.measuredHeight)
 *          }
 *          setMeasuredDimension(maxWidth, totalHeight + lineHeight)
 *      }
 *
 *      override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
 *          var x = 0; var y = 0; var lineHeight = 0
 *          for (i in 0 until childCount) {
 *              val child = getChildAt(i)
 *              if (x + child.measuredWidth > r - l) { y += lineHeight; x = 0; lineHeight = 0 }
 *              child.layout(x, y, x + child.measuredWidth, y + child.measuredHeight)
 *              x += child.measuredWidth
 *              lineHeight = maxOf(lineHeight, child.measuredHeight)
 *          }
 *      }
 *  }
 *
 *
 * ── 3.5  最佳实践 ─────────────────────────────────────────────────────────────
 *
 *  · onDraw 中禁止创建对象，Paint/Path/RectF 在构造函数中初始化
 *  · 提供 @JvmOverloads 构造函数，兼容 XML 和代码两种创建方式
 *  · 实现 onSaveInstanceState / onRestoreInstanceState 保存 View 状态
 *  · 设置 contentDescription，支持无障碍（TalkBack）
 *  · 减少过度绘制：用 clipRect 裁剪不可见区域，避免透明背景叠加
 *
 *  // 保存/恢复状态
 *  override fun onSaveInstanceState(): Parcelable = Bundle().apply {
 *      putParcelable("super", super.onSaveInstanceState())
 *      putFloat("progress", progress)
 *  }
 *  override fun onRestoreInstanceState(state: Parcelable?) {
 *      (state as? Bundle)?.let {
 *          progress = it.getFloat("progress")
 *          super.onRestoreInstanceState(it.getParcelable("super"))
 *      } ?: super.onRestoreInstanceState(state)
 *  }
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1",   "ViewBinding：消除 findViewById，Activity / Fragment / ViewHolder 用法"),
    NoteChapter("2",   "常用基础控件"),
    NoteChapter("2.1", "TextView：属性速查 / SpannableString / HTML"),
    NoteChapter("2.2", "EditText：inputType / imeOptions / addTextChangedListener"),
    NoteChapter("2.3", "Button / ImageButton：background / selector / 防重复点击"),
    NoteChapter("2.4", "ImageView：scaleType / Glide / Coil"),
    NoteChapter("3",   "自定义 View"),
    NoteChapter("3.1", "绘制三大流程：onMeasure / onLayout / onDraw"),
    NoteChapter("3.2", "自定义属性：attrs.xml / obtainStyledAttributes"),
    NoteChapter("3.3", "触摸事件：onTouchEvent / GestureDetector"),
    NoteChapter("3.4", "ViewGroup 自定义布局：FlowLayout 示例"),
    NoteChapter("3.5", "最佳实践：性能 / 状态保存 / 无障碍"),
)

@Composable
fun ViewScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "常用 View 与自定义 View",
        subtitle = "ViewBinding · 基础控件 · 自定义 View",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
