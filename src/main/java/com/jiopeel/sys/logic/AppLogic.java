package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.App;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.bean.result.AppResult;
import com.jiopeel.sys.dao.AppDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param id
     * @return App
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public App get(String id) {
        return dao.queryOne("app.get", id);
    }

    /**
     * @param id
     * @return AppResult
     * @Description: 根据id获取应用信息 自定义
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public AppResult getInfo(String id) {
        return dao.queryOne("app.getInfo", id);
    }

    /**
     * @param appQuery 查询对象
     * @param page     分页器
     * @return Base
     * @Description: 获取分页列表数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Page<AppResult> getList(AppQuery appQuery, Page<AppResult> page) {
        Page<AppResult> PageList = dao.queryPageList("app.getList", appQuery, page);
        return PageList;
    }


    /**
     * @param appQuery 查询对象
     * @return Base
     * @Description: 根据搜索条件查询数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base list(AppQuery appQuery) {
        List<AppResult> list = dao.query("app.list", appQuery);
        return Base.suc(list);
    }

    /**
     * @param form 表单提交对象
     * @return Base
     * @Description: 保存数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base save(AppForm form) {
        CheckBean(form);
        App bean = new App();
        BeanUtils.copyProperties(form, bean);
        if (BaseUtil.empty(bean.getId()))
            bean.createUUID();
        bean.createTime();
        bean.setEnable(Constant.ENABLE_YES);
        return Base.judge(dao.add(bean));
    }


    /**
     * @param form
     * @return Base
     * @Description: 保存数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base upd(AppForm form) {
        CheckBean(form);
        Assert.isNull(form.getId(), "ID不能为空");
        App bean = get(form.getId());
        bean.setName(form.getName());
        bean.setEnable(form.getEnable());
        bean.setShortname(form.getShortname());
        bean.updTime();
        dao.upd("app.upd", bean);
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
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base del(String ids) {
        String ids_[] = ids.split(",");
        if (ids_ == null || ids_.length <= 0) {
            Assert.isNull("", "删除不能为空");
        }
        dao.del("app.del", ids_);
        return Base.suc("删除成功");
    }


    /**
     * @param form 表单提交对象
     * @Description: 检查对象数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    private void CheckBean(AppForm form) {
        Assert.isNull(form, "对象不能为空");
        Assert.isNull(form.getName(), "应用名称不能为空");
        Assert.isNull(form.getShortname(), "应用标识不能为空");
    }

}
