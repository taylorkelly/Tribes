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
import playn.core.Surface;
import pythagoras.f.Point;

/**
 *
 * @author taylor
 */
class FoodTool extends Tool {

    public static final int HIT_RADIUS = 7;
    public static final float FOOD_AMOUNT = 4.0f;
    
    public FoodTool(TribesWorld world) {
        super(world);
    }

    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(Color.rgb(0, (selected ? 255 : 200), 0));
        surface.fillRect(x, y, width, height);
    }

    public String name() {
        return "Push/Pull Tool";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        return this;
    }

    @Override
    public void release(float x, float y) {
        Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
        HashSet<Tile> tiles = new HashSet<Tile>();

        tiles.add(world.tileAt(worldPoint.x, worldPoint.y));
        for (int i = 0; i < HIT_RADIUS; i++) {
            HashSet<Tile> newTiles = new HashSet<Tile>();
            for (Tile tile : tiles) {
                newTiles.addAll(Arrays.asList(tile.neighbors()));
                tile.numFood += FOOD_AMOUNT;
            }
            tiles.addAll(newTiles);
        }
//            for (Tile tile : tiles) {
//                tile.numFood += FOOD_AMOUNT;
//            }
    }

    @Override
    public void drag(float x, float y) {
//            press(x, y);
    }
}