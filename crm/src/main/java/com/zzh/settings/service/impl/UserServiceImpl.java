package com.zzh.settings.service.impl;

import com.zzh.exception.LoginException;
import com.zzh.settings.dao.UserDao;
import com.zzh.settings.domain.User;
import com.zzh.settings.service.UserService;
import com.zzh.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        map.put("ip",ip);

        User user = userDao.login(map);
        if(user == null){
            throw new LoginException("账号密码错误");
        }
        // 如果程序能够成功执行到该行，说明你账号密码是正确的。
        // 需要继续向下验证其他三项信息。

        // 验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(currentTime.compareTo(expireTime)>0){
            throw new LoginException("账号已失效");
        }
        // 验证锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定，请联系管理员");
        }

        // 验证ip合法
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限，请联系管理员");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> UserList = userDao.getUserList();
        return UserList;
    }
}
