package com.jiopeel.core.dao;

import com.jiopeel.core.bean.Bean;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.config.interceptor.PageIntercept;
import com.jiopeel.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description :数据交互dao层
 * @auhor:lyc
 * @Date:2019/12/21 11:51
 */
@Slf4j
public abstract class BaseDao<E extends Bean> {

    @Resource
    private SqlSession sqlSession;

    private static final String serialVersionUID = "serialVersionUID";

    private static final String CORE_ADD = "core.add";

    private static final String CORE_UPD = "core.upd";

    private static final String CORE_ADD_BATCH = "core.addBatch";

    private static final Integer MAX_ROW = 100;

    private static final String TABLE_HEADER = "t_";


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
     * @Description :单表添加
     * @param: bean  传递参数
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <T extends Bean> boolean add(T bean) {
        Class<? extends Bean> clazz = bean.getClass();
        String tableName = TABLE_HEADER + BaseUtil.camel2under(clazz.getSimpleName());
        Field[] Fields = BaseUtil.getAllFields(bean);
        List<String> nameList = new ArrayList<String>();
        List<Object> valueList = new ArrayList<Object>();
        for (Field field : Fields) {
            String name = field.getName();
            if (serialVersionUID.equals(name))
                continue;
            Object obj = getFieldVal(field, bean);
            nameList.add(name);
            valueList.add(obj);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nameList", nameList);
        map.put("valueList", valueList);
        map.put("tableName", tableName);
        return add(CORE_ADD, map);
    }

    /**
     * @Description :获取字段对应的值
     * @param: field  字段
     * @param: bean
     * @Return: Object 返回值
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    private <T extends Bean> Object getFieldVal(Field field, T bean) {
        field.setAccessible(true);
        Object obj = null;
        try {
            obj = field.get(bean);
            if (obj instanceof String)
                obj = String.valueOf(obj);
            if (obj instanceof Integer)
                obj = BaseUtil.parseInt(obj);
            if (obj instanceof Date)
                obj = BaseUtil.Dateformat((Date) obj);
            if (obj instanceof BigDecimal)
                obj = ((BigDecimal) obj).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @Description :批量添加
     * @param: bean  传递参数
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E extends Bean> boolean addBatch(List<E> list) {
        boolean flag = true;
        String tableName = null;
        List<String> nameList = new ArrayList<String>();
        List<List<Object>> lists = new ArrayList<List<Object>>();
        for (E bean : list) {
            Class<? extends Bean> clazz = bean.getClass();
            if (BaseUtil.empty(tableName))
                tableName = TABLE_HEADER + BaseUtil.camel2under(clazz.getSimpleName());
            Field[] Fields = BaseUtil.getAllFields(bean);
            List<Object> valueList = new ArrayList<Object>();
            for (Field field : Fields) {
                String name = field.getName();
                if (serialVersionUID.equals(name))
                    continue;
                Object obj = getFieldVal(field, bean);
                if (!nameList.contains(name))
                    nameList.add(name);
                valueList.add(obj);
            }
            lists.add(valueList);
        }
        if (lists == null || lists.isEmpty()) {
            log.error("无添加数据");
            return false;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nameList", nameList);
        map.put("lists", lists);
        map.put("tableName", tableName);
        if (lists.size() < MAX_ROW)
            return add(CORE_ADD_BATCH, map);
        List<List<List<Object>>> items = BaseUtil.splitList(lists, MAX_ROW);
        for (List<List<Object>> item : items) {
            map.put("lists", item);
            flag = flag && add(CORE_ADD_BATCH, map);
        }
        return flag;
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
     * @Description :单表更新
     * @param: bean  传递参数
     * @param: clause 作为where的条件
     * @param: strings 不需要更新的字段
     * @Return: boolean 是否执行成功
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <T extends Bean> boolean upd(T bean, String whereValues, String... strings) {
        if (BaseUtil.empty(whereValues)) {
            whereValues = "";
            log.warn("where的条件字段为空");
        }
        if (strings == null || strings.length == 0)
            strings = new String[1];
        List<String> splitfield = Arrays.asList(whereValues.split(","));
        List<String> strlist = Arrays.asList(strings);
        Class<? extends Bean> clazz = bean.getClass();
        String tableName = TABLE_HEADER + BaseUtil.camel2under(clazz.getSimpleName());
        Field[] Fields = BaseUtil.getAllFields(bean);
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        Map<String, Object> clauseMap = new HashMap<String, Object>();
        for (Field field : Fields) {
            String name = field.getName();
            if (serialVersionUID.equals(name) || strlist.contains(name))
                continue;
            Object obj = getFieldVal(field, bean);
            if (!splitfield.isEmpty() && splitfield.contains(name))
                clauseMap.put(name, obj);
            else
                fieldMap.put(name, obj);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableName", tableName);
        map.put("clauseMap", clauseMap);
        map.put("fieldMap", fieldMap);
        return upd(CORE_UPD, map);
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
     * 1.1.1 判断长度是否大于MAX_ROW,大于则分批每MAX_ROW行个执行1次
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
            if (clazz.equals(int[].class) && ((int[]) object).length > MAX_ROW)
                object = Arrays.asList((int[]) object);// 如果是arrays则转换为list,以方便后续分批次
            else if (clazz.equals(long[].class) && ((long[]) object).length > MAX_ROW)
                object = Arrays.asList((long[]) object);
            else if (clazz.equals(String[].class) && ((String[]) object).length > MAX_ROW)
                object = Arrays.asList((String[]) object);
            else if (object.getClass().isArray() && !(object instanceof Map) && !(clazz.equals(int[].class))
                    && !(clazz.equals(long[].class)) && ((Object[]) object).length > MAX_ROW)
                object = BaseUtil.list((Object[]) object);

            List<?> items = null;
            if (object instanceof List && (items = (List<?>) object).size() > MAX_ROW)
                return this.query(nameSpec, items);
        }
        return this.getSqlSession().selectList(nameSpec, object);
    }

    /**
     * list集合查询条件且长度大于MAX_ROW行后的分批查询
     *
     * @param nameSpec 命名空间
     * @param items    查询条件
     * @return 符合查询条件的集合
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    private <E> List<E> query(String nameSpec, List<?> items) {
        List<E> beans = new ArrayList<E>(MAX_ROW);
        int len = items.size(), steps = MAX_ROW;
        int[] nums = new int[len / steps + 1];
        for (int i = 0; i < len; i++) {
            nums[i] = (i + 1) * steps;
            if (nums[i] >= len)
                break;
        } // 计算每MAX_ROW次1个批次,结果分几个批次完成

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
     * @Description :分页查询
     * @param: nameSpec  命名空间
     * @param: object  传递参数
     * @param: page  分页器
     * @Return: 符合查询条件的集合
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public <E> Page<E> queryPageList(String nameSpec, Object object, Page<E> page) {
        isInfoLog(nameSpec);
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