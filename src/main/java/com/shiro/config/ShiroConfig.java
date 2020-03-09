package com.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.shiro.config.realmconfig.CustomRealm;
import com.shiro.config.realmconfig.CustomRealm2;
import com.shiro.config.realmconfig.CustomerModul;
import com.shiro.config.rememberconfig.RememberMeConfig;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**常用的过滤器
* anon:无认证
* authc:必须认证 登陆即可
* user: 使用记住我可以直接访问
* perms: 必须有资源权限 比如crud
* roles: 必须有角色权限
* */
@Configuration
public class ShiroConfig {
    /**
     * 创建自定义配置的Realm
     */
    @Bean("R1")
    CustomRealm myRealm1() {
        CustomRealm customRealm = new CustomRealm ( );

        //注入加密算法
        customRealm.setCredentialsMatcher (hashedCredentialsMatcher ());
        return customRealm;
    }
    @Bean("R2")
    CustomRealm2 myRealm2() {
        CustomRealm2 customRealm = new CustomRealm2 ( );

        //注入加密算法
        customRealm.setCredentialsMatcher (hashedCredentialsMatcher ());
        return customRealm;
    }
    //认证策略
    @Bean
    public ModularRealmAuthenticator authenticators(){
        ModularRealmAuthenticator model=new ModularRealmAuthenticator ();
        AllSuccessfulStrategy authenticationStrategy= new AllSuccessfulStrategy();
        //最少一个成功验证  都失败则返回null
        AtLeastOneSuccessfulStrategy atLeastOne=new AtLeastOneSuccessfulStrategy ();
        //最先验证成功的那个返回,剩下的不验证  都失败则返回null
        FirstSuccessfulStrategy first =new FirstSuccessfulStrategy ();
        //添加策略属性为其中一个对象
        List list= new ArrayList<Realm> ();
        list.add (myRealm1() );
        list.add (myRealm2());
        //多策略需要先把realm配置属性加进去
        model.setRealms (list);
        model.setAuthenticationStrategy (first);
        return model;

    }

    /**
     * 创建DefaultWebSecurityManager管理器,使它管理自定义的Realm
     */
    @Bean
    DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager ( );
       List list= new ArrayList<Realm> ();
        list.add (myRealm1 ());
        list.add (myRealm2 ());
        //realm配置通过以下获取
        manager.setRealms (list);
        //自定义的单一Realm交给manager

        // manager.setRealm (myRealm ( ));
        manager.setAuthenticator (authenticators ());

        // 自定义缓存实现 使用redis
         // manager.setCacheManager(cacheManager ());
        // 自定义session管理 使用redis
          //manager.setSessionManager(SessionManager ());
        // 使用记住我,注入配置
       manager.setRememberMeManager(new RememberMeConfig ().rememberMeManager ());
        return manager;
    }

    /**
     *创建shiroFilterFactoryBean
     * 关联一个securityManager ( )管理器
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean ( );
        bean.setSecurityManager (securityManager ( ));

        //登陆页
        bean.setLoginUrl ("/login");
        //登陆成功后界面
        bean.setSuccessUrl ("/index");
        //未授权跳转到
        bean.setUnauthorizedUrl ("/tip");
        Map<String, String> map = new LinkedHashMap<> ( );
        //anon是把限制权限改为无限制
        //map.put ("/index", "anon");
        //authc 登陆后可以访问
       // map.put ("/**", "authc");
        map.put ("/add", "authc");
        //权限必须有addProduct才可以访问
        map.put ("/update","perms[addProduct]");
        //角色是admin 才可以访问超级管理员界面
        map.put ("/admin","roles[admin]");
        bean.setFilterChainDefinitionMap (map);
        return bean;
    }
    /**用于ShiroDialect和thymeleaf标签配合使用*/
    @Bean(name = "shiroDialect")

    public ShiroDialect shiroDialect(){

        return new ShiroDialect ();

    }



    /**
     * 密码加密算法设置
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    };
    /*@Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }*/

}
