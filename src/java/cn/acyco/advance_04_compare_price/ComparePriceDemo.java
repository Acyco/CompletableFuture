package cn.acyco.advance_04_compare_price;

public class ComparePriceDemo {
    public static void main(String[] args) {
        // 方案一测试： 串行方式操作商品比价
        ComparePriceService service = new ComparePriceService();
        /*
        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice("iPhone14");
        long end = System.currentTimeMillis();
        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second\n", costTime);
        System.out.println("priceResult = " + priceResult);
        */

        // 方案二测试：使用Future+线程池增加并行
        /*
        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice2("iPhone14");
        long end = System.currentTimeMillis();
        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second\n", costTime);
        System.out.println("priceResult = " + priceResult);
        */
        
        // 方案三测试：使用CompletableFuture进一步增强并行
        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice3("iPhone14");
        long end = System.currentTimeMillis();
        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second\n", costTime);
        System.out.println("priceResult = " + priceResult);

        /**
         * 方案一：串行方式操作商品比价                consTime 6.19
         * 方案二：Future+线程池 提高了任务处理的并行性  costTime 2.19
         * 方案三：使用CompletableFuture进一步增强并行 costTime 1.10
         */
    }
}
