package com.zzh.workbench.dao;

import com.zzh.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<String> getCustomerNameList(String name);
}
