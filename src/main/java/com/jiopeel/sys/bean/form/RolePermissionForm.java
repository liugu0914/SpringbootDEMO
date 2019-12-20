package com.jiopeel.sys.bean.form;

import com.jiopeel.sys.bean.RolePermission;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @description：角色对应权限表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class RolePermissionForm extends RolePermission {

    private static final long serialVersionUID = 9163453692525450867L;

    @Tolerate
    public RolePermissionForm() {
    }

}
