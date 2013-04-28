/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tile;
import com.first.tribes.core.Tribes;
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
public class PushPullTool extends Tool {

    public static final int HIT_RADIUS = 4;
    public static final float HEIGHT_CHANGE = 1.0f;

    public PushPullTool(TribesWorld world) {
        super(world);
    }

    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(Color.rgb((selected ? 255 : 200), (selected ? 255 : 200), 0));
        surface.fillRect(x, y, width, height);
    }

    public String name() {
        return "Push/Pull Tool";
    }

    @Override
    public void press(float x, float y) {
        float heightChange = HEIGHT_CHANGE;
        if (Tribes.SHIFT) {
            heightChange = -heightChange;
        }

        Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
        HashSet<Tile> tiles = new HashSet<Tile>();

        tiles.add(world.tileAt(worldPoint.x, worldPoint.y));
        for (int i = 0; i < HIT_RADIUS; i++) {
            HashSet<Tile> newTiles = new HashSet<Tile>();
            for (Tile tile : tiles) {
                newTiles.addAll(Arrays.asList(tile.neighbors()));
                tile.setHeight(tile.height() + heightChange);
            }
            tiles.addAll(newTiles);
        }
    }

    @Override
    public void release(float x, float y) {
    }

    @Override
    public void drag(float x, float y) {
        press(x, y);
    }
}