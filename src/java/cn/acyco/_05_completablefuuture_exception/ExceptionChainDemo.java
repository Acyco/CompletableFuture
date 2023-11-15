package cn.acyco._05_completablefuuture_exception;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class ExceptionChainDemo {
    public static void main(String[] args) {
        // 异常如何在回调链中传播

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
//            int r = 1 / 0;
            return "result1";
        }).thenApply(result -> {
            CommonUtils.printTheadLog(result);
            String str = null;
            int len = str.length();
            return result + " result2";
        }).thenApply(result -> {
            return result + " result3";
        }).thenAccept(result -> {
            CommonUtils.printTheadLog(result);
        });

        /**
         * 如果回调链中出现任何异常，回调链不在向下执行，立即转入异常处理。
         */
    }
}
