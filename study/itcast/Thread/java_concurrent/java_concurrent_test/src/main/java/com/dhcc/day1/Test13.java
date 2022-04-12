package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/22 22:24
 * 1.0
 */
@Slf4j(topic = "c.Test13")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();

        Thread.sleep(3500);

        twoPhaseTermination.stop();
    }
}
@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination{
    private Thread monitor;
    //启动监控线程
    public void start() {
        monitor = new Thread(()->{
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000); //情况2
                    log.debug("执行监控记录"); //情况1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //重新设置打断标记
                    current.interrupt();
                }
            }
        });
        monitor.start();
    }
    //停止监控线程
    public void stop() {
        monitor.interrupt();
    }
}