package com.jiopeel;

public class Card implements  Runnable{

    private Acount acount=new Acount();

    private  String name;

    public  Card(String name,Acount acount){
        this.name=name;
        this.acount=acount;
    }


    @Override
    public void run() {
        for(int i=0;i<10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            acount.addAcount(name,100);
        }
    }
}
