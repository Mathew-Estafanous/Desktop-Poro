package com.javacados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import static com.javacados.PoroAnimations.*;

public class Poro extends JLabel implements ActionListener {

    private ImageIcon poroGif;

    private PoroState currentState;
    private int walkEndpoint = 100;
    private int walkDirection = 0;
    private boolean hasPointedInDirection = false;
    private final int walkSpeed = 5;

    private int poroX = 900;
    private int poroY = 800;

    private int currentSize = 100;

    private final int GROW_AMOUNT = 30;
    private final int STARTING_SIZE = 100;
    private final int MAX_SIZE = 300;

    private final Random rand = new Random();
    private final Timer timer = new Timer(8000, this);
    private final Rectangle rect;

    public Poro() {
        this(PORO_IDLE);
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
                hasPointedInDirection = false;
                currentState = PoroState.Eating;
                updatePoroImage(PORO_EAT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hasPointedInDirection = false;
                currentState = PoroState.Idling;
                updatePoroImage(PORO_IDLE);
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentState = PoroState.Dragged;
                int mouseX = e.getX();
                int mouseY = e.getY();
                poroX += mouseX - currentSize / 2;
                poroY += mouseY - currentSize / 2;
                updatePoroLabel();
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
        updatePoroImage(PORO_EAT);
        if(this.currentSize > MAX_SIZE) {
            splitPoro();
        }
    }

    private void splitPoro() {
        System.out.println("I AM SPLITTING UP!!!");
        currentSize = STARTING_SIZE;
    }

    private void walkTowardsPoint() {
        pointPoroInWalkingDirection();
        poroX += walkSpeed * walkDirection;
        updatePoroLabel();

        //Checking if poro reached their final endpoint
        if(walkEndpoint < poroX && walkDirection > 0) {
            resetPoroToIdle();
        } else if(walkEndpoint > poroX && walkDirection < 0) {
            resetPoroToIdle();
        }
    }

    private void resetPoroToIdle() {
        currentState = PoroState.Idling;
        updatePoroImage(PORO_IDLE);
        this.timer.setDelay(8000);
        hasPointedInDirection = false;
    }

    private void pointPoroInWalkingDirection() {
        if(hasPointedInDirection)
            return;

        if(walkDirection < 0) {
            updatePoroImage(PORO_WALK_LEFT);
        } else {
            updatePoroImage(PORO_WALK_RIGHT);
        }
        hasPointedInDirection = true;
    }

    private void startWalkingSequence() {
        currentState = PoroState.Walking;
        int minimumX = currentSize/2;
        int maximumX = (int) (rect.getMaxX() - currentSize/2);
        walkEndpoint = rand.nextInt(maximumX) + minimumX;

        walkDirection = (walkEndpoint < poroX)? -1:1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(currentState);
        if(currentState == PoroState.Eating)
            return;
        else if (currentState == PoroState.Dragged) {
            return;
        }
        else if(currentState == PoroState.Walking) {
            walkTowardsPoint();
            return;
        }

        final int choice = rand.nextInt(10);
        if(choice < 4) {
            this.timer.setDelay(100);
            startWalkingSequence();
        }
    }
}
