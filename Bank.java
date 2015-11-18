import java.util.ArrayList;
import java.util.Observable;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank extends Observable {

    private ArrayList<Account> accounts;

    // bankfile format:
    // one account per line: <id> <type> <pin> <balance>
    // <type> is CHK, SAV, or COD
    public Bank(String bankFile, String batchFile) {
        accounts = new ArrayList<>();
        fillFromFile(bankFile);
        if (batchFile != null) {
            batchProcess(batchFile);
        }
    }
    
    public void batchProcess(String batchFile) {
        
    }
    
    public static void main(String[] args) {
        if (args.length > 2 || args.length < 1) {
            System.err.println("Usage: java Bank bankFile [batchFile]");
            return;
        }
        
        if (args.length == 2) {
            Bank bankModel = new Bank(args[0], args[1]);
        } else {
            Bank bankModel = new Bank(args[1], null);
            BankGUI bGUI = new BankGUI(bankModel);
        }
    }

    boolean fillFromFile(String name) {
        
        return true;
    }
    
    Account getAccount(String id) {
        for (Account a : accounts) {
            if (a.getID().equals(id)) {
                return a;
            }
        }
        return null;
    }
    
}
