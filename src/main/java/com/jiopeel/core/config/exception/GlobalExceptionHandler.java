package com.jiopeel.core.config.exception;

import com.jiopeel.core.base.Base;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description :全局异常处理
 * @auhor:lyc
 * @Date:2019/10/30 23:44
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public Base defaultErrorHandler(Exception e) {
        e.printStackTrace();
        return Base.fail(e.getMessage());
    }

    @ExceptionHandler(value = {ServerException.class})
    public Base ServerExceptionHandler(ServerException e) {
        e.printStackTrace();
        Base base = new Base(e.getStatus(), e.getMessage());
        base.setResult(false);
        return base;
    }
}
