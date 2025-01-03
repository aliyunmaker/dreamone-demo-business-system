package org.example.task;

import java.util.Random;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/19
 */
@Slf4j
@Component
public class ExceptionTask {

    @WithSpan
    @Scheduled(fixedDelay = 600000)
    public void throwSimulatedException() {
        try {
            throw new RuntimeException("Simulated Error");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Span.current().setStatus(StatusCode.ERROR, e.getMessage());
            Span.current().recordException(e);
            throw e;
        }
    }
}