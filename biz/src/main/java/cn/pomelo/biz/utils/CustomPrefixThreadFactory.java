package cn.pomelo.biz.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义前缀线程工厂，方便日志查询
 */
public class CustomPrefixThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber   = new AtomicInteger(1);
    private final ThreadGroup          group;
    private final AtomicInteger        threadNumber = new AtomicInteger(1);
    private final String               namePrefix;

    public CustomPrefixThreadFactory(String customPrefix){
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = (null == customPrefix ? "" : customPrefix) + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
