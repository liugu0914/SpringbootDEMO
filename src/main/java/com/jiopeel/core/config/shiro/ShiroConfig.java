package com.jiopeel.core.config.shiro;

import com.jiopeel.core.config.filter.OAuthFilter;
import com.jiopeel.core.config.filter.TokenFilter;
import com.jiopeel.core.constant.Constant;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.*;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description :Shiro 配置
 * @auhor:lyc
 * @Date:2020/7/14 16:34
 */
@Configuration
public class ShiroConfig {


    /**
     * @Description : 配置自定义Realm
     * @Return: RealmX
     * @auhor:lyc
     * @Date:2020/7/14 18:11
     */
    @Bean
    public RealmX RealmX() {
        RealmX realmx = new RealmX();
        realmx.setCredentialsMatcher(credentialsMatcher()); //配置使用哈希密码匹配
        return realmx;
    }

    /**
     * @Description : 配置url过滤器
     * @Return: ShiroFilterChainDefinition
     * @auhor:lyc
     * @Date:2020/7/14 18:11
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/main");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        //拦截器.
        Map<String, String> map = new LinkedHashMap<String, String>();
        //自定义拦截器
        Map<String, Filter> customisedFilter = new HashMap<>();
        customisedFilter.put("url", new OAuthFilter());
        customisedFilter.put("token", new TokenFilter());

        //登录模块
        map.put("/", "anon");
        map.put("/error", "anon");
        map.put("/register", "anon");
        map.put("/login", "anon");
        map.put("/signin", "anon");
        map.put("/oauth/**", "anon");
        map.put("/favicon.ico", "anon");
        //静态资源
        map.put("/assets/**", "anon");
        map.put("/bootstrap/**", "anon");
        map.put("/img/**", "anon");
        map.put("/login/**", "anon");
        //不需要权限
        map.put("/logout", "token");
        map.put("/main", "token");
        map.put("/na/**", "token");
//        //需要权限
        map.put("/admin/**", "url");
        shiroFilterFactoryBean.setFilters(customisedFilter);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    /**
     * @Description : 设置密码的加密方式
     * @Return: HashedCredentialsMatcher
     * @auhor:lyc
     * @Date:2020/7/14 18:11
     */
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);  // 散列算法，这里使用更安全的sha256算法
        credentialsMatcher.setStoredCredentialsHexEncoded(false);  // 数据库存储的密码字段使用HEX还是BASE64方式加密
        credentialsMatcher.setHashIterations(Constant.SALT_TIMES);  // 散列迭代次数
        return credentialsMatcher;
    }

    /**
     * @Description : 配置securityManager
     * @Return: SessionsSecurityManager
     * @auhor:lyc
     * @Date:2020/7/14 18:11
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(RealmX());

        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        securityManager.setSubjectDAO(defaultSubjectDAO);
        securityManager.setSubjectFactory(subjectFactory());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * @Description : 解决 Shiro与AOP冲突问题
     * @Return: DefaultAdvisorAutoProxyCreator
     * @auhor:lyc
     * @Date:2020/7/14 18:11
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        //这一句比较重要
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 注入SessionManager,关闭Session验证
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultSessionManager defaultSessionManager = new DefaultSessionManager();
        defaultSessionManager.setSessionValidationSchedulerEnabled(false);
        return defaultSessionManager;
    }

    /**
     * 注入SessionStorageEvaluator,关闭Session存储
     */
    @Bean
    public SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        return defaultSessionStorageEvaluator;
    }

    /**
     * 注入SubjectFactory，不创建session
     */
    @Bean
    public SubjectFactory subjectFactory() {
        return new CusSubjectFactory();
    }


}
