package com.zzh.workbench.dao;

import com.zzh.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getCountByCondition(Map<String, Object> map);

    int deleteByIds(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);

    Activity getActivityAndUserById(String id);

    List<Activity> getActivityListByClueId(String id);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
