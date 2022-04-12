package com.dhcc.day3;

import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * 李锦卓
 * 2022/4/10 21:37
 * 1.0
 */
@Slf4j(topic = "c.Demo02Test20")
public class Demo02Test20 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }


        Thread.sleep(1000);

        for (Integer integer : Mailboxes.getId()) {
            new Postman(integer, "内容"+ integer).start();
        }

    }
}
//居民类
@Slf4j(topic = "c.People")
class People extends Thread{
    @Override
    public void run() {
        //收信
        GuardedObject guardeObject = Mailboxes.createGuardeObject();
        log.debug("开始收信id {}",guardeObject.getId());
        Object mail = guardeObject.get(5000);
        log.debug("结束收信id {},内容{}",guardeObject.getId(),mail);
    }
}

//邮递员类
@Slf4j(topic = "c.Postman")
class Postman extends Thread{
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guarde = Mailboxes.getGuarde(id);
        log.debug("开始送信 {},内容{}",id,mail);
        guarde.complete(mail);
    }
}

//邮箱
class Mailboxes{
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;
    //产生唯一id
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject getGuarde(int id) {

        return boxes.remove(id);
    }
    public static GuardedObject createGuardeObject(){
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }
    public static Set<Integer> getId() {
        return boxes.keySet();
    }
}

class GuardedObject{
    private int id;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    //结果
    private Object response;
    //获取结果
    public Object get(long timeout) {
        synchronized (this) {
            //开始时间
            long begin = System.currentTimeMillis();
            //经历时间
            long passedTime = 0;
            while (response == null) {
                //这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                //经历的时间超过了最大等待时间，推出循环
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //求得经历时间
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(Object response) {
        synchronized (this) {
            //给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
/**
 * 22:05:22.755 [Thread-1] c.People - 开始收信id 1
 * 22:05:22.755 [Thread-2] c.People - 开始收信id 3
 * 22:05:22.755 [Thread-0] c.People - 开始收信id 2
 * 22:05:23.767 [Thread-3] c.Postman - 开始送信 3,内容内容3
 * 22:05:23.767 [Thread-4] c.Postman - 开始送信 2,内容内容2
 * 22:05:23.767 [Thread-5] c.Postman - 开始送信 1,内容内容1
 * 22:05:23.767 [Thread-0] c.People - 结束收信id 2,内容内容2
 * 22:05:23.767 [Thread-2] c.People - 结束收信id 3,内容内容3
 * 22:05:23.767 [Thread-1] c.People - 结束收信id 1,内容内容1
 */