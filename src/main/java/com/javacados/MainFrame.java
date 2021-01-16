package com.javacados;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {

    public MainFrame() {
        setSize(100, 100);
        Rectangle rect = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        setLocation((int) rect.getMaxX() - this.getWidth() - 80,
                    (int) rect.getMaxY() - this.getHeight() - 80);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);

        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        //setType(Type.UTILITY);

        ImageIcon poroGif = new ImageIcon(this.getClass().getResource("/static/Poro.gif"));
        poroGif.setImage(poroGif.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        JLabel label = new JLabel(poroGif);
        label.setBounds(668, 43, 500, 476);
        add(label);
    }
}
