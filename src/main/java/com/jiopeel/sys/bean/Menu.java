package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @description：菜单表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
public class Menu extends Bean implements Serializable {

    private static final long serialVersionUID = -2418633026700955554L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 地址
     */
    private String url;

    /**
     * 是否可用
     */
    private String enable;


    /**
     * 是否为父节点 0否 1是
     */
    private String parent;

    /**
     * 菜单级别
     */
    private Integer level;

    /**
     * 上级id
     */
    private String superid;

    /**
     * 菜单顺序
     */
    private Integer ordernum;

    /**
     * 应用id
     */
    private String appid;
}
