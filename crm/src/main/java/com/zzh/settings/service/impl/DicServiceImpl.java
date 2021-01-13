package com.zzh.settings.service.impl;

import com.zzh.settings.dao.DicTypeDao;
import com.zzh.settings.dao.DicValueDao;
import com.zzh.settings.domain.DicType;
import com.zzh.settings.domain.DicValue;
import com.zzh.settings.service.DicService;

import com.zzh.utils.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Resource
    private DicTypeDao dicTypeDao;
    @Resource
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();

        List<DicType> dtList = dicTypeDao.getTypeList();
        for(DicType dt:dtList){
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getValueListByCode(code);
            map.put(code+"List",dvList);
        }
        return map;

    }
}
