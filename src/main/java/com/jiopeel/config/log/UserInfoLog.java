package com.jiopeel.config.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class UserInfoLog extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String userInfo="";
        return userInfo;
    }
}
