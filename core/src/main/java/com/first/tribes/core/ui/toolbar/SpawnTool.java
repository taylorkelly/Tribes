/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Point;

/**
 *
 * @author taylor
 */
public class SpawnTool extends Tool {
    public static final int MANNA_COST_PER_DROP = 100;
    public static final int SPAWN_TYPE = 0;

    public SpawnTool(TribesWorld world) {
        super(world);
    }

    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(Color.rgb(0, (selected ? 255 : 200), (selected ? 255 : 200)));
        surface.fillRect(x, y, width, height);
    }

    public String name() {
        return "Spawn Tool";
    }

    public String costDescription() {
        return MANNA_COST_PER_DROP + " manna";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        return this;
    }

    public void spawn(float x, float y, int village){
    	 world.villages().get(village).spawnVillager(x, y);
    }
    
    @Override
    public void release(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DROP) {
            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
            spawn(worldPoint.x,worldPoint.y,0);
            world.villages().get(0).costManna(MANNA_COST_PER_DROP);
            world.ping(worldPoint);
        }

    }

    @Override
    public void drag(float x, float y) {
    }
}