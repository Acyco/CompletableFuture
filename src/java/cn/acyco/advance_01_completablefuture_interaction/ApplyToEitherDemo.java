package cn.acyco.advance_01_completablefuture_interaction;

import cn.acyco.utils.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ApplyToEitherDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步任务交互

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

        // 哪些异步任务的结果先到达，就使用哪个异步任务的结果
        CompletableFuture<Integer> future = future1.applyToEither(future2, result -> {
            CommonUtils.printTheadLog("最先到达的结果：" + result);
            return result;
        });

        CommonUtils.sleepSecond(4);

        Integer ret = future.get();
        CommonUtils.printTheadLog("ret = " + ret);

        /**
         * 异步任务交互指两个任务异步，哪个结果先到，就使用哪个结果（先到先得）
         */
    }
}
