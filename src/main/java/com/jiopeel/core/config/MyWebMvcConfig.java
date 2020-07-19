package com.jiopeel.core.config;


import com.jiopeel.core.config.resolver.FormMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

//    @Resource
//    private OAuthInterceptor oAuthInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(oAuthInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/index","/error", "/register", "/login","/signin","/oauth/**")
//                .excludePathPatterns("/assets/**","/bootstrap/**","/img/**","/login/**");
//    }

    /**
     * 参数解析器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FormMethodArgumentResolver());
    }

    /**
     * 资源
     * @Param: registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    }

    /**
     * 跨域支持
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)//允许cookie
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600L)
                .exposedHeaders(
                "access-control-allow-headers",
                "access-control-allow-methods",
                "access-control-allow-origin",
                "access-control-max-age",
                "X-Frame-Options"
                );
    }

//    /**
//     * 视图跳转控制器
//     *
//     * @param registry
//     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/main");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

}
