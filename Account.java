/**
 * Account.java
*/

public abstract class Account {
    private String id;
    private String pin;
    private double balance;

    public Account(String pin) {

        balance = 0.0;
    }

    public abstract double getMinimumBalance();

    public abstract double getMonthlyPenalty();

    public abstract double getInterestRate();

    public synchronized String getID() {
        return id;
    }

    public synchronized String getPIN() {
        return pin;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public abstract void deposit(double amt);

    public static boolean isValidPIN(String n) {
        for (
    }
}