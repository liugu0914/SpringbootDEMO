package com.jiopeel.enter.logic;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiopeel.enter.bean.Base;
import com.jiopeel.enter.dao.EnterDao;
@Service("com.jiopeel.enter.Logic.BaseLogic")
public class BaseLogic {

	@Resource
	private EnterDao dao;

	
	public Base syncqueryInfo() throws Exception{
		Base bean1=this.dao.queryOne("base.getbaselist");
		System.out.println(bean1);
		this.dao.add("base.add_login",bean1);
		System.out.println(bean1);
		return bean1;
	}
}
