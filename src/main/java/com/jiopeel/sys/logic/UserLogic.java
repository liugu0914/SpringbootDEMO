package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.App;
import com.jiopeel.sys.bean.User;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.form.UserForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.bean.query.UserQuery;
import com.jiopeel.sys.bean.result.AppResult;
import com.jiopeel.sys.bean.result.UserResult;
import com.jiopeel.sys.dao.UserDao;
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
public class UserLogic extends BaseLogic {

    @Resource
    private UserDao dao;

    /**
     * @param id
     * @return App
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public User get(String id) {
        User bean = new User();
        if (!BaseUtil.empty(id))
            bean = dao.queryOneById(User.class, id);
        return bean;
    }

    /**
     * @param id
     * @return AppResult
     * @Description: 根据id获取应用信息 自定义
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public UserResult getInfo(String id) {
        UserResult bean = new UserResult();
        if (!BaseUtil.empty(id))
            bean = dao.queryOne("user.getInfo", id);
        return bean;
    }

    /**
     * @param query 查询对象
     * @param page     分页器
     * @return Base
     * @Description: 获取分页列表数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Page<UserResult> getListPage(UserQuery query, Page<UserResult> page) {
        Page<UserResult> PageList = dao.queryPageList("user.getListPage", query, page);
        return PageList;
    }


    /**
     * @param query 查询对象
     * @return Base
     * @Description: 根据搜索条件查询数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base list(UserQuery query) {
        List<UserResult> list = dao.query("user.list", query);
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
    public Base save(UserForm form) {
        CheckBean(form);
        String id = form.getId();
        User bean = new User();
        if (BaseUtil.empty(id)) {//添加
            BeanUtils.copyProperties(form, bean);
            if (BaseUtil.empty(bean.getId()))
                bean.createUUID();
            bean.createTime();
            bean.setEnable(Constant.ENABLE_YES);
            dao.add(bean);
        } else {//修改
            bean = get(id);
            BeanUtils.copyProperties(form, bean);
            bean.updTime();
            dao.upd4n(bean, "id", "username","email","enable","updtime");
        }
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
        Assert.isNull(ids, "未选择不能删除");
        String[] ids_ = ids.split(",");
        if (ids_.length <= 0) {
            Assert.isNull("", "未选择不能删除");
        }

        dao.delByIds(User.class, ids_);
        return Base.suc("删除成功");
    }


    /**
     * @param form 表单提交对象
     * @Description: 检查对象数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    private void CheckBean(UserForm form) {
        Assert.isNull(form, "对象不能为空");
        Assert.isNull(form.getAccount(), "应用名称不能为空");
        Assert.isNull(form.getUsername(), "应用标识不能为空");
    }

}
