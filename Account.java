/*
 * Account.java
 */

import java.text.DecimalFormat;
import java.util.Observable;

public abstract class Account extends Observable {
    
    public static final double ZERO = 0;
    
    private static DecimalFormat df = new DecimalFormat("$#0.00");
    
    private final String id;
    private final String pin;
    private double balance;

    /**
     * If the PIN is invalid, it is set to 0000
     * @param pin The pin number string for this account
     * @param startingBalance The initial balance of this account
     * @param id This account's id number string
     * @throws IllegalArgumentException if the starting balance is negative
     */
    public Account(String id, String pin, double startingBalance) throws IllegalArgumentException {
        if (!isValidPIN(pin)) {
            this.pin = "0000";
            this.id = "0000";
            throw new IllegalArgumentException("Bad PIN format");
        }
        this.pin = pin;
        if (!isValidID(id)) {
            this.id = "0000";
            throw new IllegalArgumentException("Bad account number format");
        }
        this.id = id;
        if (startingBalance < 0) {
            this.balance = 0;
            throw new IllegalArgumentException("Cannot open account with negative balance.");
        }
        this.balance = startingBalance;
    }
    
    public static String formatCash(double f) {
        return df.format(f);
    }
    
    public abstract double getMinimumBalance();

    /**
     * Gets the monthly penalty to be applied if the balance falls below the minimum.
     * This amount may depend on the balance of the account.
     * This method does not check that the balance is below the minimum.
     * @return the monthly penalty
     */
    public abstract double getMonthlyPenalty();

    /**
     * Gets the monthly interest rate on this account.
     * This amount may depend on the balance of the account.
     * @return the interest rate
     */
    public abstract double getInterestRate();

    /**
     * Applies one month of interest and penalties to this account.
     * Both interest and penalties are applied based on the initial account balance.
     */
    public double applyCharges() {
        double amt = getInterestRate() * this.getBalance();
        if (this.getBalance() < getMinimumBalance())
            amt -= getMonthlyPenalty();
        setBalance(getBalance() + amt);
        triggerUpdate();
        return amt;
    }

    protected synchronized double setBalance(double newBalance) {
        return this.balance = newBalance;
    }

    public synchronized String getID() {
        return id;
    }

    public synchronized boolean matchesPIN(String trypin) {
        return getPIN().equals(trypin);
    }

    protected synchronized String getPIN() {
        return pin;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized boolean deposit(double amt) {
        if (amt <= 0)
            return false;
        this.setBalance(this.getBalance() + amt);
        triggerUpdate();
        return true;
    }

    public static boolean isValidPIN(String n) {
        if (n.length() != 4)
            return false;
        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(n.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidID(String n) {
        if (n.length() < 4)
            return false;
        for (int i = 0; i < n.length(); i++) {
            if (!Character.isDigit(n.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Account)) {
            return false;
        }
        Account rhs = (Account)other;
        return rhs.getID().equals(this.getID());
    }

    protected void triggerUpdate() {
        setChanged();
        notifyObservers();
    }
    
    @Override
    public String toString() {
        return "Account #" + getID() + " has balance " + formatCash(getBalance());
    }
    
}
