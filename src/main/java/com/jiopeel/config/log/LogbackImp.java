package com.jiopeel.config.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

@Slf4j
public class LogbackImp implements Log {
    private static final String FQCN = LogbackImp.class.getName();

    public LogbackImp(String calzz) {

    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(FQCN, s, e);
        System.err.println(s);
        e.printStackTrace(System.err);
    }

    @Override
    public void error(String s) {
        log.error(FQCN, s);
        System.err.println(s);
    }

    @Override
    public void debug(String s) {
        log.info(s);
    }

    @Override
    public void trace(String s) {
        if (log.isTraceEnabled())
            log.info(s);
    }

    @Override
    public void warn(String s) {
        log.warn(FQCN, s);
    }

}
