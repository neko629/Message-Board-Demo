package com.laorunzi.msgboard.model;

import lombok.Data;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 13:02
 * 4 封装返回页面的留言消息
 */
@Data
public class ShowMsg extends MbMsg{
    // 当前留言的下级留言, 按时间倒排
    private Queue<ShowMsg> subMsgList =
            new PriorityQueue<>((msg1, msg2) -> {
                Long t2 =
                        Long.parseLong(msg2.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
                Long t1 =
                        Long.parseLong(msg1.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
                return t2.compareTo(t1);
            });

    public ShowMsg(MbMsg mbMsg) {
        this.setId(mbMsg.getId());
        this.setParentId(mbMsg.getParentId());
        this.setMsg(mbMsg.getMsg());
        this.setUserName(mbMsg.getUserName());
        this.setCreateTime(mbMsg.getCreateTime().substring(0, 19));
    }
}
