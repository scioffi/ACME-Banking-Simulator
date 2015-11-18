/* SavingsAccount.java
 */

/**
 * @author Michael Incardona mji8299
 */
public class SavingsAccount extends Account implements Withdrawable {

    public static final double MINIMUM_BALANCE = 200;
    private static final double INTEREST_RATE = 0.005 / 12;

    public SavingsAccount(String pin, double startingBalance, String id) {
        super(pin, startingBalance, id);
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public synchronized double getMonthlyPenalty() {
        if (getBalance() > 10)
            return 10;
        return 0.1 * this.getBalance();
    }

    @Override
    public synchronized double getInterestRate() {
        if (getBalance() < getMinimumBalance())
            return Account.ZERO;
        return INTEREST_RATE;
    }

    @Override
    public synchronized void withdraw(double amt) {
        if (amt <= Account.ZERO || amt > this.getBalance())
            return;
        this.setBalance(this.getBalance() - amt);
    }

}
