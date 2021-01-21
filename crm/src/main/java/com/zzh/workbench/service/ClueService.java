package com.zzh.workbench.service;

import com.zzh.workbench.domain.Clue;
import com.zzh.workbench.domain.Tran;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityId);

    boolean convert(String flag, String clueId, Tran tran, String createBy);
}
