package com.example.androidlearn.feature.junior.detail.stage3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 网络请求基础笔记
 * Retrofit 文档：https://square.github.io/retrofit/
 * OkHttp 文档：https://square.github.io/okhttp/
 *
 * ── 1  网络请求技术栈 ─────────────────────────────────────────────────────────
 *
 *  标准组合：Retrofit（API 定义）+ OkHttp（底层 HTTP）+ 协程（异步）+ Gson/Moshi（JSON 解析）
 *
 *  // 依赖（build.gradle.kts）
 *  implementation("com.squareup.retrofit2:retrofit:2.11.0")
 *  implementation("com.squareup.retrofit2:converter-gson:2.11.0")
 *  implementation("com.squareup.okhttp3:okhttp:4.12.0")
 *  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
 *  // 或使用 Moshi
 *  implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
 *  implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
 *  // 或使用 kotlinx.serialization
 *  implementation("com.jakewharton.retrofit2:retrofit2-kotlinx-serialization-converter:1.0.0")
 *
 *  // AndroidManifest.xml 添加网络权限
 *  <uses-permission android:name="android.permission.INTERNET" />
 *
 *
 * ── 2  Retrofit 定义 API ──────────────────────────────────────────────────────
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
 *      // 动态 Header
 *      @GET("profile")
 *      suspend fun getProfile(@Header("Authorization") token: String): User
 *
 *      // 上传文件
 *      @Multipart
 *      @POST("upload")
 *      suspend fun uploadFile(
 *          @Part file: MultipartBody.Part,
 *          @Part("description") description: RequestBody
 *      ): UploadResponse
 *  }
 *
 *
 * ── 3  OkHttp + Retrofit 配置 ─────────────────────────────────────────────────
 *
 *  // OkHttp 客户端（添加拦截器）
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
 *              .addHeader("Accept", "application/json")
 *              .build()
 *          chain.proceed(request)
 *      }
 *      .connectTimeout(30, TimeUnit.SECONDS)
 *      .readTimeout(30, TimeUnit.SECONDS)
 *      .writeTimeout(30, TimeUnit.SECONDS)
 *      .build()
 *
 *  // Retrofit 实例（单例，通过 Hilt 注入）
 *  val retrofit = Retrofit.Builder()
 *      .baseUrl("https://api.example.com/")
 *      .client(okHttpClient)
 *      .addConverterFactory(GsonConverterFactory.create())
 *      .build()
 *
 *  val apiService: ApiService = retrofit.create(ApiService::class.java)
 *
 *
 * ── 4  错误处理 ───────────────────────────────────────────────────────────────
 *
 *  // 方式一：try-catch（简单场景）
 *  viewModelScope.launch {
 *      try {
 *          val user = apiService.getUser(1)
 *          _uiState.value = UiState.Success(user)
 *      } catch (e: HttpException) {
 *          // HTTP 错误（4xx / 5xx）
 *          val errorBody = e.response()?.errorBody()?.string()
 *          _uiState.value = UiState.Error("HTTP ${e.code()}: $errorBody")
 *      } catch (e: IOException) {
 *          // 网络错误（无网络、超时）
 *          _uiState.value = UiState.Error("网络连接失败")
 *      }
 *  }
 *
 *  // 方式二：runCatching（Kotlin 风格）
 *  viewModelScope.launch {
 *      val result = runCatching { apiService.getUser(1) }
 *      result
 *          .onSuccess { user -> _uiState.value = UiState.Success(user) }
 *          .onFailure { e -> _uiState.value = UiState.Error(e.message ?: "未知错误") }
 *  }
 *
 *  // 方式三：sealed class Result（推荐，统一封装）
 *  sealed class Result<out T> {
 *      data class Success<T>(val data: T) : Result<T>()
 *      data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
 *      object Loading : Result<Nothing>()
 *  }
 *
 *  suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> = try {
 *      Result.Success(call())
 *  } catch (e: HttpException) {
 *      Result.Error("HTTP ${e.code()}", e.code())
 *  } catch (e: IOException) {
 *      Result.Error("网络连接失败")
 *  }
 *
 *  // 使用
 *  when (val result = safeApiCall { apiService.getUser(1) }) {
 *      is Result.Success -> showUser(result.data)
 *      is Result.Error   -> showError(result.message)
 *      is Result.Loading -> showLoading()
 *  }
 *
 *
 * ── 5  Repository 模式 ────────────────────────────────────────────────────────
 *
 *  · 隔离 ViewModel 与数据源，便于测试和切换数据源
 *
 *  // Repository 接口
 *  interface UserRepository {
 *      suspend fun getUser(id: Int): Result<User>
 *      fun getUsersFlow(): Flow<List<User>>
 *  }
 *
 *  // 实现（网络 + 本地缓存）
 *  class UserRepositoryImpl(
 *      private val apiService: ApiService,
 *      private val userDao: UserDao
 *  ) : UserRepository {
 *
 *      override suspend fun getUser(id: Int): Result<User> = safeApiCall {
 *          val user = apiService.getUser(id)
 *          userDao.insert(user)   // 缓存到 Room
 *          user
 *      }
 *
 *      override fun getUsersFlow(): Flow<List<User>> = userDao.getAllFlow()
 *          .onStart { fetchFromNetwork() }
 *  }
 *
 *  // ViewModel 中使用
 *  @HiltViewModel
 *  class UserViewModel @Inject constructor(
 *      private val repository: UserRepository
 *  ) : ViewModel() {
 *      fun loadUser(id: Int) {
 *          viewModelScope.launch {
 *              when (val result = repository.getUser(id)) {
 *                  is Result.Success -> _user.value = result.data
 *                  is Result.Error   -> _error.value = result.message
 *              }
 *          }
 *      }
 *  }
 *
 *
 * ── 6  常见场景 ───────────────────────────────────────────────────────────────
 *
 *  // 分页请求（配合 Paging 3）
 *  @GET("posts")
 *  suspend fun getPosts(@Query("page") page: Int, @Query("size") size: Int): PagedResponse<Post>
 *
 *  // 文件上传
 *  val file = File(filePath)
 *  val requestBody = file.asRequestBody("image/jpeg".toMediaType())
 *  val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
 *  apiService.uploadFile(part, "描述".toRequestBody())
 *
 *  // 文件下载（带进度）
 *  @Streaming
 *  @GET("files/{name}")
 *  suspend fun downloadFile(@Path("name") name: String): ResponseBody
 *
 *  // 使用
 *  val body = apiService.downloadFile("report.pdf")
 *  body.byteStream().use { input ->
 *      FileOutputStream(destFile).use { output -> input.copyTo(output) }
 *  }
 *
 *  // Token 刷新（Authenticator）
 *  val authenticator = Authenticator { _, response ->
 *      val newToken = runBlocking { authApi.refreshToken(refreshToken) }
 *      response.request.newBuilder()
 *          .header("Authorization", "Bearer ${newToken.accessToken}")
 *          .build()
 *  }
 *  OkHttpClient.Builder().authenticator(authenticator).build()
 *
 *
 * ── 7  最佳实践 ───────────────────────────────────────────────────────────────
 *
 *  · Retrofit + OkHttp 实例设为单例（通过 Hilt 注入），避免重复创建
 *  · Debug 版本开启 HttpLoggingInterceptor，Release 版本关闭
 *  · 用 sealed class Result 统一封装成功/失败，ViewModel 不直接 try-catch
 *  · 网络请求放在 Repository 层，ViewModel 只处理业务逻辑
 *  · 超时时间合理设置（connectTimeout 10s，readTimeout 30s）
 *  · Token 刷新用 Authenticator，不要在每个请求手动处理 401
 *  · 敏感信息（BaseUrl、Key）放在 BuildConfig 或 local.properties，不要硬编码
 */

private val Teal = Color(0xFF009688)

private val chapters = listOf(
    NoteChapter("1", "网络请求技术栈"),
    NoteChapter("2", "Retrofit 定义 API"),
    NoteChapter("3", "OkHttp + Retrofit 配置"),
    NoteChapter("4", "错误处理"),
    NoteChapter("5", "Repository 模式"),
    NoteChapter("6", "常见场景"),
    NoteChapter("7", "最佳实践"),
)

@Composable
fun NetworkRequestScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "网络请求基础",
        subtitle = "Retrofit · OkHttp · 协程 · 错误处理 · Repository",
        color = Teal,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
