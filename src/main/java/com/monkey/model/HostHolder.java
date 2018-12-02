package com.monkey.model;

import org.springframework.stereotype.Component;

/**
 * @description: 用于保存当前用户信息
 * @author: JJC
 * @createTime: 2018/12/2
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}