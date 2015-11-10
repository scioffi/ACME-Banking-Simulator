import java.util.SortedSet;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank {

    // construct by passing in a comparator that compares account numbers
    private SortedSet<Account> accounts;

    public static void main(String[] args) {
        new Runnable() {
            public void run() {
                BankGUI b = new BankGUI();
            }
        };
    }

    // bool fillFromFile(String name)
    // Account getAccount(String id)

}