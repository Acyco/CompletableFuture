package cn.acyco.advance_03_parallelstream_complettablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        // CompletableFuture 在流式操作中的优势
        // 需求： 创建10个 MyTask 耗时的任务， 统计它们执行完的总耗时
        // 方案三：使用CompletableFuture

        // step 1: 创建10个MyTask对象，每个任务持续1s, 存入List集合
        IntStream intStream = IntStream.range(0, 10);
        List<MyTask> tasks = intStream.mapToObj(item -> {
            return new MyTask(1);
        }).collect(Collectors.toList());

        // step 2: 根据MyTask对象构建10个异步任务
        List<CompletableFuture<Integer>> futures = tasks.stream().map(myTask -> {
            return CompletableFuture.supplyAsync(()-> {
                return myTask.doWork();
            });
        }).collect(Collectors.toList());

        // step 3: 执行异步任务，执行完成后，获取异步任务的结果，存入List集合中，统计总耗时
        long start = System.currentTimeMillis();
        List<Integer> results = futures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();

        double costTime = (end - start) / 1000.0;
        System.out.printf("processed %d tasks %.2f second", tasks.size(), costTime);

        /**
         * 观察发现
         * 使用CompletableFuture和使用并行流的时间大致一样
         */
    }
}
