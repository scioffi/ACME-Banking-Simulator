/**
 * Account.java
*/

public abstract class Account {
    private String id;
    private String pin;
    private double balance;

    /**
     * If the PIN is invalid, it is set to 0000
     * @param pin
     */
    public Account(String pin) {
        if (!isValidPIN(pin))
            pin = "0000";
        else

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