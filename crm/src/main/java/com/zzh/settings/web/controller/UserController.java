package com.zzh.settings.web.controller;

import com.zzh.settings.domain.User;
import com.zzh.settings.service.UserService;
import com.zzh.utils.MD5Util;
import com.zzh.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/settings/user")
public class UserController {

    @Resource
    private UserService us;

    @RequestMapping(value = "/login.do")
    public void login(HttpServletRequest request, HttpServletResponse response){
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        // 将密码的明文形式转换为密码的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接收浏览器端的ip地址
        String ip = request.getRemoteAddr();


        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            // 如果程序执行到此处，说明service没有为controller抛出任何的异常
            // 表示登录成功
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            // 一旦程序执行了catch块的信息，说明业务层为我们验证登录失败。
            e.printStackTrace();
            String msg = e.getMessage();
            /*
                我们现在作为controller,需要为ajax请求提供多项信息，

                可以有两种手段来处理：
                    1、将多项信息打包成为map，将map解析为json串
                    2、创建一个对象。vo
                        private boolean success;
                        private String msg;

                    如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
                    如果只在这个需求中能够使用，我们使用map就可以了
             */
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }

}
