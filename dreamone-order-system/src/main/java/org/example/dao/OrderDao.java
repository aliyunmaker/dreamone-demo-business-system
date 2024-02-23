package org.example.dao;

import java.util.List;

import org.example.model.Order;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Component
public interface OrderDao {

    List<Order> listOrders();

    void insertOrders(List<Order> orders);

    void updateOrders();

}
