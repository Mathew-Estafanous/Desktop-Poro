package com.javacados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Poro extends JLabel {

    private ImageIcon poroGif;
    private String gifPath;

    private int poroSize = 100;

    private final int GROW_AMOUNT = 30;
    private final int STARTING_SIZE = 100;
    private final int MAX_SIZE = 300;

    public Poro() {
        this("/static/Poro.gif");
    }

    public Poro(String poroUrl) {
        updatePoroImage(poroUrl);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                growPoro();
            }
        });
    }

    public void updatePoroImage(String poroUrl) {
        this.gifPath = poroUrl;
        this.poroGif = new ImageIcon(this.getClass().getResource(poroUrl));
        this.poroGif.setImage(this.poroGif.getImage()
                .getScaledInstance(poroSize, poroSize, Image.SCALE_DEFAULT));
        updatePoroLabel();
    }

    private void updatePoroLabel() {
        setIcon(poroGif);
        setBounds(668, 43, 500, 476);
    }

    public void growPoro() {
        poroSize += GROW_AMOUNT;
        updatePoroImage("/static/Poro.gif");
        if(this.poroSize > MAX_SIZE) {
            splitPoro();
        }
    }

    private void splitPoro() {
        System.out.println("I AM SPLITTING UP!!!");
        poroSize = STARTING_SIZE;
    }
}
