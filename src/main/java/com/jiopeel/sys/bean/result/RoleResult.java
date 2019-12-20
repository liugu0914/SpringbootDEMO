package com.jiopeel.sys.bean.result;

import com.jiopeel.sys.bean.Role;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @description：角色表返回bean
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class RoleResult extends Role {

    private static final long serialVersionUID = 1235004268509049920L;

    @Tolerate
    public RoleResult() {
    }
}
