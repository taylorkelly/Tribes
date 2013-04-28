/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.TribesWorld;
import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Point;

/**
 *
 * @author taylor
 */
    class SpawnTool extends Tool {

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

        @Override
        public void press(float x, float y) {
        }

        @Override
        public void release(float x, float y) {
            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
            world.villages().get(0).spawnVillager(worldPoint.x, worldPoint.y);
        }

        @Override
        public void drag(float x, float y) {
        }
    }