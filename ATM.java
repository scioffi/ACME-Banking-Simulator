/*
 * ATM.java
 */

import java.util.Observable;
import java.io.*;

public class ATM extends Observable {
    private Account account;
    private Bank bank;

    public ATM(Bank b){
        bank = b;
        ATM atm = this;

        new Thread(){
            public void run(){
                new ATMGUI(atm,Thread.currentThread().getId());
            }
        }.start();
    }

    public boolean validateID(String id){
        Account a = bank.getAccount(id);
        if(a == null){
            return false;
        }
        else{
            account = a;
            return true;
        }
    }

    public static void main(String[] args){
        new ATM(new Bank("test.txt",null));
    }
}
