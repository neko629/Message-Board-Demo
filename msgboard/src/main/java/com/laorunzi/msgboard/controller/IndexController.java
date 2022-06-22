package com.laorunzi.msgboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 20:01
 * 4
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "msg_board.html";
    }
}
