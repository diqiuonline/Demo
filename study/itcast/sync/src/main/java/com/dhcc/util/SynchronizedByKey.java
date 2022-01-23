package com.dhcc.util;

import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class SynchronizedByKey {
    Map<String, ReentrantLock> mutexCache = new ConcurrentHashMap<>();

    public void exec (String key , Runnable statement ) {

        ReentrantLock mutex4key = mutexCache.computeIfAbsent(key, k -> new ReentrantLock());
        mutex4key.lock();
           try {
               statement.run();
           }finally {
               if (mutex4key.getQueueLength() == 0) {
                   mutexCache.remove(key);
               }
               mutex4key.unlock();
           }
    }

}
