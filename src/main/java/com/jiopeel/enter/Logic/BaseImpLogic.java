package com.jiopeel.enter.Logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiopeel.enter.bean.Base;
import com.jiopeel.enter.dao.BaseDao;
@Transactional
@Service("BaseLogic")
public class BaseImpLogic implements BaseLogic{

	@Autowired
	private BaseDao dao;

	@Override
	public List<Base> getBaseList() {
		return this.dao.getBaseList();
	}
}
