package cn.acyco.advance_04_compare_price;

import cn.acyco.utils.CommonUtils;

public class HttpRequest {

    private static void mockCostTimeOperation() {
        CommonUtils.sleepSecond(1);
    }

    // 获取淘宝平台的商品价格
    public static PriceResult getTaobaoPrice(String productName) {
        CommonUtils.printTheadLog("获取淘宝上" + productName + "价格");
        mockCostTimeOperation();
        PriceResult priceResult = new PriceResult("淘宝");
        priceResult.setPrice(5199);
        CommonUtils.printTheadLog("获取淘宝上" + productName + "价格完成：5199");
        return priceResult;
    }

    // 获取淘宝平台的优惠
    public static int getTaoBaoDiscount(String productName) {
        CommonUtils.printTheadLog("获取淘宝上" + productName + "优惠");
        mockCostTimeOperation();
        CommonUtils.printTheadLog("获取淘宝上" + productName + "优惠完成：-200");
        return 200;
    }

    // 获取京东平台的商品价格
    public static PriceResult getJDongPrice(String productName) {
        CommonUtils.printTheadLog("获取京东上" + productName + "价格");
        mockCostTimeOperation();
        PriceResult priceResult = new PriceResult("淘宝");
        priceResult.setPrice(5299);
        CommonUtils.printTheadLog("获取京东上" + productName + "价格完成：5299");
        return priceResult;
    }

    // 获取京东平台的优惠
    public static int getJDongDiscount(String productName) {
        CommonUtils.printTheadLog("获取京东上" + productName + "优惠");
        mockCostTimeOperation();
        CommonUtils.printTheadLog("获取京东上" + productName + "优惠完成：-150");
        return 150;
    }

    // 获取拼多多平台的商品价格
    public static PriceResult getPDDPrice(String productName) {
        CommonUtils.printTheadLog("获取拼多多上" + productName + "价格");
        mockCostTimeOperation();
        PriceResult priceResult = new PriceResult("拼多多");
        priceResult.setPrice(5399);
        CommonUtils.printTheadLog("获取拼多多上" + productName + "价格完成：5399");
        return priceResult;
    }

    // 获取拼多多平台的优惠
    public static int getPDDDiscount(String productName) {
        CommonUtils.printTheadLog("获取拼多多上" + productName + "优惠");
        mockCostTimeOperation();
        CommonUtils.printTheadLog("获取拼多多上" + productName + "优惠完成：-5300");
        return 5300;
    }

}
