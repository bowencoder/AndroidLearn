package com.example.androidlearn.feature.junior.detail.stage1

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.androidlearn.feature.shared.TopicDetail
import com.example.androidlearn.feature.shared.TopicDetailScaffold

private val detail = TopicDetail(
    title = "Java 核心基础",
    description = "面向对象、集合框架、泛型、IO 流、多线程、反射、注解",
    overview = "Java 是 Android 开发的基础语言。尽管 Kotlin 是官方首选，但 Java 基础（面向对象、集合、多线程）依然是理解 Android 框架源码的必备能力。",
    keyPoints = listOf(
        "面向对象：封装/继承/多态，抽象类 vs 接口，static/final 关键字，内部类",
        "集合框架：List/Set/Map 接口，ArrayList/LinkedList/HashMap/LinkedHashMap/TreeMap 区别",
        "泛型：类型参数、通配符（? extends / ? super），类型擦除原理",
        "IO 流：InputStream/OutputStream，Reader/Writer，BufferedIO，NIO 基础",
        "多线程：Thread/Runnable，synchronized，volatile，线程池 ExecutorService，Future",
        "反射：Class.forName，getDeclaredField/Method，setAccessible，动态代理",
        "注解：@Retention，@Target，自定义注解，运行时注解解析"
    ),
    codeSnippet = """
// 集合框架使用
List<String> list = new ArrayList<>();
list.add("Alice"); list.add("Bob");
Map<String, Integer> map = new HashMap<>();
map.put("score", 100);

// 多线程 ExecutorService
ExecutorService pool = Executors.newFixedThreadPool(4);
Future<String> future = pool.submit(() -> {
    // 后台任务
    return "result";
});
String result = future.get(); // 阻塞等待结果

// 反射获取私有字段
Class<?> clazz = Class.forName("com.example.MyClass");
Field field = clazz.getDeclaredField("secret");
field.setAccessible(true);
Object value = field.get(instance);

// 自定义注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    String tag() default "DEFAULT";
}
    """.trimIndent(),
    tips = listOf(
        "Android 源码大量使用 Java，读懂 Handler/Looper/Binder 源码需要扎实的 Java 基础",
        "Kotlin 完全兼容 Java，理解 Java 泛型擦除有助于理解 Kotlin 的 reified 类型参数",
        "Java 多线程知识（synchronized/volatile/AQS）是 Android 并发编程的底层基础"
    )
)

@Composable
fun JavaBasicsScreen(onBack: () -> Unit) {
    TopicDetailScaffold(
        detail = detail,
        stageColor = Color(0xFF4CAF50),
        stageTitle = "语言与开发基础",
        onBack = onBack
    )
}
