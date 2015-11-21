/*
 * ATM.java
 */

import java.util.Observable;
import java.util.Observer;

public class ATM extends Observable implements Observer {
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

    public boolean validatePIN(String pin){
        if(account.matchesPIN(pin)){
            return true;
        }
        else{
            return false;
        }
    }

    public void close(){
        this.deleteObservers();
        account.deleteObserver(this);
    }

    public static void main(String[] args){
        new ATM(new Bank("test.txt",null));
    }

    @Override
    public void update(Observable o, Object arg) {
        o.notifyObservers();
    }
}
