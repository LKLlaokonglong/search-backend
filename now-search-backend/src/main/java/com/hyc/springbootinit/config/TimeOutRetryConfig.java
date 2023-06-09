package com.hyc.springbootinit.config;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.WaitStrategy;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hyc
 * 超时重试配置类 提高接口稳定性
 */
@Configuration
public class TimeOutRetryConfig {

    @Bean(value = "retryer")
    public Retryer retryer() {

        // 重试机制(发生下列错误时会触发重试)
        return RetryerBuilder.<Boolean>newBuilder()
                // io异常
                .retryIfExceptionOfType(IOException.class)
                // 运行时异常
                .retryIfRuntimeException()
                // 结果为空
                .retryIfResult(Predicates.isNull())
                // 等待策略
                .withWaitStrategy(WaitStrategies.incrementingWait(500, TimeUnit.MILLISECONDS, 200, TimeUnit.MILLISECONDS))
                // 停止策略 尝试请求4次之后停止
                .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                .build();

    }

}
