package com.jiopeel.core.config.annotation;

import java.lang.annotation.*;

/**
 * 用于绑定特殊数据的注解标识
 *
 * @author liud at 2013-10-25
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormParam {

    /**
     * List对象映射时以此值在request域中值的长度做为list集合的长度;Map则以值对应request域中值做为key
     * 例:FormParam("id")List<Bean>则以request域中id对应的数组长度做为list集合的长度 特殊：
     * FormParam("id==a")则取request域中id的值为a的值放入集体中;
     * FormParam("id=='a'")则取request域中id的值符合对应request域中a的值放入集合中
     * 三种规则:==,!=,~=分别为等于,不等于,包含
     *
     * @return 设置的值
     */
    String value() default "id";

}
