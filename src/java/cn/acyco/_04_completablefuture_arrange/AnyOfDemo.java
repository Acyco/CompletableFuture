package cn.acyco._04_completablefuture_arrange;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AnyOfDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //anyOf()
        CompletableFuture<String> future1 =CompletableFuture.supplyAsync(()->{
            CommonUtils.sleepSecond(2);
            return "Future1的结果";
        });

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            CommonUtils.sleepSecond(1);
            return "Future2的结果";
        });

        CompletableFuture<String> future3 =CompletableFuture.supplyAsync(()->{
            CommonUtils.sleepSecond(3);
            return "Future3的结果";
        });

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3);

        Object ret = anyOfFuture.get();
        System.out.println("ret = " + ret);
    }
}
