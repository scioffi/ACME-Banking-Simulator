/* CDAccount.java
 */

/**
 * @author Michael Incardona mji8299
 */
public class CDAccount extends Account {

    private static final double INTEREST_RATE = .05/12;
    public static final double MINIMUM_BALANCE = 500;

    public CDAccount(String id, String pin, double startingBalance) throws IllegalArgumentException {
        super(id, pin, ZERO);
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
        return Account.ZERO;
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

}
