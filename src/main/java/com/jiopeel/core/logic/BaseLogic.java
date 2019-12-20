package com.jiopeel.core.logic;


import com.jiopeel.core.bean.Bean;
import com.jiopeel.core.dao.BaseDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登陆信息处理
 */
@Service
public abstract class BaseLogic {

    @Resource
    public BaseDao<Bean> dao;

}
