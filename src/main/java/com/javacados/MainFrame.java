package com.javacados;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        Rectangle rect = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        setSize((int) rect.getMaxX(), (int) rect.getMaxY());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);

        setBackground(new Color(0, 0, 0, 0));
        setLayout(null);
        setAlwaysOnTop(true);

        Poro poroCharacter = new Poro();
        add(poroCharacter);
    }
}