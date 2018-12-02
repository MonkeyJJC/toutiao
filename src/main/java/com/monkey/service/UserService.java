package com.monkey.service;

import com.monkey.dao.LoginTicketDAO;
import com.monkey.dao.UserDAO;
import com.monkey.model.LoginTicket;
import com.monkey.model.User;
import com.monkey.util.TouTiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String, Object> register(String userName, String passWord) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            resultMap.put("msg", "用户名不能为空");
            return resultMap;
        }
        if (StringUtils.isBlank(passWord)) {
            resultMap.put("msg", "密码不能为空");
            return resultMap;
        }
        User user = userDAO.selectByName(userName);
        if (null != user) {
            resultMap.put("msg", "用户已经被注册");
            return resultMap;
        }
        user = new User();
        user.setName(userName);
        /**
         * 保存密码，避免明文保存，进行MD5+salt加密
         */
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        // 用户在系统里的头像
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        // 盐加密
        user.setPassword(TouTiaoUtil.MD5(passWord+user.getSalt()));
        userDAO.addUser(user);

        return resultMap;
    }

    public Map<String, Object> login(String userName, String passWord) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            resultMap.put("msg", "用户名不能为空");
            return resultMap;
        }
        if (StringUtils.isBlank(passWord)) {
            resultMap.put("msg", "密码不能为空");
            return resultMap;
        }
        User user = userDAO.selectByName(userName);
        if (null == user) {
            resultMap.put("msg", "用户不存在");
            return resultMap;
        }

        /**
         * 验证用户密码
         */
        if (!TouTiaoUtil.MD5(passWord+user.getSalt()).equals(user.getPassword())) {
            resultMap.put("msg", "密码不正确");
            return resultMap;
        }
        //ticket
        String ticket = addLoginTicket(user.getId());
        resultMap.put("ticket", ticket);

        return resultMap;
    }

    public String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        /**
         * 设置有效期为24小时,单位毫秒
         */
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void logout(String ticket) {
        /**
         * 设置ticket失效
         */
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
