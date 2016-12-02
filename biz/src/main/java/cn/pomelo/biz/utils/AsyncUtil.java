package cn.pomelo.biz.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步线程
 */
public class AsyncUtil {

    private final static Logger          logger          = LoggerFactory.getLogger(AsyncUtil.class);

    private static final int             corePoolSize    = Runtime.getRuntime().availableProcessors() * 50;                       // 4 processes mean 200 threads

    private static final int             maxPoolSize     = corePoolSize * 2;                                                      // 400 threads

    private static final int             maxQueueSize    = maxPoolSize * 10;                                                      // 4000 queue size

    /**
     * 平均每秒任务数量 * 平均执行等待秒数 / 平均可接受任务处理秒数
     * <p>
     * 假设每秒1000个任务
     */
    private static final ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60L,
                                                                                  TimeUnit.SECONDS,
                                                                                  new LinkedBlockingQueue<Runnable>(maxQueueSize),
                                                                                  new CustomPrefixThreadFactory("AsyncUtil"),
                                                                                  new CustomRejectedPolicy());

    public static void execute(Runnable command) {
        logger.debug("execute command :{}", System.currentTimeMillis());
        executorService.execute(command);
    }

    public static <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        logger.debug("invokeAll command:{} ,count:{}", System.currentTimeMillis(), tasks.size());
        try {
            return executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Future<T> submit(Callable<T> task) {
        logger.debug("submit command :{}", System.currentTimeMillis());
        return executorService.submit(task);
    }

    public static void shutdown() {
        try {
            executorService.shutdown();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static int getQueueSize() {
        return ((ThreadPoolExecutor) executorService).getQueue().size();
    }

    private static class CustomRejectedPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.info("executorService reject execute: {}", r.getClass().getName());
            if (!executor.isShutdown()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // do nothing
                }
                executor.execute(r);
            }
        }
    }
}
