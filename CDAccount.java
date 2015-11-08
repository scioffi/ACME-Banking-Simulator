/* CDAccount.java
 * Version:
 *  $Id$
 * Revision:
 *  $Log$
 */

/**
 * @author Michael Incardona mji8299
 */
public class CDAccount extends Account {

    private static final double INTEREST_RATE = .05/12;
    public static final double MINIMUM_BALANCE = 500;

    public CDAccount(String pin, double startingBalance, String id) throws IllegalArgumentException {
        super(pin, 0, id);
        if (startingBalance < getMinimumBalance())
            throw new IllegalArgumentException("Can't open a certificate of deposit account with" +
                    " less than the minimum balance.");
        this.setBalance(startingBalance);
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public double getMonthlyPenalty() {
        return 0;
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

}