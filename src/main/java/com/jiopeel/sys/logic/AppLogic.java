package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.sys.bean.App;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.bean.result.AppResult;
import com.jiopeel.sys.dao.AppDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

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
     * @Description: 根据id获取应用信息
     * @param id
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base get(String id) {
        App app = dao.queryOne("", id);
        return Base.suc(app);
    }

    /**
     * @Description: 根据id获取应用信息
     * @param id
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base getInfo(String id) {
        AppResult app = dao.queryOne("", id);
        return Base.suc(app);
    }

    /**
     * @Description: 根据搜索条件查询数据
     * @param appQuery
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base getList(AppQuery appQuery) {
        List<AppResult> list = dao.query("", appQuery);
        return Base.suc(list);
    }


    /**
     * @Description: 根据搜索条件查询数据
     * @param appQuery
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base list(AppQuery appQuery) {
        List<AppResult> list = dao.query("", appQuery);
        return Base.suc(list);
    }

    /**
     * @Description: 保存数据
     * @param appForm
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base save(AppForm appForm) {
        return Base.suc();
    }
    /**
     * @Description: 保存数据
     * @param appForm
     * @return Base
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base upd(AppForm appForm) {
        return Base.suc();
    }

    /**
     * @param ids
     * @return Base
     * @Description: 删除
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base del(String ids) {
        String ids_[] = ids.split(",");
        if (ids_ == null || ids_.length <= 0) {
            Assert.isNull("", "删除不能为空");
        }
        dao.del("",ids_);
        return Base.suc("删除成功");
    }

}
