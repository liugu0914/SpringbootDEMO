package com.jiopeel;

public class thead {

    public static void main(String[] args) {
        Acount acount=new Acount();

        Card card=new Card("Card",acount);

        Paper paper=new Paper("存折",acount);

        Thread thread1 = new Thread(card);

        Thread thread2 = new Thread(paper);

        thread1.start();

        thread2.start();
    }
}
