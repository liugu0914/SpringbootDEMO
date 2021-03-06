package com.jiopeel.core.bean;

import com.jiopeel.core.util.BaseUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class Bean implements Serializable {

    private static final long serialVersionUID = -3540383839044057287L;

    /**
     * id
     */
    private String id;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date updtime;

    public void createID() {
        this.id = BaseUtil.getSnowFlakeID();
    }

    public void createTime() {
        this.ctime=new Date();
        this.updtime=new Date();
    }

    public void updTime() {
        this.updtime=new Date();
    }
}
