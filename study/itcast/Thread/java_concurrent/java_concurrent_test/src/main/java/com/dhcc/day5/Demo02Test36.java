package com.dhcc.day5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 李锦卓
 * 2022/4/27 22:41
 * 1.0
 */
@Slf4j(topic = "c.Demo02Test36")
public class Demo02Test36 {
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start .....");
        //获取值 A
        //这个共享变量被其他线程修改过》
        String prev = ref.getReference();
        //获取版本号
        int stamp = ref.getStamp();
        log.debug("{}",stamp);
        other();
        Thread.sleep(1000);
        //尝试改为c
        log.debug("{}",ref.getStamp());
        log.debug("change A->C {}",ref.compareAndSet(prev,"C",stamp,stamp+1));
    }

    private static void other() {
        new Thread(()->{
            log.debug("{}",ref.getStamp());
            log.debug("change A->B {}",ref.compareAndSet(ref.getReference(),"B",ref.getStamp(), (ref.getStamp())+1));
        },"t1").start();

        new Thread(()->{
            log.debug("{}",ref.getStamp());
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), (ref.getStamp()) + 1));
        },"t2").start();
    }
}
