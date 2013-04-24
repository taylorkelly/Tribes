/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font;
import playn.core.Gradient;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import static playn.core.PlayN.*;
import playn.core.TextFormat;
import playn.core.TextLayout;

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
    private boolean dead;
    float hunger;
    String name;
    int number;
    static int villagerCount = 0;

    public Villager(float xPos, float yPos, Village village) {
        super(xPos, yPos, VILLAGER_SIZE, VILLAGER_SIZE);
        this.village = village;
        this.xVel = random() - 0.5f;
        this.yVel = random() - 0.5f;
        name = genName();
        number = ++villagerCount;
    }
    final static String firstSounds[] = {"'Ai", "Ali'", "Al", "'Au", "'Eh", "Ha'", "Ha", "Hi'", "Ho'", "'Io", "Ka'", "Ka", "Kai", "Ke'", "Ke", "Ki", "Ko", "Ku", "Ku'", "La'", "La", "Lei", "Li", "Lo", "Lu", "Ma", "Me", "Mi", "Mo", "Na'", "Nai'", "No", "Ona", "Pa", "Pi'", "Po'", "Pu", "U'", "Ulu", "Wa"};
    final static String laterSounds[] = {"la", "loa", "ka", "ne", "na", "kai", "hu", "wa", "ok", "ni", "pa", "ke", "leo", "le", "mi", "mue", "pe", "ma", "mo", "ki", "lo", "pau", "nu", "ke"};

    public static String genName() {
        String name = firstSounds[(int) (random() * firstSounds.length)];

        int additionalPieces = (int) ((random() * 4) + 1);
        for (int i = 0; i < additionalPieces; i++) {
            name += laterSounds[(int) (random() * laterSounds.length)];
        }

        return name;
    }

    public void paintToRect(Rectangle rect, Surface surface) {
        surface.setFillColor(color);
        surface.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    void update(float delta) {
        age += delta;

        if (age >= this.personality.longevity() * MAX_AGE) {
            setDead(true);
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
        if (village.isUnsafe(xPos, yPos)) {
            setDead(true);
        }

        if (random() < 0.05) {
            xVel = (random() - 0.5f);
            yVel = (random() - 0.5f);
        }
        hunger += 0.5;

        float food = village.gatherFood(this, Math.max(hunger, 0.5f));

        hunger -= food;

        if (hunger > 20) {
            setDead(true);
        }
    }

    private void setDead(boolean dead) {
        if (this.dead != dead) {
            this.dead = dead;
            visualInfo = null; // Invalidate visualInfo
        }
    }

    public boolean isDead() {
        return dead;
    }

    public String toString() {
        return personality.toString();
    }
    CanvasImage visualInfo;

    void drawInfoAt(Surface surface, int x, int y, int width, int height) {
        if (visualInfo == null) {
            visualInfo = graphics().createImage((int) width, (int) height);
            visualInfo.canvas().setFillGradient(graphics().createLinearGradient(width, 0, width, height, new int[]{Color.rgb(50, 50, 50), Color.rgb(0, 0, 0)}, new float[]{0, 1}));
            visualInfo.canvas().fillRoundRect(0, 0, width, height, 10);

            visualInfo.canvas().setFillColor(this.color);
            visualInfo.canvas().fillRoundRect(4, 4, width - 8, height - 8, 7);

            Font titleFont = graphics().createFont("Sans serif", Font.Style.PLAIN, 18);
            TextLayout nameLayout = graphics().layoutText(name, new TextFormat().withFont(titleFont).withWrapWidth(200));
            visualInfo.canvas().setFillColor(Color.argb(200, 255, 255, 255));
            visualInfo.canvas().fillText(nameLayout, 8, 4);

            if (dead) {
                Font deadFont = graphics().createFont("Sans serif", Font.Style.BOLD, 16);
                TextLayout deadLayout = graphics().layoutText("(DEAD)", new TextFormat().withFont(deadFont).withWrapWidth(200));
                visualInfo.canvas().setFillColor(Color.argb(255, 255, 0, 0));
                visualInfo.canvas().fillText(deadLayout, 14 + nameLayout.width(), 4 + nameLayout.height() - deadLayout.height());
            }


        }
        surface.drawImage(visualInfo, x, y);
    }
}
