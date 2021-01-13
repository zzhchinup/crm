package com.zzh.workbench.service;

import com.zzh.vo.PaginationVO;
import com.zzh.workbench.domain.Activity;
import com.zzh.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> edit(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String activityRemarkId);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String id);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);
}
