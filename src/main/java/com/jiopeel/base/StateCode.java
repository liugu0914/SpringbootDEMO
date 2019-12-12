package com.jiopeel.base;

import lombok.Getter;
import lombok.Setter;

public enum StateCode {
    SUCCESS(200, "成功"),
    BADREQUEST(400, "请求错误"),
    OUTIME(408, "请求超时"),
    ERROR(500, "服务器错误"),
    NOTFOUND(404, "404"),
    FAIL(-1, "失败");
    @Getter
    @Setter
    private Integer code;

    @Getter
    @Setter
    private String message;

    StateCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
