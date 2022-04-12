package com.dhcc.day3;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 李锦卓
 * 2022/4/7 20:52
 * 1.0
 */
@Slf4j(topic = "c.Demo01TestBiased")
public class Demo01TestBiased {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        System.out.println(ClassLayout.parseInstance(dog).toPrintable());


        synchronized (dog) {
            System.out.println(ClassLayout.parseInstance(dog).toPrintable());
        }


        System.out.println(ClassLayout.parseInstance(dog).toPrintable());
    }
}

class Dog{

}
