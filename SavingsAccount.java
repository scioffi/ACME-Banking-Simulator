/* 
 * SavingsAccount.java
 */

/**
 * @author Michael Incardona mji8299
 * @author Steven Cioffi scc3459
 */
public class SavingsAccount extends Account implements Withdrawable {

    /** The minimum balance for all savings accounts. */
    public static final double MINIMUM_BALANCE = 200;
    /** The interest rate for all accounts which meet the minimum balance. */
    public static final double INTEREST_RATE = 0.005 / 12;

    /**
     * @see Account#Account
     */
    public SavingsAccount(String id, String pin, double startingBalance) {
        super(id, pin, startingBalance);
    }

    /**
     * @see SavingsAccount#MINIMUM_BALANCE
     * @return
     */
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    /**
     * $10 if this account has at least a $10 balance; 10% of this account's balance otherwise.
     * @see Account#getMonthlyPenalty
     */
    @Override
    public synchronized double getMonthlyPenalty() {
        if (getBalance() > 10)
            return 10;
        return 0.1 * this.getBalance();
    }

    /**
     * If this account has less than the minimum balance, then its interest rate is 0%.
     * @see SavingsAccount#INTEREST_RATE
     * @see Account#getInterestRate
     */
    @Override
    public synchronized double getInterestRate() {
        if (getBalance() < getMinimumBalance())
            return Account.ZERO;
        return INTEREST_RATE;
    }

    @Override
    public synchronized boolean withdraw(double amt) {
        if (amt <= Account.ZERO || amt > this.getBalance())
            return false;
        this.setBalance(this.getBalance() - amt);
        triggerUpdate();
        return true;
    }
    
    @Override
    public synchronized String toString() {
        return "Savings account #" + getID() + " has balance " + formatCash(getBalance());
    }

    @Override
    public String toTypeString(int minlength) {
        StringBuilder sb = new StringBuilder("Savings");
        while (sb.length() < minlength) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
}
