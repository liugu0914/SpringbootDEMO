package com.jiopeel.core.config.exception;

import com.jiopeel.core.util.BaseUtil;

public class Assert {
    public static void isNull(Object object, String msg) {
        if (BaseUtil.empty(object))
            throw new ServerException(msg);
    }
}
