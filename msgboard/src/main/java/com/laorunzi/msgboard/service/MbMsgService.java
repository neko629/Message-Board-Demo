package com.laorunzi.msgboard.service;

import com.laorunzi.msgboard.model.ServiceResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 11:42
 * 4
 */
public interface MbMsgService {

    /**
     * 提交留言
     * @param request
     * @param response
     * @param parentId 被评论的留言 id
     * @param msg 留言内容
     * @return
     */
    ServiceResult submitMsg(HttpServletRequest request, HttpServletResponse response, int parentId,
                            String msg);

    /**
     * 查询留言
     * @param request
     * @param response
     * @return
     */
    ServiceResult getMsgList(HttpServletRequest request, HttpServletResponse response);
}
