import java.util.Observable;
import java.util.SortedSet;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank extends Observable {

    // construct by passing in a comparator that compares account numbers
    private SortedSet<Account> accounts;

    public static void main(String[] args) {
        BankGUI bank = new BankGUI();
    }

    // bool fillFromFile(String name)
    // Account getAccount(String id)

}
