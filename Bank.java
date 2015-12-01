/*
 * Bank.java
 */

import java.io.*;
import java.util.*;

/**
 * Represents a bank with zero or more accounts.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class Bank extends Observable implements Observer {

    /** Message to display when a batch command succeededs. */
    private static final String SUCCESS = "SUCCEEDED";
    /** Message to display when a batch command failed. */
    private static final String FAIL = "FAILED";
    
    /** All of the accounts registered at this bank. */
    private ArrayList<Account> accounts;

    /** String used to represent a checking account in bank file input. */
    private final static String ACCOUNT_CHECKING_STR = "x";
    /** String used to represent a savings account in bank file input. */
    private final static String ACCOUNT_SAVINGS_STR = "s";
    /** String used to represent a CD account in bank file input. */
    private final static String ACCOUNT_CD_STR = "c";
    
    /** Name/path of a file containing account data. */
    private String bankFile;

    /**
     * Creates a new bank with an optional input file of accounts and an optional batch command file.
     * @param bankFile File containing account data. Can be null or nonexistent.
     * @param batchFile File containing batch commands to run. May be null or nonexistent.
     */
    public Bank(String bankFile, String batchFile) {
        this.bankFile = bankFile;
        accounts = new ArrayList<>();
        fillFromFile();
        if (batchFile != null) {    // if batch commands are provided, execute them
            batchProcess(batchFile);
            printSummary();
            save();
        }
    }

    /**
     * Prints a summary of the account data in this bank.
     */
    public void printSummary() {
        System.out.println("=========== Bank Report ===========");
        System.out.println("Account\t\tType\t\tBalance");
        System.out.println("-------\t\t--------\t---------");
        for (Account a : accounts) {       // print each account's info in columns
            System.out.println(a.getID() + "\t\t" +
                    a.toTypeString(8) + "\t" +
                    Account.formatCash(a.getBalance()));
        }
    }
    
    /**
     * Executes a batch of commands on this Bank.
     * @param batchFile File containing the instructions to execute. May be null or nonexistent.
     */
    private void batchProcess(String batchFile) {
        ArrayList<String> cmds = getLinesFromFile(batchFile);
        if (cmds == null) {     // if no lines in the file
            return;
        }
        for (String cmd : cmds) {
            cmd = cmd.toLowerCase().trim();    // doesn't change ArrayList contents
            if (cmd.length() < 1) {            // skip empty strings/lines
                continue;
            }
            String[] words = cmd.split(" ");    // split the command into it's arguments
            switch (words[0]) {
            case "o":   // open account
                batchOpenAccount(words);
                break;
            case "c":   // close account
                batchCloseAccount(words);
                break;
            case "d":   // deposit to account
                batchDeposit(words);
                break;
            case "w":   // withdraw from account
                batchWithdraw(words);
                break;
            case "a":   // apply interest/penalties
                batchApplyCharges(words);
                break;
            }
            triggerUpdate();
        }
    }

    /**
     * Opens a new account.
     * @param args batch command string broken up by arguments. args[0] is the command letter.
     */
    private void batchOpenAccount(String[] args) {
        double bal = Double.parseDouble(args[4]);
        boolean success = addAccount(args[1], args[2], args[3], bal);
        // id, cmd letter, type, fail
        System.out.println(args[2] + " " + args[0] + " " + args[1] + " " + (success?SUCCESS:FAIL));
    }

    /**
     * Close an existing account.
     * @param args batch command string broken up by arguments. args[0] is the command letter.
     */
    private void batchCloseAccount(String[] args) {
        boolean success = removeAccount(args[1]);
        // id, cmd letter, fail
        System.out.println(args[1] + " " + args[0] + " " + (success?SUCCESS:FAIL));
    }

    /**
     * Make a deposit to an account.
     * @param args batch command string broken up by arguments. args[0] is the command letter.
     */
    private void batchDeposit(String[] args) {
        boolean success;
        Account a = getAccount(args[1]);
        if (a == null) {    // if account does not exist, fail
            success = false;
        } else {
            double amt;
            amt = Double.parseDouble(args[2]);
            a.deposit(amt);     // deposit the provided dollar amount
            success = true;
        }
        // id, cmd letter, fail, balance
        System.out.print(args[1] + " " + args[0] + " ");
        if (success) {
            System.out.println(SUCCESS + " " + "New balance: " + Account.formatCash(a.getBalance()));
        } else {
            System.out.println(FAIL);
        }
    }

    /**
     * Withdraw from an account.
     * @param args batch command string broken up by arguments. args[0] is the command letter.
     */
    private void batchWithdraw(String[] args) {
        boolean success;
        Account a = getAccount(args[1]);
        if (a == null || !(a instanceof Withdrawable)) {
            success = false;
        } else {
            double amt = Double.parseDouble(args[2]);
            ((Withdrawable)a).withdraw(amt);
            success = true;
        }
        // id, cmd letter, fail, balance
        System.out.print(args[1] + " " + args[0] + " ");
        if (success) {
            System.out.println(SUCCESS + " " + "New balance: " + Account.formatCash(a.getBalance()));
        } else {
            System.out.println(FAIL);
        }
    }

    /**
     * Apply one month of interest and penalties to each account.
     * @param args batch command string broken up by arguments. args[0] is the command letter.
     */
    private void batchApplyCharges(String[] args) {
        if (args.length != 1) {
            return;
        }
        System.out.println("=========== Interest Report ===========");
        System.out.println("Account\t\tAdjustment\t\tNew Balance");
        System.out.println("-------\t\t----------\t\t-----------");
        for (Account a : accounts) {
            double change = a.applyCharges();
            System.out.println(a.getID() + "\t\t" +
                    Account.formatCash(change) + "\t\t\t" +
                    Account.formatCash(a.getBalance()));
        }
    }

    /**
     * Opens a new bank with account information from a file, and either opens a GUI or executes batch commands.
     * @param args args[0] = a bank file to read account data from. May be nonexistent.
     *             args[1] = a batch file to process commands from (my be nonexistent). If this is not provided, then
     *                       a bank GUI will open.
     */
    public static void main(String[] args) {
        if (args.length > 2 || args.length < 1) {   // if args are incorrect, print proper arg format
            System.err.println("Usage: java Bank bankFile [batchFile]");
            return;
        }
        
        Bank model = null;
        if (args.length == 2) {     // batch file provided
            model = new Bank(args[0], args[1]);
        } else {
            model = new Bank(args[0], null);    // no batch file provided, so open the GUI
            BankGUI bGUI = new BankGUI(model);
            model.notifyObservers(null);
        }
    }

    /**
     * Extracts account information from the bank file and adds it to this Bank.
     * This method skips accounts which are incorrectly specified in the file.
     * @return true if all accounts were added successfully; false if there was 
     *  some sort of error for at least one account.
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
                String[] args = line.split(" ");    // gets the account data in pieces
                if (args.length != 4) {     // each account must have an id, pin, balance, and type
                    continue;
                }
                double balance = Double.parseDouble(args[3]);   // convert the balance
                // this makes withoutError false if at least one account creation failed
                withoutError &= addAccount(args[1], args[0], args[2], balance);
            } catch (NumberFormatException e) {
                // a dollar amount was invalid, so at least one account failed
                withoutError = false;
            }
        }
        return withoutError;
    }

    /**
     * Gets a list of lines from an input file.
     * @param fname The name/path of the file to read.
     * @return An array of all lines in the file, or null if the file was null or could not be found.
     */
    private ArrayList<String> getLinesFromFile(String fname) {
        if (fname == null) {
            return null;
        }
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

    /**
     * Gets one of the accounts in this bank.
     * @param id the ID of the account to get
     * @return The account with the given ID, or null if the ID did not correspond to a known account.
     */
    public Account getAccount(String id) {
        for (Account a : accounts) {    // search for the account with the matching ID
            if (a.getID().equals(id)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Checks to see if this bank has a specific open account.
     * @param id The ID of the account to look up.
     * @return true if the account was found at this bank; false otherwise
     */
    public boolean hasAccount(String id) {
        return getAccount(id) != null;
    }

    /**
     * Gets a list of all the accounts open at this bank.
     * @return a list of all open accoutns at this bank
     */
    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    /**
     * Notifies this object's observers.
     */
    private void triggerUpdate() {
        setChanged();
        notifyObservers();
    }

    /**
     * Removes an account from the bank, if it exists.
     * @param id the ID of the account to remove
     * @return true if the account was found and removed; false otherwise
     */
    private boolean removeAccount(String id) {
        Account a = getAccount(id);
        if (a == null) {
            return false;
        }
        a.deleteObservers();    // ensure the account contains no dangling references that prevent garbage collection
        return accounts.remove(a);
    }

    /**
     * Adds an account to this bank.
     * @param type the type of the account to add
     * @param id the ID of the new account
     * @param pin the PIN of the new account
     * @param balance the initial balance of the new account
     * @return true if the addition succeeded; 
     *         false if the account ID exists, could not be created, or could not be added
     */
    private boolean addAccount(String type, String id, String pin, double balance) {
        Account act;
        if (hasAccount(id)) {
            return false;
        }
        try {
            // try to create a new account of the appropriate type
            switch (type.toLowerCase()) {
            case ACCOUNT_CHECKING_STR:
                act = new CheckingAccount(id, pin, balance);
                break;
            case ACCOUNT_SAVINGS_STR:
                act = new SavingsAccount(id, pin, balance);
                break;
            case ACCOUNT_CD_STR:
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
        triggerUpdate();
        return true;
    }

    /**
     * Writes account data to bank file.
     * NOTE: If a 0 is present as the second decimal value, it will be omitted.
     */
    public void save(){
        try {
            FileWriter fw = new FileWriter(bankFile);
            for(Account a : accounts){
                String type;
                if(a instanceof SavingsAccount){
                    type = "s";
                } else if(a instanceof CheckingAccount){
                    type = "x";
                }
                else{
                    type = "c";
                }
                fw.write(a.getID() + " " + type + " " + a.getPIN() + " " + a.getBalance() + "\n");
            }
            fw.close();
        } catch(IOException e){
            System.err.println("ERROR: Unable to write to file. Please contact your system administrator.");
        }
    }

    /**
     * Notifies this bank's observers that account information has changed.
     */
    @Override
    public void update(Observable o, Object arg) {
        triggerUpdate();
    }
    
}
