package org.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.core.annotation.Timed;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ErrorInfo;
import org.example.model.Customer;
import org.example.model.WebResult;
import org.example.service.CustomerService;
import org.example.task.ExceptionTask;
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

    @Autowired
    ExceptionTask exceptionTask;

    private Boolean simulateError = false;

    @RequestMapping("/getSimulateErrorStatus")
    public void getSimulateErrorStatus(HttpServletRequest request, HttpServletResponse response) {
        WebResult result = new WebResult();
        result.setData(simulateError);
        log.info("getSimulateErrorStatus: " + simulateError);
        outputToJSON(response, result);
    }

    @RequestMapping("/simulateError")
    public void simulateError(HttpServletRequest request, HttpServletResponse response) {
        simulateError = !simulateError;
        WebResult result = new WebResult();
        result.setData("Simulated error: " + simulateError);
        log.info("simulateError: " + simulateError);
        outputToJSON(response, result);
    }

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
        ErrorInfo errorInfo = RequestUtils.getErrorInfo("OK");
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
        Span span = Span.current();
        int callTime = RequestUtils.getRandomCallTime();
        ErrorInfo errorInfo;
        String responseInfo;
        WebResult result = new WebResult();
        try {
            if (simulateError) {
                // 模拟故障
                exceptionTask.throwSimulatedException();
            }
            Long custKey = Long.valueOf(request.getParameter("custKey"));
            Customer customer = customerService.getCustomer(custKey);
            errorInfo = RequestUtils.getErrorInfo("OK");
            responseInfo = String.format(
                "{\"Action\":\"%s\", \"Duration\": \"%s\", \"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\", \"Customer\":\"%s\"}",
                "getCustomer", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage(), customer);
            log.info("responseInfo: {}", responseInfo);
            result.setSuccess(true);
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            log.info("spanId: {}, traceId: {}", span.getSpanContext().getSpanId(), span.getSpanContext().getTraceId());
            errorInfo = RequestUtils.getErrorInfo("Error");
            responseInfo = String.format(
                "{\"Action\":\"%s\", \"Duration\": \"%s\", \"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\"}",
                "getCustomer", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage());
            log.error("getCustomer error, responseInfo: {}", responseInfo, e);
            result.setSuccess(false);
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
        }
        log.info(String.format(
            "%s|%s|%s|%s|%s|%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "getCustomer", callTime, errorInfo.getHttpStatusCode(), errorInfo.getCode(), errorInfo.getMessage()));
        result.setData(responseInfo);
        outputToJSON(response, result);
    }
}
