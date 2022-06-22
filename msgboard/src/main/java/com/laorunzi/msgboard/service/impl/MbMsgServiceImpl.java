package com.laorunzi.msgboard.service.impl;

import com.laorunzi.msgboard.enums.ErrorEnum;
import com.laorunzi.msgboard.mapper.MbMsgMapper;
import com.laorunzi.msgboard.model.MbMsg;
import com.laorunzi.msgboard.model.MbUser;
import com.laorunzi.msgboard.model.ServiceResult;
import com.laorunzi.msgboard.model.ShowMsg;
import com.laorunzi.msgboard.service.MbMsgService;
import com.laorunzi.msgboard.utils.StringUtils;
import com.laorunzi.msgboard.utils.UserUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 11:47
 * 4 留言相关操作
 */
@Service
public class MbMsgServiceImpl implements MbMsgService {

    @Resource
    private MbMsgMapper mbMsgMapper;

    @Override
    public ServiceResult submitMsg(HttpServletRequest request, HttpServletResponse response, int parentId, String msg) {
        // 验证是否登录
        MbUser mbUser = UserUtils.checkLogin(request);
        if (mbUser == null) {
            return new ServiceResult(false, ErrorEnum.NEED_LOGIN.getCode(),
                    ErrorEnum.NEED_LOGIN.getMsg());
        }
        // 验证留言内容, 不可为空, 长度在 3-200 之间
        if (StringUtils.isBlank(msg) || msg.length() < 3 || msg.length() > 200) {
            return new ServiceResult(false, ErrorEnum.MSG_ERROR.getCode(),
                    ErrorEnum.MSG_ERROR.getMsg());
        }
        // 如果不是最上级留言, 判断回复的对象是否存在
        if (!isFirstLayer(parentId)) {
            MbMsg parentMsg = mbMsgMapper.selectById(parentId);
            if (parentMsg == null) {
                return new ServiceResult(false, ErrorEnum.PARENT_NOT_EXIST.getCode(),
                        ErrorEnum.PARENT_NOT_EXIST.getMsg());
            }
        }
        // 留言入库
        Date now = new Date();
        MbMsg mbMsg = new MbMsg();
        mbMsg.setParentId(parentId);
        mbMsg.setMsg(msg);
        mbMsg.setUserName(mbUser.getUserName());
        mbMsg.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(now));
        int result = mbMsgMapper.insertMsg(mbMsg);
        if (result != 1) { // 入库失败
            return new ServiceResult(false, ErrorEnum.MSG_ERROR.getCode(), "留言失败, 请稍后重试");
        }
        return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "");
    }

    @Override
    public ServiceResult getMsgList(HttpServletRequest request, HttpServletResponse response) {
        // 批量查出所有留言, 按时间正序, 保证后面组装的留言一定能招到它的上一级留言
        List<MbMsg> msgList = mbMsgMapper.selectAllMsgOrderByTime();
        // 组装树形结构, 优先级队列, 按时间倒排
        Queue<ShowMsg> msgQueue =
                new PriorityQueue<>((msg1, msg2) -> {
                    Long t2 =
                            Long.parseLong(msg2.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
                    Long t1 =
                            Long.parseLong(msg1.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
                    return t2.compareTo(t1);
                });
        if (!CollectionUtils.isEmpty(msgList)) {
            for (MbMsg mbMsg : msgList) {
                ShowMsg showMsg = new ShowMsg(mbMsg);
                // 如果是最上级留言, 直接放入队列
                if (isFirstLayer(mbMsg.getParentId())) {
                    msgQueue.add(showMsg);
                } else {
                    // 如果不是最上级留言, 在队列里找到它的上一级留言
                    ShowMsg parentMsg = findFromQueue(msgQueue, mbMsg.getParentId());
                    if (parentMsg == null) {
                        continue;
                    }
                    parentMsg.getSubMsgList().add(showMsg);
                }

            }
        }
        return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "", msgQueue);
    }

    /**
     * 是否是最上级留言
     * @param parentId
     * @return
     */
    private boolean isFirstLayer(int parentId) {
        return parentId == -1;
    }

    /**
     * 在队列里找到上一级消息
     * @param queue
     * @param parentId
     * @return
     */
    private ShowMsg findFromQueue(Queue<ShowMsg> queue, int parentId) {
        for (ShowMsg showMsg : queue) {
            if (showMsg.getId() == parentId) {
                return showMsg;
            } else {
                return findFromQueue(showMsg.getSubMsgList(), parentId);
            }
        }
        return null;
    }
}
