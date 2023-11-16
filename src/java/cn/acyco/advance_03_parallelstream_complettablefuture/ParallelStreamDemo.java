package cn.acyco.advance_03_parallelstream_complettablefuture;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelStreamDemo {
    public static void main(String[] args) {
        // 并行流的局限性
        // 需求： 创建10个 MyTask 耗时的任务， 统计它们执行完的总耗时
        // 方案二：使用串行流
        // step 1: 创建10个MyTask对象，每个任务持续1s, 存入List集合
        IntStream intStream = IntStream.range(0, 10);
        List<MyTask> tasks = intStream.mapToObj(item -> {
            return new MyTask(1);
        }).collect(Collectors.toList());

        // step 2: 执行10个MyTask,统计总耗时
        long start = System.currentTimeMillis();
        List<Integer> results = tasks.parallelStream().map(myTask -> {
            return myTask.doWork();
        }).collect(Collectors.toList());

        long end = System.currentTimeMillis();

        double costTime = (end - start) / 1000.0;
        System.out.printf("processed %d tasks %.2f second", tasks.size(), costTime);
    }
}
