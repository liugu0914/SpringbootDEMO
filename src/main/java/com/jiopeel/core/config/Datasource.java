package com.jiopeel.core.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

//@Configuration
public class Datasource {


    /**
     * 根据数据源创建SqlSessionFactory
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory( DataSource dataSource)
            throws Exception {
//        String driverClassPath = this.sys.getProperty("eman.pagings", "master.driverClassName"); // 指定分页驱动类型Key
//        String driverClassName = this.sys.getProperty(driverClassPath);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);// 指定数据源(这个必须有，否则报错)

        String typeAliasesPackage = "com.jiopeel.*.dao.*";
        if (typeAliasesPackage != null)
            sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);// 指定基包

        String mLocations = "classpath*:com/jiopeel/**/dao/*.xml";
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mLocations));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        configuration.setMapUnderscoreToCamelCase(true); // 设置驼峰命名规则
//        configuration.addInterceptor(pageInterceptor()); // 分页拦截器
//        configuration.addInterceptor(new SQLInterceptor());
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }
}
