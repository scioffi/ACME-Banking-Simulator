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

    public Bank(String bankFile, String batchFile) {
        
    }
    
    public static void main(String[] args) {
        if (args.length > 2 || args.length < 1) {
            System.err.println("Usage: java Bank bankFile [batchFile]");
            return;
        }
        String bankFile = args[0];
        String batchFile;
        if (args.length == 2)
            batchFile = args[1];
        else
            batchFile = null;
        
        Bank bankModel = new Bank(bankFile, batchFile);
        
        BankGUI bGUI = new BankGUI(bankModel);
    }

    // bool fillFromFile(String name)
    // Account getAccount(String id)

}
