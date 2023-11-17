package cn.acyco.advance_04_compare_price;

public class PriceResult {
    private int price;
    private int discount;
    private int realPrice;
    private String platform;

    public PriceResult() {
    }

    public PriceResult(String platform) {
        this.platform = platform;
    }

    public PriceResult(int price, int discount, int realPrice, String platform) {
        this.price = price;
        this.discount = discount;
        this.realPrice = realPrice;
        this.platform = platform;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(int realPrice) {
        this.realPrice = realPrice;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "PriceResult{" +
                "平台='" + platform + '\'' +
                ", 平台价=" + price +
                ", 优惠价=" + discount +
                ", 最终价=" + realPrice +
                '}';
    }
}
