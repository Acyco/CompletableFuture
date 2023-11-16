package cn.acyco.advance_01_completablefuture_interaction;

import cn.acyco.utils.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class runAfterEitherDemo {
    public static void main(String[] args) {
        // 异步任务交互 runAfterEither

        // 异步任务1
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int x = new Random().nextInt(3);
            CommonUtils.sleepSecond(x);
            CommonUtils.printTheadLog("任务1耗时" + x + "秒");
            return x;
        });

        // 异步任务2
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int y = new Random().nextInt(3);
            CommonUtils.sleepSecond(y);
            CommonUtils.printTheadLog("任务2耗时" + y + "秒");
            return y;
        });

        future1.runAfterEither(future2, () -> {
            CommonUtils.printTheadLog("有一个异步任务完成");
        });

        CommonUtils.sleepSecond(4);

        /**
         * thenApply            thenAccept              thenRun
         * 对上一个异步任务的结果进行操作（转换、消费使用等）
         *
         * applyToEither        acceptEither            runAfterEither
         * 对两个异步任务先到的结果进行操作（转换、消费使用等）
         */
    }
}
