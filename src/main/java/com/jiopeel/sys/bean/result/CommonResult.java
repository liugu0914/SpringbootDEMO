package com.jiopeel.sys.bean.result;

import com.jiopeel.sys.bean.Common;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description：公共事件返回层 应用于 自定义返回结果集
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Getter
@Setter
public class CommonResult extends Common {

    private static final long serialVersionUID = 3841750295863678698L;

    public CommonResult(String id, String text) {
        super(id, text);
    }
}
