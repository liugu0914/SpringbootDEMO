package com.jiopeel.core.config.exception;

import com.jiopeel.core.base.StateCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description : 自定义服务异常处理器
 * @auhor:lyc
 * @Date:2019/10/30 23:03
 */
@Slf4j
public class ServerException extends RuntimeException {
    private static final long serialVersionUID = -8892899092208135415L;

    /**
     * 状态码
     */
    @Getter
    @Setter
    private Integer status;

    /**
     * 消息
     */
    @Getter
    @Setter
    private String message;


    public ServerException() {
        super();
    }

    public ServerException(String mes, Throwable cause) {
        super(mes, cause);
        this.message = mes;
        log.error(mes);
    }


    public ServerException(StateCode stateCode) {
        super(stateCode.getMessage());
        this.status = stateCode.getStatus();
        this.message = stateCode.getMessage();
        log.error("status= {}, message= {}");
    }

    public ServerException(String mes) {
        super(mes);
        this.message = mes;
        this.status=StateCode.FAIL.getStatus();
        log.error(mes);
    }


    public ServerException(Throwable cause) {
        super(cause);
        log.error(cause.getMessage(),cause);
    }
}
