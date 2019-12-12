package com.jiopeel.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Base implements Serializable {

    private static final long serialVersionUID = -3540383839044057287L;

    public Base() {
    }

    public Base(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Base(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Base(StateCode stateCode) {
        this(stateCode.getCode(), stateCode.getMessage());
    }

    public Base(StateCode stateCode, Object data) {
        this(stateCode.getCode(), stateCode.getMessage(), data);
    }

    public void setStateCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setStateCode(StateCode stateCode) {
        this.setStateCode(stateCode.getCode(), stateCode.getMessage());
    }

    public static Base suc() {
        return new Base(StateCode.SUCCESS);
    }

    public static Base suc(String msg) {
        return new Base(StateCode.SUCCESS.getCode(), msg);
    }

    public static Base suc(String msg, Object data) {
        return new Base(StateCode.SUCCESS.getCode(), msg, data);
    }

    public static Base fail() {
        Base base = new Base(StateCode.FAIL);
        base.setResult(false);
        return base;
    }

    public static Base fail(String msg) {
        Base base = new Base(StateCode.FAIL.getCode(), msg);
        base.setResult(false);
        return base;
    }

    public static Base fail(String msg, Object data) {
        Base base = new Base(StateCode.FAIL.getCode(), msg, data);
        base.setResult(false);
        return base;
    }

    /**
     * 令牌
     */
    private String token;

    /**
     * 数据返回
     */
    private Object data;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息通知
     */
    private String message;

    /**
     * 返回成功或失败
     */
    private boolean result = true;

}
