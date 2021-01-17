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
    private final int WALK_SPEED = 5;
    private final int GRAVITY = 10;

    private int poroX = 900;
    private int poroY = 800;

    private int currentSize = 100;

    private final int FLOOR = 800;
    private final int GROW_AMOUNT = 30;
    private final int STARTING_SIZE = 100;
    private final int MAX_SIZE = 300;

    private final Random rand = new Random();
    private final Timer timer = new Timer(100, this);
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
                if(currentState == PoroState.Falling)
                    return;
                growPoro();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(currentState == PoroState.Falling)
                    return;
                hasPointedInDirection = false;
                currentState = PoroState.Eating;
                updatePoroImage(PORO_EAT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(currentState == PoroState.Falling)
                    return;
                hasPointedInDirection = false;
                currentState = PoroState.Idling;
                updatePoroImage(PORO_IDLE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(poroY + currentSize < FLOOR) {
                    System.out.println("FALLING");
                    currentState = PoroState.Falling;
                    updatePoroImage(PORO_IN_AIR);
                } else {
                    currentState = PoroState.Idling;
                    updatePoroImage(PORO_IDLE);
                    poroY = FLOOR;
                }
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
                updatePoroImage(PORO_IN_AIR);
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
        poroX += WALK_SPEED * walkDirection;
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

    private void fallFromGravity() {
        poroY += GRAVITY;
        if(poroY > FLOOR) {
            poroY = FLOOR;
            currentState = PoroState.Idling;
            updatePoroImage(PORO_IDLE);
            return;
        }
        //TODO: Change to falling animation when possible
        updatePoroLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (currentState) {
            case Eating:
                return;
            case Dragged:
                return;
            case Falling:
                fallFromGravity();
                return;
            case Walking:
                walkTowardsPoint();
                return;
        }

        final int choice = rand.nextInt(30);
        if(choice < 1) {
            startWalkingSequence();
        }
    }
}
