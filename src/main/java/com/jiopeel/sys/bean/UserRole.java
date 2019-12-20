package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @description：用户对应角色表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@Builder
public class UserRole extends Bean implements Serializable {

    private static final long serialVersionUID = 2900400799253271456L;

    @Tolerate
    public UserRole() {
    }

    /**
     * 用户id
     */
    private String userid;

    /**
     * 角色id
     */
    private String roleid;
}
