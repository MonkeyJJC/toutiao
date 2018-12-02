package com.monkey.model;

import java.util.Date;

/**
 * @description: token登记（token）,用户登录成功生成ticket
 * @author: JJC
 * @createTime: 2018/12/2
 */
public class LoginTicket {

    private int id;
    private int userId;
    private Date expired;
    /**
     * 0有效，1无效
     */
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    private String ticket;
}
