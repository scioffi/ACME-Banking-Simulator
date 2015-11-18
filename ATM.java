/*
 * ATM.java
 */

import java.util.Observable;

public class ATM extends Observable {
    private static int number = 0;

    public ATM(){

    }
    public static void main(String[] args){
        new Thread(){
            public void run(){
                number++;
                ATMGUI atm = new ATMGUI(Thread.currentThread().getId());
            }
        }.start();
    }
}
