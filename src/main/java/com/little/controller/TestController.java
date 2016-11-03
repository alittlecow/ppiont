package com.little.controller;

import com.little.model.domain.Test;
import com.little.service.TestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pengx
 * @date 2016/11/3
 */
@Controller
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    private TestService testService;

    private static final Logger log = Logger.getLogger(TestController.class);

    @RequestMapping
    public String index (HttpServletRequest request){
        Test test = testService.getRecord(1);
        request.setAttribute("test",test);
        //测试页面
        return "test";
    }
}
