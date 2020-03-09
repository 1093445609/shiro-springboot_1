package com.shiro.service;

import com.shiro.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User getPwdByName(String name);

    List<String> listRoles(String name);


    List<String>  listPermissions(String name);
    Integer  insert(User user);

}
