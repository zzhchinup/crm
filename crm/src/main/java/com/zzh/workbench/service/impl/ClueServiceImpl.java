package com.zzh.workbench.service.impl;

import com.zzh.utils.DateTimeUtil;
import com.zzh.utils.UUIDUtil;
import com.zzh.workbench.dao.*;
import com.zzh.workbench.domain.*;
import com.zzh.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {
    // 线索相关表
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;

    // 客户相关表
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;

    // 联系人相关表
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    // 交易相关表
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Transactional
    @Override
    public boolean convert(String flag, String clueId, Tran tran, String createBy) {
        String createTime = DateTimeUtil.getSysTime();

        boolean flag1 = true;

        //1通过线索id获取线索对象
        Clue clue = clueDao.getById(clueId);
        //2通过线索对象获取客户信息，当客户不存在，新建客户，(根据公司名称精确匹配，判断该客户是否存在)
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        //如果customer 为空，说明以前没有这个客户，新建
        if(null==customer){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());

            // 添加客户
            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag1 = false;
            }
        }
        //3通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        int count2 = contactsDao.save(contacts);
        if(count2 != 1){
            flag1 = false;
        }
        //----------------------------------------------------------
        // 第三步之后，联系人的信息我们已经拥有了。
        // 将线索的备注，转换到客户备注和联系人的备注，

        //4线索备注转换到客户备注以及联系人备注
        //查询出该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for(ClueRemark clueRemark:clueRemarkList){
            // 取出每一条线索的备注
            // 取出备注信息
            String noteContent = clueRemark.getNoteContent();
            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3!=1){
                flag1 = false;
            }

            // 创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4!=1){
                flag1 = false;
            }
        }
        //5“线索和市场活动”的关系转换到联系人和市场活动的关系。
        //查询出于该条线索关联的市场活动。查询与市场活动的关联关系列表。
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        //遍历出每一条与市场活动关联的关联关系记录
        for(ClueActivityRelation car: clueActivityRelationList){
            // 从每一条遍历出来的记录取出关联的市场活动id
            String activityId = car.getActivityId();
            // 创建 联系人与市场活动的关联关系对象。让第三步声场的联系人与市场活动做关联。
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());

            // 添加联系人与市场活动的关联关系。
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){
                flag1 = false;
            }
        }
        //6如果有创建交易的需求，创建一条交易
        if("a".equals(flag)){
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());

            // 添加交易
            int count6 = tranDao.save(tran);
            if(count6!=1){
                flag1=false;
            }
            //7如果创建了交易，则创建一条该交易下的交易历史。
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());

            // 添加交易历史
            int count7 = tranHistoryDao.save(tranHistory);
            if(count7!=1){
                flag1=false;
            }
        }
        //8删除线索备注
        for(ClueRemark clueRemark : clueRemarkList){
            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag1 = false;
            }
        }
        //9删除线索和市场活动的关系
        for(ClueActivityRelation car: clueActivityRelationList){
            int count9 = clueActivityRelationDao.unbund(car.getId());
            if(count9!=1){
                flag1 = false;
            }
        }
        //10删除线索
        int count10 = clueDao.delete(clueId);
        if(count10!=1){
            flag1 = false;
        }


        return flag1;
    }

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
