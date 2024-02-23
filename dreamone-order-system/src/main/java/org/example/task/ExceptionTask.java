package org.example.task;

import java.util.Random;

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

    @Scheduled(fixedDelay = 600000)
    void scheduleThrowException() {
        if (new Random().nextBoolean()) {
            return;
        }
        try {
            throw new RuntimeException();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
