package com.jiopeel.core.config.freemark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * spring:
 * freemarker:
 * request-context-attribute: req
 * content-type: text/html
 * enabled: true
 * cache: false #缓存配置
 * template-loader-path: classpath:/templates/ #模板加载路径 按需配置
 * charset: UTF-8 #编码格式
 * suffix: .html
 * FreeMarker 配置
 *
 * @author lyc
 * @Date 2019年12月30日16:07:30
 */
@Configuration
public class FreeMarkerConfig {
    @Autowired
    protected freemarker.template.Configuration configuration;
    @Autowired
    protected FreeMarkerViewResolver resolver;

    private static final String TEMPLATE = "/templates/";

    @PostConstruct
    public void setVariableConfiguration() {
        configuration.setClassForTemplateLoading(FreeMarkerConfig.class, TEMPLATE);
        configuration.setTagSyntax(freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX);//自定义格式，系统会选择第一个标签作为参照；
        configuration.setTemplateUpdateDelayMilliseconds(1000);//设置模板文件更新时间（毫秒）；
        configuration.setDefaultEncoding("UTF-8");
        configuration.setURLEscapingCharset("UTF-8");
        configuration.setLocale(Locale.SIMPLIFIED_CHINESE);//国际化
        configuration.setBooleanFormat("true,false");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setNumberFormat("0.##");
        configuration.setWhitespaceStripping(true);//开启/关闭空白移除，默认为true；
        Map<String,String> map=new HashMap<String,String>();
        map.put("c","/core/common/common.html");
        configuration.setAutoImports(map);//macro设置
        resolver.setSuffix(".html");
        resolver.setContentType("text/html;charset=UTF-8");
        resolver.setCache(false);
        resolver.setRequestContextAttribute("req"); //为模板调用时，调用request对象的变量名
        resolver.setOrder(0);
        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);

    }
}
