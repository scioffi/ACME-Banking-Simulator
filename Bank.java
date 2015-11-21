import java.util.ArrayList;
import java.util.Observable;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank extends Observable {

    private ArrayList<Account> accounts;

    private final static String 
    
    /*
     * bankfile format:
     *      one account per line: <id> <type> <pin> <balance>
     *      <type> is CHK, SAV, or COD
     */
    private Bank(String bankFile, String batchFile) {
        accounts = new ArrayList<>();
        fillFromFile(bankFile);
        if (batchFile != null) {
            batchProcess(batchFile);
        }
    }

    /**
     * Executes a batch of commands on this Bank.
     * @param batchFile File containing the instructions to execute
     */
    private void batchProcess(String batchFile) {
        
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

    /**
     * Extracts account information from a file and adds it to this Bank.
     * This method skips accounts which are incorrectly specified in the file, but will return false in that case.
     * @param name The name/path of a file containing account information
     * @return true if all accounts were added successfully; false if there was some sort of error.
     */
    private boolean fillFromFile(String name) {
        
        return true;
    }
    
    public Account getAccount(String id) {
        for (Account a : accounts) {
            if (a.getID().equals(id)) {
                return a;
            }
        }
        return null;
    }
    
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    
    private void triggerUpdate() {
        setChanged();
        notifyObservers();
    }
    
    private boolean addAccount(String type) {
        if (type == "
    }
            
}
