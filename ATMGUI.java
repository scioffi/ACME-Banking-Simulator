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
    private String activeWindow = "login1";
    private ATM atm;
    private long id;
    private JLabel mainlabel;
    private JPasswordField pass;

    private JPanel loginscreen;
    private JPanel sidebar;
    private JPanel homescreen;

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

        BoxLayout box = new BoxLayout(loginscreen,BoxLayout.Y_AXIS);
        //loginscreen.setLayout(box);

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

        ActionListener numbers = e -> {
            JButton b = (JButton)e.getSource();
            String s = new String(pass.getPassword());
            pass.setText(s + b.getText());
        };

        for (int i = 0; i < 10; i++) {
            numberBtns[i].addActionListener(numbers);
        }

        buttok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(activeWindow){
                    case "login1":
                        if (atm.validateID(new String(pass.getPassword()))) {
                            changeWindow("login2");
                        }
                        break;
                    case "login2":
                        if (atm.validatePIN(new String(pass.getPassword()))){
                            changeWindow("home");
                        } else {
                            changeWindow("login1");
                        }
                        break;
                }
                buttonPressed("OK");
            }
        });
        buttcancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(activeWindow){
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
        activeWindow = window;
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
                loginscreen.removeAll();
                this.homescreen = new JPanel();
                homescreen.setLayout(new FlowLayout());
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
