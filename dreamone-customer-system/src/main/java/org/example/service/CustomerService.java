package org.example.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.CustomerDao;
import org.example.model.Customer;
import org.example.utils.TpchDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/2
 */
@Component
@Slf4j
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    public List<Customer> listCustomers() {
        return customerDao.listCustomers();
    }

    public Integer createCustomers(Integer count) {
        List<Customer> customers = TpchDataGenerator.generateCustomers(count);
        customerDao.insertCustomers(customers);
        return customers.size();
    }

    public void listAllCustomers() {
        customerDao.listAllCustomers();
    }

    public Customer getCustomer(Long custKey) {
        //return customerDao.getCustomer(custKey);
        return new Customer();
    }
}
