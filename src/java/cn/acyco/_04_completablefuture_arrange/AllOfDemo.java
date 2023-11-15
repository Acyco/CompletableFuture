package cn.acyco._04_completablefuture_arrange;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AllOfDemo {

    public static CompletableFuture<String> readFileFuture(String fileName){
        return CompletableFuture.supplyAsync(()->{
            String content = CommonUtils.readFile(fileName);
            return content;
        });
    }

    public static void main(String[] args) {
        // 需求： 统计news1.txt,news2.txt,news3.txt文件中包含CompletableFuture关键字的文件的个数

        // step 1: 创建List集合存储文件名
        List<String> fileList = Arrays.asList("news1.txt", "news2.txt", "news3.txt");

        // step 2: 根据文件名调用readFileFuture创建多个CompletableFuture,并存入List集合中
        List<CompletableFuture<String>> readFileFutureList = fileList.stream().map(fileName -> {
            return readFileFuture(fileName);
        }).collect(Collectors.toList());

        // step 3: 把List集合转换成数组待用，以便传入allOf方法中
        int len = readFileFutureList.size();
        CompletableFuture[] readFileFutureArr = readFileFutureList.toArray(new CompletableFuture[len]);

        // step 4: 使用allOf方法合并多个异步任务
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(readFileFutureArr);

        // step 5: 当多个异步任务都完成后，使用回调操作文件结果，统计符合条件的文件个数
        CompletableFuture<Long> countFuture = allOfFuture.thenApply(v -> {
            return readFileFutureList.stream()
                    .map(future -> future.join())
                    .filter(content -> content.contains("CompletableFuture"))
                    .count();
        });

        // step 6: 主线程打印输出文件个数
        Long count = countFuture.join();
        System.out.println("count = " + count);

        /**
         * allOf 特别适合合并多个异步任务，当所有异步任务都完成时可以进一步操作
         */
    }
}
