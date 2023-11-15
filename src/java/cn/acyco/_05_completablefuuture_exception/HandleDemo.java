package cn.acyco._05_completablefuuture_exception;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HandleDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // handle()
        CommonUtils.printTheadLog("main start");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            int r = 1 / 0;
            return "result1";
        }).handle((result,ex)->{
            CommonUtils.printTheadLog("上一步异常的恢复");
            if(ex != null){
                CommonUtils.printTheadLog("出现异常：" + ex.getMessage());
                return "UnKnown";
            }
            return result;
        });

        CommonUtils.printTheadLog("main continue");
        String ret = future.get();
        CommonUtils.printTheadLog("ret = " + ret);

        CommonUtils.printTheadLog("main end");

        /**
         * 异步任务不管是否发生异常，handle()方法都会执行。
         * 所以，handle核心作用在于对上一步异步任务进行现场修复
         */
    }
}
