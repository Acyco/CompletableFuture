package cn.acyco._05_completablefuuture_exception;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HandleDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求： 对回调链中的一次异常进行恢复处理

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            int r = 1 / 0;
            return "result1";
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println("出现异常：" + ex.getMessage());
                return "UnKnown1";
            }
            return result;
        }).thenApply(result -> {

            String str = null;
            int len = str.length();

            return result + " result2";
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println("出现异常：" + ex.getMessage());
                return "UnKnown2";
            }
            return result;
        }).thenApply(result -> {
            return result + " result3";
        });

        String ret = future.get();
        CommonUtils.printTheadLog("ret = " + ret);
    }
}
