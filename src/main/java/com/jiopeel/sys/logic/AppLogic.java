package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.sys.dao.AppDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author ：lyc
 * @description：角色Logic层
 * @date ：2019/12/20 10:25
 */
@Slf4j
@Service
public class AppLogic extends BaseLogic {

    @Resource
    private AppDao dao;

    /**
     * @Description: 删除
     * @param ids
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base del(String ids) {
        String ids_[] = ids.split(",");
        if (ids_ == null || ids_.length <= 0) {
            Assert.isNull("", "删除不能为空");
        }
//        dao.del();
        return Base.suc("删除成功");
    }
}
