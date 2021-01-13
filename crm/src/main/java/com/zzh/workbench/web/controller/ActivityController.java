package com.zzh.workbench.web.controller;

import com.zzh.settings.domain.User;
import com.zzh.settings.service.UserService;
import com.zzh.utils.DateTimeUtil;
import com.zzh.utils.PrintJson;
import com.zzh.utils.UUIDUtil;
import com.zzh.vo.PaginationVO;
import com.zzh.workbench.domain.Activity;
import com.zzh.workbench.domain.ActivityRemark;
import com.zzh.workbench.service.ActivityService;
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
@RequestMapping(value = "/workbench/activity")
public class ActivityController {

    @Resource
    private UserService userService;

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
    public void save(Activity activity, HttpServletRequest request, HttpServletResponse response){

        // 市场活动的添加操作
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        boolean flag = activityService.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/pageList.do")
    public void pageList(HttpServletRequest request, HttpServletResponse response){
        // 查询市场活动信息列表的操作(结合条件查询+分页查询)

        // 接收数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        // 每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        Integer pageSize = Integer.valueOf(pageSizeStr);
        String pageNoStr = request.getParameter("pageNo");
        Integer pageNo = Integer.valueOf(pageNoStr);

        // 计算略过的记录数 LIMIT (pageNo-1)*pageSize,pageSize
        Integer skipCount = (pageNo-1)*pageSize;

        // 将这些数据封装成一个map
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        /*
            交给service处理
            前端要：市场活动信息表
                查询的总条数

            service拿到了以上两条信息之后，如果做返回呢？
            map/vo

            分页查询之后每个模块都有
            vo
         */
        PaginationVO<Activity> vo = activityService.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }


    @RequestMapping(value = "/delete.do")
    public void delete(HttpServletRequest request, HttpServletResponse response){

        // 市场活动的删除操作。

        // 接收参数
        String[] ids =request.getParameterValues("id");

        boolean flag = activityService.delete(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/edit.do")
    public void edit(HttpServletRequest request, HttpServletResponse response){

        // 市场活动的删除操作。

        // 接收参数
        String id =request.getParameter("id");

        // 返回用户列表，还有市场活动信息表。
        Map<String,Object> map = activityService.edit(id);

        PrintJson.printJsonObj(response,map);

    }
    @RequestMapping(value = "/update.do")
    public void update(Activity activity, HttpServletRequest request, HttpServletResponse response){

        // 市场活动的添加操作

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setCreateTime(editTime);
        activity.setCreateBy(editBy);

        boolean flag = activityService.update(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/detail.do")
    public ModelAndView detail(HttpServletRequest request){

        ModelAndView mv = new ModelAndView();
        // 跳转到详细信息页的操作
        String id = request.getParameter("id");
        System.out.println("----------------------"+id);

        Activity activity = activityService.detail(id);
        mv.addObject("activity",activity);
        mv.setViewName("/workbench/activity/detail.jsp");

        return mv;
    }

    @RequestMapping(value = "/getRemarkListByAid.do")
    public void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response){
        // 根据市场活动取得的id，获得市场活动备注信息
        String activityId = request.getParameter("activityId");

        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response,arList);
    }

    @RequestMapping(value = "/deleteRemark.do")
    public void deleteRemark(HttpServletRequest request, HttpServletResponse response){
        String activityRemarkId = request.getParameter("id");
        boolean flag = activityService.deleteRemark(activityRemarkId);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping(value = "/saveRemark.do")
    public void saveRemark(HttpServletRequest request, HttpServletResponse response){
        // 取得活动id和备注信息。
        String activityid=request.getParameter("activityId");
        String noteContent=request.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setActivityId(activityid);

        boolean flag = activityService.saveRemark(activityRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        PrintJson.printJsonObj(response,map);
    }

    @RequestMapping(value = "/updateRemark.do")
    public void updateRemark(ActivityRemark activityRemark,HttpServletRequest request, HttpServletResponse response){
        // 取得活动id和备注信息。
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);

        boolean flag = activityService.updateRemark(activityRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        PrintJson.printJsonObj(response,map);
    }
}
