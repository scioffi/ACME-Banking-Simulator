/*
 * ATM.java
 */

import java.util.Observable;
import java.util.Observer;

/**
 * A model of an ATM which negotiates account operations.
 * @author Michael incardona mji8299
 * @author Steven Cioffi scc3459
 */
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

    /**
     * Verify whether or not account ID exists.
     * @param id numerical account ID.
     * @return Whether or not account ID exists.
     */
    public boolean validateID(String id) {
        Account a = bank.getAccount(id); // Get account associated with ID.
        if (a == null) {
            return false;
        } else {
            close(); // Close the ATM
            account = a;
            a.addObserver(this);
            return true;
        }
    }

    /**
     * Verify whether the PIN is correct.
     * @param pin PIN number to be verified.
     * @return Whether or not the PIN matches the account.
     */
    public boolean validatePIN(String pin) {
        return account.matchesPIN(pin);
    }

    /**
     * Remove all observers and terminate the ATM session.
     */
    public void closeAll() {
        this.deleteObservers();
        close(); // Close ATM
    }

    /**
     * Close window and delete instance of ATM.
     */
    public void close() {
        if (account == null) // If account does not exist
            return;
        account.deleteObserver(this);
        account = null;
    }

    /**
     * Update the ATM and notify observers.
     */
    private void triggerUpdate() {
        this.setChanged(); // Update ATM
        this.notifyObservers();
    }

    /**
     * Add dollar sign in front of cash.
     * @param numbers money.
     * @return new String containing a dollar sign in front of money.
     */
    public static String formatCash(String numbers) {
        return "$" + digitsToCash(numbers);
    }

    /**
     * Convert numerical value to a money syntax.
     * @param numbers numerical value.
     * @return Syntax.
     */
    public static String digitsToCash(String numbers) {
        String temp = "";
        if (numbers.length() == 0) { // If amount is $0.00
            temp = "0.00";
        } else if (numbers.length() == 1) { // If amount is $0.0x
            temp += "0.0" + numbers;
        } else if(numbers.length() == 2) { // If amount is $0.xx
            temp += "0." + numbers;
        } else { // Any amount greater than $0.99
            temp += numbers.substring(0, numbers.length()-2) + 
                    "." + 
                    numbers.substring(numbers.length()-2, numbers.length());
        }
        return temp;
    }

    /**
     * Deposit funds into the account.
     * @param cash Amount of money to deposit.
     * @return boolean result of deposit success.
     */
    public boolean deposit(double cash) {
        return account.deposit(cash);
    }

    /**
     * Withdraw funds from the account.
     * @param cash Amount of money to withdraw.
     * @return boolean result of withdraw success.
     */
    public boolean withdraw(double cash) {
        if (canWithdraw()) {
            return ((Withdrawable)(account)).withdraw(cash);
        }
        return false;
    }

    /**
     * Determines whther this ATM's account supports withdrawals.
     * @return true if withdrawals are allowed; false if they are not
     */
    public boolean canWithdraw() {
        return (account instanceof Withdrawable);
    }
    
    /**
     * Return the balance of the account.
     * @return account balance.
     */
    public double balance() {
        return account.getBalance();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        triggerUpdate();
    }
}
