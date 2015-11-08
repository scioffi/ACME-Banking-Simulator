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


    public CheckingAccount(String pin) {
        super(pin);
    }

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
