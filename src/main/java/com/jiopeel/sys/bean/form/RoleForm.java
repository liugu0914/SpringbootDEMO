package com.jiopeel.sys.bean.form;

import com.jiopeel.sys.bean.Role;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @description：角色表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class RoleForm extends Role {

    private static final long serialVersionUID = -1235267327658242929L;

    @Tolerate
    public RoleForm() {
    }

}
