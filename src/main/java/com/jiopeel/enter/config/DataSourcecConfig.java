package com.jiopeel.enter.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourcecConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource DataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean(DataSource DataSource) throws Exception {
		SqlSessionFactoryBean sqlFactory = new SqlSessionFactoryBean();
		try {
			sqlFactory.setDataSource(DataSource);
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			// 设置mapper 文件的路径
			Resource[] resource = resolver.getResources("classpath*:com/jiopeel/**/dao/*.xml");// {resourceMapXML};
			sqlFactory.setMapperLocations(resource);
			// 扫描实体类所在包
			String typeAliasesPackage = "com.jiopeel.*.bean.*";
			sqlFactory.setTypeAliasesPackage(typeAliasesPackage);

			org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
			configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
			configuration.setMapUnderscoreToCamelCase(true);
			configuration.setCacheEnabled(true);
			// configuration.addInterceptor(pageInterceptor());
			// configuration.addInterceptor(new SQLInterceptor());
			sqlFactory.setConfiguration(configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sqlFactory.getObject();
	}

	@Bean({ "sqlSession" })
	public SqlSession sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean({ "DataSourceTransactionManager" })
	public DataSourceTransactionManager getDataSourceTransactionManager(DataSource Datasource) {
		return new DataSourceTransactionManager(Datasource);
	}

	@Bean({ "txInterceptor" })
	public TransactionInterceptor getTransactionInterceptor(DataSourceTransactionManager transactionManager) {
		return new TransactionInterceptor(transactionManager, transactionAttributeSource());
	}

	@Bean({ "txSource" })
	public TransactionAttributeSource transactionAttributeSource() {
		RuleBasedTransactionAttribute readonlyTx = new RuleBasedTransactionAttribute();
		readonlyTx.setReadOnly(true);
		readonlyTx.setPropagationBehavior(4);

		RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(0,
				Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		requiredTx.setTimeout(10);

		Map<String, TransactionAttribute> txManager = new LinkedHashMap();
		txManager.put("add*", requiredTx);
		txManager.put("upd*", requiredTx);
		txManager.put("del*", requiredTx);
		txManager.put("save*", requiredTx);
		txManager.put("sync*", requiredTx);
		txManager.put("insert*", requiredTx);
		txManager.put("update*", requiredTx);
		txManager.put("delete*", requiredTx);
		txManager.put("*", readonlyTx);

		NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource = new NameMatchTransactionAttributeSource();
		nameMatchTransactionAttributeSource.setNameMap(txManager);
		return nameMatchTransactionAttributeSource;
	}

	@Bean({ "pointcutAdvisor" })
	public AspectJExpressionPointcutAdvisor pointcutAdvisor(TransactionInterceptor txInterceptor) {
		AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
		pointcutAdvisor.setAdvice(txInterceptor);
		pointcutAdvisor.setExpression("execution (* com.jiopeel.*.logic.*.*(..))");
		return pointcutAdvisor;
	}
}
