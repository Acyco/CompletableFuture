package cn.acyco.advance_04_compare_price;

public class ComparePriceDemo {
    public static void main(String[] args) {
        // 方案一测试： 串行方式操作商品比价
        ComparePriceService service = new ComparePriceService();
        long start = System.currentTimeMillis();
        PriceResult priceResult = service.getCheapestPlatformPrice("iPhone14");
        long end = System.currentTimeMillis();
        double costTime = (end - start) / 1000.0;
        System.out.printf("cost %.2f second\n", costTime);
        System.out.println("priceResult = " + priceResult);
    }
}
