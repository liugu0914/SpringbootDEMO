package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @description：角色表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
public class Role extends Bean implements Serializable {

    private static final long serialVersionUID = -7344215447622185693L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否可用
     */
    private String enable;

    /**
     * 权重
     */
    private String weight;
}
