/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tile;
import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import java.util.Arrays;
import java.util.HashSet;
import playn.core.Color;
import playn.core.Image;
import playn.core.PlayN;
import playn.core.Surface;
import pythagoras.f.Point;

/**
 *
 * @author taylor
 */
public class FoodTool extends Tool {

    public static final int HIT_RADIUS = 7;
    public static final float FOOD_AMOUNT = 4.0f;
    public static final int MANNA_COST_PER_DROP = 200;
    private Image foodImage;

    public FoodTool(TribesWorld world) {
        super(world);
        foodImage = PlayN.assets().getImage("images/food.png");
    }

    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(Color.rgb(0, (selected ? 255 : 200), 0));
        surface.fillRect(x, y, width, height);
        surface.drawImage(foodImage, x + width * 0.05f, y + height * 0.05f, width * 0.9f, height * 0.9f);
    }

    public String name() {
        return "Food Tool";
    }

    public String costDescription() {
        return MANNA_COST_PER_DROP + " manna";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        return this;
    }

    public void dropFood(float x, float y) {
        HashSet<Tile> tiles = new HashSet<Tile>();

        tiles.add(world.tileAt(x, y));
        for (int i = 0; i < HIT_RADIUS; i++) {
            HashSet<Tile> newTiles = new HashSet<Tile>();
            for (Tile tile : tiles) {
                newTiles.addAll(Arrays.asList(tile.neighbors()));
                tile.numFood += FOOD_AMOUNT;
            }
            tiles.addAll(newTiles);
        }
    }

    @Override
    public void release(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DROP) {
            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
            dropFood(worldPoint.x, worldPoint.y);
            world.villages().get(0).costManna(MANNA_COST_PER_DROP);
        }
    }

    @Override
    public void drag(float x, float y) {
//            press(x, y);
    }
}