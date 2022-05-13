package com.dhcc.day5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 李锦卓
 * 2022/4/28 20:57
 * 1.0
 */
@Slf4j(topic = "c.Demo05Test40")
public class Demo05Test40 {
    public static void main(String[] args) {
        student stu = new student();
        AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(student.class, String.class, "name");
        System.out.println(updater.compareAndSet(stu, null, "张三"));
    }
}

class student{
    volatile String  name;

    /*public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    @Override
    public String toString() {
        return "student{" +
                "name='" + name + '\'' +
                '}';
    }
}