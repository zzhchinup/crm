package com.zzh.workbench.dao;

import com.zzh.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation car);
}
