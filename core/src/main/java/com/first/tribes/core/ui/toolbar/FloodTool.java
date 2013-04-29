package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tile;
import com.first.tribes.core.Tribes;
import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import com.first.tribes.core.util.Timer;
import com.first.tribes.core.util.Timer.TimerTask;

import playn.core.Color;
import playn.core.Surface;

public class FloodTool extends Tool {

    public static final int MANNA_COST_PER_DELTA = 50;
    private static final float WATER_LEVEL_DELTA = 1f;
    private static final long TIMER_DELAY = 200;

    public FloodTool(TribesWorld world) {
        super(world);
    }

    @Override
    public void render(Surface surface, float x, float y, float width,
            float height) {
        surface.setFillColor(Color.rgb(0, 0, (selected ? 255 : 200)));
        surface.fillRect(x, y, width, height);

    }

    @Override
    public String name() {
        return "Flood Tool";
    }
    
    public final int MANNA_COST_PER_DELTA() {
        return (int)(50 + Math.pow((world.waterLevel - TribesWorld.NORMAL_WATER_LEVEL)*10,2));
    }

    public String costDescription() {
        return MANNA_COST_PER_DELTA() + " manna/update";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DELTA()) {
            if (Tribes.SHIFT) {
                world.waterLevel -= WATER_LEVEL_DELTA;
            } else {
                world.waterLevel += WATER_LEVEL_DELTA;
            }
            world.villages().get(0).costManna(MANNA_COST_PER_DELTA());
        }

        return this;
    }

    @Override
    public void release(float x, float y) {
        final Timer timer = new Timer(world.game());
        timer.schedule(new TimerTask() {
            public void run() {
                if (world.waterLevel < TribesWorld.NORMAL_WATER_LEVEL) {
                    world.waterLevel += WATER_LEVEL_DELTA;
                } else if (world.waterLevel > TribesWorld.NORMAL_WATER_LEVEL) {
                    world.waterLevel -= WATER_LEVEL_DELTA;
                } else {
                    timer.cancel();
                }
            }
        }, TIMER_DELAY, TIMER_DELAY);
    }

    @Override
    public void drag(float x, float y) {
        this.press(x, y);
    }
}
