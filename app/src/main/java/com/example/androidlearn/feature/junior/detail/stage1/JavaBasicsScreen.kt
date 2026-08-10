package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.NoteChapter
import com.example.androidlearn.feature.shared.NoteDetailScaffold

/*
 * 廖雪峰 Java 教程笔记
 * https://liaoxuefeng.com/books/java/quick-start/basic/index.html
 *
 * ── 1  基础语法 & 数组 ────────────────────────────────────────────────────────
 *
 *  类型对照（Swift → Java）
 *    Int    → int / Integer      Bool   → boolean / Boolean
 *    Double → double / Double    String → String（同名，不可变）
 *    [T]    → int[] 或 List<T>   Any    → Object    nil → null
 *
 *  基本类型与运算：
 *  · 基本类型（值类型）：byte/short/int/long/float/double/char/boolean，其余引用类型
 *  · 整数除法截断：10/3=3；取余：10%3=1；long 字面量加 L：100L
 *  · 浮点不能用 == 比较，用 Math.abs(a - b) < 1e-6
 *  · 强制转型：(int) 3.9 → 3（截断，不是四舍五入）
 *  · 位运算：& | ^ ~ << >> >>>（无符号右移）
 *
 *  流程控制：
 *  · if/else、switch（Java 14+ 支持 switch 表达式，箭头语法）
 *  · while / do-while / for / for-each
 *  · break 跳出循环，continue 跳过本次，带标签可跳出多层循环
 *
 *  // switch 表达式（Java 14+）
 *  int day = 3;
 *  String name = switch (day) {
 *      case 1 -> "Monday";
 *      case 2 -> "Tuesday";
 *      case 3 -> "Wednesday";
 *      default -> "Other";
 *  };
 *
 *  数组：
 *  · 声明：int[] arr = new int[5]; 或 int[] arr = {1, 2, 3};
 *  · 下标从 0，越界抛 ArrayIndexOutOfBoundsException
 *  · for-each：for (int n : arr) {} ← 等价于 Swift 的 for n in arr {}
 *  · 多维数组：int[][] matrix = new int[3][4]; matrix[0][1] = 99;
 *  · Arrays.sort() 原地排序，Arrays.toString() 打印，Arrays.copyOf() 复制
 *
 *  int[] scores = {90, 85, 78};
 *  Arrays.sort(scores);
 *  System.out.println(Arrays.toString(scores)); // [78, 85, 90]
 *  int[][] m = {{1,2},{3,4},{5,6}};
 *  System.out.println(Arrays.deepToString(m)); // [[1, 2], [3, 4], [5, 6]]
 *
 *
 * ── 2  面向对象基础 ───────────────────────────────────────────────────────────
 *
 *  封装与继承：
 *  · 封装：字段 private + public getter/setter，方法内做参数校验
 *  · 继承：extends，单继承；Object 是所有类的根（≈ Swift AnyObject）
 *  · 方法重载（Overload）：同名方法，参数类型/数量不同，返回值不算
 *  · @Override：重写父类方法时加，编译器检查签名；super.method() 调用父类实现
 *  · 多态：父类引用指向子类实例，运行时调用实际类型的方法（动态绑定）
 *  · instanceof：判断类型，Java 16+ 支持模式匹配：if (obj instanceof String s) {}
 *
 *  抽象类与接口：
 *  · 抽象类 abstract：不能实例化，子类必须实现所有抽象方法；可有普通方法和字段
 *  · 接口 interface ≈ Swift protocol：纯契约，Java 8+ 支持 default/static 方法
 *  · 一个类可实现多个接口，但只能继承一个类
 *
 *  作用域：
 *  · public：所有地方可访问
 *  · protected：同包 + 子类可访问
 *  · （默认 package）：同包可访问
 *  · private：仅本类可访问
 *  · 推荐：字段 private，方法按需设置；局部变量尽量缩小作用域
 *
 *  static 与内部类：
 *  · static：属于类本身，不依赖实例 ← 类似 Swift static/class 成员
 *  · 静态内部类（static class）：不依赖外部类实例，常用于 Builder 模式
 *  · 匿名内部类：new Interface() { ... }，Java 8+ 简写为 Lambda () -> {}
 *  · 局部内部类：定义在方法内，作用域仅限该方法，很少用
 *
 *  interface Flyable {
 *      void fly();
 *      default String desc() { return "I can fly"; }
 *  }
 *  abstract class Shape { abstract double area(); }
 *  class Circle extends Shape implements Flyable {
 *      double r;
 *      Circle(double r) { this.r = r; }
 *      @Override public double area() { return Math.PI * r * r; }
 *      @Override public void fly() {}
 *  }
 *  // 模式匹配 instanceof（Java 16+）
 *  if (obj instanceof String s && s.length() > 5) { System.out.println(s.toUpperCase()); }
 *  // Lambda 替代匿名内部类
 *  Runnable r = () -> System.out.println("run");
 *
 *
 * ── 3  Java 核心类 ────────────────────────────────────────────────────────────
 *
 *  3.1 字符串和编码
 *  · String 不可变，每次修改都产生新对象；内容比较用 equals()，忽略大小写用 equalsIgnoreCase()
 *  · 常用方法：contains / indexOf / startsWith / endsWith / substring / trim / strip
 *             replace / replaceAll（支持正则）/ split / join / formatted（Java 15+）
 *  · 类型转换：Integer.parseInt("123") / String.valueOf(123) / Integer.toString(255, 16)
 *  · 编码：Java 内部用 UTF-16，char 是 16 位；与外部交互需指定编码
 *    String → byte[]：str.getBytes(StandardCharsets.UTF_8)
 *    byte[] → String：new String(bytes, StandardCharsets.UTF_8)
 *  · 注意：不要用 str.getBytes() 不带参数，依赖平台默认编码，跨平台会乱码
 *
 *  String s = "Hello, World";
 *  s.contains("World")          // true
 *  s.substring(7)               // "World"
 *  s.replace("World", "Java")   // "Hello, Java"
 *  String.join(", ", "A", "B", "C") // "A, B, C"
 *  "  hello  ".strip()          // "hello"（推荐，支持 Unicode 空白）
 *
 *  byte[] utf8 = "你好".getBytes(StandardCharsets.UTF_8); // 6 字节
 *  String back = new String(utf8, StandardCharsets.UTF_8); // "你好"
 *
 *  3.2 StringBuilder & StringJoiner
 *  · StringBuilder：可变字符串，循环拼接用它，避免大量临时 String 对象
 *    链式调用：append / insert / delete / reverse / toString
 *  · StringJoiner：专门处理分隔符拼接，可指定前缀/后缀，底层也是 StringBuilder
 *    String.join() 内部就是用 StringJoiner 实现的
 *
 *  StringBuilder sb = new StringBuilder();
 *  for (int i = 0; i < 5; i++) sb.append(i).append(",");
 *  sb.deleteCharAt(sb.length() - 1); // "0,1,2,3,4"
 *
 *  StringJoiner sj = new StringJoiner(", ", "[", "]");
 *  sj.add("Alice").add("Bob").add("Charlie");
 *  sj.toString(); // "[Alice, Bob, Charlie]"
 *
 *  3.3 包装类型
 *  · 每种基本类型都有对应包装类：int→Integer, long→Long, double→Double 等
 *  · 自动装箱/拆箱（Auto-boxing）：编译器自动转换，但频繁装箱有性能开销
 *  · Integer 缓存：-128~127 范围内 == 比较为 true，超出则 false，统一用 equals()
 *  · 常用静态方法：Integer.parseInt / Integer.valueOf / Integer.toBinaryString /
 *    Integer.toHexString / Integer.max / Integer.min / Integer.compare
 *
 *  Integer a = 200, b = 200;
 *  a == b        // false（超出缓存范围，不同对象）
 *  a.equals(b)   // true ✓
 *  Integer.parseInt("FF", 16) // 255（16进制转int）
 *  Integer.toBinaryString(10) // "1010"
 *
 *  3.4 JavaBean
 *  · 规范：无参构造 + private 字段 + public getter/setter，字段名首字母小写
 *  · getter：getXxx()，boolean 字段用 isXxx()
 *  · IDE 可自动生成，框架（Spring/MyBatis/Gson）依赖此规范做反射赋值
 *  · 与 Swift 的 struct 对比：JavaBean 是可变的，record 才是不可变的
 *
 *  public class Person {
 *      private String name;
 *      private int age;
 *      public String getName() { return name; }
 *      public void setName(String name) { this.name = name; }
 *      public int getAge() { return age; }
 *      public void setAge(int age) { this.age = age; }
 *  }
 *
 *  3.5 枚举类
 *  · enum 是类型安全的常量，比 int 常量更安全，switch 原生支持
 *  · 可以定义字段、构造方法、方法；每个枚举值都是该类的一个实例
 *  · 常用方法：name() / ordinal() / values() / valueOf()
 *
 *  enum Planet {
 *      MERCURY(3.303e+23, 2.4397e6),
 *      VENUS(4.869e+24, 6.0518e6);
 *      final double mass, radius;
 *      Planet(double mass, double radius) { this.mass = mass; this.radius = radius; }
 *  }
 *  Planet.MERCURY.name()    // "MERCURY"
 *  Planet.MERCURY.ordinal() // 0
 *
 *  3.6 记录类（record）
 *  · Java 16+ 正式引入，≈ Swift struct：不可变数据类
 *  · 自动生成：构造方法、getter（无 get 前缀）、equals、hashCode、toString
 *  · 适合做 DTO / Value Object，不能继承其他类（隐式继承 Record）
 *
 *  record Point(int x, int y) {}
 *  var p = new Point(3, 4);
 *  p.x()        // 3（getter 无 get 前缀）
 *  p.toString() // "Point[x=3, y=4]"
 *
 *  3.7 BigInteger & BigDecimal
 *  · BigInteger：任意精度整数，用于超出 long 范围的计算（密码学、大数运算）
 *  · BigDecimal：精确小数，金融计算必用，避免浮点误差
 *    构造必须传字符串：new BigDecimal("0.1")，不能传 double（会有精度问题）
 *    比较用 compareTo()，不用 equals()（equals 还比较精度位数）
 *    除法需指定精度和舍入模式：divide(b, 2, RoundingMode.HALF_UP)
 *
 *  BigDecimal a = new BigDecimal("1.0");
 *  BigDecimal b = new BigDecimal("9.0");
 *  a.divide(b, 10, RoundingMode.HALF_UP) // 0.1111111111
 *  a.compareTo(new BigDecimal("1.00"))   // 0（相等）
 *
 *  3.8 常用工具类
 *  · Math：abs / max / min / pow / sqrt / random / floor / ceil / round
 *  · Random / ThreadLocalRandom（多线程推荐）：nextInt / nextDouble / nextBoolean
 *  · Arrays：sort / binarySearch / fill / copyOf / copyOfRange / equals / toString
 *  · Objects：isNull / nonNull / requireNonNull / toString(obj, "default") / equals
 *
 *  Math.pow(2, 10)          // 1024.0
 *  Math.round(3.5)          // 4
 *  ThreadLocalRandom.current().nextInt(1, 100) // [1, 100) 随机整数
 *  Objects.requireNonNull(obj, "obj must not be null") // 空则抛 NPE
 *
 *
 * ── 4  异常处理 ───────────────────────────────────────────────────────────────
 *
 *  异常体系：
 *  · Throwable → Error（JVM 级，不处理）/ Exception
 *  · Exception → 受检异常（Checked）/ RuntimeException（非受检）
 *  · 受检异常：编译器强制 catch 或 throws 声明，如 IOException、SQLException
 *  · 非受检：RuntimeException 子类，如 NPE / ArrayIndexOutOfBoundsException，不强制处理
 *
 *  捕获与抛出：
 *  · 多个 catch 从子类到父类排列；catch (A | B e) 合并捕获同级异常
 *  · finally：无论是否异常都执行，但 try-with-resources 更推荐
 *  · 异常链：new RuntimeException("msg", cause) 保留原始原因，排查问题必备
 *  · 自定义异常：继承 RuntimeException（非受检）或 Exception（受检）
 *
 *  NullPointerException：
 *  · Java 14+ 的 NPE 信息更详细，会指出哪个变量为 null
 *  · 防御：用 Objects.requireNonNull() 提前检查；或用 Optional 包装可能为 null 的值
 *
 *  日志（实际项目必用）：
 *  · 不要用 System.out.println，用日志框架
 *  · SLF4J（门面）+ Logback（实现）是主流组合
 *  · 级别：TRACE < DEBUG < INFO < WARN < ERROR
 *
 *  // 自定义异常
 *  class AppException extends RuntimeException {
 *      AppException(String msg) { super(msg); }
 *      AppException(String msg, Throwable cause) { super(msg, cause); }
 *  }
 *
 *  try (InputStream is = new FileInputStream("f.txt")) {
 *      // 自动 close，不用写 finally
 *  } catch (IOException e) {
 *      throw new AppException("读取失败", e); // 保留原因
 *  }
 *
 *  // SLF4J 日志
 *  private static final Logger log = LoggerFactory.getLogger(MyClass.class);
 *  log.info("用户登录: {}", username);
 *  log.error("发生异常", e);
 *
 *
 * ── 5  反射 ───────────────────────────────────────────────────────────────────
 *
 *  要点：
 *  · 运行时动态获取类信息/访问字段/调用方法，是 Spring/Retrofit/Hilt 的底层基础
 *  · 获取 Class：Class.forName("全限定名") / obj.getClass() / 类名.class
 *  · 访问私有成员：getDeclaredField/Method() + setAccessible(true)
 *  · 动态代理：Proxy.newProxyInstance() + InvocationHandler（AOP 基础）
 *  · 性能：比直接调用慢，频繁使用时缓存 Method/Field 对象
 *
 *  Field f = Secret.class.getDeclaredField("value");
 *  f.setAccessible(true);
 *  f.get(obj); // 读取私有字段
 *
 *  Greeter proxy = (Greeter) Proxy.newProxyInstance(
 *      Greeter.class.getClassLoader(),
 *      new Class[]{ Greeter.class },
 *      (p, method, args) -> "Hello, " + args[0]
 *  );
 *
 *
 * ── 6  注解 ───────────────────────────────────────────────────────────────────
 *
 *  要点：
 *  · @Retention：SOURCE（编译丢弃）/ CLASS（字节码）/ RUNTIME（运行时可读）
 *  · @Target：TYPE / FIELD / METHOD / PARAMETER / CONSTRUCTOR 等
 *  · 运行时读取：method.getAnnotation(Log.class)，框架魔法的核心
 *  · 编译期处理：APT/KSP 生成代码，如 Dagger、Room、Retrofit 的原理
 *
 *  @Retention(RetentionPolicy.RUNTIME)
 *  @Target(ElementType.METHOD)
 *  @interface Log { String tag() default "DEFAULT"; }
 *
 *  Log log = method.getAnnotation(Log.class);
 *  log.tag(); // "USER"
 *
 *
 * ── 7  泛型 ───────────────────────────────────────────────────────────────────
 *
 *  要点：
 *  · 类型擦除：编译后 List<String> 和 List<Integer> 都变成 List
 *    （Kotlin reified 内联函数解决了这个问题）
 *  · <? extends T>：只读，接受 T 及子类（Producer Extends）
 *  · <? super T>：只写，接受 T 及父类（Consumer Super）
 *  · PECS 原则：Producer Extends, Consumer Super
 *  · 泛型与反射：运行时无法直接获取泛型参数，需通过 ParameterizedType 获取
 *    如 Gson/Retrofit 的 TypeToken 就是利用此机制保留泛型信息
 *
 *  static <T extends Comparable<T>> T max(T a, T b) {
 *      return a.compareTo(b) >= 0 ? a : b;
 *  }
 *  static double sum(List<? extends Number> list) { ... } // 只读
 *  static void addInts(List<? super Integer> list) { ... } // 只写
 *
 *  // 获取泛型参数（Gson TypeToken 原理）
 *  Type type = new TypeToken<List<String>>(){}.getType();
 *  // ParameterizedType
 *  ParameterizedType pt = (ParameterizedType) field.getGenericType();
 *  Type[] args = pt.getActualTypeArguments(); // 获取 List<String> 中的 String
 *
 *
 * ── 8  集合框架 ───────────────────────────────────────────────────────────────
 *
 *  List / Set / Map：
 *  · List：有序可重复；ArrayList（随机访问快）vs LinkedList（头尾插删快）
 *  · Map：HashMap（无序）/ LinkedHashMap（插入顺序）/ TreeMap（键排序）
 *  · EnumMap：key 为枚举类型，性能优于 HashMap，枚举 key 场景首选
 *  · Set：HashSet / TreeSet，底层基于对应 Map；元素必须正确实现 equals/hashCode
 *  · equals/hashCode：放入 HashMap/HashSet 必须正确重写，两者必须一致
 *
 *  Queue / Deque / PriorityQueue：
 *  · Queue：offer 入队，poll 出队（FIFO），peek 查看队头
 *  · Deque：双端队列，ArrayDeque 替代 Stack；push/pop 当栈，offer/poll 当队列
 *  · PriorityQueue：最小堆，poll() 返回最小元素；自定义排序传 Comparator
 *
 *  Properties：
 *  · 继承自 Hashtable，专门用于读写 .properties 配置文件（key=value 格式）
 *  · load(InputStream) 读取，store(OutputStream, comment) 写入
 *
 *  Collections 工具类：
 *  · sort / shuffle / reverse / binarySearch / frequency / disjoint
 *  · unmodifiableList/Map/Set：包装为只读视图（≠ List.of，原集合变化会反映）
 *  · synchronizedList/Map：线程安全包装（不如 ConcurrentHashMap 高效）
 *
 *  Iterator：
 *  · 所有集合都实现 Iterable，支持 for-each；Iterator 可在遍历时安全删除元素
 *  · 遍历时不能直接用集合的 remove()，要用 iterator.remove()，否则抛 ConcurrentModificationException
 *
 *  不可变集合（Java 9+）：List.of() / Map.of() / Set.of()
 *
 *  map.getOrDefault("key", 0);
 *  map.putIfAbsent("key", 1);
 *  map.forEach((k, v) -> ...);
 *  Deque<String> stack = new ArrayDeque<>();
 *  stack.push("a"); stack.pop(); // LIFO
 *  List<String> list = List.of("x", "y", "z"); // 不可变
 *
 *  // Iterator 安全删除
 *  Iterator<String> it = list.iterator();
 *  while (it.hasNext()) { if (it.next().isEmpty()) it.remove(); }
 *
 *  // Properties
 *  Properties props = new Properties();
 *  props.load(new FileInputStream("config.properties"));
 *  String host = props.getProperty("db.host", "localhost");
 *
 *
 * ── 9  IO ─────────────────────────────────────────────────────────────────────
 *
 *  File 对象（传统 IO）：
 *  · File 表示路径（文件或目录），不代表实际内容
 *  · 常用：exists / isFile / isDirectory / length / getName / getAbsolutePath
 *  · 列出目录：listFiles() 返回 File[]，可传 FilenameFilter 过滤
 *  · 创建/删除：createNewFile / mkdir / mkdirs / delete
 *  · 推荐用 Path + Files（NIO）替代 File，API 更现代
 *
 *  流体系：
 *  · 字节流：InputStream / OutputStream，处理二进制数据（图片、音频等）
 *  · 字符流：Reader / Writer，处理文本，必须指定编码（UTF-8）
 *  · 装饰器模式：Buffered* 包装原始流增加缓冲，提升性能；Data* 读写基本类型
 *  · try-with-resources：IO 资源必须关闭，此写法最安全
 *
 *  NIO Files（推荐）：
 *  · Java 11+：readString / writeString，一行搞定文本读写
 *  · Java 7+：copy / move / delete / exists / createDirectories / walk
 *  · Files.walk() 递归遍历目录树，返回 Stream<Path>
 *
 *  Zip：
 *  · ZipInputStream 读取 zip，ZipOutputStream 写入 zip
 *  · 逐条 getNextEntry() 遍历 ZipEntry
 *
 *  序列化：
 *  · 实现 Serializable（标记接口），ObjectOutputStream 写 / ObjectInputStream 读
 *  · serialVersionUID：建议显式声明，防止类变更后反序列化失败
 *  · 注意：序列化不安全，生产中推荐用 JSON/Protobuf 替代
 *
 *  classpath 资源：
 *  · getClass().getResourceAsStream("/data/config.json") 读取 jar 内资源
 *  · 路径以 / 开头表示从 classpath 根目录查找
 *
 *  // NIO 推荐写法
 *  Path p = Path.of("data.txt");
 *  Files.writeString(p, "Hello", StandardCharsets.UTF_8);
 *  String s = Files.readString(p, StandardCharsets.UTF_8);
 *
 *  // 字节流拷贝
 *  try (InputStream in  = new BufferedInputStream(new FileInputStream("src"));
 *       OutputStream out = new BufferedOutputStream(new FileOutputStream("dst"))) {
 *      byte[] buf = new byte[4096];
 *      int n;
 *      while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
 *  }
 *
 *  // 读取 classpath 资源
 *  try (InputStream is = getClass().getResourceAsStream("/config.json")) {
 *      String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
 *  }
 *
 *
 * ── 10  日期与时间 ────────────────────────────────────────────────────────────
 *
 *  要点：
 *  · 旧 API（不推荐）：Date / Calendar，设计混乱，月份从 0 开始
 *  · 新 API（Java 8+，推荐）：LocalDate / LocalTime / LocalDateTime，不可变、线程安全
 *  · 时区：ZonedDateTime = LocalDateTime + ZoneId，跨时区场景必用
 *  · 格式化：DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
 *  · Instant：时间戳（距 1970-01-01 的秒数），与 long 互转方便存储
 *  · 最佳实践：内部用 Instant/long 存储，展示时转 LocalDateTime + 时区
 *
 *  LocalDateTime now = LocalDateTime.now();
 *  String s = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 *  LocalDateTime parsed = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 *
 *  ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
 *  Instant instant = Instant.now();
 *  long epochMilli = instant.toEpochMilli(); // 毫秒时间戳
 *
 *
 * ── 11  多线程 ────────────────────────────────────────────────────────────────
 *
 *  基础：
 *  · 创建线程：继承 Thread 或实现 Runnable，推荐 Runnable（解耦）
 *  · 线程状态：NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
 *  · interrupt()：中断线程，线程需检查 isInterrupted() 或捕获 InterruptedException 响应
 *  · 守护线程（Daemon）：setDaemon(true)，JVM 所有非守护线程结束后自动退出
 *
 *  同步：
 *  · synchronized：对象锁，同一时刻只有一个线程进入；注意死锁（两个锁互等）
 *  · volatile：保证可见性（修改立即刷新到主内存），不保证原子性
 *  · ReentrantLock：比 synchronized 更灵活，支持 tryLock / 超时 / 公平锁
 *  · Atomic 原子类：AtomicInteger / AtomicLong / AtomicReference，无锁线程安全操作
 *  · Concurrent 集合：ConcurrentHashMap / CopyOnWriteArrayList，线程安全，高效
 *  · CountDownLatch / CyclicBarrier / Semaphore：线程协调工具
 *
 *  线程池与异步：
 *  · ExecutorService：避免频繁创建/销毁线程；submit() 返回 Future，get() 阻塞等待
 *  · CompletableFuture（Java 8+）：异步编排，支持链式回调
 *    thenApply（转换）/ thenAccept（消费）/ thenCompose（串联）/ allOf（并行等待）
 *  · 虚拟线程（Java 21+）：Thread.ofVirtual().start(...)，轻量级，百万级并发
 *
 *  ThreadLocal：
 *  · 线程私有变量，每个线程独立一份，用完记得 remove() 防内存泄漏
 *  · 典型用途：存储当前用户信息、数据库连接、事务上下文
 *
 *  ExecutorService pool = Executors.newFixedThreadPool(4);
 *  Future<String> future = pool.submit(() -> { Thread.sleep(1000); return "result"; });
 *  String result = future.get(); // 阻塞等待
 *  pool.shutdown();
 *
 *  // CompletableFuture 异步链式
 *  CompletableFuture.supplyAsync(() -> fetchData())
 *      .thenApply(data -> process(data))
 *      .thenAccept(r -> System.out.println(r))
 *      .exceptionally(e -> { log.error("失败", e); return null; });
 *
 *  // Atomic 原子操作
 *  AtomicInteger count = new AtomicInteger(0);
 *  count.incrementAndGet(); // 线程安全的 ++
 *
 *  synchronized (this) { count++; }
 *  volatile boolean running = true;
 *
 *
 * ── 12  函数式编程 ────────────────────────────────────────────────────────────
 *
 *  Lambda 与函数式接口：
 *  · Lambda：(参数) -> 表达式 或 (参数) -> { 语句; }，替代匿名内部类
 *  · 函数式接口：只有一个抽象方法的接口，@FunctionalInterface 标记
 *  · 内置函数式接口（java.util.function）：
 *    Function<T,R>：T→R 转换（apply）
 *    Predicate<T>：T→boolean 判断（test）
 *    Consumer<T>：T→void 消费（accept）
 *    Supplier<T>：()→T 生产（get）
 *    BiFunction<T,U,R>：(T,U)→R
 *  · 方法引用四种形式：Math::abs（静态）/ String::toLowerCase（实例）/
 *    System.out::println（特定实例）/ ArrayList::new（构造）
 *
 *  Optional：
 *  · 包装可能为 null 的值，强制调用方处理空值，避免 NPE
 *  · 创建：Optional.of(val) / Optional.ofNullable(val) / Optional.empty()
 *  · 使用：isPresent / get / orElse / orElseGet / orElseThrow / map / filter / ifPresent
 *  · 注意：不要用 Optional.get() 不检查；不要作为方法参数或字段类型
 *
 *  Stream：
 *  · 惰性求值的数据管道，不修改原集合，可并行（parallelStream()）
 *  · 中间操作：filter / map / flatMap / sorted / distinct / limit / skip / peek
 *  · 终止操作：collect / forEach / count / reduce / findFirst / anyMatch / allMatch
 *  · 常用收集器：Collectors.toList() / toSet() / joining() / groupingBy() / counting() / toMap()
 *
 *  List<String> names = List.of("Alice", "Bob", "Charlie", "Anna");
 *
 *  List<String> result = names.stream()
 *      .filter(n -> n.startsWith("A"))
 *      .map(String::toUpperCase)
 *      .collect(Collectors.toList()); // ["ALICE", "ANNA"]
 *
 *  Map<Integer, List<String>> byLength = names.stream()
 *      .collect(Collectors.groupingBy(String::length));
 *
 *  int sum = IntStream.rangeClosed(1, 100).reduce(0, Integer::sum); // 5050
 *
 *  // Optional
 *  Optional<String> opt = Optional.ofNullable(getName());
 *  String name = opt.map(String::toUpperCase).orElse("UNKNOWN");
 *
 *  // Function 组合
 *  Function<String, Integer> len = String::length;
 *  Function<Integer, Boolean> isLong = n -> n > 5;
 *  Function<String, Boolean> isLongStr = len.andThen(isLong);
 */

private val Green = Color(0xFF4CAF50)

private val chapters = listOf(
    NoteChapter("1",  "基础语法 & 数组"),
    NoteChapter("2",  "面向对象基础（OOP）"),
    NoteChapter("3",  "Java 核心类"),
    NoteChapter("4",  "异常处理"),
    NoteChapter("5",  "反射"),
    NoteChapter("6",  "注解"),
    NoteChapter("7",  "泛型"),
    NoteChapter("8",  "集合框架"),
    NoteChapter("9",  "IO"),
    NoteChapter("10", "日期与时间"),
    NoteChapter("11", "多线程"),
    NoteChapter("12", "函数式编程"),
)

@Composable
fun JavaBasicsScreen(
    onBack: () -> Unit,
    onChapterClick: (NoteChapter) -> Unit = {}
) {
    NoteDetailScaffold(
        title = "Java 核心基础",
        subtitle = "廖雪峰教程 2.3 → 19（函数式编程）",
        color = Green,
        chapters = chapters,
        onBack = onBack,
        onChapterClick = onChapterClick
    )
}
