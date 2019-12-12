package com.jiopeel.config.exception;

import com.jiopeel.base.StateCode;

/**
 * @Description : 自定义服务异常处理器
 * @auhor:lyc
 * @Date:2019/10/30 23:03
 */
public class ServerException extends RuntimeException {
    private static final long serialVersionUID = -8892899092208135415L;

    private Integer code;

    private String message;

    public ServerException() {
        super();
    }

    public ServerException(String mes, Throwable cause, boolean str, boolean str1) {
        super(mes, cause, str, str1);
    }

    public ServerException(String mes, Throwable cause) {
        super(mes, cause);
    }

    public ServerException(StateCode stateCode) {
        super(stateCode.getMessage());
        this.code = stateCode.getCode();
        this.message = stateCode.getMessage();
    }

    public ServerException(String mes) {
        super(mes);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
