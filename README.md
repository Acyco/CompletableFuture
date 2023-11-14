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