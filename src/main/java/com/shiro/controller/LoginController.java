package com.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    /**表单提交,处理后返回首页**/
    @PostMapping("/tologin")
    String tologin(String name, String password, Model model) {
        //处理逻辑
        //使用Shiro编写认证操作
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject ( );
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken (name, password);
        //执行登陆方法

        try {
            subject.login (token);
            //登陆信息会交给Realm去执行认证逻辑,比如去数据库对比用户,密码
            //然后会设置角色,权限

        } catch (UnknownAccountException e) {
            model.addAttribute ("msg", "用户名不存在");
            return "/login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute ("msg", "密码错误");
            return "/login";
        }catch (Exception e){
            model.addAttribute ("msg", "用户名不存在");
            return "/login";
        }
        //这里的请求不能返回"/index"这是返回index.html了 没有经过mvc拦截到
        // 必须使用"redirect:/index" 或者"forward:/index"
        //catch块可以跳转是因为我的页面就叫login.html 请求也是/login
        return "redirect:/index";
    }
    @RequestMapping("/tip")
    @ResponseBody
    String  tip(){

        return "抱歉,您没有权限访问该页面!";
    }
    @RequestMapping("/loginout")
    String  loginout(){
        Subject subject=SecurityUtils.getSubject ();
        subject.logout ();
        return "redirect:/index";
    }
}
