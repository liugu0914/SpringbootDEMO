package com.jiopeel;

/**
 * 银行账户
 * @author Administrator
 *
 */
public class Acount {
    private int count=0;

    /**
     * 存钱
     * @param money
     */
    public void addAcount(String name,int money) {
                    // 存钱
        count += money;
        System.out.println(name+"...存入："+money+"..."+Thread.currentThread().getName());
        SelectAcount(name);
           }

    /**
     * 取钱
     * @param money
     */
    public void subAcount(String name,int money) {
                    // 先判断账户现在的余额是否够取钱金额
        if(count-money < 0){
            System.out.println("账户余额不足！");
            return;
        }
        // 取钱
        count -= money;
        System.out.println(name+"...取出："+money+"..."+Thread.currentThread().getName());
        SelectAcount(name);
            }

    /**
     * 查询余额
     */
    public void SelectAcount(String name) {
        System.out.println(name+"...余额："+count);
    }
}