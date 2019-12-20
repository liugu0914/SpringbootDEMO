package com.jiopeel.sys.bean.query;

import com.jiopeel.sys.bean.Permission;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @description：权限表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class PermissionQuery extends Permission {

    private static final long serialVersionUID = -4666449341534539333L;

    @Tolerate
    public PermissionQuery() {
    }

}
