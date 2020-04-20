package com.jiopeel.core.bean;

import lombok.Data;

import java.util.List;

@Data
public class Page<E> {

    /**
     * 第几页
     */
    private int pageNum;
    /**
     * 每页大小
     */
    private int pageSize;
    /**
     * 开始行
     */
    private int startRow;
    /**
     * 结束行
     */
    private int endRow;
    /**
     * 总行数
     */
    private int total;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 返回集合
     */
    private List<E> result;

    public Page(int pageNum, int pageSize) {
        pageNum=pageNum+1;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.startRow = pageNum > 1 ? (pageNum-1) * pageSize : 0;
        this.endRow = pageNum * pageSize;
    }

    public Page() {
        this(0,20);
    }

    public Page(int pageNum) {
        this(pageNum,20);
    }

}