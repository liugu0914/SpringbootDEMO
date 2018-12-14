package com.jiopeel.enter.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;

import com.jiopeel.enter.bean.Base;

public abstract class BaseDao<T extends Base> {

	@Resource
	public SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public SqlSession getSqlSession() {
		return this.sqlSession;
	}

	public <E> List<E> query(String sqlmapper, Object obj) {
		List<E> beans = getSqlSession().selectList(sqlmapper, obj);
		return beans;
	}

	public <E> List<E> query(String sqlmapper) {
		List<E> beans = getSqlSession().selectList(sqlmapper);
		return beans;
	}

	public <E> E queryOne(String sqlmapper) {
		return getSqlSession().selectOne(sqlmapper);
	}

	public boolean add(String nameSpec, Object object) {
		return getSqlSession().insert(nameSpec, object) > 0;
	}
	
	public boolean add(String nameSpec) {
		return add(nameSpec,null);
	}
}
