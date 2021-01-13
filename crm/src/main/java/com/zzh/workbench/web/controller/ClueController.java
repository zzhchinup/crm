package com.zzh.workbench.web.controller;

import com.zzh.settings.domain.User;
import com.zzh.settings.service.UserService;
import com.zzh.utils.DateTimeUtil;
import com.zzh.utils.PrintJson;
import com.zzh.utils.UUIDUtil;
import com.zzh.vo.PaginationVO;
import com.zzh.workbench.domain.Activity;
import com.zzh.workbench.domain.ActivityRemark;
import com.zzh.workbench.domain.Clue;
import com.zzh.workbench.service.ActivityService;
import com.zzh.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Resource
    private UserService userService;

    @Resource
    private ClueService clueService;

    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/getUserList.do")
    public void getUserList(HttpServletResponse response){
        // 市场活动中获取人名下拉列表。
        // 虽然是市场活动的模块，但是是用户的业务
        List<User> userList = userService.getUserList();

        PrintJson.printJsonObj(response,userList);
    }

    @RequestMapping(value = "/save.do")
    public void save(Clue clue,HttpServletRequest request,HttpServletResponse response){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);

        boolean flag = clueService.save(clue);

        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(HttpServletRequest request){
        String id = request.getParameter("id");
        Clue clue = clueService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "/getActivityListByClueId.do")
    public void getActivityListByClueId(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("clueId");
        List<Activity> aList = activityService.getActivityListByClueId(id);
        PrintJson.printJsonObj(response,aList);

    }

    @RequestMapping(value = "/unbund.do")
    public void unbund(String id,HttpServletResponse response){
        boolean flag = clueService.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/getActivityListByNameAndNotByClueId.do")
    public void getActivityListByNameAndNotByClueId(String activityName,String clueId,HttpServletResponse response){
        Map<String,String> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response,aList);
    }

    @RequestMapping(value = "/bund.do")
    public void bund(String clueId,String[] activityId,HttpServletResponse response){
        boolean flag = clueService.bund(clueId,activityId);

        PrintJson.printJsonFlag(response,flag);
    }
}
