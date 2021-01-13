package com.zzh.settings.service;

import com.zzh.settings.dao.DicTypeDao;
import com.zzh.settings.dao.DicValueDao;
import com.zzh.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();

}
