import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Represents a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank extends Observable implements Observer {

    private ArrayList<Account> accounts;

    private final static String ACCOUNT_CHECKING_STRING = "x";
    private final static String ACCOUNT_SAVING_STRING = "s";
    private final static String ACCOUNT_CD_STRING = "c";
    
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
        ArrayList<String> cmds = getLinesFromFile(batchFile);
        for (String cmd : cmds) {
            cmd = cmd.toLowerCase();    // doesn't change ArrayList contents
            String[] words = cmd.split(" ");
            boolean success = false;
            switch (words[0]) {
            case "o":
                success = batchOpenAccount(words);
                break;
            case "c":
                success = batchCloseAccount(words);
                break;
            case "d":
                success = batchDeposit(words);
                break;
            case "w":
                success = batchWithdraw(words);
                break;
            case "a":
                success = batchApplyCharges(words);
                break;
            }
            System.out.println(cmd + " " + (success ? "SUCCEEDED" : "FAILED"));
        }
    }
    
    private boolean batchOpenAccount(String[] args) {
        if (args.length != 5) {
            return false;
        }
        double bal;
        try {
            bal = Double.parseDouble(args[4]);
        } catch (NumberFormatException e) {
            return false;
        }
        return addAccount(args[1], args[2], args[3], bal);
    }
    
    private boolean batchCloseAccount(String[] args) {
        if (args.length != 2) {
            return false;
        }
        return removeAccount(args[1]);
    }
    
    private boolean batchDeposit(String[] args) {
        if (args.length != 3) {
            return false;
        }
        Account a = getAccount(args[1]);
        if (a == null) {
            return false;
        }
        double amt;
        try {
            amt = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        a.deposit(amt);
        return true;
    }
    
    private boolean batchWithdraw(String[] args) {
        if (args.length != 3) {
            return false;
        }
        Account a = getAccount(args[1]);
        if (a == null || !(a instanceof Withdrawable)) {
            return false;
        }
        double amt;
        try {
            amt = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        ((Withdrawable)a).withdraw(amt);
        return true;
    }
    
    private boolean batchApplyCharges(String[] args) {
        if (args.length != 1) {
            return false;
        }
        for (Account a : accounts) {
            a.applyCharges();
        }
        return true;
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
        ArrayList<String> lines = getLinesFromFile(bankFile);
        if (lines == null) {
            return false;
        }
        for (String line : lines) {
            try {
                String[] args = line.split(" ");
                if (args.length != 4) {
                    continue;
                }
                double balance = Double.parseDouble(args[3]);
                withoutError &= addAccount(args[1], args[0], args[2], balance);
            } catch (NumberFormatException e) {
                withoutError = false;
            }
        }
        return withoutError;
    }
    
    ArrayList<String> getLinesFromFile(String fname) {
        ArrayList<String> lines = new ArrayList<>();
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(fname));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines.add(line);
            }
        } catch (FileNotFoundException | NoSuchElementException | IllegalStateException e) {
            return null;
        } finally {
            if (sc != null)
                sc.close();
        }
        return lines;
    }
    
    public Account getAccount(String id) {
        for (Account a : accounts) {
            if (a.getID().equals(id)) {
                return a;
            }
        }
        return null;
    }
    
    public boolean hasAccount(String id) {
        return getAccount(id) != null;
    }
    
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    
    private void triggerUpdate() {
        setChanged();
        notifyObservers();
    }
    
    private boolean removeAccount(String id) {
        Account a = getAccount(id);
        if (a == null) {
            return false;
        }
        a.deleteObservers();
        return accounts.remove(a);
    }
    
    private boolean addAccount(String type, String id, String pin, double balance) {
        Account act;
        if (hasAccount(id)) {
            return false;
        }
        try {
            // try to create a new account of the appropriate type
            switch (type.toLowerCase()) {
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
