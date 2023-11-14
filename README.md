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

### 2、创建异步任务

#### 2.1 runAsync

如果你要异步运行某些耗时的后台任务，并且不想从任务中返回任何内容，则可以使用`CompletableFuture.runAsync()`方法。它接受一个Runnable接口实现类对象，方法返回`CompletableFuture<Void>`对象

```
static CompletableFuture<Void> runAsync(Runnable runnable)
```
演示案例：创建一个不从任务中返回任何内容的CompletableFuture异步任务对象
```java

public class RunAsyncDemo {
  public static void main(String[] args) {
    // runAsync 创建异步任务
    CommonUtils.printTheadLog("main start");
    // 使用Runnable匿名内部类
    CompletableFuture.runAsync(new Runnable() {
      @Override
      public void run() {
        CommonUtils.printTheadLog("读取文件开始");
        // 使用睡眠来模拟一个长时间的工作任务（例如读取文件，网络请求等）
        CommonUtils.sleepSecond(3);
        CommonUtils.printTheadLog("读取文件结束");
      }
    });

    CommonUtils.printTheadLog("here are not blacked,main continue");
    CommonUtils.sleepSecond(4); // 此处休眠 为的是等待CompletableFuture背后的线程池执行完成。
    CommonUtils.printTheadLog("main end");
  }
}
```

我们也可以以Lambda表达式的形式传递Runnable接口实现类对象

```java
public class RunAsyncDemo2 {
    public static void main(String[] args) {
        // runAsync 创建异步任务
        CommonUtils.printTheadLog("main start");
        // 使用Lambda表达式
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                CommonUtils.printTheadLog("读取文件开始");
                // 使用睡眠来模拟一个长时间的工作任务（例如读取文件，网络请求等）
                CommonUtils.sleepSecond(3);
                CommonUtils.printTheadLog("读取文件结束");
            }
        });

        CommonUtils.printTheadLog("here are not blacked,main continue");
        CommonUtils.sleepSecond(4); // 此处休眠 为的是等待CompletableFuture背后的线程池执行完成。
        CommonUtils.printTheadLog("main end");
    }
}

```

需求：使用CompletableFuture开启异步任务读取news.txt文件中的新闻稿，并打印输出。

```java

public class RunAsyncDemo3 {
    public static void main(String[] args) {
        // 需求：使用CompletableFuture开启异步任务读取news.txt文件中的新闻稿，并打印输出。
        CommonUtils.printTheadLog("main start");

        CompletableFuture.runAsync(() -> {
            CommonUtils.printTheadLog("读取文件");
            String content = CommonUtils.readFile("news.txt");
            System.out.println(content);
        });

        CommonUtils.printTheadLog("here not blacked main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");
    }
}

```

 **疑问**： 异步任务是并发执行还是并行执行？ 
 
* 如果是单核CPU，那么异步任务之间就是并发执行，如果是多核CPU（多CPU）异步任务就是并行执行
* **重点**： 作为开发者，我们只需要清楚如何开启异步任务，CPU硬件会把异步任务合理的分配给CPU上的核运行。

#### 2.2 supplyAsync
`Completable?Future.runAsync()`开启不带返回结果异步任务。但是，如果你想从后台的异步任务中返回一个结果怎么办？此时，`CompletalbeFuture.supplyAsync()`是你的最好的选择了。

```
static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
```
它入参一个`Supplier<U>`供给者，用于供给带返回值的异步任务

并返回`CompletableFuture<U>`,其中的U是供给者给值的类型。

需求： 开启异步任务读取news.txt文件中的新闻稿，返回文件中内容并在主线程打印输出

```java
public class SupplyAsyncDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求： 开启异步任务读取news.txt文件中的新闻稿，返回文件中内容并在主线程打印输出
        CommonUtils.printTheadLog("main start");

        CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                String content = CommonUtils.readFile("news.txt");
                return content;
            }
        });

        CommonUtils.printTheadLog("here not blacked,main continue");
        // 阻塞并等待newsFuture完成
        String news = newsFuture.get();
        System.out.println("news = " + news);
        CommonUtils.printTheadLog("main end");
    }
}

```

