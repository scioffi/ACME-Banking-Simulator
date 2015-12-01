/*
 * ATM.java
 */

import java.util.Observable;
import java.util.Observer;

public class ATM extends Observable implements Observer {
    private Account account;
    private Bank bank;
    private long id;

    public ATM(Bank b, long ATMID) {
        bank = b;
        id = ATMID;
    }

    public long getATMID() {
        return id;
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

    public static String formatCash(String numbers) {
        return "$" + digitsToCash(numbers);
    }
    
    public static String digitsToCash(String numbers) {
        String temp = "";
        if (numbers.length() == 0) {
            temp = "0.00";
        } else if (numbers.length() == 1) {
            temp += "0.0" + numbers;
        } else if(numbers.length() == 2) {
            temp += "0." + numbers;
        } else {
            temp += numbers.substring(0, numbers.length()-2) + 
                    "." + 
                    numbers.substring(numbers.length()-2, numbers.length());
        }
        return temp;
    }

    public boolean deposit(double cash) {
        return account.deposit(cash);
    }
    
    public boolean withdraw(double cash) {
        if (account instanceof Withdrawable) {
            return ((Withdrawable)(account)).withdraw(cash);
        }
        return false;
    }

    public double balance() {
        return account.getBalance();
    }

    @Override
    public void update(Observable o, Object arg) {
        triggerUpdate();
    }
}
