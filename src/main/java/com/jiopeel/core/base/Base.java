package com.jiopeel.core.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class Base implements Serializable {

    private static final long serialVersionUID = -3540383839044057287L;

    public Base() {
    }

    public Base(Integer code, String message) {
        this.status = code;
        this.message = message;
    }

    public Base(Integer code, String message, Object data) {
        this.status = code;
        this.message = message;
        this.data = data;
    }

    public Base(StateCode stateCode) {
        this(stateCode.getStatus(), stateCode.getMessage());
    }

    public Base(StateCode stateCode, Object data) {
        this(stateCode.getStatus(), stateCode.getMessage(), data);
    }

    public void setStateCode(Integer code, String message) {
        this.status = code;
        this.message = message;
    }

    public void setStateCode(StateCode stateCode) {
        this.setStateCode(stateCode.getStatus(), stateCode.getMessage());
    }

    public static Base suc() {
        return new Base(StateCode.SUCCESS);
    }

    public static Base suc(StateCode stateCode) {
        return new Base(stateCode.getStatus(), stateCode.getMessage());
    }

    public static Base suc(String msg) {
        return new Base(StateCode.SUCCESS.getStatus(), msg);
    }

    public static Base suc(Object data) {
        return new Base(StateCode.SUCCESS.getStatus(), StateCode.SUCCESS.getMessage(), data);
    }

    public static Base suc(String msg, Object data) {
        return new Base(StateCode.SUCCESS.getStatus(), msg, data);
    }

    public static Base fail() {
        Base base = new Base(StateCode.FAIL);
        base.setResult(false);
        return base;
    }

    public static Base fail(String msg) {
        Base base = new Base(StateCode.FAIL.getStatus(), msg);
        base.setResult(false);
        return base;
    }

    public static Base fail(StateCode stateCode) {
        Base base = new Base(stateCode.getStatus(), stateCode.getMessage());
        base.setResult(false);
        return base;
    }

    public static Base fail(String msg, Object data) {
        Base base = new Base(StateCode.FAIL.getStatus(), msg, data);
        base.setResult(false);
        return base;
    }

    public static Base judge(boolean flag) {
        return flag ? suc() : fail();
    }

    /**
     * 数据返回
     */
    private Object data;

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 消息通知
     */
    private String message;

    /**
     * 返回成功或失败
     */
    private boolean result = true;

}
