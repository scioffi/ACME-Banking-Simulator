/**
 *  BankGUI.java
 *
 *  @author Michael Incardona mji8299
 *  @author Stephen Cioffi scc3459
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
import javax.swing.border.Border;
import javax.swing.*;
import java.lang.*;
import java.util.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BankGUI extends JFrame{
    public BankGUI() {
        this.setTitle("BankGUI");
        this.setSize(700,500);
        JPanel sidebar = new JPanel();
        //JPanel topSidebar = new JPanel();
        JPanel content = new JPanel();

        FlowLayout flow = new FlowLayout();

        this.setLayout(flow);

        content.setPreferredSize(new Dimension(450,500));
        content.setBackground(Color.green);

        sidebar.setPreferredSize(new Dimension(250,400));
        sidebar.setBackground(Color.red);

        JPanel bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(250,100));
        bottom.setBounds(250,400,250,100);
        bottom.setLayout(new GridLayout(1,2));

        GridLayout grid = new GridLayout(4,3);
        sidebar.setLayout(grid);

        JButton butt0 = new JButton("0");
        JButton butt1 = new JButton("1");
        JButton butt2 = new JButton("2");
        JButton butt3 = new JButton("3");
        JButton butt4 = new JButton("4");
        JButton butt5 = new JButton("5");
        JButton butt6 = new JButton("6");
        JButton butt7 = new JButton("7");
        JButton butt8 = new JButton("8");
        JButton butt9 = new JButton("9");

        JButton buttok = new JButton("OK");
        JButton buttcancel = new JButton("Cancel");
        JButton buttclear = new JButton("Clear");
        JButton buttclose = new JButton("Close");

        sidebar.add(butt0);
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
        sidebar.add(buttcancel);
        bottom.add(buttclear);
        bottom.add(buttclose);

        Font numberFont = new Font("sans-serif",Font.BOLD,50);

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
        buttcancel.setFont(new Font("sans-serif",Font.BOLD,12));
        buttclear.setFont(new Font("sans-serif",Font.BOLD,16));
        buttclose.setFont(new Font("sans-serif",Font.BOLD,16));

        this.add(content);
        this.add(sidebar);
        this.add(bottom);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}