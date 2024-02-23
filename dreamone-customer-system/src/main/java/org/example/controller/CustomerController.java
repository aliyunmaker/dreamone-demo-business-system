package org.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ErrorInfo;
import org.example.model.Customer;
import org.example.model.WebResult;
import org.example.service.CustomerService;
import org.example.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 恬裕
 * @date 2024/1/2
 */
@Controller
@Slf4j
public class CustomerController extends BaseController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("/listCustomers")
    public void listCustomers(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        List<Customer> customers = customerService.listCustomers();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("listCustomers execution time: " + executionTime + "ms");

        WebResult result = new WebResult();
        result.setData(customers);
        outputToJSON(response, result);
    }

    @RequestMapping("/createCustomers")
    public void createCustomers(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        Integer customerCount = customerService.createCustomers(15);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("createCustomers execution time: " + executionTime + "ms");

        WebResult result = new WebResult();
        result.setData("Created customers: " + customerCount);
        outputToJSON(response, result);
    }

    @RequestMapping("/listAllCustomers")
    @Timed(value = "list_all_customers_request_duration", description = "Time taken to list all customers", histogram = true)
    public void listAllCustomers(HttpServletRequest request, HttpServletResponse response) {
        /* 打印模拟错误码日志 */
        int callTime = RequestUtils.getRandomCallTime();
        ErrorInfo errorInfo = RequestUtils.getErrorInfo();
        String responseInfo;
        if (errorInfo.getHttpStatusCode() == 200) {
            customerService.listAllCustomers();
            responseInfo = String.format(
                "{\"Action\":\"%s\", \"Duration\": \"%s\", \"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\"}",
                "listAllCustomers", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage());
        } else {
            responseInfo = String.format(
                "{\"Action\":\"%s\", \"Duration\": \"%s\", \"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\"}",
                "listAllCustomers", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage());
        }
        log.info(responseInfo);
        WebResult result = new WebResult();
        result.setData(responseInfo);
        outputToJSON(response, result);
    }

    @RequestMapping("/getCustomer")
    public void getCustomer(HttpServletRequest request, HttpServletResponse response) {
        if (RequestUtils.getRandomNumber(1, 100) < 3) {
            throw new RuntimeException();
        }

        Long custKey = Long.valueOf(request.getParameter("custKey"));

        /* 打印模拟错误码日志 */
        int callTime = RequestUtils.getRandomCallTime();
        ErrorInfo errorInfo = RequestUtils.getErrorInfo();
        String responseInfo;
        if (errorInfo.getHttpStatusCode() == 200) {
            Customer customer = customerService.getCustomer(custKey);
             responseInfo = String.format(
                "{\"Action\":\"%s\", \"Duration\": \"%s\", \"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\", \"Customer\":\"%s\"}",
                "getCustomer", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage(), customer);
        } else {
            responseInfo = String.format(
                "{\"Action\":\"%s\", \"Duration\": \"%s\", \"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\"}",
                "getCustomer", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage());
        }
        log.info(String.format(
            "%s|%s|%s|%s|%s|%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "getCustomer", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage()));
        WebResult result = new WebResult();
        result.setData(responseInfo);
        outputToJSON(response, result);
    }
}
