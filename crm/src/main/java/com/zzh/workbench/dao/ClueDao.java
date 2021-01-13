package com.zzh.workbench.dao;

import com.zzh.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);
}
