package com.monkey.controller;

import com.monkey.service.UserService;
import com.monkey.util.TouTiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description:
 * @author: JJC
 * @createTime: 2018/12/2
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "reg", method = RequestMethod.GET)
    @ResponseBody
    public String reg(Model model, @RequestParam("userName") String userName,
                      @RequestParam("passWord") String passWord,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberMe) {
        try {
            Map<String, Object> map = userService.register(userName, passWord);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                /**
                 * cookie设置有效范围
                 */
                cookie.setPath("/");
                /**
                 * 设置cookie有效时长,不设置的话，默认浏览器关闭，cookie失效
                 */
                if (rememberMe > 0) {
                    /**
                     * 单位为秒，cookie有效时间为5天
                     */
                    cookie.setMaxAge(3600*24*5);
                }
                return TouTiaoUtil.getJSONStrin(0, "注册成功");
            } else {
                return TouTiaoUtil.getJSONStrin(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            return TouTiaoUtil.getJSONStrin(1, "注册异常");
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public String login(Model model, @RequestParam("userName") String userName,
                        @RequestParam("passWord") String passWord,
                        @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(userName, passWord);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                /**
                 * cookie设置有效范围
                 */
                cookie.setPath("/");
                /**
                 * 设置cookie有效时长,不设置的话，默认浏览器关闭，cookie失效
                 */
                if (rememberMe > 0) {
                    /**
                     * 单位为秒，cookie有效时间为5天
                     */
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return TouTiaoUtil.getJSONStrin(0, "注册成功");
            } else {
                return TouTiaoUtil.getJSONStrin(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            return TouTiaoUtil.getJSONStrin(1, "注册异常");
        }
    }


    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
