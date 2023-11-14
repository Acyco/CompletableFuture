package cn.acyco._02_completablefuture_create;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class RunAsyncDemoDiff {

    public static void main(String[] args) {
        // 回顾线程的创建和启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonUtils.printTheadLog("读取文件开始");
                // 使用睡眠来模拟一个长时间的工作任务（例如读取文件，网络请求等）
                CommonUtils.sleepSecond(3);
                CommonUtils.printTheadLog("读取文件结束");
            }
        }).start();

        // 开启一个异步任务读取文件
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                CommonUtils.printTheadLog("读取文件开始");
                // 使用睡眠来模拟一个长时间的工作任务（例如读取文件，网络请求等）
                CommonUtils.sleepSecond(3);
                CommonUtils.printTheadLog("读取文件结束");
            }
        });

        /**
         * CompletableFuture 中的异步任务底层通过开启线程的方式完成的
         */
    }
}
