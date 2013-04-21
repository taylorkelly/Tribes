/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import playn.core.Color;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;
import static playn.core.PlayN.*;
import pythagoras.f.Point;

/**
 *
 * @author taylor
 */
class Toolbar {

    public boolean shift;
    private TribesWorld world;
    private Tool[] tools;
    private int activeTool;

    Toolbar(TribesWorld world) {
        this.world = world;
        tools = new Tool[6];
        tools[0] = new GrabTool();
        tools[1] = new PushPullTool();
        tools[0].selected = true;
        activeTool = 0;
    }

    float width() {
        return Tribes.SCREEN_WIDTH / 2.0f;
    }

    float height() {
        return 50.0f;
    }

    void render(Surface surface) {
        surface.setFillColor(Color.rgb(0, 0, 0));
        surface.fillRect(0, 0, width(), height());

        for (int i = 0; i < tools.length; i++) {
            Tool tool = tools[i];

            if (tool != null) {
                tool.render(surface, i * 50f, 0, 50f, 50f);
            }
        }
    }

    void press(float x, float y) {
        tools[activeTool].press(x, y);
    }

    void release(float x, float y) {
        tools[activeTool].release(x, y);
    }

    void drag(float x, float y) {
        tools[activeTool].drag(x, y);
    }

    private abstract class Tool {

        boolean selected;

        public abstract void render(Surface surface, float x, float y, float width, float height);

        public abstract String name();

        public abstract void press(float x, float y);

        public abstract void release(float x, float y);

        public abstract void drag(float x, float y);
    }

    class GrabTool extends Tool {

        private ImmediateLayer dragLayer;
        private int dragLayerIndex;
        Point dragStart;
        Point dragCurrent;

        public void render(Surface surface, float x, float y, float width, float height) {
            surface.setFillColor(Color.rgb((selected ? 255 : 200), 0, 0));
            surface.fillRect(x, y, width, height);
            dragLayer = graphics().createImmediateLayer(new Renderer() {
                @Override
                public void render(Surface surface) {
                    if (dragStart != null) {
                        surface.setFillColor(Color.argb(120, 0, 0, 255));
                        Point dragStartScreen = world.screenPointFromWorldPoint(dragStart);
                        Point dragCurrScreen = world.screenPointFromWorldPoint(dragCurrent);

                        surface.fillRect(dragStartScreen.x, dragStartScreen.y, dragCurrScreen.x - dragStartScreen.x, dragCurrScreen.y - dragStartScreen.y);
                    }
                }
            });

        }

        public String name() {
            return "Grab Tool";
        }

        @Override
        public void press(float x, float y) {
            dragStart = world.worldPointFromScreenPoint(new Point(x, y));
            dragCurrent = dragStart;
            dragLayerIndex = world.addExtraLayer(dragLayer);
        }

        @Override
        public void release(float x, float y) {
            world.removeExtraLayer(dragLayerIndex);
            dragStart = null;
            dragCurrent = null;
        }

        @Override
        public void drag(float x, float y) {
            dragCurrent = world.worldPointFromScreenPoint(new Point(x, y));
        }
    }

    class PushPullTool extends Tool {

        public void render(Surface surface, float x, float y, float width, float height) {
            surface.setFillColor(Color.rgb(0, (selected ? 255 : 200), 0));
            surface.fillRect(x, y, width, height);
        }

        public String name() {
            return "Push/Pull Tool";
        }

        @Override
        public void press(float x, float y) {
            if (!shift) {
                world.pull(x, y);
            } else {
                world.push(x, y);
            }
        }

        @Override
        public void release(float x, float y) {
        }

        @Override
        public void drag(float x, float y) {
            if (!shift) {
                world.pull(x, y);
            } else {
                world.push(x, y);
            }
        }
    }
}
