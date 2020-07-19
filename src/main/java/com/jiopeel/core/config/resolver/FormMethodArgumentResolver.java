package com.jiopeel.core.config.resolver;

import com.jiopeel.core.bean.Bean;
import com.jiopeel.core.bean.BindMap;
import com.jiopeel.core.config.annotation.FormParam;
import com.jiopeel.core.util.BaseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于绑定特殊数据的转换类(暂用于list<Bean>和Map<String,Bean>)
 *
 * @author liud
 */
public class FormMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public FormMethodArgumentResolver() {
    }

    /**
     * 重构
     */
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer viewcontainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory factory) throws Exception {
        Object target = this.createAttribute(parameter, factory, webRequest);
        WebDataBinder binder = factory.createBinder(webRequest, target, null);
        if (binder.getTarget() != null) {
            this.bindRequestParameters(binder, parameter, viewcontainer, webRequest, factory);
        }
        return binder.getTarget();
    }

    /**
     * 是否存在@EMan标识
     */
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FormParam.class);
    }

    /**
     * 创建属性对象(此方法可供子類調用)
     *
     * @param parameter
     * @param factory
     * @param webRequest
     * @return Object
     * @throws Exception
     */
    protected Object createAttribute(MethodParameter parameter, WebDataBinderFactory factory,
                                     NativeWebRequest webRequest) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        if (List.class.isAssignableFrom(parameterType)) {
            return ArrayList.class.newInstance();
        }

        if (BindMap.class.isAssignableFrom(parameterType)) {
            return BindMap.class.newInstance();
        }

        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    /**
     * 绑定参数
     *
     * @param binder
     * @param parameter
     * @param viewcontainer
     * @param webRequest
     * @param factory
     * @throws Exception
     */
    protected void bindRequestParameters(WebDataBinder binder, MethodParameter parameter,
                                         ModelAndViewContainer viewcontainer, NativeWebRequest webRequest,
                                         WebDataBinderFactory factory) throws Exception {
        String name = parameter.getParameterAnnotation(FormParam.class).value(); // 注解名称
        if (BaseUtil.empty(name))
            throw new BindException("@FormParam(value)中[value]不能为空!");

        Class<?> parameterType = parameter.getParameterType();
        if (List.class.isAssignableFrom(parameterType)) {
            this.bindListRequestParameters(name, binder, parameter, viewcontainer, webRequest,
                    factory);
            return;
        } // list值绑定

        if (BindMap.class.isAssignableFrom(parameterType)) {
            this.bindMapRequestParameters(name, binder, parameter, viewcontainer, webRequest,
                    factory);
            return;
        }

    }

    /**
     * 绑定list对象值
     *
     * @param name
     * @param binder
     * @param parameter
     * @param viewcontainer
     * @param request
     * @param factory
     * @throws Exception
     */
    @SuppressWarnings("all")
    protected void bindListRequestParameters(String name, WebDataBinder binder,
                                             MethodParameter parameter, ModelAndViewContainer viewcontainer,
                                             NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        Map<String, String[]> param = request.getParameterMap(); // 客户端所获得参数
        Class clazz = this.getBindGenericTypes(parameter)[0]; // 获得泛型类型
        String anno = this.matchNames(name); // 注解中的名称(同时对应list<K>的值)
        int annoAri = this.getArithmetic(name); // 算法规则
        String[] matchs = null;
        if (annoAri != 0)
            matchs = this.matchValues(name); // 算法后匹配的类型
        if (annoAri != 0 && matchs == null)
            throw new BindException("请注解符合规范的名称;例:@FormParam(\"参数名称\")或@EMan(\"参数名称==1\")");
        boolean match = matchs != null && matchs[1] != null; // true则采用名称匹配
        String[] annos = param.get(anno);

        List bindParameterList = (List) binder.getTarget(); // 请求绑定的对象
        Object claoo = this.isBaseType(clazz) ? null : clazz.newInstance(); // 泛型实例化对象
        if (claoo != null && claoo instanceof Bean) {
            Set<String> paramKeys = param.keySet(); // 所有参数的Key
            Field[] fields = BaseUtil.getAllFields(claoo); // 获得bean的所有属性
            for (int i = 0, j = annos != null ? annos.length : 0; i < j; i++) {
                String value = BaseUtil.empty(annos[i]) ? null : annos[i];
                String matchVal = null;
                if (!(matchs == null || "NULL".equals(matchs[0])))
                    matchVal = matchs[0];

                if (match) {
                    String[] s = param.get(matchs[0]);
                    matchVal = s != null && s.length > i ? s[i] : null;
                }

                if (!this.matchParameterValue(annoAri, value, matchVal))
                    continue;

                Object beans = clazz.newInstance();
                for (String key : paramKeys) {
                    String[] values = param.get(key);
                    if (values == null || values.length <= i || BaseUtil.empty(values[i]))
                        continue;
                    Field field = BaseUtil.getFieldByName(fields, key);
                    if (field != null)
                        BaseUtil.setFieldVal(beans, field, values[i]);
                }
                bindParameterList.add(beans);
            }
            return;
        } // list<Bean>绑定

        for (int i = 0, j = annos != null ? annos.length : 0; i < j; i++) {
            String value = BaseUtil.empty(annos[i]) ? null : annos[i];
            String matchVal = null;
            if (!(matchs == null || "NULL".equals(matchs[0])))
                matchVal = matchs[0];

            if (match) {
                String[] s = param.get(matchs[0]);
                matchVal = s != null && s.length > i ? s[i] : null;
            }
            if (this.matchParameterValue(annoAri, value, matchVal)) {
                bindParameterList.add(BaseUtil.obj(clazz, value));
            }

        }

    }

    /**
     * 是否为基本类型
     *
     * @param clazz
     * @return true则为8大基本类型
     */
    private boolean isBaseType(Class<?> clazz) {
        String s = clazz != null ? clazz.getSimpleName() : null;
        return "Integer".equals(s) || "String".equals(s);
    }

    /**
     * 是否符合匹配的算法
     *
     * @param annoAri  算法
     * @param value    源值
     * @param matchVal 需判断的值
     * @return 是否符合匹配的算法
     */
    private boolean matchParameterValue(int annoAri, String value, String matchVal) {
        boolean isMatch = false;
        switch (annoAri) {
            case 0:
                isMatch = true;
                break;
            case 1: // 相等
                if (BaseUtil.equals(value, matchVal))
                    isMatch = true;
                break;
            case 2: // 不相等
                if (!BaseUtil.equals(value, matchVal))
                    isMatch = true;
                break;
            case 3: // 包含
                if (isContain(value, matchVal))
                    isMatch = true;
                break;
        }
        return isMatch;
    }

    /**
     * 绑定map对象值(仅支持Map<String,String>;Map<String,String[]>;Map<String,Bean>)
     * 三种映射方式
     *
     * @param name
     * @param binder
     * @param parameter
     * @param viewcontainer
     * @param request
     * @param factory
     * @throws Exception
     */
    @SuppressWarnings("all")
    protected void bindMapRequestParameters(String name, WebDataBinder binder,
                                            MethodParameter parameter, ModelAndViewContainer viewcontainer,
                                            NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        String anno = this.matchNames(name); // 注解中的名称(同时对应map<K>的值)

        Class[] clazz = this.getBindGenericTypes(parameter); // 获得泛型类型
        Map<String, String[]> param = request.getParameterMap(); // 客户端所获得参数
        BindMap bindParameterMap = (BindMap) binder.getTarget(); // 请求绑定的对象
        if (clazz[2] != null && !clazz[1].equals(String.class))
            throw new BindException("请求对象[Map]仅支持[value]为[String]类型的数组!");

        if (clazz[2] != null && clazz[1].equals(String.class)) {
            bindParameterMap.putAll(param);
            return;
        } // Map<String,String[]>绑定

        if (clazz[1].equals(String.class)) {
            for (String key : param.keySet()) {
                String[] val = param.get(key);
                bindParameterMap.put(key, val == null ? null : val[0]);
            }
            return;
        } // Map<String,String>绑定

        Object claoo = clazz[1].newInstance();
        if (claoo instanceof Bean) { // 绑定bean
            if (BaseUtil.empty(anno))
                throw new BindException("请求对象[Map]中[Key]不能为空!");
            String[] annos = param.get(anno); // 匹配注解名称对应的值数组
            Field[] fields = BaseUtil.getAllFields(claoo); // 获得bean的所有属性

            Set<String> paramKeys = param.keySet(); // 所有参数的Key
            for (int i = 0, j = annos != null ? annos.length : 0; i < j; i++) {
                String value = annos[i];
                Object beans = clazz[1].newInstance();
                for (String key : paramKeys) {
                    String[] values = param.get(key);
                    if (values == null || values.length <= i || BaseUtil.empty(values[i]))
                        continue;
                    Field field = BaseUtil.getFieldByName(fields, key);
                    if (field != null)
                        BaseUtil.setFieldVal(beans, field, values[i]);
                }
                bindParameterMap.put(value, beans);
            }
        } // Map<String,Bean>绑定
    }

    /**
     * 获得绑定参数的泛型类型1K;2V;3V是否数组
     *
     * @param parameter
     * @return Class[]
     */
    @SuppressWarnings("all")
    private Class[] getBindGenericTypes(MethodParameter parameter) {
        Class[] clazz = new Class[]{Object.class, Object.class, null};
        Type type = parameter.getGenericParameterType();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            clazz[0] = (Class) types[0]; // 获得Map<K>
            if (types.length == 1)
                return clazz;

            if (!(types[1] instanceof GenericArrayType)) // map的V为非数组泛型
                clazz[1] = (Class) types[1]; // 获得Map<V>
            else {
                types[1] = ((GenericArrayType) types[1]).getGenericComponentType();
                clazz[1] = (Class) types[1];
                clazz[2] = Class.class;
            }
        }
        return clazz;
    }

    /**
     * 获得算法的符号 1==;2!=;3~=(1为等于2为不等于3为包含)
     *
     * @param name
     * @return (1为等于2为不等于3为包含)
     */
    private int getArithmetic(String name) {
        if (name.contains("~="))
            return 3;
        if (name.contains("!="))
            return 2;
        if (name.contains("=="))
            return 1;
        return 0;
    }

    /**
     * 匹配的值null则无匹配;[value,null]具体值;[value,"1"]对应参数名称
     *
     * @param name
     * @return [value, null]具体值;[value,"1"]对应参数名称
     */
    private String[] matchValues(String name) {
        int index = name.lastIndexOf("=");
        if (index == -1)
            return null;
        String match = name.substring(index + 1);
        String value = match.replace("'", "");
        return new String[]{value, value.equals(match) ? null : "1"};
    }

    /**
     * 匹配的名称
     *
     * @param name 源字符串(例:name==1)
     * @return String
     */
    private String matchNames(String name) {
        int index = name.lastIndexOf("=");
        if (index == -1)
            return name;
        return name.substring(0, index - 1);
    }

    /**
     * 源字符串是否包含数组中任各一字符串
     *
     * @param str    源字符串
     * @param params 比较的字符串
     * @return true則至少一个包含
     */
    private boolean isContain(String str, String... params) {
        if (!BaseUtil.empty(params))
            for (String s : params) {
                if (BaseUtil.empty(s))
                    continue;
                if (str.contains(s))
                    return true;

            }
        return false;
    }
}
