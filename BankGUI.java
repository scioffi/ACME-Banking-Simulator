/**
    BankGUI.java
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

public class BankGUI extends JFrame {
    public BankGUI() {
        this.setTitle("BankGUI");
        this.setSize(500,400);
        JPanel sidebar = new JPanel();
        JPanel content = new JPanel();

        FlowLayout flow = new FlowLayout();

        this.setLayout(flow);

        content.setPreferredSize(new Dimension(300,400));
        content.setBackground(Color.green);

        sidebar.setPreferredSize(new Dimension(200,400));
        sidebar.setBackground(Color.red);

        GridLayout grid = new GridLayout(4,3);
        sidebar.setLayout(grid);

        for(int i = 9; i <= 0;i++){
            sidebar.add(new JButton("" + i));
        }

        JButton butt0 = new JButton("0");
        JButton butt1 = new JButton("1");
        JButton butt2 = new JButton("2");
        JButton butt3 = new JButton("3");
        JButton butt4 = new JButton("4");
        JButton butt5 = new JButton("5");
        JButton butt6 = new JButton("6");
        JButton butt7 = new JButton("7");

        JButton butt = new JButton("THIS IS A BUTTON");

        sidebar.add(butt0);
        sidebar.add(butt1);
        sidebar.add(butt2);
        sidebar.add(butt3);
        sidebar.add(butt);

        this.add(content);
        this.add(sidebar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}