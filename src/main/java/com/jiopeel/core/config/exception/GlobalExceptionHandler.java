package com.jiopeel.core.config.exception;

import com.jiopeel.core.base.Base;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description :全局异常处理
 * @auhor:lyc
 * @Date:2019/10/30 23:44
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "core/error/error";

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e)  {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        e.printStackTrace();
        return mav;
    }

    @ExceptionHandler(value = {ServerException.class})
    public Base ServerExceptionHandler(ServerException e)  {
        e.printStackTrace();
        return Base.fail(e.getMessage());
    }
}
