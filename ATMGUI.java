/**
 *  BankGUI.java
 *
 *  @author Stephen Cioffi scc3459
 *  @author Michael Incardona mji8299
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ATMGUI extends JFrame {
    
    private static final String SCR_LOGIN_1 = "login1";
    private static final String SCR_LOGIN_2 = "login2";
    private static final String SCR_DEPOSIT = "deposit";
    private static final String SCR_HOME = "home";
    private static final String SCR_BALANCE = "balance";
    private static final String SCR_WITHDRAW = "withdraw";
    private static final String SCR_DEPOSIT_OK = "deposit_ok";
    private static final String SCR_WITHDRAW_OK = "withdraw_ok";
    private static final String SCR_WITHDRAW_FAIL = "withdraw_fail";
    
    private static final Border BORDER_SUNKEN = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    private static final Border BORDER_SCREEN = BORDER_SUNKEN;
    
    private String activeWindow = SCR_LOGIN_1;
    private final ATM atm;
    private JLabel mainlabel;
    
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

        this.depositCashField = makeCashField();
        this.withdrawCashField = makeCashField();
        this.sidebar = makeSidebar();
        this.homescreen = makeHomeScreen(true);
        this.homescreen_CD = makeHomeScreen(false);
        this.loginscreen1 = makeLoginScreen1();
        this.loginscreen2 = makeLoginScreen2();
        this.depositscreen = makeTransactionScreen("Enter an amount to deposit:");
        this.depositscreen.add(depositCashField);
        this.withdrawscreen = makeTransactionScreen("Enter an amount to withdraw:");
        this.withdrawscreen.add(withdrawCashField);
        this.depositokscreen = makeResultScreen("Deposit successful.");
        this.withdrawokscreen = makeResultScreen("Withdraw sucessful.");
        this.withdrawfailscreen = makeResultScreen("Withdraw failed. Insufficient funds.");
        
        FlowLayout flow = new FlowLayout();
        
        this.setLayout(flow);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                atm.closeAll();
            }
        });
        
        setWindow(SCR_LOGIN_1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Changes the window shown in the ATM screen.
     * @param window Identifier of the new window
     */
    private void setWindow(String window) {
        removeAllComponents();
        switch (window) {
            case SCR_LOGIN_1:
                add(loginscreen1, BorderLayout.WEST);
                textField.setText("");
                break;
                
            case SCR_LOGIN_2:
                add(loginscreen2, BorderLayout.WEST);
                passwordField.setText("");
                break;
                
            case SCR_HOME:
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
        add(sidebar, BorderLayout.EAST);
        activeWindow = window;
        render();
    }

    /**
     * Removes all components from this frame 
     */
    private void removeAllComponents() {
        getContentPane().removeAll();
    }

    /**
     * Redraws the GUI so that all components appear updated.
     */
    private void render() {
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

        mainlabel = new JLabel("Please enter your account ID:");
        mainlabel.setPreferredSize(new Dimension(430, 70));
        mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        
        ls.add(mainlabel);
        ls.add(textField = makeIDField());
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
        
        mainlabel = new JLabel("Please enter your PIN:");
        mainlabel.setPreferredSize(new Dimension(430, 70));
        mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        
        ls.add(mainlabel);
        ls.add(passwordField = makePasswordField());
        return ls;
    }

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
            l3.setForeground(Color.LIGHT_GRAY);
        }
        
        JLabel l4 = new JLabel("4) Log Off");
        l4.setFont(labelFont);
        
        hs.setLayout(new BoxLayout(hs, BoxLayout.Y_AXIS));
        
        hs.add(l1);
        hs.add(l2);
        hs.add(l3);
        hs.add(l4);
        
        return hs;
    }
    
    private JPanel makeSidebar() {
        JButton[] numberBtns = new JButton[10];
        JPanel sb = new JPanel();
        sb.setPreferredSize(new Dimension(250, 500));

        GridLayout grid = new GridLayout(5, 3);
        sb.setLayout(grid);

        for (int i = 0; i < 10; i++) {
            numberBtns[i] = new JButton(Integer.toString(i));
        }
        
        JButton buttok = new JButton("OK");
        JButton buttcancel = new JButton("Cancel");
        JButton buttclear = new JButton("Clear");
        JButton buttclose = new JButton("Close");
        
        for (int i = 1; i < 10; i++) {
            sb.add(numberBtns[i]);
        }
        
        sb.add(buttok);
        sb.add(numberBtns[0]);
        sb.add(buttcancel);
        sb.add(buttclear);
        sb.add(new JPanel());
        sb.add(buttclose);
        
        Font numberFont = new Font("sans-serif", Font.BOLD, 50);
        
        for (int i = 0; i < 10; i++) {
            numberBtns[i].setFont(numberFont);
        }
        
        buttok.setFont(new Font("sans-serif", Font.BOLD, 30));
        buttcancel.setFont(new Font("sans-serif", Font.BOLD, 14));
        buttclear.setFont(new Font("sans-serif", Font.BOLD, 16));
        buttclose.setFont(new Font("sans-serif", Font.BOLD, 16));
        
        ActionListener numbers = e -> {
            JButton b = (JButton) e.getSource();
            switch(activeWindow) {
            case SCR_LOGIN_1:
                textField.setText(textField.getText() + b.getText());
                break;
            case SCR_LOGIN_2:
                String s = new String(passwordField.getPassword());
                passwordField.setText(s + b.getText());
                break;
            case SCR_HOME:
                switch (b.getText()) {
                case "1":
                    setWindow(SCR_BALANCE);
                    break;
                case "2":
                    setWindow(SCR_DEPOSIT);
                    break;
                case "3":
                    if (atm.canWithdraw()) {
                        setWindow(SCR_WITHDRAW);
                    }
                    break;
                case "4":
                    atm.close();
                    setWindow(SCR_LOGIN_1);
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

        for (int i = 0; i < 10; i++) {
            numberBtns[i].addActionListener(numbers);
        }

        buttok.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_1:
                if (atm.validateID(textField.getText())) {
                    setWindow(SCR_LOGIN_2);
                }
                break;
            case SCR_LOGIN_2:
                if (atm.validatePIN(new String(passwordField.getPassword()))) {
                    setWindow(SCR_HOME);
                } else {
                    setWindow(SCR_LOGIN_1);
                }
                break;
            case SCR_DEPOSIT:
                double depcash = Double.parseDouble(ATM.digitsToCash(valuestr));
                if (atm.deposit(depcash)) {
                    valuestr = "";
                    setWindow(SCR_DEPOSIT_OK);
                }
                break;
            case SCR_WITHDRAW:
                double withcash = Double.parseDouble(ATM.digitsToCash(valuestr));
                if (atm.withdraw(withcash)) {
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
                setWindow(SCR_HOME);
                break;
            }
        });
        
        buttcancel.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_2:
                setWindow(SCR_LOGIN_1);
                break;
            case SCR_WITHDRAW:
            case SCR_DEPOSIT:
                setWindow(SCR_HOME);
                break;
            }
        });
        
        buttclear.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_1:
                textField.setText("");
                break;
            case SCR_LOGIN_2:
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
        
        buttclose.addActionListener( e -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        
        return sb;
    }
    
    private JPanel makeTransactionScreen(String msg) {
        JPanel ds = new JPanel();
        ds.setPreferredSize(new Dimension(450, 500));
        ds.setBorder(BORDER_SCREEN);
        ds.setLayout(new FlowLayout());

        JLabel lmsg = new JLabel(msg);
        lmsg.setPreferredSize(new Dimension(430, 70));
        lmsg.setFont(new Font("sans-serif", 0, 30));
        ds.add(lmsg);
        return ds;
    }
    
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
    
    private JPanel makeResultScreen(String msg) {
        JPanel dcs = new JPanel();
        dcs.setPreferredSize(new Dimension(450, 500));

        dcs.setLayout(new BoxLayout(dcs, BoxLayout.Y_AXIS));
        dcs.setBorder(BORDER_SCREEN);
        
        JLabel lab = new JLabel(msg);
        lab.setPreferredSize(new Dimension(430, 70));
        lab.setFont(new Font("sans-serif", 0, 30));

        dcs.add(lab);

        return dcs;
    }
    
}
