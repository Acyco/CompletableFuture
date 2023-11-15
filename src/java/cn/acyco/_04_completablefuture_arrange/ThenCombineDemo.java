package cn.acyco._04_completablefuture_arrange;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.*;

public class ThenCombineDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：替换新闻稿（ news.txt )中敏感词汇，把敏感词汇替换成*，敏感词存储在 filter_words.txt 中
        CommonUtils.printTheadLog("main start");

        // step 1: 读取filter_words.txt文件内容，并解析成敏感数组
        CompletableFuture<String[]> future1 = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            String[] filterWords = filterWordsContent.split(",");
            return filterWords;
        });

        // step 2: 读取news.txt文件内容
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取news文件");
            String newsContent = CommonUtils.readFile("news.txt");
            return newsContent;
        });

        // step 2: 替换操作
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture<String> combineFuture = future1.thenCombineAsync(future2, (filterWords, newsContent) -> {
            CommonUtils.printTheadLog("替换操作");
            for (String word : filterWords) {
                if (newsContent.indexOf(word) >= 0) {
                    newsContent = newsContent.replace(word, "**");
                }
            }
            return newsContent;
        },executor);

        CommonUtils.printTheadLog("main continue");
        String news = combineFuture.get();
        CommonUtils.printTheadLog("news = " + news);
        CommonUtils.printTheadLog("main end");

        /**
         * thenCombine 用于合并2个没有依赖关系的异步任务
         */
    }
}
