package com.jiopeel.sys.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：lyc
 * @description：公共应用bean
 * @date ：2019/12/20 9:47
 */
@Data
public class Common implements Serializable {

    private static final long serialVersionUID = 3690132590084714685L;

    public Common(String id , String text){
        this.id=id;
        this.text=text;
    }

    /**
     * 主id
     */
    private String id;

    private String text;

    private String name;
}
