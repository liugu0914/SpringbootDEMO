package com.jiopeel.sys.bean.result;

import com.jiopeel.sys.bean.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @description：用户对应角色表返回bean
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class UserRoleResult extends UserRole {

    private static final long serialVersionUID = 2900400799253271456L;

    @Tolerate
    public UserRoleResult() {
    }

}