如果想要获取newsFuture结果，可以调用completableFuture.get()方法，get()将阻塞，直到newsFuture完成。

**疑问：** get方法阻塞的，会不会影响程序性能？

后面会讲解回调函数

我们依然可以使用Java 8的Lambda表达式使上面的代码更简洁。

```java
CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(() -> {
    String news = CommonUtils.readFile("news.txt");
    return news;
});
```
#### 2.3 异步任务中的线程池

大家已经知道，`runAsync()`和`supplyAsync()`方法都是开启单独的线程中执行异步任务。但是，我们从未创建线程对吗？不是吗？

CompletableFuture会从全局`ForkJoinPool.commonPool()`线程池获取来执行这些任务

当然， 你也可以创建一个线程池，并将其传递给`runAsync()`
和`supplyAsync()`方法，以使它们在从您指定的线程池获得的线程中执行任务。

CompletableFuture API中的所有方法都有两种变体，一种是接受传入的`Executor`参数作为指定的线程池，而另一个则使用默认的线程池（`ForkJoinPool.commonPool()`）

```java
// runAsync()重载
static CompletableFuture<Void> runAsync(Runnable runnable)
static CompletableFuture<Void> runAsync(Runnable runnable,
        Executor executor)
// spplyAsync()重载
static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier,Executor executor)
```
需求：指定线程池，开启异步任务读取news.txt中的新闻稿，返回文件内容并在主线程打印输出

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(() -> {
    CommonUtils.printTheadLog("异步读取文件开始");
    String news = CommonUtils.readFile("news.txt");
    return news;
},executor);
```
> 最佳实践：创建属于自己的业务线程池
> 
> 如果所有`completableFuture`共享一个线程池，那么一旦有异步任务执行一些很慢的I/O操作，就会导致线程池中所有的线程都阻塞在I/O操作上，从而造成线程饥饿，进而影响整个系统的性能。
> 
> 所以，强烈建议你要根据不同的业务类型创建不同的线程池，以避免互相干扰。

#### 2.4 异步编程思想

综合上述，看到了吧，我们没有显示地创建线程，更没有涉及线程通信的概念，整个过程根本就没涉及线程知识吧，以上专业的说法是：**线程的创建和线程负责的任务进行解耦，它给我们带来的好处线程的创建和启动全部交给线程池负责，具体任务的编写交给程序员，专人专事。**

**异步编程**是可以让程序并行（也可能是并发）运行的一种手段，其可以让程序中的一个工作单元作为异步任务与主线程分开独立运行，并且在异步任务运行结束后，会通知主线程它的运行结果或者失败原因，毫无疑问，一个异步任务其实就是开启一个线程来完成的，使用异步编程可以提高应用程序的性能和响应能力等。

作为开发者，只需要有一个意识：

开发者只需要把耗时的操作交给CompletableFuture开一个异步任务，然后继续关注主线程业务，当异步任务运行完成时会通知主线程它的运行结果。我们把具备了这种编程思想的开发称为**异步编程思想**。

### 3、异步任务回调

`CompletalbeFuture.get()`方法是阻塞的。调用时它会阻塞等待，直到这个Future完成，并在完成后返回结果。但是，很多时候这不是我们想要的。

对于构建异步系统，我们应该能够将**回调**附加到CompletableFuture上，当这个Future完成时，该回调自动被调用，这样，我们就不必等待结果了，然后在Future的回调函数内编写完成Future之后需要执行的逻辑。您可以使用`thenApply()`,`thenAccept()`和`thenRun()`方法，它们可以把回调函数附加到CompletableFuture

#### 3.1 thenApply

使用`thenApply()`方法可以处理和转换CompletableFuture的结果，它以Function<T, U>作为参数。 Function<T, U>是一个函数式接口，表示一个转换操作，它接受类型T的参数并产生类型R的结果

```
CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
```
需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成数组（敏感词数组），异步任务返回敏感词数组

```java
public class ThenApplyDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成数组（敏感词数组），异步任务返回敏感词数组
        CommonUtils.printTheadLog("main start");

        CompletableFuture<String> readFileFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words文件");
            String filterWordContent = CommonUtils.readFile("filter_words.txt");
            return filterWordContent;

        });
        CompletableFuture<String[]> filterWordsFuture = readFileFuture.thenApply(content -> {
            CommonUtils.printTheadLog("把雇佣兵内容转换成敏感词数组");
            String[] filterWords = content.split(",");
            return filterWords;
        });

        CommonUtils.printTheadLog("main continue");
        String[] filterWords = filterWordsFuture.get();
        System.out.println("Arrays.toString(filterWords) = " + Arrays.toString(filterWords));
        CommonUtils.printTheadLog("main end");
    }
}

