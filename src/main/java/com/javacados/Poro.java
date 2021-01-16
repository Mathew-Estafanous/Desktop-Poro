package com.javacados;

public class Poro {
    private int food;
    private int size;

    public Poro(int food, int size) {
        this.food = food;
        this.size = size;
    }

    public int getFood() {
        return food;
    }

    public void grow(int food) {
        this.food += food;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
