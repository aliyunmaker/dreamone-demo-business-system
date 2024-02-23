package org.example.service;

import java.util.List;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.example.dao.OrderDao;
import org.example.model.Order;
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

    public OrderService(MeterRegistry registry) {
        createCounter = Counter.builder("create_orders_counter")
            .description("Number of times createOrders called")
            .register(registry);
    }

    public List<Order> createOrders(Integer count) {
        List<Order> orders = TpchDataGenerator.generateOrders(count);
        orderDao.insertOrders(orders);
        createCounter.increment();
        return orders;
    }

    public Order createOrderByCustKey(Long custKey) {
        List<Order> orders = TpchDataGenerator.generateOrders(1);
        Assert.notEmpty(orders, "TpchDataGenerator.generateOrders should not return empty list.");
        Order order = orders.get(0);
        order.setCustKey(custKey);
        orderDao.insertOrders(orders);
        return order;
    }
    
}
