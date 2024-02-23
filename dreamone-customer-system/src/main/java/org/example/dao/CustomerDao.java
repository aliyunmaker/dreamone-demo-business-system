package org.example.dao;

import java.util.List;

import org.example.model.Customer;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/2
 */
@Component
public interface CustomerDao {

    List<Customer> listCustomers();

    List<Customer> listAllCustomers();
    void insertCustomers(List<Customer> customers);

    Customer getCustomer(Long custKey);
}
