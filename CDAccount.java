/*
 * CDAccount.java
 */

/**
 * A certificate of deposit account.
 * @author Steven Cioffi scc3459
 * @author Michael Incardona mji8299
 */
public class CDAccount extends Account {

    /** The interest rate for all CD accounts. */
    public static final double INTEREST_RATE = .05/12;
    /** The minimum balance for all CD accounts. */
    public static final double MINIMUM_BALANCE = 500;

    /**
     * @see Account#Account
     */
    public CDAccount(String id, String pin, double startingBalance) throws IllegalArgumentException {
        super(id, pin, ZERO);
        if (startingBalance < getMinimumBalance())
            throw new IllegalArgumentException("Can't open a certificate of deposit account with" +
                    " less than the minimum balance.");
        this.setBalance(startingBalance);
    }

    /**
     * @see CDAccount#MINIMUM_BALANCE
     */
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    /**
     * A CD account can never have less than the minimum balance, so this is always $0.
     * @see Account#getMonthlyPenalty
     */
    @Override
    public double getMonthlyPenalty() {
        return ZERO;
    }

    /**
     * @see CDAccount#INTEREST_RATE
     */
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public synchronized String toString() {
        return "Certificate of deposit account #" + getID() + " has balance " + formatCash(getBalance());
    }
    
    @Override
    public String toTypeString(int minlength) {
        StringBuilder sb = new StringBuilder("CD");
        while (sb.length() < minlength) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
}
