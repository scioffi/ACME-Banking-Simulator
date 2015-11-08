/* CheckingAccount.java
 * Version:
 *  $Id$
 * Revision:
 *  $Log$
 */

/**
 * Created by Michael on 2015-11-08.
 */
public class CheckingAccount extends Account implements Withdrawable {

    @Override
    public double getMinimumBalance() {
        return 0;
    }

    @Override
    public double getMonthlyPenalty() {
        return 0;
    }

    @Override
    public double getInterestRate() {
        return 0;
    }

    @Override
    public void deposit(double amt) {

    }

    @Override
    public void withdraw(double amt) {

    }
}
