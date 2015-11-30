/**
 *  BankGUI.java
 *
 *  @author Stephen Cioffi scc3459
 *  @author Michael Incardona mji8299
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ATMGUI extends JFrame {
    
    private static final String SCR_LOGIN_1 = "login1";
    private static final String SCR_LOGIN_2 = "login2";
    private static final String SCR_DEPOSIT = "deposit";
    private static final String SCR_HOME = "home";

    private JFrame frame;
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
        //this.setLayout(new BorderLayout());

        this.sidebar = makeSidebar();
        this.homescreen = makeHomeScreen();
        this.loginscreen1 = makeLoginScreen1();
        this.loginscreen2 = makeLoginScreen2();

        FlowLayout flow = new FlowLayout();

        this.setLayout(flow);

        activeWindow = SCR_LOGIN_1;

        this.add(loginscreen1, BorderLayout.WEST);
        this.add(sidebar, BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        frame = this;
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

    private void changeWindow(String window) {
        removeAllComponents(frame);
        switch(activeWindow) {
            case SCR_LOGIN_1:
                frame.add(loginscreen1, BorderLayout.WEST);
                frame.add(sidebar, BorderLayout.EAST);
                mainlabel.setText("Please enter your account ID:");
                textField.setText("");
                break;

            case SCR_LOGIN_2:
                frame.add(loginscreen2, BorderLayout.WEST);
                frame.add(sidebar, BorderLayout.EAST);
                mainlabel.setText("Please enter your PIN:");
                passwordField.setText("");
                break;

            case SCR_HOME:
                frame.add(homescreen, BorderLayout.WEST);
                frame.add(sidebar, BorderLayout.EAST);
                break;
            
            case SCR_DEPOSIT:
                this.depositscreen = makeDepositScreen();
                frame.add(depositscreen, BorderLayout.WEST);
                frame.add(sidebar, BorderLayout.EAST);
                break;
            
            default:
                System.out.println("oops, that window doesn't exist");
                return;
        }
        activeWindow = window;
        validate();
    }

    public void close() {
        atm.deleteObservers();  // remove dangling references to observer objects for garbage collection
        atm.closeAll();
        atm = null;
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    private void removeAllComponents(JFrame f) {
        while (f.getContentPane().getComponentCount() > 0)
            f.remove(f.getContentPane().getComponent(0));
    }

    private JPanel makeLoginScreen1() {
        JPanel ls = new JPanel();
        ls.setPreferredSize(new Dimension(450, 500));
        ls.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel mainlabel = new JLabel("Please enter your account ID:");
        mainlabel.setPreferredSize(new Dimension(430, 70));
        mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        this.mainlabel = mainlabel;
        
        ls.add(mainlabel);
        ls.add(textField = makeIDField());
        return ls;
    }
    
    private JPanel makeLoginScreen2() {
        JPanel ls = new JPanel();
        ls.setPreferredSize(new Dimension(450, 500));
        ls.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel mainlabel = new JLabel("Please enter your PIN:");
        mainlabel.setPreferredSize(new Dimension(430, 70));
        mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
        this.mainlabel = mainlabel;
        ls.add(mainlabel);
        ls.add(passwordField = makePasswordField());
        return ls;
    }
    
    private JPanel makeHomeScreen() {
        JPanel hs = new JPanel();
        hs.setPreferredSize(new Dimension(450, 500));
        hs.setBorder(BorderFactory.createLineBorder(Color.black));
        
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
        //sidebar.setBackground(Color.red);
        sb.setBorder(BorderFactory.createLineBorder(Color.black));

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
                    break;
                case "2":
                    changeWindow(SCR_DEPOSIT);
                    break;
                case "3":
                    changeWindow("withdraw");
                    break;
                case "4":
                    atm.close();
                    changeWindow(SCR_LOGIN_1);
                    break;
                }
                break;
            case SCR_DEPOSIT:
                String temp = valuestr + b.getText();
                valuestr += b.getText();
                System.out.println(temp);
                temp = ATM.formatCash(temp);
                cashField.setText(temp);
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
                    changeWindow(SCR_LOGIN_2);
                } else {
                    System.out.println("BAD ACCOUNT");
                }
                break;
            case SCR_LOGIN_2:
                if (atm.validatePIN(new String(passwordField.getPassword()))) {
                    loginscreen1.removeAll();
                    loginscreen1.setVisible(false);
                    changeWindow(SCR_HOME);
                } else {
                    changeWindow(SCR_LOGIN_1);
                }
                break;
            case SCR_DEPOSIT:
                double cash = Double.parseDouble(ATM.digitsToCash(valuestr));
                if (atm.deposit(cash)) {
                    System.out.println("SUCCESS");
                    System.out.println(atm.balance());
                } else {
                    System.out.println("NO MONEY HERE");
                }
                break;
            }
            buttonPressed("OK");
        });
        
        buttcancel.addActionListener(e -> {
            switch (activeWindow) {
            case SCR_LOGIN_1:
                break;
            case SCR_LOGIN_2:
                changeWindow(SCR_LOGIN_1);
                break;
            case SCR_DEPOSIT:
                frame.remove(depositscreen);
                changeWindow(SCR_HOME);
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
            }
            buttonPressed("CLEAR");
        });
        
        buttclose.addActionListener(e -> buttonPressed("CLOSE"));
        
        return sb;
    }
    
    private JPanel makeDepositScreen() {
        JPanel ds = new JPanel();
        ds.setPreferredSize(new Dimension(450,500));
        ds.setBorder(BorderFactory.createLineBorder(Color.black));
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
        f.setText("$");
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
}
