/*
 * ATM.java
 */

import java.util.Observable;
import java.util.Observer;

public class ATM extends Observable implements Observer {
    private Account account;
    private Bank bank;
    public String[] windows = {"login1","login2","home","message","balance","deposit","widthdraw"};

    public ATM(Bank b) {
        bank = b;
    }

    public boolean validateID(String id) {
        Account a = bank.getAccount(id);
        if (a == null) {
            return false;
        } else {
            close();
            account = a;
            a.addObserver(this);
            return true;
        }
    }

    public boolean validatePIN(String pin) {
        return account.matchesPIN(pin);
    }

    public void closeAll() {
        this.deleteObservers();
        close();
    }

    public void close() {
        if (account == null)
            return;
        account.deleteObserver(this);
        account = null;
    }
    
    private void triggerUpdate() {
        this.setChanged();
        this.notifyObservers();
    }

    public String formatCash(String numbers){
        String temp = "";
        if(numbers.length() == 1){
            temp += "0.0" + numbers;
        }
        else if(numbers.length() == 2){
            temp += "0."+ numbers;
        }
        else{
            temp += numbers.substring(0,numbers.length()-2) + "." + numbers.substring(numbers.length()-2,numbers.length());
        }
        return "$" + temp;
    }
    public double returnCash(String str){
        return Double.parseDouble(str.substring(1,str.length()));
    }

    public boolean deposit(Double cash){
        return account.deposit(cash);
    }

    public boolean doesWindowExist(String window){
        for(int i = 0; i < windows.length; i++){
            if(windows[i].equals(window)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        new ATM(new Bank("test.txt", null));
    }

    @Override
    public void update(Observable o, Object arg) {
        triggerUpdate();
    }
}
