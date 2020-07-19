package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author ：lyc
 * @description：权限表
 * @date ：2019/12/20 9:47
 */
@Data
public class Permission extends Bean implements Serializable {

    private static final long serialVersionUID = -6502404079230573237L;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类型 sys 系统 menu 菜单 fuc 功能 column 字段
     */
    private String type;

    /**
     * 目标
     */
    private String target;

    /**
     * 通配符
     */
    private String charm;

    /**
     * 菜单id
     */
    private String menuid;

    /**
     * 应用ID
     */
    private String appid;

    /**
     * 是否可用 0否 1是
     */
    private String enable;
}
