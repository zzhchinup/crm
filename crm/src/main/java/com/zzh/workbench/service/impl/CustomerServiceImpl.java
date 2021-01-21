package com.zzh.workbench.service.impl;

import com.zzh.workbench.dao.CustomerDao;
import com.zzh.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerNameList(String name) {
        List<String> nameList = customerDao.getCustomerNameList(name);
        return nameList;
    }
}
