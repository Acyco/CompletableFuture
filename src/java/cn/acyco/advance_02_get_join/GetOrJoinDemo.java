package cn.acyco.advance_02_get_join;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetOrJoinDemo {
    public static void main(String[] args) {
        // get or join
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "hello";
        });

        String ret = future.join();
        CommonUtils.printTheadLog("ret = " + ret);

        // get
        /*try {
            String ret = future.get();
            CommonUtils.printTheadLog("ret = " + ret);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }*/
    }
}
