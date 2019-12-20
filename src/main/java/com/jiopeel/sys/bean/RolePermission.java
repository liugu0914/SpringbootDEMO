package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @description：角色对应权限表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class RolePermission extends Bean implements Serializable {

    private static final long serialVersionUID = -5955554989678229558L;

    @Tolerate
    public RolePermission() {
    }

    /**
     * 权限id
     */
    private String permissionid;

    /**
     * 角色id
     */
    private String roleid;
}
