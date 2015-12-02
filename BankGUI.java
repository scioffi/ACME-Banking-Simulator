/**
 *  BankGUI.java
 *
 *  @author Stephen Cioffi scc3459
 *  @author Michael Incardona mji8299
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
    
    private Bank bank;

    private JTextArea text;
    private JScrollPane listScrollPane;
    
    private JPanel panelOfButtons;
    private JButton btnATM;
    private JButton btnUpdate;

    /**
     * Create a new instance of a BankGUI interface.
     * @param bank Bank object
     */
    public BankGUI(Bank bank) {
        this.bank = bank;
        bank.addObserver(this);
        
        this.setSize(640, 360);     // same size as a windows command prompt
        this.setLocationRelativeTo(null);   // center the window
        
        text = new JTextArea();
        text.setLineWrap(false);
        text.setEditable(false); // Prevent textfield from being editable by users.
        
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
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Close the frame and temrinate the thread on close.
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bank.printSummary();
                bank.save(); // Flush data to disk.
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
            sb.append(acct.toString()).append('\n');
        }
        text.setText(sb.toString());
    }

    /**
     * When a change is observed, update the GUI.
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
    
}
