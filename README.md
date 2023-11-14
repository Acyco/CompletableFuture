# CompletableFuture入门
## 学习内容

- 为什么选择CompletableFuture
- 创建CompletableFuture异步任务
- CompletableFuture异步任务回调
- CompletableFuture异步任务编排
- CompletableFuture的异常处理

## 学习目标

- 了解 CompletableFuture 的优点
- 掌握创建异步任务
    * 创建异步任务的2种方式
    * 知道异步任务中线程池的作用
    * 理解异步任务编程思想
- 掌握异步任务回调
    * thenApply / thenAccept / thenRun 3类方法使用和区别
    * 解锁一系列Async版本回调 (thenXxxAsync)
- 掌握异步任务的异常处理
    * 会对异步任务进行异常处理
    * 会对回调链上对单个异步任务的异常进行现场恢复

## 课程学习说明

- 熟悉多线程理论知识
- 接触过 Future 和 线程池 的经历
- 会使用Lambda表达式和 Stream-API

### 1、Future vs CompletableFuture

#### 1.1 准备工作

为了便于后续更好地调试和学习，我们需要定义一个工具类辅助我们对知识的理解

```java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务辅助工具类
 */
public class CommonUtils {
  // 读取文件路径的文件
  public static String readFile(String pathToFile) {
    try {
      return Files.readString(Paths.get(pathToFile));
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  // 休眠指定的毫秒数
  public static void sleepMillis(long millis) {
    try {
      TimeUnit.MILLISECONDS.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // 休眠指定的秒数
  public static void sleepSecond(long second) {
    try {
      TimeUnit.SECONDS.sleep(second);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  // 打印输出带线程信息的日志
  public static void printTheadLog(String message) {
    // 时间戳 | 线程id | 线程名 | 日志信息
    String result = new StringJoiner(" | ")
            .add(String.valueOf(System.currentTimeMillis()))
            .add(String.format("%2d", Thread.currentThread().getId()))
            .add(Thread.currentThread().getName())
            .add(message)
            .toString();
    System.out.println(result);
  }

}
```

#### 1.2 Future 的局限性

需求： 替换新闻稿（news.txt)中的敏感词汇，把敏感词汇替换成*，敏感词存储在 filter_words.txt 中
```java
public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        // step 1: 读取敏感词汇 => thread1
        Future<String[]> filterWordFuture = executor.submit(() -> {
            String str = CommonUtils.readFile("filter_words.txt");
            String[] filterWords = str.split(",");
            return filterWords;
        });

        // step 2: 读取新闻稿 => thread2
        Future<String> newsFuture = executor.submit(() -> {
            return CommonUtils.readFile("news.txt");
        });

        // step 3: 替换操作 => thread3
        Future<String> replaceFuture = executor.submit(() -> {
            String[] words = filterWordFuture.get();
            String news = newsFuture.get();
            for (String word : words) {
                if (news.indexOf(word) > 0) {
                    news = news.replace(word, "**");
                }
            }
            return news;
        });
        // step 4: 打印输出替换后的新闻稿 => main
        String filteredNews = replaceFuture.get();
        System.out.println("filteredNews = " + filteredNews);

        executor.shutdown();
    }
}
```
通过上面的代码，我们会发现， Future相比于所有任务都直接在主线程处理，有很多优势，但同时也存在不足，至少表现如下：

* **在没有阻塞的情况下，无法对Future的结果执行进一步的操作。** Future不会告知你它什么时候完成，你如果想要得到结果，必须通过一个get()方法，该方法会阻塞直到结果可用为止。它不具备将回调函数附加到Future后并在Future的结果可用时自动调用回调的能力。
* **无法解决任务相互依赖的问题。** filterWordFuture和newsFuture的结果不能自动发送给replaceFuture, 需要在replaceFuture中手动获取，所以使用Future不能轻而易举地创建异步工作流。
* **不能将多个Future合并在一起。** 假设你有多种不同的Future, 你想在它们全部并行完成后然后运行某个函数，Future很难独立完成这一需要。
* **没有异常处理。** Future提供的方法中没有专门的API应对异常处理，还需要开发者自己手动异常处理。

#### 1.3 CompletableFuture 的优势

![CompletableFuture](./images/CompletableFuture.png)

**CompletableFuture**实现了**Future**和**CompletionStage**接口

CompletableFuture相对于Future具有以下优势：
* 为快速创建、链接依赖和结合多个Future提供了大量的便利方法。
* 提供了适用于各种开发场景的回调函数。它还提供了非常全面的异常处理支持。
* 无疑衔接和亲和lambda表达式和Stream - API。
* 我见过的真正意义上的异步编程，把异步编程和函数式编程、响应式编程多种高阶编程思维集于一身，设计上更优雅。
