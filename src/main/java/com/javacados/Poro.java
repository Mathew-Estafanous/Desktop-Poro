package com.javacados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Poro extends JLabel implements ActionListener {

    private ImageIcon poroGif;

    private PoroState currentState;
    private int walkEndpoint = 100;
    private int walkDirection = 0;
    private int walkSpeed = 10;

    private int poroX = 900;
    private int poroY = 800;

    private int currentSize = 100;

    private final int GROW_AMOUNT = 30;
    private final int STARTING_SIZE = 100;
    private final int MAX_SIZE = 300;
    private final Random rand = new Random();
    private final Timer timer = new Timer(10000, this);
    private final Rectangle rect;

    public Poro() {
        this("/static/Poro.gif");
    }

    public Poro(String poroUrl) {
        this.currentState = PoroState.Idling;
        rect = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

        updatePoroImage(poroUrl);
        addMouseEventListener();
        this.timer.start();
    }

    private void addMouseEventListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                growPoro();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                currentState = PoroState.Eating;
                updatePoroImage("/static/Poro-Eat.gif");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentState = PoroState.Idling;
                updatePoroImage("/static/Poro.gif");
            }
        });
    }

    public void updatePoroImage(String poroUrl) {
        this.poroGif = new ImageIcon(this.getClass().getResource(poroUrl));
        this.poroGif.setImage(this.poroGif.getImage()
                .getScaledInstance(currentSize, currentSize, Image.SCALE_DEFAULT));
        setIcon(poroGif);
        updatePoroLabel();
    }

    private void updatePoroLabel() {
        setBounds(poroX - currentSize /2, poroY - currentSize, currentSize, currentSize);
        repaint();
    }

    public void growPoro() {
        currentSize += GROW_AMOUNT;
        updatePoroImage("/static/Poro.gif");
        if(this.currentSize > MAX_SIZE) {
            splitPoro();
        }
    }

    private void splitPoro() {
        System.out.println("I AM SPLITTING UP!!!");
        currentSize = STARTING_SIZE;
    }

    private void walkTowardsPoint() {
        poroX += walkSpeed * walkDirection;
        updatePoroLabel();

        //Checking if poro reached their final endpoint
        if(walkEndpoint < poroX && walkDirection > 0) {
            System.out.println("DONE WALKING");
            currentState = PoroState.Idling;
        } else if(walkEndpoint > poroX && walkDirection < 0) {
            System.out.println("DONE WALKING");
            currentState = PoroState.Idling;
        }
    }

    private void startWalkingSequence() {
        currentState = PoroState.Walking;
        int minimumX = currentSize/2;
        int maximumX = (int) (rect.getMaxX() - currentSize/2);
        walkEndpoint = rand.nextInt(maximumX) + minimumX;

        if(walkEndpoint < poroX) {
            //TODO: SET THIS ANIMATION TO WALK_LEFT;
            System.out.println("LEFT");
            updatePoroImage("/static/Poro.gif");
            walkDirection = -1;
        } else {
            //TODO: SET THIS ANIMATION TO WALK_RIGHT;
            System.out.println("Right");
            updatePoroImage("/static/Poro.gif");
            walkDirection = 1;
        }
        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(currentState == PoroState.Eating)
            return;
        else if(currentState == PoroState.Walking) {
            System.out.println("Test");
            walkTowardsPoint();
        }

        final int choice = 2; //rand.nextInt(10);
        if(choice < 3) {
            this.timer.stop();
            this.timer.setDelay(500);
            startWalkingSequence();
        }
    }
}
