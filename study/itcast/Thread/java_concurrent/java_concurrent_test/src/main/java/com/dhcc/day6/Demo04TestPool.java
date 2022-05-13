package com.dhcc.day6;

import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/5/13 16:07
 * 1.0
 */
@Slf4j(topic = "c.Demo04TestPool")
public class Demo04TestPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            // 1 死等
            //queue.put(task);
            //2 带超时的等待
            //queue.offer(task, 1500, TimeUnit.MILLISECONDS);
            //3 让调用者放弃任务执行
            //log.debug("放弃{}",task);
            //4 让调用者抛出异常
            //throw new RuntimeException("任务执行失败" + task);
            //5 让调用者自己执行任务
            task.run();

        });
        for (int i = 0; i < 4; i++) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("{}",j);
            });
        }
    }
}

@FunctionalInterface //拒绝策略
interface RejectPolicy<T>{
    void reject(BlockingQuenue<T> quenue,T task);
}


@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    //任务队列
    private BlockingQuenue<Runnable> taskQueue;
    //线程集合
    private HashSet<Worker> workers = new HashSet();
    //核心线程数
    private int coreSize;
    //超时时间 获取任务的超时时间
    private long timeout;
    private TimeUnit timeUnit;
    //拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    //执行任务
    public void execute(Runnable task) {
        //当任务数没有超过核心 coreSize 时 直接交给worker对象执行
        //如果任务数超过coreSize 加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增worker {},{}",worker,task);
                workers.add(worker);
                worker.start();
            } else {
                //1 队列满了就死等
                //taskQueue.put(task);
                //2 带超时的等待
                //3 让调用者放弃任务执行
                //4 让调用者抛出异常
                //5 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy,task);
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapcity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQuenue<>(queueCapcity);
        this.rejectPolicy = rejectPolicy;
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务
            //1 当task不为空 执行任务

            //2 当task执行完毕，接着从任务队列获取任务并执行
            //while (task != null || (task = taskQueue.tack()) != null) {
            while (task != null || (task = taskQueue.poll(timeout,timeUnit)) != null) {

                try {
                    log.debug("正在执行。。。。{}",task);
                    task.run();
                } catch (Exception e) {

                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker 被移除 {}",this);
                workers.remove(this);
            }
        }
    }

}
@Slf4j(topic = "c.BlockingQuenue")
class BlockingQuenue<T> {
    // 1 任务队列
    private Deque<T> queue = new ArrayDeque<>();
    // 2 锁
    private ReentrantLock lock = new ReentrantLock();
    // 3 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //4 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    // 5 容量
    private int capcity;

    public BlockingQuenue(int capcity) {
        this.capcity = capcity;
    }

    //带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            //将timeout 统一转化为 纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    //返回的是剩余的时间
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            fullWaitSet.signal();
            return queue.removeFirst();
        }finally {
            lock.unlock();
        }
    }

    //阻塞获取
    public T tack() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            fullWaitSet.signal();
            return queue.removeFirst();
        }finally {
            lock.unlock();
        }
    }

    //阻塞添加
    public void put(T task) {
        lock.lock();
        try {
            while (queue.size() == capcity) {
                try {
                    log.debug("等待加入任务队列 {}",task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.debug("加入任务队列 {}",task);
            queue.addLast(task);
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }

    //带超时时间的阻塞添加
    public boolean offer(T task,long timeout,TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capcity) {
                try {
                    if (nanos <= 0) {
                        return false;
                    }
                    log.debug("等待加入任务队列 {}",task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.debug("加入任务队列 {}",task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }
    //获取大小
    public int size() {
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            //判断队列是否已满
            if (queue.size() == capcity) {
                rejectPolicy.reject(this,task);
            } else { //空闲
                log.debug("加入任务队列 {}",task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }
}
