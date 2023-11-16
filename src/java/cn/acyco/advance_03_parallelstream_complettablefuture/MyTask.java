package cn.acyco.advance_03_parallelstream_complettablefuture;

import cn.acyco.utils.CommonUtils;

public class MyTask {
    private int duration;

    public MyTask(int duration) {
        this.duration = duration;
    }

    // 模拟耗时的长任务
    public int doWork() {
        CommonUtils.printTheadLog("doWork");
        CommonUtils.sleepSecond(duration);
        return duration;
    }
}
