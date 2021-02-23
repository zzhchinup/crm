package com.zzh.workbench.service.impl;

import com.zzh.utils.DateTimeUtil;
import com.zzh.utils.UUIDUtil;
import com.zzh.workbench.dao.CustomerDao;
import com.zzh.workbench.dao.TranDao;
import com.zzh.workbench.dao.TranHistoryDao;
import com.zzh.workbench.domain.Customer;
import com.zzh.workbench.domain.Tran;
import com.zzh.workbench.domain.TranHistory;
import com.zzh.workbench.service.TranService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    @Transactional
    @Override
    public boolean save(Tran tran, String customerName) {
        /*
            交易添加业务：
                在做添加之前，参数t少了信息，客户的主键，
                先处理客户相关的需求。
                1、判断customerName，根据客户名称在用户表进行精确查询
                    如果有这个客户，则取出这个客户的id，封装到t对象中。
                    如果没有这个客户，则在新建一条客户信息，然后将新建的客户id取出，封装到t对象中
                2、经过以上的操作，t对象中的信息就完全了，执行添加操作
                3、添加交易完成之后，创建交易历史
         */
        boolean flag = true;
        Customer cus = customerDao.getCustomerByName(customerName);
        if(cus==null){
            // 创建一个客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(tran.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(tran.getContactSummary());
            cus.setNextContactTime(tran.getNextContactTime());
            cus.setOwner(tran.getOwner());
            // 添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }
        }
        // 这时候，cus里面肯定有东西。
        tran.setCustomerId(cus.getId());
        int count2 = tranDao.save(tran);
        if (count2 != 1){
            flag = false;
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setExpectedDate(tran.getExpectedDate());
        th.setMoney(tran.getMoney());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(tran.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3 != 0){
            flag = false;
        }



        return flag;
    }
}
