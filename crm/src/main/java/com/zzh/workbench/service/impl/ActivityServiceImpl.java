package com.zzh.workbench.service.impl;

import com.zzh.settings.dao.UserDao;
import com.zzh.settings.domain.User;
import com.zzh.vo.PaginationVO;
import com.zzh.workbench.dao.ActivityDao;
import com.zzh.workbench.dao.ActivityRemarkDao;
import com.zzh.workbench.domain.Activity;
import com.zzh.workbench.domain.ActivityRemark;
import com.zzh.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private UserDao userDao;

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityRemarkDao activityRemarkDao;


    @Override
    public List<Activity> getActivityListByClueId(String id) {
        List<Activity> aList = activityDao.getActivityListByClueId(id);
        return aList;
    }

    @Transactional
    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int count = activityRemarkDao.updateRemark(activityRemark);
        if(count==1){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if(count==1){
            flag = true;
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean deleteRemark(String activityRemarkId) {
        boolean flag = false;
        int count = activityRemarkDao.deleteRemark(activityRemarkId);
        if(count==1){
            flag=true;
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        // 取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        // 取得total
        int total = activityDao.getCountByCondition(map);
        // 封装到vo
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        // 返回vo
        return vo;
    }

    @Transactional
    @Override
    public boolean delete(String[] ids) {
        boolean flag = false;

        // 先查询出需要删除的备注的数量
        int searchCount = activityRemarkDao.getCountByActivityIds(ids);

        // 删除备注，返回受到影响的条数(实际删除的数量)，进行比对，一样就成功了
        int deleteCount = activityRemarkDao.deleteByActivityIds(ids);

        // 删除市场活动。
        int deleteCount2 = activityDao.deleteByIds(ids);

        if(searchCount==deleteCount && deleteCount2==ids.length){
            flag = true;
        }

        return flag;
    }

    @Transactional
    @Override
    public Map<String, Object> edit(String id) {
        // 取得用户信息列表。
        List<User> userList = userDao.getUserList();
        // 根据id取得市场活动表
        Activity activity = activityDao.getActivityById(id);

        // 封装到map
        Map<String,Object> map = new HashMap<>();
        map.put("userList",userList);
        map.put("activityInfo",activity);
        return map;
    }

    @Transactional
    @Override
    public boolean update(Activity activity) {
        boolean flag = true;

        int count = activityDao.update(activity);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.getActivityAndUserById(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);
        return arList;
    }
}
