/*
 * BankGUI.java
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A graphical ATM interface which sita on top of an underlying ATM model.
 * @author Michael incardona mji8299
 * @author Steven Cioffi scc3459
 */
public class ATMGUI extends JFrame {

    /** Identifier contants for the various atm screens. */
    private static final String SCR_LOGIN_ID = "login1";
    private static final String SCR_LOGIN_PASSWORD = "login2";
    private static final String SCR_DEPOSIT = "deposit";
    private static final String SCR_HOME = "home";
    private static final String SCR_BALANCE = "balance";
    private static final String SCR_WITHDRAW = "withdraw";
    private static final String SCR_DEPOSIT_OK = "deposit_ok";
    private static final String SCR_WITHDRAW_OK = "withdraw_ok";
    private static final String SCR_WITHDRAW_FAIL = "withdraw_fail";
    
    /** The border which surrounds the ATM screens */
    private static final Border BORDER_SCREEN = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    
    /** The currently active ATM screen. */
    private String activeWindow;
    /** The underlying ATM model. */
    private final ATM atm;
    
    /** Text fields used in atm input. */
    private JPasswordField passwordField;
    private JTextField depositCashField;
    private JTextField withdrawCashField;
    private JTextField textField;
    
    private JPanel loginscreen1;
    private JPanel loginscreen2;
    private JPanel sidebar;
    private JPanel homescreen;
    private JPanel homescreen_CD;
    private JPanel depositscreen;
    private JPanel withdrawscreen;
    private JPanel depositokscreen;
    private JPanel withdrawokscreen;
    private JPanel withdrawfailscreen;
    
    private String valuestr = "";
    
