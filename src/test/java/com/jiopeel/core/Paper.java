package com.jiopeel.core;

public class Paper implements Runnable {

    private Acount acount=new Acount();

    private  String name;

    public  Paper(String name,Acount acount){
        this.name=name;
        this.acount=acount;
    }


    @Override
    public  void run() {
        for(int i=0;i<10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            acount.subAcount(name,100);
        }
    }
}
