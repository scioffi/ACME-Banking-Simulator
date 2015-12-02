/*
 * BankGUI.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * A graphical interface for a logical Bank.
 * @author Michael Incardona mji8299
 * @author Stephen Cioffi scc3459
 */
public class BankGUI extends JFrame implements Observer {
    
    /** The underlying bank model. */
    private Bank bank;

    /** Scrolling text pane that displays account info. */
    private JTextArea text;
    private JScrollPane listScrollPane;
    
    /** Panel which holds the buttons. */
    private JPanel panelOfButtons;
    /** Button to open an ATM. */
    private JButton btnATM;
    /** Button to force an account update. */
    private JButton btnUpdate;

    /**
     * Create a new instance of a BankGUI interface.
     * @param bank Bank object
     */
    public BankGUI(Bank bank) {
        this.bank = bank;
        bank.addObserver(this);
        
        this.setSize(640, 360);     // easter egg: same size as a windows command prompt
        this.setLocationRelativeTo(null);   // center the window
        
        // add account info area
        text = new JTextArea();
        text.setLineWrap(false);
        text.setEditable(false); // Prevent textfield from being editable by users
        
        listScrollPane = new JScrollPane(text);
        this.getContentPane().add(listScrollPane, BorderLayout.CENTER);
        
        btnATM = new JButton("Open ATM");
        btnATM.addActionListener(e -> newATM());
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> refresh());
        
        panelOfButtons = new JPanel();
        panelOfButtons.setLayout(new FlowLayout(FlowLayout.TRAILING));
        panelOfButtons.add(btnUpdate);
        panelOfButtons.add(btnATM);
        
        this.getContentPane().add(panelOfButtons, BorderLayout.SOUTH);
        
        this.setTitle("Bank: Michael Incardona (mji8299), Stephen Cioffi (scc3459)");
        
        this.update(null, null);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Close the frame and temrinate the thread on logout.
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bank.printSummary();    // print a summary of the current account status
                bank.save();    // save the account data to the bank file
                super.windowClosing(e);
            }
        });
        
        this.setVisible(true);
    }

    /**
     * Create and launch a new ATM and ATMGUI instance.
     */
    private void newATM() {
        new Thread() {
            public void run() {
                ATM atm = new ATM(bank, this.getId()); // Create a new ATM with the thread ID acting as the unique identifier.
                new ATMGUI(atm); // Launch a new instance of ATM.
            }
        }.start();
    }

    /**
     * Update BankGUI interface with updated account information.
     */
    private void refresh() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Account> accts = bank.getAccounts(); // List of accounts
        for (Account acct : accts) {
            sb.append(acct.toString()).append('\n');    // prints each account's info on a new line
        }
        text.setText(sb.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
    
}
