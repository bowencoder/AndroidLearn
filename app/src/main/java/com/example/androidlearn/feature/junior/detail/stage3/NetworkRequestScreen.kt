package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 网络与图片加载笔记
 * Retrofit 文档：https://square.github.io/retrofit/
 * OkHttp 文档：https://square.github.io/okhttp/
 * Glide 文档：https://bumptech.github.io/glide/
 * Coil 文档：https://coil-kt.github.io/coil/
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  1  技术栈与依赖
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  标准组合：Retrofit（API 定义）+ OkHttp（底层 HTTP）+ 协程（异步）+ Gson（JSON 解析）
 *
 *  // 依赖（build.gradle.kts）
 *  implementation("com.squareup.retrofit2:retrofit:2.11.0")
 *  implementation("com.squareup.retrofit2:converter-gson:2.11.0")
 *  implementation("com.squareup.okhttp3:okhttp:4.12.0")
 *  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
 *
 *  // AndroidManifest.xml 添加网络权限
 *  <uses-permission android:name="android.permission.INTERNET" />
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  2  Retrofit 定义 API
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // 数据模型
 *  data class User(val id: Int, val name: String, val email: String)
 *  data class LoginRequest(val username: String, val password: String)
 *  data class LoginResponse(val token: String, val userId: Int)
 *
 *  // API 接口（suspend 函数，协程原生支持）
 *  interface ApiService {
 *      // GET 请求
 *      @GET("users/{id}")
 *      suspend fun getUser(@Path("id") id: Int): User
 *
 *      // GET 带查询参数
 *      @GET("users")
 *      suspend fun getUsers(
 *          @Query("page") page: Int,
 *          @Query("size") size: Int = 20
 *      ): List<User>
 *
 *      // POST 请求（Body）
 *      @POST("auth/login")
 *      suspend fun login(@Body request: LoginRequest): LoginResponse
 *
 *      // POST 表单
 *      @FormUrlEncoded
 *      @POST("auth/login")
 *      suspend fun loginForm(
 *          @Field("username") username: String,
 *          @Field("password") password: String
 *      ): LoginResponse
 *
 *      // PUT / DELETE
 *      @PUT("users/{id}")
 *      suspend fun updateUser(@Path("id") id: Int, @Body user: User): User
 *
 *      @DELETE("users/{id}")
 *      suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
 *
 *      // 静态 Header（固定值）
 *      @Headers("Content-Type: application/json", "Accept: application/json")
 *      @GET("profile")
 *      suspend fun getProfile(): User
 *
 *      // 动态 Header（运行时传入）
 *      @GET("profile")
 *      suspend fun getProfileWithToken(@Header("Authorization") token: String): User
 *
 *      // 返回完整 Response（可获取状态码、Header）
 *      @GET("users/{id}")
 *      suspend fun getUserResponse(@Path("id") id: Int): Response<User>
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  3  OkHttp + Retrofit 配置
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // OkHttp 客户端
 *  val okHttpClient = OkHttpClient.Builder()
 *      // 日志拦截器（仅 Debug）
 *      .addInterceptor(HttpLoggingInterceptor().apply {
 *          level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
 *                  else HttpLoggingInterceptor.Level.NONE
 *      })
 *      // 认证拦截器（自动添加 Token）
 *      .addInterceptor { chain ->
 *          val request = chain.request().newBuilder()
 *              .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
 *              .build()
 *          chain.proceed(request)
 *      }
 *      .connectTimeout(10, TimeUnit.SECONDS)
 *      .readTimeout(30, TimeUnit.SECONDS)
 *      .build()
 *
 *  // Retrofit 单例封装（Application 中初始化，全局复用）
 *  object RetrofitClient {
 *      private const val BASE_URL = "https://api.example.com/"
 *
 *      private val okHttpClient = OkHttpClient.Builder()
 *          .addInterceptor(HttpLoggingInterceptor().apply {
 *              level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
 *                      else HttpLoggingInterceptor.Level.NONE
 *          })
 *          .connectTimeout(10, TimeUnit.SECONDS)
 *          .readTimeout(30, TimeUnit.SECONDS)
 *          .build()
 *
 *      val apiService: ApiService = Retrofit.Builder()
 *          .baseUrl(BASE_URL)
 *          .client(okHttpClient)
 *          .addConverterFactory(GsonConverterFactory.create())
 *          .build()
 *          .create(ApiService::class.java)
 *  }
 *
 *  // 使用
 *  val user = RetrofitClient.apiService.getUser(1)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  4  错误处理
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // 方式一：Response<T> 包装（可判断状态码）
 *  lifecycleScope.launch {
 *      val response = apiService.getUserResponse(1)
 *      if (response.isSuccessful) {
 *          val user = response.body()   // 成功时的数据
 *          showUser(user)
 *      } else {
 *          val errorBody = response.errorBody()?.string()
 *          showError("HTTP ${response.code()}: $errorBody")
 *      }
 *  }
 *
 *  // 方式二：try-catch（直接返回数据类型，异常即失败）
 *  lifecycleScope.launch {
 *      try {
 *          val user = apiService.getUser(1)
 *          showUser(user)
 *      } catch (e: HttpException) {
 *          // HTTP 错误（4xx / 5xx）
 *          showError("HTTP ${e.code()}")
 *      } catch (e: IOException) {
 *          // 网络错误（无网络、超时）
 *          showError("网络连接失败")
 *      }
 *  }
 *
 *  // 方式三：runCatching（Kotlin 风格）
 *  lifecycleScope.launch {
 *      runCatching { apiService.getUser(1) }
 *          .onSuccess { user -> showUser(user) }
 *          .onFailure { e -> showError(e.message ?: "未知错误") }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  5  文件上传与下载
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // 文件上传（Multipart）
 *  @Multipart
 *  @POST("upload")
 *  suspend fun uploadFile(
 *      @Part file: MultipartBody.Part,
 *      @Part("description") description: RequestBody
 *  ): UploadResponse
 *
 *  val file = File(filePath)
 *  val requestBody = file.asRequestBody("image/jpeg".toMediaType())
 *  val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
 *  apiService.uploadFile(part, "描述".toRequestBody())
 *
 *  // 文件下载
 *  @Streaming
 *  @GET("files/{name}")
 *  suspend fun downloadFile(@Path("name") name: String): ResponseBody
 *
 *  val body = apiService.downloadFile("report.pdf")
 *  body.byteStream().use { input ->
 *      FileOutputStream(destFile).use { output -> input.copyTo(output) }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  6  图片加载库选型
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  Glide（View 体系首选）：
 *  · 成熟稳定，自动绑定 Activity/Fragment 生命周期，页面销毁自动取消请求
 *  · 支持 GIF、WebP、自定义 Transformation
 *
 *  Coil（Kotlin 友好）：
 *  · Kotlin 优先，协程原生支持，体积更小
 *  · View 体系用扩展函数 imageView.load()，简洁易用
 *
 *  // 依赖（build.gradle.kts）
 *  implementation("com.github.bumptech.glide:glide:4.16.0")   // Glide
 *  implementation("io.coil-kt:coil:2.6.0")                    // Coil
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  7  Glide 基础用法
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // 基础加载
 *  Glide.with(context).load(url).into(imageView)
 *
 *  // 完整配置
 *  Glide.with(context)
 *      .load(url)
 *      .placeholder(R.drawable.ic_placeholder)   // 加载中占位图
 *      .error(R.drawable.ic_error)               // 加载失败图
 *      .centerCrop()
 *      .override(300, 300)                       // 指定目标尺寸，节省内存
 *      .diskCacheStrategy(DiskCacheStrategy.ALL)
 *      .into(imageView)
 *
 *  // 圆形裁剪 / 圆角
 *  Glide.with(context).load(url).circleCrop().into(imageView)
 *  Glide.with(context).load(url).transform(RoundedCorners(16)).into(imageView)
 *
 *  // 加载本地资源 / 文件
 *  Glide.with(context).load(R.drawable.ic_banner).into(imageView)
 *  Glide.with(context).load(File("/sdcard/photo.jpg")).into(imageView)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  8  Coil 基础用法（View 体系）
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  // 基础加载（扩展函数）
 *  imageView.load(url)
 *
 *  // 完整配置
 *  imageView.load(url) {
 *      placeholder(R.drawable.ic_placeholder)
 *      error(R.drawable.ic_error)
 *      crossfade(true)
 *      size(300, 300)
 *      transformations(CircleCropTransformation())
 *  }
 *
 *  // 圆角
 *  imageView.load(url) {
 *      transformations(RoundedCornersTransformation(16f))
 *  }
 *
 *  // 取消加载
 *  imageView.dispose()
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  9  缓存策略
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  两级缓存：内存缓存（LRU）→ 磁盘缓存 → 网络请求
 *
 *  Glide 磁盘缓存策略（DiskCacheStrategy）：
 *  · NONE      不缓存
 *  · DATA      只缓存原始数据
 *  · RESOURCE  只缓存处理后的图片
 *  · ALL       缓存原始数据和处理后的图片（默认）
 *
 *  // 强制刷新（忽略缓存重新请求）
 *  Glide.with(context)
 *      .load(url)
 *      .diskCacheStrategy(DiskCacheStrategy.NONE)
 *      .skipMemoryCache(true)
 *      .into(imageView)
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  10  在 RecyclerView 中使用图片
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  · Glide 自动绑定 RecyclerView 生命周期，滚动时自动暂停/恢复请求
 *  · with() 传 itemView.context，不要传 Application（会导致内存泄漏）
 *
 *  class ImageViewHolder(val binding: ItemImageBinding) :
 *      RecyclerView.ViewHolder(binding.root) {
 *
 *      fun bind(url: String) {
 *          Glide.with(binding.root.context)
 *              .load(url)
 *              .placeholder(R.drawable.ic_placeholder)
 *              .centerCrop()
 *              .override(200, 200)   // 固定尺寸，避免布局抖动
 *              .into(binding.ivCover)
 *      }
 *  }
 *
 *
 * ════════════════════════════════════════════════════════════════════════════
 *  11  最佳实践
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  网络请求：
 *  · Retrofit + OkHttp 实例设为单例，避免重复创建
 *  · Debug 版本开启 HttpLoggingInterceptor，Release 版本关闭
 *  · 网络请求在协程中调用，不要在主线程执行
 *  · 超时时间合理设置（connectTimeout 10s，readTimeout 30s）
 *  · 敏感信息（BaseUrl、Key）放在 local.properties，不要硬编码
 *
 *  图片加载：
 *  · View 体系项目优先用 Glide，生命周期自动管理
 *  · with() 传 Activity/Fragment，不要传 Application
 *  · 列表中图片用 override() 指定固定尺寸，避免布局抖动和内存浪费
 *  · 大图加载用 override() 降采样，防止 OOM
 *  · 不要在主线程手动 decode Bitmap，交给图片库处理
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    // ── 网络请求 ──────────────────────────────────────────────
    NoteChapter("1",  "技术栈与依赖"),
    NoteChapter("2",  "Retrofit 定义 API"),
    NoteChapter("3",  "OkHttp + Retrofit 单例配置"),
    NoteChapter("4",  "错误处理（Response / try-catch / runCatching）"),
    NoteChapter("5",  "文件上传与下载"),
    // ── 图片加载 ──────────────────────────────────────────────
    NoteChapter("6",  "图片加载库选型"),
    NoteChapter("7",  "Glide 基础用法"),
    NoteChapter("8",  "Coil 基础用法（View 体系）"),
    NoteChapter("9",  "缓存策略"),
    NoteChapter("10", "在 RecyclerView 中使用图片"),
    // ── 综合 ──────────────────────────────────────────────────
    NoteChapter("11", "最佳实践"),
)

@Composable
fun NetworkRequestScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "网络与图片加载",
        subtitle = "Retrofit · OkHttp · Glide · Coil · 缓存策略",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
