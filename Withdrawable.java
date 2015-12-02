/**
 * Represents a store of value that supports the withdraw operation
 * @author Michael Incardona mji8299
 */
public interface Withdrawable {
    /**
     * Withdraw money.
     * @param amt The amount fo money to withdraw
     * @return true if the withdraw succeeded; false otherwise
     */
    boolean withdraw(double amt);
}
