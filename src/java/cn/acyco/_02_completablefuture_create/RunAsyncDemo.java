package cn.acyco._02_completablefuture_create;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class RunAsyncDemo {
    public static void main(String[] args) {
        // runAsync 创建异步任务
        CommonUtils.printTheadLog("main start");
        // 使用Runnable匿名内部类
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                CommonUtils.printTheadLog("读取文件开始");
                // 使用睡眠来模拟一个长时间的工作任务（例如读取文件，网络请求等）
                CommonUtils.sleepSecond(3);
                CommonUtils.printTheadLog("读取文件结束");
            }
        });

        CommonUtils.printTheadLog("here are not blacked,main continue");
        CommonUtils.sleepSecond(4); // 此处休眠 为的是等待CompletableFuture背后的线程池执行完成。
        CommonUtils.printTheadLog("main end");
    }
}
