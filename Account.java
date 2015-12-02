/*
 * Account.java
 */

import java.text.DecimalFormat;
import java.util.Observable;

/**
 * A bank account.
 * @author Michael Incardona mji8299
 * @author Steven Cioffi scc3459
 */
public abstract class Account extends Observable {
    
    /** Floating-point constant used to represent zero dollars and zero cents. */
    public static final double ZERO = 0;
    
    /** Used to format dollar amounts for output. */
    private static DecimalFormat df = new DecimalFormat("$#0.00");
    
    /** This account's ID. */
    private final String id;
    /** This PIN number for this account. */
    private final String pin;
    /** The amount of money in this account. */
    private double balance;

    /**
     * Creates a new Account with the specified PIN, balance, and ID.
     * @param pin the pin number string for this account
     * @param startingBalance the initial balance of this account
     * @param id this account's id number string
     * @throws IllegalArgumentException if the starting balance is negative. If this exception is thrown, then
     *                                  this object should be considered to have an undefined state.
     */
    public Account(String id, String pin, double startingBalance) throws IllegalArgumentException {
        if (!isValidPIN(pin)) {
            throw new IllegalArgumentException("Bad PIN format");
        }
        this.pin = pin;
        if (!isValidID(id)) {
            throw new IllegalArgumentException("Bad account number format");
        }
        this.id = id;
        if (startingBalance < ZERO) {
            throw new IllegalArgumentException("Cannot open account with negative balance.");
        }
        this.balance = startingBalance;
    }

    /**
     * Formats a cash value into a string. A '$' is prefixed, and
     * the value is rounded and displayed to 2 decimal places.
     * @param f the cash value to format
     * @return a formatted string
     */
    public static String formatCash(double f) {
        return df.format(f);    
    }

    /**
     * Gets the minimum balance for this account.
     * @return the minimum balance
     */
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
    public synchronized double applyCharges() {
        double amt = getInterestRate() * this.getBalance(); // calculate interest
        if (this.getBalance() < getMinimumBalance())    // apply monthly penalty if balance is too low
            amt -= getMonthlyPenalty();
        setBalance(getBalance() + amt);
        triggerUpdate();
        return amt;
    }

    /**
     * Sets the account's balance. Does NOT notify listeners.
     * @param newBalance the new balance of the account.
     * @return the new balance of the account.
     */
    protected synchronized double setBalance(double newBalance) {
        return this.balance = newBalance;
    }

    /**
     * Gets this account's ID number.
     * @return this account's id number
     */
    public synchronized String getID() {
        return id;
    }

    /**
     * Checks a PIN against this account's PIN to see if they match.
     * @param trypin the pin to check
     * @return true if trypin is valid for this account; false if it is not
     */
    public synchronized boolean matchesPIN(String trypin) {
        return getPIN().equals(trypin);
    }

    /**
     * Gets the PIN for this account.
     * @return the PIN for this account
     */
    protected synchronized String getPIN() {
        return pin;
    }

    /**
     * Gets this account's current balance.
     * @return this account's current balance
     */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Deposits money into this account.
     * @param amt the amount to deposit
     * @return true if the deposit succeeded; false otherwise
     */
    public synchronized boolean deposit(double amt) {
        if (amt <= ZERO)       // cannot deposit a negative amount of money
            return false;
        this.setBalance(this.getBalance() + amt);
        triggerUpdate();
        return true;
    }

    /**
     * Checks whether a PIN number is well-formed.
     * All PINs must be four decimal digits.
     * @param n The PIN to check
     * @return true if the PIN is well-formed; false otherwise
     */
    public static boolean isValidPIN(String n) {
        if (n.length() != 4)    // check that PIN is 4 characters
            return false;
        for (int i = 0; i < 4; i++) {   // check that each character is a digit
            if (!Character.isDigit(n.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether an account number is well-formed.
     * All account numbers must be at least four decimal digits.
     * @param n The account ID to check
     * @return true if the ID is well-formed; false otherwise
     */
    public static boolean isValidID(String n) {
        if (n.length() < 4)     // check that ID is at least 4 characters long
            return false;
        for (int i = 0; i < n.length(); i++) {  // check that each character is a digit
            if (!Character.isDigit(n.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether these accounts have the same ID.
     * @param other The account to check for equality
     * @return true if the accounts are equal; false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Account)) {
            return false;
        }
        Account rhs = (Account)other;
        return rhs.getID().equals(this.getID());
    }

    /**
     * Notifies this account's observers.
     */
    protected void triggerUpdate() {
        setChanged();
        notifyObservers();
    }

    /**
     * Converts this account to its string representation.
     * @return A string representing this account
     */
    @Override
    public abstract String toString();

    /**
     * Gets the human-readable string representation of this account's type, 
     * padded with suffix whitespace to a minimum width.
     * @param minlength the minimum width of the type
     * @return a string representing this account's type
     */
    public abstract String toTypeString(int minlength);
    
}
