package com.laorunzi.msgboard.controller;

import com.laorunzi.msgboard.model.ServiceResult;
import com.laorunzi.msgboard.service.MbMsgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 13:41
 * 4 留言功能相关 controller
 */
@RestController
@RequestMapping("/app/msg")
public class MbMsgController {

    @Resource
    private MbMsgService mbMsgService;

    /**
     * 提交留言
     * @param request
     * @param response
     * @param msg 留言内容
     * @param parentId 上一级留言 id
     * @return
     */
    @RequestMapping(value = "/submitMsg.json", method = RequestMethod.POST)
    public Object submitMsg(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(required = false) String msg,
                            @RequestParam(defaultValue = "-1") int parentId) {
        ServiceResult result = mbMsgService.submitMsg(request, response, parentId, msg);
        return ServiceResult.transfer2Map(result);
    }

    /**
     * 查询留言列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getMsgList.json", method = RequestMethod.POST)
    public Object getMsgList(HttpServletRequest request, HttpServletResponse response) {
        ServiceResult result = mbMsgService.getMsgList(request, response);
        return ServiceResult.transfer2Map(result);
    }
}
