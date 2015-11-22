/**
 *  BankGUI.java
 *
 *  @author Stephen Cioffi scc3459
 *  @author Michael Incardona mji8299
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

public class ATMGUI extends JFrame implements Observer{
    private JFrame frame;
    private String activeWindow = "login1";
    private ATM atm;
    private long id;
    private JLabel mainlabel;
    private JPasswordField pass;

    private JPanel loginscreen;
    private JPanel sidebar;
    private JPanel homescreen;
    private JPanel message;

    private JButton[] numberBtns;

    public ATMGUI(ATM atm, long ATMID) {
        
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
        
        this.atm = atm;
        this.id = ATMID;

        this.setTitle("Stephen Cioffi (scc3459) & Michael Incardona (mji8299) | ATM ID: " + ATMID);
        this.setSize(700, 500);
        //this.setLayout(new BorderLayout());

        this.sidebar = new JPanel();
        this.loginscreen = new JPanel();

        FlowLayout flow = new FlowLayout();

        this.setLayout(flow);

        loginscreen.setPreferredSize(new Dimension(450, 500));
        //loginscreen.setBackground(Color.green);
        loginscreen.setBorder(BorderFactory.createLineBorder(Color.black));

        sidebar.setPreferredSize(new Dimension(250, 500));
        //sidebar.setBackground(Color.red);
        sidebar.setBorder(BorderFactory.createLineBorder(Color.black));

        GridLayout grid = new GridLayout(5, 3);
        sidebar.setLayout(grid);

        numberBtns = new JButton[10];
        
        for (int i = 0; i < 10; i++) {
            numberBtns[i] = new JButton(Integer.toString(i));
        }
        JButton buttok = new JButton("OK");
        JButton buttcancel = new JButton("Cancel");
        JButton buttclear = new JButton("Clear");
        JButton buttclose = new JButton("Close");

        for (int i = 1; i < 10; i++) {
            sidebar.add(numberBtns[i]);
        }
        sidebar.add(buttok);
        sidebar.add(numberBtns[0]);
        sidebar.add(buttcancel);
        sidebar.add(buttclear);
        sidebar.add(new JPanel());
        sidebar.add(buttclose);

        Font numberFont = new Font("sans-serif", Font.BOLD, 50);
            /*
        Border compound;
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
            */

        for (int i = 0; i < 10; i++) {
            numberBtns[i].setFont(numberFont);
        }

        buttok.setFont(new Font("sans-serif", Font.BOLD,30));
        //buttok.setBackground(Color.green);
        //buttok.setBorder(compound = BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
        buttcancel.setFont(new Font("sans-serif", Font.BOLD, 14));
        buttclear.setFont(new Font("sans-serif", Font.BOLD, 16));
        buttclose.setFont(new Font("sans-serif", Font.BOLD, 16));

        JLabel mainlabel = new JLabel("Please enter your account ID:");
            mainlabel.setPreferredSize(new Dimension(430, 70));
            mainlabel.setFont(new Font("sans-serif", Font.BOLD, 30));
            this.mainlabel = mainlabel;
        // NOTE: for some reason, you need to add one to the JPasswordField arg to display all characters properly.
        JPasswordField pass = new JPasswordField(7);
            pass.setEditable(false);
            pass.setPreferredSize(new Dimension(300, 70));
            pass.setSize(300, 70);
            pass.setBounds(0, 200, 300, 70);
            pass.setFont(new Font("sans-serif", Font.BOLD, 30));
            this.pass = pass;

        loginscreen.add(mainlabel);
        loginscreen.add(pass);

        activeWindow = "login1";

        this.add(loginscreen,BorderLayout.WEST);
        this.add(sidebar,BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        frame = this;

        ActionListener numbers = e -> {
            JButton b = (JButton) e.getSource();
            switch(activeWindow) {
                case "login1":
                case "login2":
                    String s = new String(pass.getPassword());
                    pass.setText(s + b.getText());
                    break;
                case "home":
                    System.out.println("HOME -> " + activeWindow);
                    switch(b.getText()) {
                        case "1":
                            changeWindow("balance");
                            break;
                        case "2":
                            changeWindow("deposit");
                            break;
                        case "3":
                            changeWindow("withdraw");
                            break;
                        case "4":
                            changeWindow("logout");
                            break;
                    }
                    break;
            }
        };

        for (int i = 0; i < 10; i++) {
            numberBtns[i].addActionListener(numbers);
        }

        buttok.addActionListener(e -> {
            switch(activeWindow){
                case "login1":
                    if (atm.validateID(new String(pass.getPassword()))) {
                        changeWindow("login2");
                    }
                    else{
                        System.out.println("BAD ACCOUNT");
                    }
                    break;
                case "login2":
                    if (atm.validatePIN(new String(pass.getPassword()))){
                        System.out.println(pass.getPassword());
                        loginscreen.removeAll();
                        loginscreen.setVisible(false);
                        changeWindow("home");
                    } else {
                        changeWindow("login1");
                    }
                    break;
            }
            buttonPressed("OK");
        });
        buttcancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(activeWindow){
                    case "login1":
                        break;
                    case "login2":
                        changeWindow("login1");
                        break;
                }
                buttonPressed("CANCEL");
            }
        });
        buttclear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(activeWindow){
                    case "login1":
                    case "login2":
                        pass.setText("");
                        break;
                }
                buttonPressed("CLEAR");
            }
        });
        buttclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPressed("CLOSE");
            }
        });
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
                atm.deleteObservers();  // remove dangling references to observer objects for garbage collection
                atm.closeAll();
                atm = null;
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
        }
    }

    private void changeWindow(String window) {
        if (atm.doesWindowExist(window)) {
            activeWindow = window;
        }
        switch(activeWindow) {
            case "login1":
                mainlabel.setText("Please enter your account ID:");
                pass.setText("");
                break;

            case "login2":
                mainlabel.setText("Please enter your PIN:");
                pass.setText("");
                break;

            case "home":
                this.homescreen = new JPanel();
                homescreen.setPreferredSize(new Dimension(450,500));
                homescreen.setBorder(BorderFactory.createLineBorder(Color.black));

                Font labelFont = new Font("sans-serif",0,40);

                JLabel l1 = new JLabel("1) Balance Inquiry");
                    l1.setFont(labelFont);
                JLabel l2 = new JLabel("2) Deposit Money");
                    l2.setFont(labelFont);
                JLabel l3 = new JLabel("3) Withdraw Money");
                    l3.setFont(labelFont);
                JLabel l4 = new JLabel("4) Log Off");
                    l4.setFont(labelFont);

                homescreen.setLayout(new BoxLayout(homescreen,BoxLayout.Y_AXIS));

                homescreen.add(l1);
                homescreen.add(l2);
                homescreen.add(l3);
                homescreen.add(l4);

                this.message = new JPanel();
                message.setPreferredSize(new Dimension(450,500));
                message.setLayout(new FlowLayout());
                message.setBorder(BorderFactory.createLineBorder(Color.black));

                frame.add(homescreen,BorderLayout.WEST);
                frame.remove(sidebar);
                frame.add(sidebar,BorderLayout.EAST);

                break;
            case "deposit":
                getContentPane().removeAll();
                break;
            default:
                System.out.println("Well Shit...");
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
