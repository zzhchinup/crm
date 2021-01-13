package com.zzh.workbench.service.impl;

import com.zzh.utils.UUIDUtil;
import com.zzh.workbench.dao.ClueActivityRelationDao;
import com.zzh.workbench.dao.ClueDao;
import com.zzh.workbench.domain.Clue;
import com.zzh.workbench.domain.ClueActivityRelation;
import com.zzh.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;

    @Transactional
    @Override
    public boolean bund(String clueId, String[] activityId) {
        boolean flag =true;
        ClueActivityRelation car = new ClueActivityRelation();
        for(String aid:activityId){
            // 取得每一个aid和clueId关联
            car.setActivityId(aid);
            car.setClueId(clueId);
            car.setId(UUIDUtil.getUUID());

            int count = clueActivityRelationDao.bund(car);
            if(1!=count){
                flag = false;
            }
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean save(Clue clue) {
        boolean flag = false;
        int count = clueDao.save(clue);

        if(1 == count) flag = true;
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Transactional
    @Override
    public boolean unbund(String id) {
        boolean flag = false;

        int count = clueActivityRelationDao.unbund(id);
        if(1==count) flag = true;

        return flag;
    }
}
