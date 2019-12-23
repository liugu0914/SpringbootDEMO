package com.jiopeel.core.config;

import com.jiopeel.core.config.interceptor.PageIntercept;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfigBean {
    /**
     * @description：自定义mybatis插件
     * @author     ：lyc
     * @date       ：2019/12/23 16:37
     * @version    :
     */
    @Bean
    public PageIntercept pageIntercept() {
        return new PageIntercept();
    }

    /**
     * @description：mybatis自定义配置
     * @author     ：lyc
     * @date       ：2019/12/23 16:37
     * @version    :
     */
    @Bean
    public ConfigurationCustomizer customizer() {
        return new ConfigurationCustomizer(){
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.addInterceptor(pageIntercept());
            }
        };
    }
}
