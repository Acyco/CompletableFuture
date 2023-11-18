package cn.acyco.advance_04_compare_price;

import cn.acyco.utils.CommonUtils;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class ComparePriceService {
    // 方案一：串行方式操作商品比价

    public PriceResult getCheapestPlatformPrice(String productName) {
        PriceResult priceResult;
        int discount;

        // 获取淘宝平台的商品价格和优惠
        priceResult = HttpRequest.getTaobaoPrice(productName);
        discount = HttpRequest.getTaoBaoDiscount(productName);
        PriceResult taoBaoPriceResult = this.computeRealPrice(priceResult, discount);

        // 获取京东平台的商品价格和优惠
        priceResult = HttpRequest.getJDongPrice(productName);
        discount = HttpRequest.getJDongDiscount(productName);
        PriceResult jDongPriceResult = this.computeRealPrice(priceResult, discount);

        // 获取拼多多平台的商品价格和优惠
        priceResult = HttpRequest.getPDDPrice(productName);
        discount = HttpRequest.getPDDDiscount(productName);
        PriceResult pddPriceResult = this.computeRealPrice(priceResult, discount);

        // 计算最优的平台和价格
//        Stream<PriceResult> stream = Stream.of(taoBaoPriceResult, jDongPriceResult, pddPriceResult);
//        Optional<PriceResult> minOpt = stream.min(Comparator.comparing(priceRes -> {
//            return priceRes.getRealPrice();
//        }));
//        PriceResult result = minOpt.get();
//        return result;
        return Stream.of(taoBaoPriceResult, jDongPriceResult, pddPriceResult)
                .min(Comparator.comparing(PriceResult::getRealPrice))
                .get();
    }

    // 计算商品的最终价格 = 平台价格 - 优惠价
    public PriceResult computeRealPrice(PriceResult priceResult, int discount) {
        priceResult.setRealPrice(priceResult.getPrice() - discount);
        priceResult.setDiscount(discount);
        CommonUtils.printTheadLog(priceResult.getPlatform() + "最终价格计算完成" + priceResult.getRealPrice());
        return priceResult;
    }
}
