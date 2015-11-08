/**
 * Account.java
*/

public abstract class Account {
    private String id;
    private String pin;
    private double balance;

    /**
     * If the PIN is invalid, it is set to 0000
     * @param pin The pin number for this account
     * @throws IllegalArgumentException if the starting balance is negative
     */
    public Account(String pin, double startingBalance) throws IllegalArgumentException {
        if (!isValidPIN(pin)) {
            pin = "0000";
            throw new IllegalArgumentException("Bad PIN format");
        }
        this.id = "0001";
        if (startingBalance < 0) {
            this.balance = 0;
            throw new IllegalArgumentException("Cannot open account with negative balance.");
        }

        this.balance = startingBalance;
    }

    public abstract double getMinimumBalance();

    /**
     * Gets the monthly penalty to be applied if the balance falls below the minimum.
     * This amount may depend on the balance of the account.
     * @return the monthly penalty
     */
    public abstract double getMonthlyPenalty();

    /**
     * Gets the monthly interest rate on this account.
     * This amount may depend on the balance of the account.
     * @return the interest rate
     */
    public abstract double getInterestRate();

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

    public synchronized void deposit(double amt) {
        if (amt <= 0)
            return;
        this.setBalance(this.getBalance() + amt);
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
        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(n.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}