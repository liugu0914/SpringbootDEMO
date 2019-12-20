package com.jiopeel.core.config.exception;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.base.StateCode;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description :重写ErrorController 实现404错误监控
 * @auhor:lyc
 * @Date:2019/10/30 23:47
 */
@Controller
public class HttpException implements ErrorController {

    private static final String ERROR_PATH = "core/error/error";

    private static final String STATUS = "status";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * 针对text/html请求的错误
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH,produces = MediaType.TEXT_HTML_VALUE)
    public String handleHTMLError(HttpServletRequest request) {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        request.setAttribute(STATUS, statusCode);
        return "error/error";
    }

    /**
     * 针对json请求的错误
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH,produces = MediaType.APPLICATION_JSON_VALUE)
    public Base handleJSONError(HttpServletRequest request) {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Base base=new Base();
        switch (statusCode){
            case 400:
                base.setStateCode(StateCode.BADREQUEST);
                break;
            case 404:
                base.setStateCode(StateCode.NOTFOUND);
                break;
            case 500:
                base.setStateCode(StateCode.ERROR);
                break;
        }
        base.setResult(false);
        return base;
    }

}
