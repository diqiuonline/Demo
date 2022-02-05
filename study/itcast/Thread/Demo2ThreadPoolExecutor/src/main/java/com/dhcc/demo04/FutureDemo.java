package com.dhcc.demo04;

import java.util.concurrent.*;

/**
 * 李锦卓
 * 2022/2/5 14:43
 * 1.0
 * 练习异步计算结果
 */
public class FutureDemo {
    public static void main(String[] args) throws Exception {
        // 获取线程池对象
        ExecutorService es = Executors.newCachedThreadPool();
        // 创建callable类型的任务对象
        Future<Integer> f = es.submit(new myCall(1, 1));
        //test1(f);
        //boolean cancel = f.cancel(true);
        //System.out.println("取消任务执行的结果" + cancel);
        Integer integer = f.get(1, TimeUnit.SECONDS);//由于等待时间果断 来不及完成任务 会报异常
        System.out.println("任务执行的结果" + integer);
    }
    //正常测试流程
    private static void test1(Future<Integer> f) throws InterruptedException, ExecutionException {
        // 判断任务是否已经完成
        boolean done = f.isDone();

        boolean cancelled = f.isCancelled();
        System.out.println("第一次判断任务是否已经取消" + cancelled);
        Integer integer = f.get(); //一直等待任务的执行，知道完成为止
        System.out.println("任务执行的结果是" + integer);

        boolean done2 = f.isDone();
        System.out.println("第二次判断任务是否已经完成"+done2);

        boolean cancelled2 = f.isCancelled();
        System.out.println("第二次判断任务是否已经取消" + cancelled2);
    }
}


class myCall implements Callable<Integer> {
    //通过构造方法传递两个参数
    private int a;
    private int b;

    public myCall(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Integer call() throws Exception {
        String name = Thread.currentThread().getName();
        System.out.println(name + "准备开始计算");
        Thread.sleep(2000);
        System.out.println(name + "计算完成");
        return a + b;
    }
}