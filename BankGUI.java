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
    
    public BankGUI(Bank bank) {

        /*
        // try to make the window match the look of the current platform
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException |
                IllegalAccessException |
                ClassNotFoundException |
                InstantiationException e) {
            // handle exception
            // do nothing
        }
         */
        
        this.bank = bank;
        bank.addObserver(this);
        
        this.setSize(640, 360);     // same size as a windows command prompt
        this.setLocationRelativeTo(null);   // center the window
        
        text = new JTextArea();
        text.setLineWrap(false);
        text.setEditable(false);
        
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
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bank.printSummary();
                bank.save();
                super.windowClosing(e);
            }
        });
        
        this.setVisible(true);
    }
    
    private void newATM() {
        ATM atm = new ATM(bank);
        new Thread() {
            public void run() {
                new ATMGUI(atm, this.getId());
            }
        }.start();
    }
    
    private void refresh() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Account> accts = bank.getAccounts();
        for (Account acct : accts) {
            sb.append(acct.toString()).append('\n');
        }
        text.setText(sb.toString());
    }
    
    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
    
}
