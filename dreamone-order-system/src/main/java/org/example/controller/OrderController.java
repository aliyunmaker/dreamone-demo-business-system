package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ErrorInfo;
import org.example.model.WebResult;
import org.example.service.OrderService;
import org.example.task.OrderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 恬裕
 * @date 2024/8/19
 */
@Slf4j
@Controller
public class OrderController extends BaseController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTask orderTask;

    @RequestMapping("/createOrders")
    public void createOrders(HttpServletRequest request, HttpServletResponse response) {
        ErrorInfo errorInfo = orderTask.createOrders(request.getParameter("count"), request.getParameter("type"), request.getParameter("region"));
        WebResult result = new WebResult();
        result.setData(errorInfo.toString());
        outputToJSON(response, result);
    }
}
