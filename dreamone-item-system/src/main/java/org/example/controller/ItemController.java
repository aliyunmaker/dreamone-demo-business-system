package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.ErrorInfo;
import org.example.model.WebResult;
import org.example.service.ItemService;
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
public class ItemController extends BaseController {

    @Autowired
    ItemService itemService;

    @RequestMapping("/getItem")
    public void getItem(HttpServletRequest request, HttpServletResponse response) {
        String item = itemService.getItem(request.getParameter("itemId"));
        WebResult result = new WebResult();
        result.setData(item);
        outputToJSON(response, result);
    }
}
