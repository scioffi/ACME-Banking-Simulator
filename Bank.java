import java.util.SortedSet;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Steven Cioffi
 */
public class Bank {

    // construct by passing in a comparator that compares account numbers
    private SortedSet<Account> accounts;

    public static void main(String[] args) {
        System.out.println("hello world");
        BankGUI b = new BankGUI();
    }

}