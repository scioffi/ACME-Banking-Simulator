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
        // output
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
            switch (words[0]) {
            case "o":
                batchOpenAccount(cmd, words);
                break;
            case "c":
                batchCloseAccount(cmd, words);
                break;
            case "d":
                batchDeposit(cmd, words);
                break;
            case "w":
                batchWithdraw(cmd, words);
                break;
            case "a":
                batchApplyCharges(cmd, words);
                break;
            }
        }
    }
    
    private void batchOpenAccount(String cmd, String[] args) {
        boolean success;
        do {
            if (args.length != 5) {
                success = false;
                break;
            }
            double bal;
            try {
                bal = Double.parseDouble(args[4]);
            } catch (NumberFormatException e) {
                success = false;
                break;
            }
            success = addAccount(args[1], args[2], args[3], bal);
        } while(false);
        System.out.println(cmd + " " + (success ? "SUCCEEDED" : "FAILED"));
    }
    
    private void batchCloseAccount(String cmd, String[] args) {
        boolean success;
        do {
            if (args.length != 2) {
                success = false;
                break;
            }
            success = removeAccount(args[1]);
        } while(false);
        System.out.println(cmd + " " + (success ? "SUCCEEDED" : "FAILED"));
    }
    
    private void batchDeposit(String cmd, String[] args) {
        boolean success;
        Account a = null;
        do {
            if (args.length != 3) {
                success = false;
                break;
            }
            a = getAccount(args[1]);
            if (a == null) {
                success = false;
                break;
            }
            double amt;
            try {
                amt = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                success = false;
                break;
            }
            a.deposit(amt);
            success = true;
        } while (false);
        System.out.println(cmd + " " + 
                (success ? "SUCCEEDED New Balance: " + Account.formatCash(a.getBalance()) : "FAILED"));
    }
    
    private void batchWithdraw(String cmd, String[] args) {
        boolean success;
        Account a = null;
        do {
            if (args.length != 3) {
                success = false;
                break;
            }
            a = getAccount(args[1]);
            if (a == null || !(a instanceof Withdrawable)) {
                success = false;
                break;
            }
            double amt;
            try {
                amt = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                success = false;
                break;
            }
            ((Withdrawable)a).withdraw(amt);
            success = true;
        } while(false);
        System.out.println(cmd + " " +
                (success ? "SUCCEEDED New Balance: " + Account.formatCash(a.getBalance()) : "FAILED"));
    }
    
    private void batchApplyCharges(String cmd, String[] args) {
        if (args.length != 1) {
            return;
        }
        System.out.println("=========== Interest Report ===========");
        System.out.println("Account\t\tAdjustment\t\tNew Balance");
        System.out.println("-------\t\t----------\t\t-----------");
        for (Account a : accounts) {
            double change = a.applyCharges();
            System.out.println(a.getID() + "\t\t" +
                    Account.formatCash(change) + "\t\t" +
                    Account.formatCash(a.getBalance()));
        }
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
    
    private ArrayList<String> getLinesFromFile(String fname) {
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
