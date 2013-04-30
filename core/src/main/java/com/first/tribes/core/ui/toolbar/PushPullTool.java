/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tile;
import com.first.tribes.core.Tribes;
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
public class PushPullTool extends Tool {

    public static final int MANNA_COST_PER_DELTA = 100;
    public static final int HIT_RADIUS = 4;
    public static final float HEIGHT_CHANGE = 1.0f;
    private Image mountainImage;

    public PushPullTool(TribesWorld world) {
        super(world);
        mountainImage = PlayN.assets().getImage("images/mountain.png");

    }

    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(Color.rgb((selected ? 255 : 200), (selected ? 255 : 200), 0));
        surface.fillRect(x, y, width, height);
        surface.drawImage(mountainImage, x + width * 0.125f, y + height * 0.125f, width * 0.75f, height  * 0.75f);
    }

    public String name() {
        return "Push/Pull Tool";
    }

    public String costDescription() {
        return MANNA_COST_PER_DELTA + " manna/update";
    }

    public void bulldoze(Point p, boolean up) {
        HashSet<Tile> tiles = new HashSet<Tile>();

        tiles.add(world.tileAt(p.x, p.y));
        for (int i = 0; i < HIT_RADIUS; i++) {
            HashSet<Tile> newTiles = new HashSet<Tile>();
            for (Tile tile : tiles) {
                newTiles.addAll(Arrays.asList(tile.neighbors()));
                tile.setHeight(tile.height() + (up ? HEIGHT_CHANGE : -1 * HEIGHT_CHANGE));
            }
            tiles.addAll(newTiles);
        }
    }

    @Override
    public PointerFocusable press(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DELTA) {

            float heightChange = HEIGHT_CHANGE;
            if (Tribes.SHIFT) {
                heightChange = -heightChange;
            }

            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
            bulldoze(worldPoint, !Tribes.SHIFT);
            world.villages().get(0).costManna(MANNA_COST_PER_DELTA);
        }
        return this;
    }

    @Override
    public void release(float x, float y) {
    }

    @Override
    public void drag(float x, float y) {
        press(x, y);
    }
}