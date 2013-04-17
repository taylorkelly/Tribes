/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class Villager extends Being {

    public static final int MAX_AGE = 50000;
    private static final float VILLAGER_SIZE = 10.0f;
    private Village village;
    int color = Color.rgb((int) (random() * 128), (int) (random() * 128), (int) (random() * 128));
    int age;
    boolean dead;

    public Villager(float xPos, float yPos, Village village) {
        super(xPos, yPos, VILLAGER_SIZE, VILLAGER_SIZE);
        this.village = village;
        this.xVel = random() - 0.5f;
        this.yVel = random() - 0.5f;
    }

    public void paintToRect(Rectangle rect, Surface surface) {
        surface.setFillColor(color);
        surface.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    void update(float delta) {
        age += delta;

        if (age >= this.personality.longevity() * MAX_AGE) {
            dead = true;
        }

        if (!dead) {
            xPos += xVel * delta;
            yPos += yVel * delta;
            if (xPos < 0 || xPos > village.worldWidth()) {
                xVel *= -1;
                xPos += xVel * delta;
            }
            if (yPos < 0 || yPos > village.worldHeight()) {
                yVel *= -1;
                yPos += yVel * delta;
            }
        }
        if (village.isUnsafe(xPos, yPos)) {
            xVel *= -1;
            xPos += xVel * delta;
            yVel *= -1;
            yPos += yVel * delta;
        }
        if(village.isUnsafe(xPos, yPos)) {
            dead = true;
        }
    }

    public String toString() {
        return personality.toString();
    }
}
