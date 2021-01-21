package com.zzh.workbench.web.controller;

import com.zzh.settings.domain.User;
import com.zzh.settings.service.UserService;
import com.zzh.utils.PrintJson;
import com.zzh.workbench.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/workbench/transaction")
public class TranController {

    @Resource
    UserService userService;
    @Resource
    CustomerService customerService;

    @RequestMapping(value = "/add.do")
    public ModelAndView add(){
        System.out.println("进入到跳转到交易添加页的操作");
        ModelAndView mv = new ModelAndView();

        List<User> userList = userService.getUserList();
        mv.addObject("uList",userList);
        mv.setViewName("/workbench/transaction/save.jsp");

        return mv;
    }

    @RequestMapping(value = "/getCustomerName.do")
    public void getCustomerName(String name, HttpServletResponse response){
        System.out.println("取得客户名称列表(按照用户名称查询)");
        List<String> nameList = customerService.getCustomerNameList(name);

        PrintJson.printJsonObj(response,nameList);
    }
}
