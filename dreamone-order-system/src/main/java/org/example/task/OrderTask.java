package org.example.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ErrorInfo;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.service.OrderService;
import org.example.utils.HttpClientUtils;
import org.example.utils.RequestUtils;
import org.example.utils.TpchDataGenerator;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/8
 */
@Slf4j
@Component
public class OrderTask {

    @Value("${dreamone.customer.server}")
    String dreamoneCustomerSystemServer;

    @Value("${dreamone.customer.ingress}")
    String dreamoneCustomerSystemIngress;

    @Autowired
    OrderService orderService;

    public final Queue<Long> taskQueue;

    public final Gauge taskQueueGauge;

    public final Counter requestCounter;

    public final Counter dispatchCounter;

    public final DistributionSummary requestTimeSummary;

    public final Timer requestTimeHistogram;

    public OrderTask(MeterRegistry registry) {
        taskQueue = new ArrayDeque<>();
        taskQueueGauge = Gauge.builder("task_queue_gauge", taskQueue, Collection::size)
            .description("Size of the taskQueue")
            .register(registry);

        requestCounter = Counter.builder("request_counter")
            .description("Number of getCustomer requests")
            .register(registry);
        dispatchCounter = Counter.builder("dispatch_counter")
            .description("Number of dispatches")
            .register(registry);

        requestTimeHistogram = Timer.builder("request_time_histogram")
            .description("Statistics of request time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .publishPercentileHistogram()
            .minimumExpectedValue(Duration.ofMillis(50))
            .maximumExpectedValue(Duration.ofMillis(3500))
            .register(registry);

        requestTimeSummary = DistributionSummary.builder("request_time_summary")
            .description("Percentiles of request time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
    }

    public ErrorInfo createOrder(Long custKey, String type) {
        ErrorInfo errorInfo = RequestUtils.getErrorInfo();
        Double price = null;
        if (errorInfo.getHttpStatusCode() == 200) {
            Order order = orderService.createOrderByCustKey(custKey);
            taskQueue.offer(order.getOrderKey());
            price = order.getTotalPrice();
        }
        JSONObject data = new JSONObject();
        data.put("Action", "createOrder");
        data.put("Duration", RequestUtils.getRandomCallTime());
        data.put("HttpStatusCode", errorInfo.getHttpStatusCode());
        data.put("Code", errorInfo.getCode());
        data.put("Message", errorInfo.getMessage());
        data.put("Price", price);
        data.put("Type", type);
        log.info(data.toString());
        log.info(String.format(
            "%s|%s|%s|%s|%s|%s|%s|%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "createOrder", RequestUtils.getRandomCallTime(), errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage(), price, type));
        return errorInfo;
    }

    /**
     * 生成订单，发起getCustomer请求
     */
    @Scheduled(fixedDelay = 5000)
    void scheduleCreateOrder() {
        // 随机执行
        if (getRandomBoolean()) {
            return;
        }
        String action = "getCustomer";
        Long custKey = Integer.toUnsignedLong(getRandomNumber(1, 100));
        Map<String, String> params = new HashMap<>();
        params.put("custKey", custKey.toString());
        JSONObject result = JSON.parseObject(HttpClientUtils.postUrlAndFormBody(dreamoneCustomerSystemServer + "/" + action, params));

        log.info("CreateOrderTask result from customer system: " + result);

        JSONObject data = JSON.parseObject(result.getString("data"));

        /* 打印响应日志 */
        int callTime = Integer.parseInt(data.getString("Duration"));

        log.info(data.toString());
        log.info("{\\\"eventName\\\":\\\"WAIT_PROCESS\\\",\\\"timestamp\\\":1707121183123}");
        log.info(String.format(
            "%s|%s|%s|%s|%s|%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            action, callTime, Integer.parseInt(data.getString("HttpStatusCode")), data.getString("Code"), data.getString("Message")));

        requestCounter.increment();
        requestTimeSummary.record(callTime);
        requestTimeHistogram.record(Duration.ofMillis(callTime));

        Order order = orderService.createOrderByCustKey(custKey);

        taskQueue.offer(order.getOrderKey());

    }

    /**
     * 发起listAllCustomers请求
     */
    @Scheduled(fixedDelay = 5000)
    void scheduleListAllCustomers() {
        // 随机执行
        if (getRandomBoolean()) {
            return;
        }
        String action = "listAllCustomers";
        Map<String, String> params = new HashMap<>();
        JSONObject result = JSON.parseObject(HttpClientUtils.postUrlAndFormBody(dreamoneCustomerSystemServer + "/" + action, params));

        JSONObject data = JSON.parseObject(result.getString("data"));
        log.info(data.toString());

    }


    /**
     * 处理订单
     */
    @Scheduled(fixedDelay = 5000)
    void scheduleDispatchOrder() {
        // 随机执行
        if (getRandomBoolean() || taskQueue.isEmpty()) {
            return;
        }
        Long orderKey = taskQueue.poll();
        log.info(String.format("Task: DispatchOrder completed. OrderKey: %s", orderKey));
        dispatchCounter.increment();
    }

    /**
     * 每天0点将任务队列清零
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearTaskQueue() {
        taskQueue.clear();
    }

    /**
     * 每十秒钟生成少量订单
     */
    @Scheduled(fixedDelay = 10000)
    void generateSmallBatchOrders() {
        try {
            //orderService.createOrders(5);
        } catch (Exception e) {
            log.error("Scheduled task: generateSmallBatchOrders failed", e);
        }
    }

    /**
     * 10-16点生成中量订单
     */
    @Scheduled(cron = "0 * 10-16 * * ?")
    void generateMediumBatchOrders() {
        try {
            //orderService.createOrders(50);
        } catch (Exception e) {
            log.error("Scheduled task: generateMediumBatchOrders failed", e);
        }
    }

    /**
     * 16-19点，22-23点生成稍少量订单
     */
    @Scheduled(cron = "0 * 16-19,22-23 * * ?")
    void generateSlightlyLessBatchOrders() {
        try {
            //orderService.createOrders(30);
        } catch (Exception e) {
            log.error("Scheduled task: generateSlightlyLessBatchOrders failed", e);
        }
    }

    /**
     * 19-22点生成大量订单
     */
    @Scheduled(cron = "0 * 19-22 * * ?")
    @Timed(value = "generate_big_batch_orders_duration", description = "Time taken to generate big batch orders", histogram = true)
    void generateBigBatchOrders() {
        try {
            //orderService.createOrders(100);
        } catch (Exception e) {
            log.error("Scheduled task: generateBigBatchOrders failed", e);
        }
    }

    private static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private static int getRandomCallTime() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        if (randomNumber < 95) {
            return getRandomNumber(50, 200);
        } else {
            return getRandomNumber(200, 3500);
        }
    }

    private static ErrorInfo getErrorInfo() {
        ErrorInfo errorInfo;
        int randomNumber = getRandomNumber(1, 100);
        if (randomNumber <= 80) {
            errorInfo = ErrorInfo.OK;
        } else if (randomNumber <= 90) {
            errorInfo = ErrorInfo.INVALID_PARAMETER;
        } else if (randomNumber <= 95) {
            errorInfo = ErrorInfo.SYSTEM_ERROR;
        } else {
            errorInfo = ErrorInfo.NO_PERMISSION;
        }
        return errorInfo;
    }
}
