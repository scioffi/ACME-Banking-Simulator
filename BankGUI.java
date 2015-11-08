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

public class BankGUI extends JFrame {
    public BankGUI() {
        this.setTitle("BankGUI");
        this.setSize(500,400);
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(500,400));
        sidebar.setBackground(Color.red);

        this.add(sidebar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }
}