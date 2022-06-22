package com.laorunzi.msgboard.model;

import lombok.Data;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 11:35
 * 4 留言类, 对应 mb_msg 表
 */
@Data
public class MbMsg {
    private int id; // 自增主键
    private int parentId; // 被评论的留言的 id, 最顶级留言该字段为 -1
    private String msg; // 留言内容
    private String userName; // 留言人
    private String createTime; // 留言时间
}
