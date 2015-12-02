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
    /** The account currently accessed by this ATM. */
    private Account account;
    /** The bank this ATM is tied to. */
    private Bank bank;
    /** Unique ATM ID. */
    private long id;

    /**
     * Creates a new ATM model.
     * @param b The bank this ATM is tied to
     * @param ATMID The unique ID of this ATM
     */
    public ATM(Bank b, long ATMID) {
        bank = b;
        id = ATMID;
    }

    /**
     * Gets the unique ID of this ATM.
     * @return unique ATM ID
     */
    public long getATMID() {
        return id;
    }

    /**
     * Verify whether or not account ID exists.
     * @param id numerical account ID.
     * @return Whether or not account ID exists.
     */
    public boolean validateID(String id) {
        Account a = bank.getAccount(id); // Get account associated with ID
        if (a == null) {
            return false;
        } else {
            logout(); // Close the ATM
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
        this.deleteObservers(); // clean up dangling references to clear the way for garbage collection
        logout(); // Close ATM
    }

    /**
     * Logs out of the account in this ATM.
     */
    public void logout() {
        if (account == null) // If account does not exist
            return;
        account.deleteObserver(this);   // remove the account's reference to this ATM
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
     * Convert numerical value to a money syntax (dollars and cents)
     * @param numbers string of digits
     * @return money string of the form "X.XX"
     */
    public static String digitsToCash(String numbers) {
        String temp = "";
        // pas the numerical input to three digits
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
     * @return true if deposit was successful
     */
    public boolean deposit(double cash) {
        return account.deposit(cash);
    }

    /**
     * Withdraw funds from the account.
     * @param cash Amount of money to withdraw.
     * @return true if withdraw was successful
     */
    public boolean withdraw(double cash) {
        if (canWithdraw()) {
            return ((Withdrawable)(account)).withdraw(cash);
        }
        return false;
    }

    /**
     * Determines whether this ATM's account supports withdrawals.
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
