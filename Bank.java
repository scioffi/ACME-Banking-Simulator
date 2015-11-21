import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank extends Observable implements Observer {

    private ArrayList<Account> accounts;

    private final static String ACCOUNT_CHECKING_STRING = "CHK";
    private final static String ACCOUNT_SAVING_STRING = "SAV";
    private final static String ACCOUNT_CD_STRING = "COD";
    
    private String bankFile;
    
    public Bank(String bankFile, String batchFile) {
        this.bankFile = bankFile;
        accounts = new ArrayList<>();
        fillFromFile();
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
            Bank bankModel = new Bank(args[0], null);
            BankGUI bGUI = new BankGUI(bankModel);
            bankModel.notifyObservers(null);
        }
    }

    /**
     * Extracts account information from the bank file and adds it to this Bank.
     * This method skips accounts which are incorrectly specified in the file, but will return false in that case.
     * @return true if all accounts were added successfully; false if there was some sort of error.
     */
    private boolean fillFromFile() {
        /*
         * bankfile format:
         *      one account per line: <id> <type> <pin> <balance>
         *      <type> is CHK, SAV, or COD
         */
        boolean withoutError = true;
        Scanner sc = null;
        try {
            FileReader fread = new FileReader(bankFile);
            sc = new Scanner(fread);
            while (sc.hasNextLine()) {
                try {
                    String line = sc.nextLine();
                    String[] args = line.split(" ");
                    if (args.length != 4) {
                        continue;
                    }
                    double balance = Double.parseDouble(args[3]);
                    withoutError &= addAccount(args[1], args[0], args[2], balance);
                } catch (NumberFormatException e) {
                    //noinspection UnnecessaryContinue
                    continue;
                }
            }
            return withoutError;
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (sc != null)
                sc.close();
        }
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
    
    private boolean addAccount(String type, String id, String pin, double balance) {
        Account act;
        try {
            // try to create a new account of the appropriate type
            switch (type) {
            case ACCOUNT_CHECKING_STRING:
                act = new CheckingAccount(id, pin, balance);
                break;
            case ACCOUNT_SAVING_STRING:
                act = new SavingsAccount(id, pin, balance);
                break;
            case ACCOUNT_CD_STRING:
                act = new CDAccount(id, pin, balance);
                break;
            default:
                return false;
            }
            // add the new account
            accounts.add(act);
            act.addObserver(this);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        triggerUpdate();
    }
}
