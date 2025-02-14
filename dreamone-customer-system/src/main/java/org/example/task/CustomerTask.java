package org.example.task;

import lombok.extern.slf4j.Slf4j;
import org.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Slf4j
@Component
public class CustomerTask {

    @Autowired
    CustomerService customerService;

    /**
     * 每十秒钟生成小批量顾客
     */
    @Scheduled(fixedDelay = 10000)
    void generateSmallBatchCustomers() {
        try {
            //customerService.createCustomers(2);
        } catch (Exception e) {
            log.error("Scheduled task: generateSmallBatchCustomers failed", e);
        }
    }

    /**
     * 每十分钟生成大批量顾客
     */
    @Scheduled(fixedDelay = 600000)
    void generateBigBatchCustomers() {
        try {
            //customerService.createCustomers(5);
            log.info("Scheduled task: generateBigBatchCustomers success");
        } catch (Exception e) {
            log.error("Scheduled task: generateBigBatchCustomers failed", e);
        }
    }

}