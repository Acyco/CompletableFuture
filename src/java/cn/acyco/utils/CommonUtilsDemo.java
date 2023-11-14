package cn.acyco.utils;

public class CommonUtilsDemo {

    public static void main(String[] args) {
        // 测试 CommonUtils 工具类
        String content = cn.acyco.utils.CommonUtils.readFile("news.txt");
        cn.acyco.utils.CommonUtils.printTheadLog(content);
    }

}
