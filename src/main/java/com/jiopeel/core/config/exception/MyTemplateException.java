package com.jiopeel.core.config.exception;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.Writer;

/**
 * @Description :freemaker 自定义的异常处理器
 * @auhor: lyc
 * @Date: 2020/7/14 23:34
 */
@Slf4j
public class MyTemplateException implements TemplateExceptionHandler {


    @Override
    public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
        log.warn("[Freemarker Error: " + te.getMessage() + "]");
        String missingVariable = "undefined";
        try {
            String[] tmp = te.getMessageWithoutStackTop().split("\n");
            if (tmp.length > 1)
                tmp = tmp[1].split(" ");
            if (tmp.length > 1)
                missingVariable = tmp[1];

            out.write("[出错了，请联系网站管理员：${ " + missingVariable
                    + "}]");
            log.error("[出错了，请联系网站管理员]", te);
            throw new ServerException(te.getMessage());
        } catch (Exception e) {
            throw new TemplateException(
                    "Failed to print error message. Cause: " + e, env);
        }
    }
}
