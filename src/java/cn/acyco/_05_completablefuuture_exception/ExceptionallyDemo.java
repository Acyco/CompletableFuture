package cn.acyco._05_completablefuuture_exception;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class ExceptionallyDemo {
    public static void main(String[] args) {
        // exceptionally 处理回调链中的异常

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            int r = 1 / 0;
            return "result1";
        }).thenApply(result -> {
            String str = null;
            int len = str.length();
            return result + " result2";
        }).thenApply(result -> {
            return result + " result3";
        }).exceptionally(ex -> {
            System.out.println("出现异常：" + ex.getMessage());
            return "UnKnown";
        });
    }
}
