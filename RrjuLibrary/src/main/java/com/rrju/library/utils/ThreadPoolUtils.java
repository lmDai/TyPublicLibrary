package com.rrju.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池辅助类，整个应用程序就只有一个线程池去管理线程。 可以设置核心线程数、最大线程数、额外线程空状态生存时间，阻塞队列长度来优化线程池。
 * 下面的数据都是参考Android的AsynTask里的数据。
 */
public class ThreadPoolUtils {

    private ThreadPoolUtils() {
    }

    // 线程池核心线程数
    private static int CORE_POOL_SIZE = 3;
    // 线程池最大线程数
    private static int MAX_POOL_SIZE = 10;
    // 额外线程空状态生存时间
    private static int KEEP_ALIVE_TIME = 2000;
    // 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
    // 线程工厂
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "XYThreadPool thread:"
                    + integer.getAndIncrement());
            return t;
        }
    };

    // 线程池
    private static ThreadPoolExecutor threadPool;

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    /**
     * 线程池runable
     *
     * @author abreal
     */
    public static abstract class ThreadPoolRunnable implements Runnable {
        Handler m_hlrEvent;

        /**
         * 构造函数
         */
        public ThreadPoolRunnable() {
            m_hlrEvent = new Handler(Looper.getMainLooper(),
                    new Handler.Callback() {

                        @Override
                        public boolean handleMessage(Message msg) {
                            if (msg.what == 0) {
                                onStart();
                            } else if (msg.what == 1) {
                                isrunning = false;
                                onFailure();
                            } else if (msg.what == 2) {
                                onprogressUpdate(Integer.parseInt(msg.obj
                                        .toString()));
                            } else if (msg.what == 3) {
                                onEnd();
                            }
                            return false;
                        }
                    });
        }

        /**
         * 响应开始(主线程)
         */
        public abstract void onStart();

        /**
         * 响应线程池线程调用
         */
        public abstract void onBackground();

        /**
         * 响应成功
         */
        public abstract void onSuccess();

        /**
         * 响应结束(主线程)
         */
        public abstract void onEnd();

        /**
         * 响应失败(主线程)
         */
        public abstract void onFailure();

        /**
         * 更新进度UI
         *
         * @param progress
         */
        public abstract void onprogressUpdate(int progress);

        private boolean isrunning = false;

        /**
         * 开始执行
         */
        @Override
        public void run() {
            if (!isrunning) {
                isrunning = true;
                m_hlrEvent.sendEmptyMessage(0);
                onBackground();
            }
        }

        /**
         * 失败情况
         */
        public void failure() {
            m_hlrEvent.sendEmptyMessage(1);
        }

        /**
         * 发布更新UI进度
         */
        public void publishProgress(int values) {
            if (isrunning) {
                Message progress = m_hlrEvent.obtainMessage(2);
                progress.obj = values;
                progress.sendToTarget();
            }
        }

        public void success() {
            isrunning = false;
            onSuccess();
        }

        /**
         * 结束情况
         */
        public void finish() {
            m_hlrEvent.sendEmptyMessage(3);
        }
    }
}
