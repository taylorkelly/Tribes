/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.DrawnObject;
import com.first.tribes.core.Tile;
import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import java.util.Arrays;
import java.util.HashSet;
import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

/**
 *
 * @author taylor
 */
public class IrrigationTool extends Tool {

    public static final int MANNA_COST_PER_DROP = 1000;

    public IrrigationTool(TribesWorld world) {
        super(world);
    }

    @Override
    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(Color.rgb((selected ? 255 : 200), (selected ? 255 : 200), 0));
        surface.fillRect(x, y, width, height);
    }

    @Override
    public String name() {
        return "Irrigation Tool";
    }

    public String costDescription() {
        return MANNA_COST_PER_DROP + " manna";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        return this;
    }

    @Override
    public void release(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DROP) {
            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
            world.addExtraDrawnObject(new IrrigationPipe(worldPoint.x, worldPoint.y));
            world.villages().get(0).costManna(MANNA_COST_PER_DROP);
        }
    }

    @Override
    public void drag(float x, float y) {
    }

    public class IrrigationPipe extends DrawnObject {

        private float xPos;
        private float yPos;
        private final Dimension IRRIGATION_PIPE_SIZE = new Dimension(40, 40);
        public static final int HIT_RADIUS = 4;
        public static final float FOOD_CHANGE = 0.01f;

        public IrrigationPipe(float x, float y) {
            this.xPos = x - IRRIGATION_PIPE_SIZE.width / 2f;
            this.yPos = y - IRRIGATION_PIPE_SIZE.height / 2f;
        }

        @Override
        protected Rectangle bounds() {
            return new Rectangle(xPos, yPos, IRRIGATION_PIPE_SIZE.width, IRRIGATION_PIPE_SIZE.height);
        }

        @Override
        public void paintToRect(Rectangle rect, Surface surface) {
            surface.setFillColor(Color.rgb(0, 0, 0));
            surface.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        @Override
        public void update(float delta) {
            Tile myTile = world.tileAt(bounds().centerX(), bounds().centerY());
            HashSet<Tile> tiles = new HashSet<Tile>();
            tiles.add(myTile);
            for (int i = 0; i < HIT_RADIUS; i++) {
                HashSet<Tile> newTiles = new HashSet<Tile>();
                for (Tile tile : tiles) {
                    newTiles.addAll(Arrays.asList(tile.neighbors()));
                    tile.numFood += FOOD_CHANGE;
                }
                tiles.addAll(newTiles);
            }
        }
    }
}
