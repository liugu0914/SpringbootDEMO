package com.jiopeel.core.base;

import lombok.Getter;
import lombok.Setter;

public enum StateCode {
    SUCCESS(200, "操作成功"),
    BADREQUEST(400, "请求错误"),
    NOPERMISSION(403, "没有访问权限"),
    OUTIME(408, "请求超时"),
    ERROR(500, "服务器错误"),
    NOTFOUND(404, "404"),
    FAIL(-1, "失败");
    @Getter
    @Setter
    private Integer status;

    @Getter
    @Setter
    private String message;

    StateCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

}
