package org.example.service;

import java.util.List;
import java.util.Random;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.OrderDao;
import org.example.model.Order;
import org.example.utils.RequestUtils;
import org.example.utils.TpchDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Component
@Slf4j
public class OrderService {

    Counter createCounter;

    @Autowired
    OrderDao orderDao;

    Random random;

    public OrderService(MeterRegistry registry) {
        createCounter = Counter.builder("create_orders_counter")
            .description("Number of times createOrders called")
            .register(registry);
    }

    public List<Order> createOrders(Long count) {
        List<Order> orders = TpchDataGenerator.generateOrders(count.intValue());
        Assert.notEmpty(orders, "TpchDataGenerator.generateOrders should not return empty list.");
        orders.stream().forEach(order -> {
            order.setCustKey(12345L);
            order.setTotalPrice(RequestUtils.getRandomNumber(100, 1000) * 1.0);
        });
//        order.setComment(RequestUtils.getRandomOrderType());
        orderDao.insertOrders(orders);

        return orders;
    }
    
}