```
你还可以通过附加一系列`thenApply()`回调方法，在CompletableFuture上编写一系列转换序列。一个`thenApply()`方法的结果可以传递给序列中的下一个，如果你对链式操作很了解，你会发现结果可以在链式操作上传递。

```java
CompletableFuture<String[]> filterWordsFuture = CompletableFuture.supplyAsync(() -> {
    String filterWordContent = CommonUtils.readFile("filter_words.txt");
    return filterWordContent;
}).thenApply(content -> {
    String[] filterWords = content.split(",");
    return filterWords;
});
```
#### 3.2 thenAccept

如果不想从回调函数返回结果，而只想在Future完成后运行一些代码，则可以使用`thenAccpet()`

这些方法是一个`Consumer<? super T>`，它可以对异步任务的执行结果进行消费使用，方法返回CompletableFuture<Void>

```
CompletableFuture<Void> thenAccept(Consumer<? super T> action)
```

通常用作回调链中的最后一个回调。

需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成敏感词数组，然后打印敏感词数组

```java
public class ThenAcceptDemo {
    public static void main(String[] args) {
        // 需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成敏感词数组，然后打印敏感词数组
        CommonUtils.printTheadLog("main start");

        CompletableFuture.supplyAsync(()->{
            CommonUtils.printTheadLog("读取filter_words.txt文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            return filterWordsContent;
        }).thenApply(content ->{
            CommonUtils.printTheadLog("把文件内容转换成敏感词数组");
            String[] filterWords = content.split(",");
            return filterWords;
        }).thenAccept(filterWords->{
            CommonUtils.printTheadLog("filterWorlds = " + Arrays.toString(filterWords));
        });

        CommonUtils.printTheadLog("main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");
    }
}
```
#### 3.3 thenRun

前面我们已经知道，通过`thenApply(Function<T,R>)`对链式操作中的上一个异步任务的结果进行转换，返回一个新的结果;
通过`thenAccpet(Consumer<T>)`对链式操作中的上一个异步任务的结果进行消费，不返回新结果;

如果我们只是想从CompletableFuture的链式操作得到一个完成的通知，甚至都不使用上一个链式操作的结果，那么`CompletableFuture.thenRun()`会是你最佳的选择，它需要一个Runnable并返回`CompletableFuture<Void>`

```
CompletableFuture<Void> thenRun(Runnable action);
```

演示案例： 我们仅仅想知道 filter_words.txt 的文件是否读取完成

```java
public class ThenRunDemo {
    public static void main(String[] args) {
        // 演示案例： 我们仅仅想知道敏感词汇的文件是否读取完成
        CommonUtils.printTheadLog("main start");

        CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            return filterWordsContent;
        }).thenRun(() -> {
            CommonUtils.printTheadLog("读取filter_words文件读取完成");
        });

        CommonUtils.printTheadLog("main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");
    }
}
```
