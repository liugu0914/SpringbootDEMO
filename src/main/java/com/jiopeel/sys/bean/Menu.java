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
@Builder
public class Menu extends Bean implements Serializable {

    private static final long serialVersionUID = -2418633026700955554L;

    @Tolerate
    public Menu() {
    }

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
     * 菜单级别
     */
    private String level;

    /**
     * 上级id
     */
    private Integer superid;

    /**
     * 菜单顺序
     */
    private Integer ordernum;

    /**
     * 应用id
     */
    private String appid;
}
