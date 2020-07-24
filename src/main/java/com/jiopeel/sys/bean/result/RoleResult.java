package com.jiopeel.sys.bean.result;

import com.jiopeel.sys.bean.Role;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author ：lyc
 * @description：角色表返回bean
 * @date ：2019/12/20 9:47
 */
@Data
public class RoleResult extends Role {

    private static final long serialVersionUID = 1235004268509049920L;

    /**
     * 是否被使用 0否 1是
     */
    private String used;
}
