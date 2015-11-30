/* 
 * CheckingAccount.java
 */

/**
 * A checking account.
 * @author Steven Cioffi scc3459
 * @author Michael Incardona mji8299
 */
public class CheckingAccount extends Account implements Withdrawable {

    /** The interest rate for all checking accounts. */
    public static final double MINIMUM_BALANCE = 50;
    /** The minimum balance for all checking accounts. */
    public static final double INTEREST_RATE = Account.ZERO;

    /**
     * @see Account#Account
     */
    public CheckingAccount(String id, String pin, double startingBalance) throws IllegalArgumentException {
        super(id, pin, startingBalance);
    }

    /**
     * @see CheckingAccount#MINIMUM_BALANCE
     */
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    /**
     * $5 if this account has at least a $5 balance; otherwise, 10% of this account's balance.
     * @see Account#getMonthlyPenalty
     */
    @Override
    public synchronized double getMonthlyPenalty() {
        if (this.getBalance() > 5.0)
            return 5.0;
        else
            return getBalance() * 0.1;
    }

    /**
     * @see CheckingAccount#INTEREST_RATE
     */
    @Override
    public synchronized double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public synchronized boolean withdraw(double amt) {
        if (amt <= 0 || amt > this.getBalance())
            return false;
        this.setBalance(this.getBalance() - amt);
        triggerUpdate();
        return true;
    }
    
    @Override
    public synchronized String toString() {
        return "Checking account " + getID() + " has balance " + formatCash(getBalance());
    }

    @Override
    public String toTypeString(int minlength) {
        StringBuilder sb = new StringBuilder("Checking");
        while (sb.length() < minlength) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
}