    /**
     * Creates an ATM GUI and displays the login screen.
     * @param atm The ATM model this GUI should reflect
     */
    public ATMGUI(ATM atm) {
        this.atm = atm;
        
        this.setTitle("Stephen Cioffi (scc3459) & Michael Incardona (mji8299) | ATM ID: " + atm.getATMID());
        this.setSize(700, 500);

        // create input fields and screens
        this.depositCashField = makeCashField();
        depositCashField.setFocusable(false);
        this.withdrawCashField = makeCashField();
        withdrawCashField.setFocusable(false);
        this.sidebar = makeSidebar();
        this.homescreen = makeHomeScreen(true);
        this.homescreen_CD = makeHomeScreen(false);
        this.loginscreen1 = makeLoginScreen1();
        this.loginscreen2 = makeLoginScreen2();
        this.depositscreen = makeTransactionScreen("Enter an amount to deposit:", depositCashField);
        this.withdrawscreen = makeTransactionScreen("Enter an amount to withdraw:", withdrawCashField);
        this.withdrawscreen.add(withdrawCashField);
        this.depositokscreen = makeResultScreen("Deposit successful.");
        this.withdrawokscreen = makeResultScreen("Withdraw sucessful.");
        this.withdrawfailscreen = makeResultScreen("Withdraw failed. Insufficient funds.");
        
        FlowLayout flow = new FlowLayout();
        
        this.setLayout(flow);
        
        // clean up before the window closes
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                atm.closeAll();
                super.windowClosing(e);
            }
        });
        
        // start on the ID login screen
        setWindow(SCR_LOGIN_ID);
        // use dispose instead of close, or the entire process will die
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);   // center the window on the screen
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Changes the window shown in the ATM screen.
     * @param window Identifier of the new window
     */
    private void setWindow(String window) {
        // clear all components in the atm frame
        getContentPane().removeAll();
        // add the newly selected window to the left section
        switch (window) {
            case SCR_LOGIN_ID:
                add(loginscreen1, BorderLayout.WEST);
                textField.setText("");
                break;
                
            case SCR_LOGIN_PASSWORD:
                add(loginscreen2, BorderLayout.WEST);
                passwordField.setText("");
                break;
                
            case SCR_HOME:
                // if a CD account is loaded, use the greyed-out withdraw option
                if (atm.canWithdraw()) {
                    add(homescreen, BorderLayout.WEST);
                } else {
                    add(homescreen_CD, BorderLayout.WEST);
                }
                break;
                
            case SCR_DEPOSIT:
                add(depositscreen, BorderLayout.WEST);
                depositCashField.setText("$0.00");
                valuestr = "";
                break;
                
            case SCR_DEPOSIT_OK:
                add(depositokscreen, BorderLayout.WEST);
                break;
                
            case SCR_BALANCE:
                add(makeBalanceViewScreen(), BorderLayout.WEST);
                break;
                
            case SCR_WITHDRAW_OK:
                add(withdrawokscreen, BorderLayout.WEST);
                break;
                
            case SCR_WITHDRAW_FAIL:
                add(withdrawfailscreen, BorderLayout.WEST);
                break;
                
            case SCR_WITHDRAW:
                add(withdrawscreen, BorderLayout.WEST);
                withdrawCashField.setText("$0.00");
                valuestr = "";
                break;
                
            default:
                setWindow(activeWindow);
                return;
        }
        // add back the sidebar
        add(sidebar, BorderLayout.EAST);
        // change the window cariabel and redraw the GUI
        activeWindow = window;
        render();
    }

    /**
     * Redraws the GUI so that all components appear updated.
     */
    private void render() {
        // validate orders the layouts, repaint redraws everything
        validate();
        repaint();
    }

    /**
     * Creates a login screen which prompts the user to enter an account ID.
     * @return A login screen with an account field
     */
    private JPanel makeLoginScreen1() {
        JPanel ls = new JPanel();
        ls.setPreferredSize(new Dimension(450, 500));
        ls.setBorder(BORDER_SCREEN);

        JLabel mainlabel = new JLabel("Please enter your account ID:");
        mainlabel.setPreferredSize(new Dimension(430, 70));
        mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        
        ls.add(mainlabel);
        ls.add(textField = makeIDField());
        textField.setFocusable(false);
        return ls;
    }

    /**
     * Creates a login screen which prompts the user to enter an account password.
     * @return A login screen with a password field
     */
    private JPanel makeLoginScreen2() {
        JPanel ls = new JPanel();
        ls.setPreferredSize(new Dimension(450, 500));
        ls.setBorder(BORDER_SCREEN);
        
        JLabel mainlabel = new JLabel("Please enter your PIN:");
        mainlabel.setPreferredSize(new Dimension(430, 70));
        mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        
        ls.add(mainlabel);
        ls.add(passwordField = makePasswordField());
        passwordField.setFocusable(false);
        return ls;
    }

    /**
     * Creates a home screen with balance, deposit, withdraw, and logout options.
     * @param isWithdrawPossible if false, then the withdraw option will be colored grey (unusable) instead of black
     * @return The newly created home screen
     */
    private JPanel makeHomeScreen(boolean isWithdrawPossible) {
        JPanel hs = new JPanel();
        hs.setPreferredSize(new Dimension(450, 500));
        hs.setBorder(BORDER_SCREEN);

        Font labelFont = new Font("sans-serif", 0, 40);

        JLabel l1 = new JLabel("1) Balance Inquiry");
        l1.setFont(labelFont);
        JLabel l2 = new JLabel("2) Deposit Money");
        l2.setFont(labelFont);
        JLabel l3 = new JLabel("3) Withdraw Money");
        l3.setFont(labelFont);
        
        if (!isWithdrawPossible) {
            l3.setForeground(Color.LIGHT_GRAY);     // grey out the withdraw option if using a CD account
        }
        
        JLabel l4 = new JLabel("4) Log Off");
        l4.setFont(labelFont);
        
        hs.setLayout(new BoxLayout(hs, BoxLayout.Y_AXIS));  // order all labels in a column
        
        hs.add(l1);
        hs.add(l2);
        hs.add(l3);
        hs.add(l4);
        
        return hs;
    }

    /**
     * Creates the sidebar with 0-9, OK, Cancel, Clear, and Close buttons.
     * @return The newly created sidebar
     */
    private JPanel makeSidebar() {
        // store all the number buttons in an array, instead of ten local variables
        JButton[] numberBtns = new JButton[10];
        JPanel sb = new JPanel();
        sb.setPreferredSize(new Dimension(250, 500));

        GridLayout grid = new GridLayout(5, 3);     // layout all buttons in a grid
        sb.setLayout(grid);

        for (int i = 0; i < 10; i++) {
            numberBtns[i] = new JButton(Integer.toString(i));   // create number buttons
        }
        
        JButton buttok = new JButton("OK");
        JButton buttcancel = new JButton("Cancel");
        JButton buttclear = new JButton("Clear");
        JButton buttclose = new JButton("Close");

        // set the font on all the buttons
        Font numberFont = new Font("sans-serif", Font.BOLD, 50);

        for (int i = 0; i < 10; i++) {
            numberBtns[i].setFont(numberFont);
        }

        buttok.setFont(new Font("sans-serif", Font.BOLD, 30));
        buttcancel.setFont(new Font("sans-serif", Font.BOLD, 14));
        buttclear.setFont(new Font("sans-serif", Font.BOLD, 16));
        buttclose.setFont(new Font("sans-serif", Font.BOLD, 16));
        
        for (int i = 1; i < 10; i++) {
            sb.add(numberBtns[i]);
        }
        
        sb.add(buttok);
        sb.add(numberBtns[0]);
        sb.add(buttcancel);
        sb.add(buttclear);
        sb.add(new JPanel());   // dummy panel to fill space
        sb.add(buttclose);

        /**
         * Carries out the function of the number buttons on each screen.
         * This same event listener is used for every number button.
         */
        ActionListener numbers = e -> {
            JButton b = (JButton) e.getSource();
            switch(activeWindow) {
            case SCR_LOGIN_ID:
                textField.setText(textField.getText() + b.getText());   // concatenate the number to the id field
                break;
            case SCR_LOGIN_PASSWORD:
                String s = new String(passwordField.getPassword());     // concatenate the number to the password field
                passwordField.setText(s + b.getText());
                break;
            case SCR_HOME:                  // use the number buttons to select options on the home screen
                switch (b.getText()) {      // perform the operation specified by the user
                case "1":
                    setWindow(SCR_BALANCE);
                    break;
                case "2":
                    setWindow(SCR_DEPOSIT);
                    break;
                case "3":
                    if (atm.canWithdraw()) {    // only activte the withdraw screen if the account supports withdrawals
                        setWindow(SCR_WITHDRAW);
                    }
                    break;
                case "4":
                    atm.logout();    // logout and go back to the ID entry login screen
                    setWindow(SCR_LOGIN_ID);
                    break;
                }
                break;
            case SCR_WITHDRAW:
                // user cannot enter leading zeros
                if (valuestr.length() == 0 && b.getText().equals("0"))
                    break;
                valuestr += b.getText();
                withdrawCashField.setText(ATM.formatCash(valuestr));
                render();
                break;
            case SCR_DEPOSIT:
                // user cannot enter leading zeros
                if (valuestr.length() == 0 && b.getText().equals("0"))
                    break;
                valuestr += b.getText();
                depositCashField.setText(ATM.formatCash(valuestr));
                render();
                break;
            }
        };

        // add the event listener to the number buttons
        for (int i = 0; i < 10; i++) {
            numberBtns[i].addActionListener(numbers);
        }

        /**
         * Carries out the function of the ok button on each screen.
         */
        buttok.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_ID:
                if (atm.validateID(textField.getText())) {  // try to validate the ID entered by the user
                    setWindow(SCR_LOGIN_PASSWORD);
                }
                else{
                    textField.setText("");
                }
                break;
            case SCR_LOGIN_PASSWORD:    // try to validate the password entered by the user
                // if the password is correct, login and go to the home screen
                if (atm.validatePIN(new String(passwordField.getPassword()))) {
                    setWindow(SCR_HOME);
                } else {
                    setWindow(SCR_LOGIN_ID);
                }
                break;
            case SCR_DEPOSIT:   // perform a deposit
                double depcash = Double.parseDouble(ATM.digitsToCash(valuestr));
                if (atm.deposit(depcash)) {
                    valuestr = "";
                    setWindow(SCR_DEPOSIT_OK);  // go to the confirmation screen after deposit
                }
                break;
            case SCR_WITHDRAW:
                double withcash = Double.parseDouble(ATM.digitsToCash(valuestr));
                if (atm.withdraw(withcash)) {   // go to either the confirm or error screen after withdraw
                    valuestr = "";
                    setWindow(SCR_WITHDRAW_OK);
                } else {
                    valuestr = "";
                    setWindow(SCR_WITHDRAW_FAIL);
                }
                break;
            case SCR_DEPOSIT_OK:
            case SCR_BALANCE:
            case SCR_WITHDRAW_OK:
            case SCR_WITHDRAW_FAIL:
                setWindow(SCR_HOME);    // return to the home screen after the notification
                break;
            }
        });

        /**
         * Carries out the function of the cancel button on each screen.
         */
        buttcancel.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_PASSWORD:
                setWindow(SCR_LOGIN_ID);    // go back to ID entry screen
                break;
            case SCR_WITHDRAW:
            case SCR_DEPOSIT:
                setWindow(SCR_HOME);        // cancel transaction and go to the home screen
                break;
            }
        });

        /**
         * Carries out the function of the clear button on each screen.
         */
        buttclear.addActionListener(e -> {
            switch (activeWindow) {     // clear the text entry field
            case SCR_LOGIN_ID:
                textField.setText("");
                break;
            case SCR_LOGIN_PASSWORD:
                passwordField.setText("");
                break;
            case SCR_WITHDRAW:
                withdrawCashField.setText("$0.00");
                valuestr = "";
                break;
            case SCR_DEPOSIT:
                depositCashField.setText("$0.00");
                valuestr = "";
                break;
            }
        });
        
        // set the logout button to trigger a window closing event
        buttclose.addActionListener(e -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        
        return sb;
    }

    /**
     * Creates a deposit/withdraw screen with a label and cash field.
     * @param msg The message to display
     * @param cash The cash entry field to display
     * @return The newly screated transaction screen
     */
    private JPanel makeTransactionScreen(String msg, JTextField cash) {
        JPanel ds = new JPanel();
        ds.setPreferredSize(new Dimension(450, 500));
        ds.setBorder(BORDER_SCREEN);
        ds.setLayout(new FlowLayout());

        JLabel lmsg = new JLabel(msg);
        lmsg.setPreferredSize(new Dimension(430, 70));
        lmsg.setFont(new Font("sans-serif", 0, 30));
        ds.add(lmsg);   // add the message label
        ds.add(cash);   // add the cash entry field
        return ds;
    }

    /**
     * Creates a cash entry text field.
     * @return A newly created cash entry field
     */
    private JTextField makeCashField() {
        JTextField f = new JTextField();
        f.setEditable(false);
        f.setPreferredSize(new Dimension(300, 70));
        f.setSize(300, 70);
        f.setBounds(0, 200, 300, 70);
        f.setFont(new Font("sans-serif", Font.BOLD, 30));
        f.setText("$0.00");
        return f;
    }

    /**
     * Creates an account ID entry text field.
     * @return A newly created account ID field
     */
    private JTextField makeIDField() {
        JTextField idf = new JTextField();
        idf.setHorizontalAlignment(JTextField.CENTER);
        idf.setEditable(false);
        idf.setPreferredSize(new Dimension(300, 70));
        idf.setSize(300, 70);
        idf.setBounds(0, 200, 300, 70);
        idf.setFont(new Font("sans-serif", Font.BOLD, 30));
        return idf;
    }

    /**
     * Creates an account password field that hides input.
     * @return A newly created password field
     */
    private JPasswordField makePasswordField() {
        // NOTE: for some reason, you need to add one to the JPasswordField arg to display all characters properly
        JPasswordField pass = new JPasswordField(7);
        pass.setHorizontalAlignment(JTextField.CENTER);
        pass.setEditable(false);
        pass.setPreferredSize(new Dimension(300, 70));
        pass.setSize(300, 70);
        pass.setBounds(0, 200, 300, 70);
        pass.setFont(new Font("sans-serif", Font.BOLD, 30));
        return pass;
    }

    /**
     * Creates a balance view field that shows the ATM's current account balance.
     * @return A newly created balance screen
     */
    private JPanel makeBalanceViewScreen() {
        JPanel bvs = new JPanel();
        bvs.setPreferredSize(new Dimension(450, 500));
        
        bvs.setLayout(new BoxLayout(bvs, BoxLayout.Y_AXIS));
        bvs.setBorder(BORDER_SCREEN);
        
        JLabel balTitle = new JLabel("Current Balance:");
        balTitle.setPreferredSize(new Dimension(430, 70));
        balTitle.setFont(new Font("sans-serif", 0, 30));
        
        JLabel balVal = new JLabel(Account.formatCash(atm.balance()));
        balVal.setPreferredSize(new Dimension(430, 70));
        balVal.setFont(new Font("sans-serif", 0, 30));
        
        bvs.add(balTitle);
        bvs.add(balVal);
        
        return bvs;
    }

    /**
     * Creates a screen that displays a message.
     * @param msg The message to display
     * @return The newly created message screen.
     */
    private JPanel makeResultScreen(String msg) {
        JPanel dcs = new JPanel();
        dcs.setPreferredSize(new Dimension(450, 500));

        dcs.setLayout(new BoxLayout(dcs, BoxLayout.Y_AXIS));
        dcs.setBorder(BORDER_SCREEN);
        
        JLabel lab = new JLabel(msg);
        lab.setPreferredSize(new Dimension(430, 70));
        lab.setFont(new Font("sans-serif", 0, 24));

        dcs.add(lab);

        return dcs;
    }
    
}
