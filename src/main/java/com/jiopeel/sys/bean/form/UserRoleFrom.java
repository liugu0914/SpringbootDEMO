package com.jiopeel.sys.bean.form;

import com.jiopeel.sys.bean.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @description：用户对应角色表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class UserRoleFrom extends UserRole {

    private static final long serialVersionUID = 7563729267628758988L;

    @Tolerate
    public UserRoleFrom() {
    }

}
