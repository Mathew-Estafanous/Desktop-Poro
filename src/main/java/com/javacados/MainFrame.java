package com.javacados;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);

        setBackground(new Color(0, 0, 0, 0));

        Icon imgIcon = new ImageIcon(this.getClass().getResource("/static/poro.gif"));
        JLabel label = new JLabel(imgIcon);
        label.setBounds(668, 43, 200, 200);
        add(label);
    }
}
