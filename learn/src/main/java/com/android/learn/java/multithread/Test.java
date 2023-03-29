package com.android.learn.java.multithread;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public boolean b = true;
    private static boolean a = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000);
                    System.out.println("hjj");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

//        Test.yieldAndPriority();
//        synchronizedTest();
//        waitAndNotify();
//        printABCForReentrantLock();
//        AtomicInteger r = new AtomicInteger();
//        Thread t = new Thread(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            r.set(10);
//        });
//
//// 让主线程阻塞 等待t线程执行完才继续执行
//// 去除该行，执行结果为0，加上该行 执行结果为10
//
//        t.start();
//        //t.join();
//        System.out.println(r.get());
//        Test test = new Test();
////        new Thread(() -> {
////
////            while (a){
////
////            }
////                //logPrintln("true");
////
////        }).start();
//
//        a=true;
////        Thread.sleep(1000);
//
//        new Thread(()->{
//            while (a){
//
//            }
//        }).start();
        
    }

    /**
     * yield(): 让运行中的线程切换到就绪状态，重新争抢时间片
     * setPriority（）: 线程优先级: 1~10, MIN_PRIORITY = 1,NORM_PRIORITY = 5，MAX_PRIORITY = 10,
     * 默认5, 优先级较高的时候在cpu较忙时获取更多的时间片，较闲时基本没用
     */
    private static void yieldAndPriority() {
        Runnable r1 = () -> {
            int count = 0;
            for (; ; ) {
                System.out.println("---1>" + count++);
            }
        };

        Runnable r2 = () -> {
            int count = 0;
            for (; ; ) {
                Thread.yield();
                //疑问:既然执行了yield(),线程处于就绪状态，为什么下面代码还是有机会执行？
                //看方法介绍，该方法只是提示作用，并不是强制，具体是否切换，看调度器的处理。
                System.out.println("---2>" + count++);
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

        //打印结果
//                ---1>2326749
//                ---1>2326750
//                ---2>599143
//                ---2>599144
//                ---1>2326751
//                ---1>2326752
//                ---1>2326753

    }

    private static int count = 0;
    private static final Object lock = new Object();
    private static ReentrantLock reentrantLock = new ReentrantLock();

    private static void synchronizedTest() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock) {
                    count++;
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (lock) {
                    count--;
                }
            }
        });

        t1.start();
        t2.start();


        try {
            // 让t1 t2都执行完
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(count);

    }

    private static void waitAndNotify() {

        new Thread(() -> {
            synchronized (lock) {
                logPrintln("开始执行");
                try {
                    // 同步代码内部才能调用
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logPrintln("继续执行核心逻辑");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lock) {
                logPrintln("开始执行");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logPrintln("继续执行核心逻辑");
            }
        }, "t2").start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        logPrintln("开始唤醒");

        synchronized (lock) {
            // 同步代码内部才能调用
            lock.notifyAll();
        }
    }

    private static void printABCForWaitAndNotify() {
        new Thread(() -> printABCE("A", 0, 1), "t1").start();
        new Thread(() -> printABCE("B", 1, 2), "t2").start();
        new Thread(() -> printABCE("C", 2, 0), "t3").start();
    }

    private static void printABCE(String str, int c1, int c2) {
        synchronized (lock) {
            for (int i = 0; i < 5; i++) {
                while (count != c1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                logPrintln(str);
                count = c2;
                lock.notifyAll();
                try {
                    if (!(i == 4 && c2 == 0))
                        lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printABCForReentrantLock() {
        Condition a = reentrantLock.newCondition();
        Condition b = reentrantLock.newCondition();
        Condition c = reentrantLock.newCondition();
        new Thread(() -> printABCEF("A", a, b), "t1").start();
        new Thread(() -> printABCEF("B", b, c), "t2").start();
        new Thread(() -> printABCEF("C", c, a), "t3").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reentrantLock.lock();
        try {
            // 先唤醒a
            a.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    private static void printABCEF(String str, Condition cur, Condition next) {
        for (int i = 1; i <= 5; i++) {
            reentrantLock.lock();
            try {
                cur.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logPrintln("第" + i + "组" + str);
            next.signal();

            reentrantLock.unlock();
        }
    }

    private static void logPrintln(String info) {
        System.out.println("线程:" + Thread.currentThread().getName() + ", " + info);
    }
}
