package com.dhcc.util;

import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class SynchronizedByKey2 {
    Map<String, ReentrantLock> mutexCache = new ConcurrentHashMap<>();

    public void exec (String key , Runnable statement ) {
        ReentrantLock mutex4key = null;
        ReentrantLock mutexInCache;
        do {
            if (mutex4key != null) {
                mutex4key.unlock();
            }
            mutex4key = mutexCache.computeIfAbsent(key, k -> new ReentrantLock());
             mutex4key.lock();
            mutexInCache = mutexCache.get(key);
        /**
         * 1 mutexInCache == null
         * 2 mutex4key != mutexInCache
         */
        }while (mutexInCache == null || mutex4key != mutexInCache) ;

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
