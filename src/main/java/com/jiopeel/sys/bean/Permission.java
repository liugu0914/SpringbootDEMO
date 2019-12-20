package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @description：权限表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
public class Permission extends Bean implements Serializable {

    private static final long serialVersionUID = 3203908955920194752L;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类型
     */
    private String type;

    /**
     * 是否可用 0否 1是
     */
    private String enable;
}
