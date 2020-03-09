package com.shiro.util;

import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordGenerateUtil {
    /**
     * username 用户名
     * password 密码
     *  salt  未加工的盐
     *  hashTimes 散列次数
     * */
    public static String getPassword(String username,String password,String salt,int hashTimes){
        //盐是数据库 == username+salt
        Md5Hash md5Hash = new Md5Hash(password,username+salt,hashTimes);
        return md5Hash.toString();
    }
}
