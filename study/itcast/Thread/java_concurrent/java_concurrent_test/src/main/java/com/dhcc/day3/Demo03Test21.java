package com.dhcc.day3;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 李锦卓
 * 2022/4/11 11:39
 * 1.0
 */
@Slf4j(topic = "c.Demo03Test21")
public class Demo03Test21 {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);


        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id,"值"+id));
            }, "生产者" + i).start();
        }



        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message take = queue.take();
            }

        }, "消费者").start();
    }
}

//消息队列类 java线程之间的通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue{
    //消息队列的集合
    private LinkedList<Message> list = new LinkedList<>();
    //队列容量
    private int capcity;

    public MessageQueue(int capcity) {
        this.capcity = capcity;
    }

    //获取消息
    public Message take() {
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程只能等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //从队列头部获取消息并返回
            Message message = list.removeFirst();
            log.debug("以消费消息{}",message);
            list.notifyAll();
            return message;
        }
    }

    //存入消息
    public void put(Message message) {
        synchronized (list) {
            //检查队列是否已满
            while (list.size() == capcity) {
                try {
                    log.debug("队列已满，生产者线程只能等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //将消息加入队列尾部
            list.addLast(message);
            log.debug("以生产消息{}",message);
            list.notifyAll();
        }
    }
}










@Slf4j(topic = "c.Message")
final class Message{
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
/**
 * 11:58:56.660 [生产者0] c.MessageQueue - 以生产消息Message{id=0, value=值0}
 * 11:58:56.664 [生产者1] c.MessageQueue - 以生产消息Message{id=1, value=值1}
 * 11:58:56.664 [生产者2] c.MessageQueue - 队列已满，生产者线程只能等待
 * 11:58:57.663 [消费者] c.MessageQueue - 以消费消息Message{id=0, value=值0}
 * 11:58:57.663 [生产者2] c.MessageQueue - 以生产消息Message{id=2, value=值2}
 * 11:58:58.669 [消费者] c.MessageQueue - 以消费消息Message{id=1, value=值1}
 * 11:58:59.675 [消费者] c.MessageQueue - 以消费消息Message{id=2, value=值2}
 * 11:59:00.681 [消费者] c.MessageQueue - 队列为空，消费者线程只能等待
 */