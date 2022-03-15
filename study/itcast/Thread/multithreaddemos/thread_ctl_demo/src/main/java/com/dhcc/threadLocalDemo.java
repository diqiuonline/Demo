package com.dhcc;

/**
 * 李锦卓
 * 2022/3/8 20:13
 * 1.0
 */
public class threadLocalDemo {
    //创建一个银行对象 钱 存款 取款
    static class Bank{
        private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
            protected Integer initialValue() {
                return 0;
            }
        };
        public Integer get() {
            return threadLocal.get();
        }

        public void set(Integer money) {
            threadLocal.set(threadLocal.get()+money);

        }
    }

    // 创建转账对象 从银行中取钱 保存到账户
   static class Transfer implements Runnable {
        private Bank bank;
        public Transfer (Bank bank){
            this.bank = bank;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                bank.set(10);
                System.out.println(Thread.currentThread().getName()+"账户余额"+bank.get());
            }
        }
    }
    //main方法中使用两个对象模拟转账
    public static void main(String[] args) {
        Bank bank = new Bank();
        Transfer transfer = new Transfer(bank);
        Thread thread1 = new Thread(transfer,"客户1");
        Thread thread2 = new Thread(transfer,"客户2");

        thread1.start();
        thread2.start();
    }
}
/**
 * 客户1账户余额10
 * 客户2账户余额10
 * 客户1账户余额20
 * 客户2账户余额20
 * 客户1账户余额30
 * 客户2账户余额30
 * 客户1账户余额40
 * 客户2账户余额40
 * 客户1账户余额50
 * 客户2账户余额50
 * 客户1账户余额60
 * 客户2账户余额60
 * 客户1账户余额70
 * 客户2账户余额70
 * 客户2账户余额80
 * 客户2账户余额90
 * 客户2账户余额100
 * 客户1账户余额80
 * 客户1账户余额90
 * 客户1账户余额100
 */