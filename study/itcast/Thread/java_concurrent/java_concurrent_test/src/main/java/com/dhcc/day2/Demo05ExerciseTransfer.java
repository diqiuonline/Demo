package com.dhcc.day2;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 李锦卓
 * 2022/4/6 21:49
 * 1.0
 */
@Slf4j(topic = "c.Demo05ExerciseTransfer")
public class Demo05ExerciseTransfer {

    //模拟多人买票
    public static void main(String[] args) {
        for (int j = 0; j < 200; j++) {
            TicketWindow window = new TicketWindow(1000);
            //所有线程的集合
            List<Thread> threadList = new ArrayList<>();

            //卖出的票数统计
            List<Integer> amountlist = new Vector<>();
            for (int i = 0; i < 200; i++) {
                Thread thread = new Thread(() -> {
                    //买票操作
                    int sell = window.sell(randomAmount());
                    amountlist.add(sell);
                });
                threadList.add(thread);
                thread.start();
            }
            for (Thread thread : threadList) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //统计卖出的票数和剩余的票数

            if (window.getCount() + amountlist.stream().mapToInt(i -> i).sum() != 1000) {
                log.debug("剩余余票数: " + window.getCount());
                log.debug("卖出的票数： {}",amountlist.stream().mapToInt( i -> i).sum());
                log.debug("循环次数：{}",j);
                return;
            }
        }

    }


    //random为线程安全
    static Random random = new Random();
    //随机1-5
    public static int randomAmount(){
        return random.nextInt(5) + 1;
    }

}



//售票窗口
class TicketWindow{
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    //获取余票数量
    public int getCount() {
        return count;
    }

    //售票
    public  int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
