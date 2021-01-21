package com.zzh.workbench.service.impl;

import com.zzh.workbench.dao.TranDao;
import com.zzh.workbench.dao.TranHistoryDao;
import com.zzh.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao tranHistoryDao;
}
