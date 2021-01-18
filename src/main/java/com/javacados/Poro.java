package com.javacados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.lang.Math;
import static com.javacados.PoroAnimations.*;

public class Poro extends JLabel implements ActionListener {

    private ImageIcon poroGif;

    private PoroState currentState;
    private int walkEndpoint = 100;
    private int walkDirection = 0;
    private boolean hasPointedInDirection = false;
    private final int WALK_SPEED = 2;
    private final int GRAVITY = 3;
    private final int MESSAGE_COUNT = 250;
    private final int PORO_MESSAGE_SCALE = 2;
    private int messageCountdown;

    private int poroX = 900;
    private int poroY = 800;

    private double velX = 0;
    private int velY = 0;

    private double friction = 0.5;

    private int currentSize = 100;

    private final int FLOOR;
    private final int GROW_AMOUNT = 30;
    private final int STARTING_SIZE = 100;
    private final int MAX_SIZE = 300;

    private final Random rand = new Random();
    private final Timer timer = new Timer(30, this);
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

        FLOOR = (int) rect.getMaxY() - currentSize/2;
        poroY = FLOOR;
        updatePoroImage(poroUrl);
        addMouseEventListener();
        this.timer.start();
    }

    private void addMouseEventListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ensureNotInOtherStates()) return;
                growPoro();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (ensureNotInOtherStates()) return;
                hasPointedInDirection = false;
                currentState = PoroState.Eating;
                updatePoroImage(PORO_EAT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (ensureNotInOtherStates()) return;
                hasPointedInDirection = false;
                currentState = PoroState.Idling;
                updatePoroImage(PORO_IDLE);
            }

            private boolean ensureNotInOtherStates() {
                return currentState == PoroState.Falling || currentState == PoroState.Talking;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(poroY + currentSize < FLOOR) {
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
                if (currentState != PoroState.Dragged) {
                    updatePoroImage(PORO_IN_AIR);
                }
                currentState = PoroState.Dragged;
                velX = e.getX();
                velY = e.getY();
                poroX += velX;
                poroY += velY;
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
        int minimumX = currentSize/2;
        int maximumX = (int) (rect.getMaxX() - currentSize/2);
        int minimumY = currentSize/2;
        poroX += velX;
        velY += GRAVITY;
        poroY += velY;
        if (velX < 0.01) {
            velX += friction;
        } else if (velX > 0.1) {
            velX -= friction;
        }
        if (Math.abs(velX) < friction) {
            velX = 0;
        }
        if (poroX <= minimumX || poroX >= maximumX) {
            velX *= -1;
        }
        if (poroY < minimumY) {
            velY *= -1;
        }
        if(poroY >= FLOOR) {
            poroY = FLOOR;
            if (velY >= 15) {
                updatePoroImage(PORO_LAND);
                velY *= -0.85;
            } else {
                velY = 0;
                friction = 2;
            }
            if (velX == 0 && velY == 0) {
                friction = 0.2; // Reverts back to original (air resistance) friction
                currentState = PoroState.Idling;
                updatePoroImage(PORO_IDLE);
                return;
            }
        }
        updatePoroLabel();
    }

    private void sendMessageToUser() {
        messageCountdown = MESSAGE_COUNT;
        currentSize *= PORO_MESSAGE_SCALE;

        int msgChoice = rand.nextInt(PORO_MESSAGES.length);
        System.out.println("Poro Chose to send: " + msgChoice);
        updatePoroImage(PORO_MESSAGES[msgChoice]);
        currentState = PoroState.Talking;
    }

    private void countDownTalking() {
        messageCountdown -= 1;
        if(messageCountdown <= 0) {
            currentSize /= PORO_MESSAGE_SCALE;
            updatePoroImage(PORO_IDLE);
            currentState = PoroState.Idling;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (currentState) {
            case Eating:
            case Dragged:
                return;
            case Falling:
                fallFromGravity();
                return;
            case Walking:
                walkTowardsPoint();
                return;
            case Talking:
                countDownTalking();
                return;
        }

        final int choice = rand.nextInt(5000);
        if(choice < 3) {
            sendMessageToUser();
        } else if(choice < 7) {
            startWalkingSequence();
        }
    }
}
