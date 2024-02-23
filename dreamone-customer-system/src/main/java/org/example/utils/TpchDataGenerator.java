package org.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.trino.tpch.CustomerGenerator;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Customer;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Slf4j
public class TpchDataGenerator {

    public static List<Customer> generateCustomers(Integer count) {
        List<io.trino.tpch.Customer> tpchCustomers = new ArrayList<>();
        CustomerGenerator customerGenerator = new CustomerGenerator((double)count / 30 * 0.0002, 1, 1);
        for (io.trino.tpch.Customer value : customerGenerator) {
            tpchCustomers.add(value);
            log.info("1 customer created.");
        }
        return tpchCustomers.stream()
            .map(tpchCustomer -> Customer.builder()
                .name(tpchCustomer.getName())
                .phone(tpchCustomer.getPhone())
                .nationKey(tpchCustomer.getNationKey())
                .acctBal(tpchCustomer.getAccountBalance())
                .address(tpchCustomer.getAddress())
                .mktSegment(tpchCustomer.getMarketSegment())
                .comment(tpchCustomer.getComment())
                .build())
            .collect(Collectors.toList());
    }

}
