package com.shiro.serviceImpl;

import com.shiro.dao.RoleDao;
import com.shiro.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Override

    public Integer insert(Integer[] roleIds, Integer uid) {
        return roleDao.insert (roleIds, uid);
    }
}
