package com.ps.queue;

import java.util.concurrent.ArrayBlockingQueue;

public class LogQueue {
    private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(500);
    //写队列
    public static void Push(String log) throws InterruptedException {
        queue.put(log);
    }

    //读取队列
    public static String Poll() throws InterruptedException {
        return queue.take();
    }
}
