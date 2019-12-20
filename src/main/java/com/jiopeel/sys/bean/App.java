package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @description：应用表
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
public class App extends Bean implements Serializable {

    private static final long serialVersionUID = 5731452336384975505L;
    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用标识
     */
    private String shortname;

    /**
     * 是否可用 0否1是
     */
    private String enable;

}
