package com.zzh.workbench.web.controller;

import com.zzh.settings.domain.User;
import com.zzh.settings.service.UserService;
import com.zzh.utils.DateTimeUtil;
import com.zzh.utils.PrintJson;
import com.zzh.utils.UUIDUtil;
import com.zzh.workbench.domain.Tran;
import com.zzh.workbench.service.CustomerService;
import com.zzh.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/workbench/transaction")
public class TranController {

    @Resource
    private UserService userService;
    @Resource
    private CustomerService customerService;
    @Resource
    private TranService tranService;

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
    @RequestMapping(value = "/save.do")
    public void save(HttpServletRequest request, Tran tran, String customerName, HttpServletResponse response) throws IOException {
        // 执行添加交易的操作
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        tran.setCreateBy(createBy);

        boolean flag = tranService.save(tran,customerName);

        if(flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");;
        }

    }
}
