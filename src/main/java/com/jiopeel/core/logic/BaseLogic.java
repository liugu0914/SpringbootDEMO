package com.jiopeel.core.logic;


import com.jiopeel.core.config.redis.RedisUtil;
import com.jiopeel.core.dao.BeanDao;

import javax.annotation.Resource;

/**
 * 登陆信息处理
 */
public abstract class BaseLogic {

    @Resource
    public BeanDao dao;

    @Resource
    public RedisUtil redisUtil;

}
