package com.zzh.settings.dao;

import com.zzh.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueListByCode(String code);
}
