package com.zzh.workbench.service;

import com.zzh.workbench.domain.Tran;

public interface TranService {
    boolean save(Tran tran, String customerName);
}
