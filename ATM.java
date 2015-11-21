/*
 * ATM.java
 */

import java.util.Observable;
import java.util.Observer;

public class ATM extends Observable implements Observer {
    private Account account;
    private Bank bank;

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
    
    public static void main(String[] args) {
        new ATM(new Bank("test.txt", null));
    }

    @Override
    public void update(Observable o, Object arg) {
        triggerUpdate();
    }
}
