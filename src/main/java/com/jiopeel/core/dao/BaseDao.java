package com.jiopeel.core.dao;

import com.jiopeel.core.bean.Bean;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.interceptor.PageIntercept;
import com.jiopeel.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description :数据交互dao层
 * @auhor:lyc
 * @Date:2019/12/21 11:51
 */
@Slf4j
public class BaseDao<T extends Bean> {

    @Resource
    private SqlSession sqlSession;


    private SqlSession getSqlSession() {
        return this.sqlSession;
    }

    /**
     * @Description : info级别输出nameSpec
     * @param: nameSpec 命名空间
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    private void isInfoLog(String nameSpec) {
        if (log.isDebugEnabled() || log.isInfoEnabled())
            log.info("nameSpec : [{}]", nameSpec);
    }

    /**
     * @Description :添加
     * @param: nameSpec  命名空间
     * @param: object  传递参数
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public boolean add(String nameSpec, Object object) {
        isInfoLog(nameSpec);
        return getSqlSession().insert(nameSpec, object) > 0;
    }

    /**
     * @Description :添加
     * @param: nameSpec  命名空间
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public boolean add(String nameSpec) {
        isInfoLog(nameSpec);
        return add(nameSpec, null);
    }


    /**
     * @Description :更新
     * @param: nameSpec  命名空间
     * @param: object  传递参数
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public boolean upd(String nameSpec, Object object) {
        isInfoLog(nameSpec);
        return getSqlSession().update(nameSpec, object) > 0;
    }

    /**
     * @Description :更新
     * @param: nameSpec  命名空间
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public boolean upd(String nameSpec) {
        isInfoLog(nameSpec);
        return upd(nameSpec, null);
    }

    /**
     * @Description :删除
     * @param: nameSpec  命名空间
     * @param: object  传递参数
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public boolean del(String nameSpec, Object object) {
        isInfoLog(nameSpec);
        return getSqlSession().delete(nameSpec, object) > 0;
    }

    /**
     * @Description :删除
     * @param: nameSpec  命名空间
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public boolean del(String nameSpec) {
        isInfoLog(nameSpec);
        return del(nameSpec, null);
    }


    /**
     * @Description :查询
     * @param: nameSpec  命名空间
     * @param: object  传递参数
     * @Return: 符合查询条件的集合
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E> List<E> query(String nameSpec) {
        isInfoLog(nameSpec);
        List<E> beans = getSqlSession().selectList(nameSpec);
        return beans;
    }

    /**
     * 通过mybatis命名空间获得集合对象
     * 1.集合内可以存放任何对象(在mybatis中定义)
     * 1.1 假如object参数为List<?>或arrays对象则执行 1.1.1
     * 1.1.1 判断长度是否大于2000,大于则分批每2000行个执行1次
     *
     * @param nameSpec 命名空间
     * @param object   map参数
     * @return 符合查询条件的集合
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E> List<E> query(String nameSpec, Object object) {
        isInfoLog(nameSpec);

        if (object != null && (object instanceof List || object.getClass().isArray())) {
            Class<?> clazz = object.getClass();
            if (clazz.equals(int[].class) && ((int[]) object).length > 2000)
                object = Arrays.asList((int[]) object);// 如果是arrays则转换为list,以方便后续分批次
            else if (clazz.equals(long[].class) && ((long[]) object).length > 2000)
                object = Arrays.asList((long[]) object);
            else if (clazz.equals(String[].class) && ((String[]) object).length > 2000)
                object = Arrays.asList((String[]) object);
            else if (object.getClass().isArray() && !(object instanceof Map) && !(clazz.equals(int[].class))
                    && !(clazz.equals(long[].class)) && ((Object[]) object).length > 2000)
                object = BaseUtil.list((Object[]) object);

            List<?> items = null;
            if (object instanceof List && (items = (List<?>) object).size() > 2000)
                return this.query(nameSpec, items);
        }
        return this.getSqlSession().selectList(nameSpec, object);
    }

    /**
     * list集合查询条件且长度大于2000行后的分批查询
     *
     * @param nameSpec 命名空间
     * @param items    查询条件
     * @return 符合查询条件的集合
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    private <E> List<E> query(String nameSpec, List<?> items) {
        List<E> beans = new ArrayList<E>(2000);
        int len = items.size(), steps = 2000;
        int[] nums = new int[len / steps + 1];
        for (int i = 0; i < len; i++) {
            nums[i] = (i + 1) * steps;
            if (nums[i] >= len)
                break;
        } // 计算每2000次1个批次,结果分几个批次完成

        for (int i = 0; i < nums.length; i++) {
            int j = i == 0 ? 0 : nums[i - 1], t = nums[i];
            List<E> item = this.getSqlSession().selectList(nameSpec, items.subList(j, t > len ? len : t));
            if (item != null && item.size() != 0)
                beans.addAll(item);
            if (t >= len)
                break;
        } // 每个批次执行的结果放入List<?>
        return beans;
    }


    /**
     * @Description :查询
     * @param: nameSpec  命名空间
     * @param: object  传递参数
     * @param: page  分页器
     * @Return: 符合查询条件的集合
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E> Page<E> queryPageList(String nameSpec, Object object, Page<E> page) {
        PageIntercept.startPage(page);
        this.getSqlSession().selectList(nameSpec, object);
        page = PageIntercept.endPage();
        return page;
    }

    /**
     * @param nameSpec 命名空间
     * @return 符合查询条件的对象
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E> E queryOne(String nameSpec) {
        isInfoLog(nameSpec);
        return queryOne(nameSpec, null);
    }

    /**
     * @param nameSpec 命名空间
     * @param object   传参对象
     * @return 符合查询条件的对象
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E> E queryOne(String nameSpec, Object object) {
        isInfoLog(nameSpec);
        return getSqlSession().selectOne(nameSpec, object);
    }


}