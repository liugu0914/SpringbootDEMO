package com.jiopeel.config.exception;

import com.jiopeel.base.Base;
import com.jiopeel.base.StateCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description :全局异常处理
 * @auhor:lyc
 * @Date:2019/10/30 23:44
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error/error";

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e)  {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public Base RuntimeExceptionHandler(HttpServletRequest request, Exception e)  {
        return Base.fail(e.getMessage());
    }
}
