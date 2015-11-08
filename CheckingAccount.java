/* CheckingAccount.java
 * Version:
 *  $Id$
 * Revision:
 *  $Log$
 */

/**
 * @author Michael Incardona mji8299
 */
public class CheckingAccount extends Account implements Withdrawable {

    public static final double MINIMUM_BALANCE = 50;
    private static final double INTEREST_RATE = 0;

    public CheckingAccount(String pin, double startingBalance, String id) {
        super(pin, startingBalance, id);
    }

    @Override
    public synchronized double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public synchronized double getMonthlyPenalty() {
        if (this.getBalance() > 5.0)
            return 5.0;
        else
            return getBalance() * 0.1;
    }

    @Override
    public synchronized double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public synchronized void withdraw(double amt) {
        if (amt <= 0 || amt > this.getBalance())
            return;
        this.setBalance(this.getBalance() - amt);
    }
}
