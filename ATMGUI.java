/**
 *  BankGUI.java
 *
 *  @author Stephen Cioffi scc3459
 *  @author Michael Incardona mji8299
 */

import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import javax.swing.border.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ATMGUI extends JFrame{
    private String activeWindow = "Home";
    private JLabel mainlabel;
    private JPasswordField pass;

    public ATMGUI(ATM atm,long ATMID) {
        this.setTitle("Stephen Cioffi (scc3459) & Michael Incardona (mji8299) | ATM #" + ATMID);
        this.setSize(700,500);
        //this.setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        JPanel content = new JPanel();

        FlowLayout flow = new FlowLayout();

        this.setLayout(flow);

        content.setPreferredSize(new Dimension(450,500));
        //content.setBackground(Color.green);
        content.setBorder(BorderFactory.createLineBorder(Color.black));

        BoxLayout box = new BoxLayout(content,BoxLayout.Y_AXIS);
        //content.setLayout(box);

        sidebar.setPreferredSize(new Dimension(250,500));
        //sidebar.setBackground(Color.red);
        sidebar.setBorder(BorderFactory.createLineBorder(Color.black));

        GridLayout grid = new GridLayout(5,3);
        sidebar.setLayout(grid);

        JButton butt1 = new JButton("1");
        JButton butt2 = new JButton("2");
        JButton butt3 = new JButton("3");
        JButton butt4 = new JButton("4");
        JButton butt5 = new JButton("5");
        JButton butt6 = new JButton("6");
        JButton butt7 = new JButton("7");
        JButton butt8 = new JButton("8");
        JButton butt9 = new JButton("9");
        JButton butt0 = new JButton("0");

        JButton buttok = new JButton("OK");
        JButton buttcancel = new JButton("Cancel");
        JButton buttclear = new JButton("Clear");
        JButton dud = new JButton();
        JButton buttclose = new JButton("Close");

        sidebar.add(butt1);
        sidebar.add(butt2);
        sidebar.add(butt3);
        sidebar.add(butt4);
        sidebar.add(butt5);
        sidebar.add(butt6);
        sidebar.add(butt7);
        sidebar.add(butt8);
        sidebar.add(butt9);
        sidebar.add(buttok);
        sidebar.add(butt0);
        sidebar.add(buttcancel);
        sidebar.add(buttclear);
        sidebar.add(dud);
        sidebar.add(buttclose);

        Font numberFont = new Font("sans-serif",Font.BOLD,50);
            /*
        Border compound;
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
            */

        butt1.setFont(numberFont);
        butt2.setFont(numberFont);
        butt3.setFont(numberFont);
        butt4.setFont(numberFont);
        butt5.setFont(numberFont);
        butt6.setFont(numberFont);
        butt7.setFont(numberFont);
        butt8.setFont(numberFont);
        butt9.setFont(numberFont);
        butt0.setFont(numberFont);
        buttok.setFont(new Font("sans-serif",Font.BOLD,30));
        //buttok.setBackground(Color.green);
        //buttok.setBorder(compound = BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
        buttcancel.setFont(new Font("sans-serif",Font.BOLD,12));
        buttclear.setFont(new Font("sans-serif",Font.BOLD,16));
        buttclose.setFont(new Font("sans-serif",Font.BOLD,16));

        JLabel mainlabel = new JLabel("Please enter your account ID:");
            mainlabel.setPreferredSize(new Dimension(430,70));
            mainlabel.setFont(new Font("sans-serif",Font.BOLD,30));
            this.mainlabel = mainlabel;
        JPasswordField pass = new JPasswordField(6);
            pass.setEditable(false);
            pass.setPreferredSize(new Dimension(300,70));
            pass.setSize(300,70);
            pass.setBounds(0,200,300,70);
            pass.setFont(new Font("sans-serif",Font.BOLD,30));
            this.pass = pass;

        content.add(mainlabel);
        content.add(pass);

        activeWindow="login1";

        this.add(content,BorderLayout.WEST);
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

        butt0.addActionListener(numbers);
        butt1.addActionListener(numbers);
        butt2.addActionListener(numbers);
        butt3.addActionListener(numbers);
        butt4.addActionListener(numbers);
        butt5.addActionListener(numbers);
        butt6.addActionListener(numbers);
        butt7.addActionListener(numbers);
        butt8.addActionListener(numbers);
        butt9.addActionListener(numbers);

        buttok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(activeWindow){
                    case "login1":
                        if(atm.validateID(new String(pass.getPassword()))) {
                            changeWindow("login2");
                        }
                        break;
                }
                buttonPressed("OK");
            }
        });
        buttcancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPressed("CANCEL");
            }
        });
        buttclear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    private void buttonPressed(int num){

        System.out.println(num);
    }
    private void buttonPressed(String str){
        switch(str){
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
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
        }
    }

    private void changeWindow(String window){
        activeWindow = window;
        switch(activeWindow){
            case "login2":
                mainlabel.setText("Please enter your PIN:");
                pass.setText("");
                break;
        }
    }
}