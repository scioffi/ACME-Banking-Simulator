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
import java.awt.event.WindowEvent;

public class ATMGUI extends JFrame {
    
    private static final String SCR_LOGIN_1 = "login1";
    private static final String SCR_LOGIN_2 = "login2";
    private static final String SCR_DEPOSIT = "deposit";
    private static final String SCR_HOME = "home";
    private static final String SCR_BALANCE = "balance";
    private static final String SCR_WITHDRAW = "withdraw";
    private static final String SCR_DEPOSIT_OK = "deposit_ok";

    private static final Border BORDER_LINE = BorderFactory.createLineBorder(Color.BLACK);
    private static final Border BORDER_SUNKEN = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    private static final Border BORDER_SCREEN = BORDER_SUNKEN;
    
    private String activeWindow = SCR_LOGIN_1;
    private ATM atm;
    private JLabel mainlabel;
    
    private JPasswordField passwordField;
    private JTextField cashField;
    private JTextField textField;

    private JPanel loginscreen1;
    private JPanel loginscreen2;
    private JPanel sidebar;
    private JPanel homescreen;
    private JPanel depositscreen;
    private JPanel depositokscreen;
    
    private String valuestr = "";

    public ATMGUI(ATM atm, long ATMID) {
        
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
            // by doing nothing
        }
        */
        
        this.atm = atm;
        
        this.setTitle("Stephen Cioffi (scc3459) & Michael Incardona (mji8299) | ATM ID: " + ATMID);
        this.setSize(700, 500);

        this.sidebar = makeSidebar();
        this.homescreen = makeHomeScreen();
        this.loginscreen1 = makeLoginScreen1();
        this.loginscreen2 = makeLoginScreen2();
        this.depositscreen = makeDepositScreen();
        this.depositokscreen = makeDepositConfirmScreen();

        FlowLayout flow = new FlowLayout();

        this.setLayout(flow);

        setWindow(SCR_LOGIN_1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    private void buttonPressed(String str) {
        switch(str) {
            case "OK":
                System.out.println(str);
                break;
            case "CANCEL":
                System.out.println(str);
                break;
            case "CLEAR":
                System.out.println(str);
                break;
            case "CLOSE":
                System.out.println(str);
                close();
                break;
        }
    }

    private void setWindow(String window) {
        removeAllComponents();
        switch (window) {
            case SCR_LOGIN_1:
                add(loginscreen1, BorderLayout.WEST);
                add(sidebar, BorderLayout.EAST);
                textField.setText("");
                break;

            case SCR_LOGIN_2:
                add(loginscreen2, BorderLayout.WEST);
                add(sidebar, BorderLayout.EAST);
                passwordField.setText("");
                break;

            case SCR_HOME:
                add(homescreen, BorderLayout.WEST);
                add(sidebar, BorderLayout.EAST);
                break;
            
            case SCR_DEPOSIT:
                add(depositscreen, BorderLayout.WEST);
                add(sidebar, BorderLayout.EAST);
                cashField.setText("$0.00");
                valuestr = "";
                break;
            
            case SCR_DEPOSIT_OK:
                add(depositokscreen, BorderLayout.WEST);
                add(sidebar, BorderLayout.EAST);
                break;
            
            case SCR_BALANCE:
                add(makeBalanceViewScreen(), BorderLayout.WEST);
                add(sidebar, BorderLayout.EAST);
                break;
            
            default:
                System.out.println("oops, that window doesn't exist");
                return;
        }
        activeWindow = window;
        render();
    }

    public void close() {
        atm.deleteObservers();  // remove dangling references to observer objects for garbage collection
        atm.closeAll();
        atm = null;
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    private void removeAllComponents() {
        getContentPane().removeAll();
    }
    
    private void render() {
        validate();
        repaint();
    }

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
    
    private JPanel makeHomeScreen() {
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
        //sb.setBorder(BORDER_LINE);

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
        buttok.setBackground(Color.GREEN);
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
                System.out.println("HOME -> " + activeWindow);
                switch (b.getText()) {
                case "1":
                    setWindow(SCR_BALANCE);
                    break;
                case "2":
                    setWindow(SCR_DEPOSIT);
                    break;
                case "3":
                    setWindow(SCR_WITHDRAW);
                    break;
                case "4":
                    atm.close();
                    setWindow(SCR_LOGIN_1);
                    break;
                }
                break;
            case SCR_DEPOSIT:
                if (valuestr.length() == 0 && b.getText().equals("0"))
                    break;
                valuestr += b.getText();
                cashField.setText(ATM.formatCash(valuestr));
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
                } else {
                    System.out.println("BAD ACCOUNT");
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
                double cash = Double.parseDouble(ATM.digitsToCash(valuestr));
                if (atm.deposit(cash)) {
                    System.out.println("SUCCESS");
                    System.out.println(atm.balance());
                    valuestr = "";
                    setWindow(SCR_DEPOSIT_OK);
                } else {
                    System.out.println("NO MONEY HERE");
                }
                break;
            case SCR_DEPOSIT_OK:
                setWindow(SCR_HOME);
                break;
            case SCR_BALANCE:
                setWindow(SCR_HOME);
                break;
            }
            buttonPressed("OK");
        });
        
        buttcancel.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_2:
                setWindow(SCR_LOGIN_1);
                break;
            case SCR_DEPOSIT:
                setWindow(SCR_HOME);
                break;
            }
            buttonPressed("CANCEL");
        });
        
        buttclear.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_1:
                textField.setText("");
            case SCR_LOGIN_2:
                passwordField.setText("");
                break;
            case SCR_DEPOSIT:
                cashField.setText("$0.00");
                valuestr = "";
            }
            buttonPressed("CLEAR");
        });
        
        buttclose.addActionListener(e -> buttonPressed("CLOSE"));
        
        return sb;
    }
    
    private JPanel makeDepositScreen() {
        JPanel ds = new JPanel();
        ds.setPreferredSize(new Dimension(450,500));
        ds.setBorder(BORDER_SCREEN);
        ds.setLayout(new FlowLayout());

        JLabel msg = new JLabel("Enter an amount to deposit:");
        msg.setPreferredSize(new Dimension(430, 70));
        msg.setFont(new Font("sans-serif", 0, 30));
        cashField = makeCashField();
        ds.add(msg);
        ds.add(cashField);
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
        bvs.setPreferredSize(new Dimension(450,500));
        
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
    
    private JPanel makeDepositConfirmScreen() {
        JPanel dcs = new JPanel();
        dcs.setPreferredSize(new Dimension(450,500));

        dcs.setLayout(new BoxLayout(dcs, BoxLayout.Y_AXIS));
        dcs.setBorder(BORDER_SCREEN);
        
        JLabel lab = new JLabel("Deposit successful.");
        lab.setPreferredSize(new Dimension(430, 70));
        lab.setFont(new Font("sans-serif", 0, 30));

        dcs.add(lab);

        return dcs;
    }
    
}
