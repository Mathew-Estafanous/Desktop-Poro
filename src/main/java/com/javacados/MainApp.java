package com.javacados;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public MainApp() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);

        setBackground(new Color(0, 0, 0, 0));

        Icon imgIcon = new ImageIcon(this.getClass().getResource("/static/poro.gif"));
        JLabel label = new JLabel(imgIcon);
        label.setBounds(668, 43, 200, 200);
        JPanel drawPanel = new DrawPanel();
        add(label);
        add(drawPanel);
    }
}
