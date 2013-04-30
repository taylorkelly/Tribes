/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.DrawnObject;
import com.first.tribes.core.IrrigationPipe;
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

    public void dropIrrigation(Point p){
    	 IrrigationPipe pipe = new IrrigationPipe(p.x, p.y, world);
         pipe.addToWorld();
    }
    
    @Override
    public void release(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DROP) {
            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
           dropIrrigation(worldPoint);
            world.villages().get(0).costManna(MANNA_COST_PER_DROP);
        }
    }

    @Override
    public void drag(float x, float y) {
    }
}
