package cn.comtom.linkage.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Create By wujiang on 2018/11/16
 */
@Configuration
@EnableAsync
public class UploadFileTaskExecutePool {

    private int corePoolSize=10;

    private int maxPoolSize=30;

    private int keepAliveSeconds=300;

    private int queueCapacity=50;

    @Bean
    public Executor myTaskAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //队列容量
        executor.setQueueCapacity(keepAliveSeconds);
        //活跃时间
        executor.setKeepAliveSeconds(queueCapacity);
        //线程名字前缀
        executor.setThreadNamePrefix("UploadFileTaskExecutePool-");

        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }



}
