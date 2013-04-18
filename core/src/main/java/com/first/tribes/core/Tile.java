/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import com.first.tribes.core.util.Updatee;
import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Rectangle;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class Tile extends DrawnObject implements Updatee {

    public static final float TILE_SIZE = 100f;
    private int xIndex;
    private int yIndex;
//    int color = Color.rgb((int)(random()/2*128), (int)(random()/2*128), (int)(random()/2*128));
    private float height = 2;
    private static final float WATER_FOOD_INCREMENT = 0.0001f;
    private static final float FOOD_SPREAD_MULTIPLIER = 0.00005f;
    float numFood = 0;
    Tile[][] tileMap;

    public void setHeight(float height) {
        if (Math.abs(this.height - height) > 0.6) {
            this.height = height;

            if (xIndex - 1 >= 0) {
                tileMap[xIndex - 1][yIndex].smoothHeight();
            }
            if (xIndex + 1 < tileMap.length) {
                tileMap[xIndex + 1][yIndex].smoothHeight();
            }
            if (yIndex - 1 >= 0) {
                tileMap[xIndex][yIndex - 1].smoothHeight();
            }
            if (yIndex + 1 < tileMap[0].length) {
                tileMap[xIndex][yIndex + 1].smoothHeight();
            }

        }
    }

    int color() {
        if (height >= 0) {
            if (height <= 40) {
                // flat plains up to mountains
                return Color.rgb((int) (255 - height * 4 - this.numFood *2 < 0 ? 0 : 255 - height * 4 - this.numFood *2), (int) (200 - height * 5 + (this.numFood) > 200 ? 200 : 200 - height * 5 + (this.numFood)), (int) (190 - height * 8 - this.numFood*2 < 0 ? 0 : 190 - height * 8 - this.numFood*2));
            } else {
                // snow capped mountains
                if (height <= 65) {
                    return Color.rgb((int) (230 + (height - 40)), (int) (230 + (height - 40)), (int) (200 + (height * 2 - 80)));
                } else {
                    return Color.rgb(0, 0, 0);
                }
            }
        } else {
            // Water
            return Color.rgb((int) (200 + height * 2), (int) (200 + height * 2), (int) (255 + height));
        }
    }

    public float height() {
        return height;
    }

    public void smoothHeight() {
        int count = 3;

        float newHeight = (height * height * height) * 3;
        if (xIndex - 1 >= 0) {
            newHeight += tileMap[xIndex - 1][yIndex].height * tileMap[xIndex - 1][yIndex].height * tileMap[xIndex - 1][yIndex].height;
            count++;
        }
        if (xIndex + 1 < tileMap.length) {
            newHeight += tileMap[xIndex + 1][yIndex].height * tileMap[xIndex + 1][yIndex].height * tileMap[xIndex + 1][yIndex].height;
            count++;
        }
        if (yIndex - 1 >= 0) {
            newHeight += tileMap[xIndex][yIndex - 1].height * tileMap[xIndex][yIndex - 1].height * tileMap[xIndex][yIndex - 1].height;
            count++;
        }
        if (yIndex + 1 < tileMap[0].length) {
            newHeight += tileMap[xIndex][yIndex + 1].height * tileMap[xIndex][yIndex + 1].height * tileMap[xIndex][yIndex + 1].height;
            count++;
        }
        newHeight /= count;


        float mult = newHeight < 0 ? -1 : 1;

        this.setHeight((float) Math.pow(newHeight * mult, 0.333333) * mult);
    }

    public Tile(int xIndex, int yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;

    }

    public void paintToRect(Rectangle rect, Surface surface) {
        surface.setFillColor(color());
        surface.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    public Rectangle bounds() {
        return new Rectangle(xIndex * TILE_SIZE, yIndex * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void update(float delta) {
        if (xIndex - 1 >= 0) {
            if (tileMap[xIndex - 1][yIndex].height() < 0) {
                this.numFood += WATER_FOOD_INCREMENT * delta / (this.numFood + 1);
            } else if (tileMap[xIndex - 1][yIndex].numFood > 0) {
                this.numFood += FOOD_SPREAD_MULTIPLIER * tileMap[xIndex - 1][yIndex].numFood * delta / (this.numFood + 1);
            }
        }
        if (xIndex + 1 < tileMap.length) {
            if (tileMap[xIndex + 1][yIndex].height() < 0) {
                this.numFood += WATER_FOOD_INCREMENT * delta / (this.numFood + 1);
            } else if (tileMap[xIndex + 1][yIndex].numFood > 0) {
                this.numFood += FOOD_SPREAD_MULTIPLIER * tileMap[xIndex + 1][yIndex].numFood * delta / (this.numFood + 1);
            }
        }
        if (yIndex - 1 >= 0) {
            if (tileMap[xIndex][yIndex - 1].height() < 0) {
                this.numFood += WATER_FOOD_INCREMENT * delta / (this.numFood + 1);
            } else if (tileMap[xIndex][yIndex - 1].numFood > 0) {
                this.numFood += FOOD_SPREAD_MULTIPLIER * tileMap[xIndex][yIndex - 1].numFood * delta / (this.numFood + 1);
            }
        }
        if (yIndex + 1 < tileMap[0].length) {
            if (tileMap[xIndex][yIndex + 1].height() < 0) {
                this.numFood += WATER_FOOD_INCREMENT * delta / (this.numFood + 1);
            } else if (tileMap[xIndex][yIndex + 1].numFood > 0) {
                this.numFood += FOOD_SPREAD_MULTIPLIER * tileMap[xIndex][yIndex + 1].numFood * delta / (this.numFood + 1);
            }
        }
    }
}
