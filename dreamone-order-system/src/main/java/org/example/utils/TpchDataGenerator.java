package org.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.trino.tpch.CustomerGenerator;
import io.trino.tpch.OrderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Slf4j
public class TpchDataGenerator {

    public static List<Order> generateOrders(Integer count) {
        log.info("user.dir: " + System.getProperty("user.dir"));
        List<io.trino.tpch.Order> tpchOrders = new ArrayList<>();
        OrderGenerator orderGenerator = new OrderGenerator((double)count / 30 * 0.00002, 1, 1);
        for (io.trino.tpch.Order value : orderGenerator) {
            tpchOrders.add(value);
            log.info("1 order created.");
        }
        return tpchOrders.stream()
            .map(tpchOrder -> Order.builder()
                .rowNumber(tpchOrder.getRowNumber())
                .custKey(tpchOrder.getCustomerKey())
                .orderStatus(String.valueOf(tpchOrder.getOrderStatus()))
                .totalPrice(tpchOrder.getTotalPrice())
                .orderDate(tpchOrder.getOrderDate())
                .orderPriority(tpchOrder.getOrderPriority())
                .clerk(tpchOrder.getClerk())
                .shipPriority(tpchOrder.getShipPriority())
                .comment(tpchOrder.getComment())
                .build())
            .collect(Collectors.toList());
    }
}
