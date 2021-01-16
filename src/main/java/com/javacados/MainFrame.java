package com.javacados;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setSize(300, 300);
        Rectangle rect = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        setLocation((int) rect.getMaxX() - this.getWidth() - 40,
                    (int) rect.getMaxY() - this.getHeight() - 40);

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
