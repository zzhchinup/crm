package com.zzh.settings.service;

import com.zzh.exception.LoginException;
import com.zzh.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
