/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import java.util.List;
import playn.core.CanvasImage;
import playn.core.CanvasLayer;
import playn.core.Color;
import playn.core.Font;
import playn.core.ImageLayer;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;
import static playn.core.PlayN.*;
import playn.core.TextFormat;
import playn.core.TextLayout;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

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

    float x() {
        return (Tribes.SCREEN_WIDTH - width()) / 2;
    }

    float y() {
        return (Tribes.SCREEN_HEIGHT - 20) - height();
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
        if (x > x() && x < x() + width() && y > y() && y < y() + height()) {
            int index = (int) ((x - x()) / 50);
            tools[activeTool].selected = false;
            activeTool = index;
            tools[activeTool].selected = true;
        } else {
            tools[activeTool].press(x, y);
        }
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
        private ImmediateLayer selectionDisplayLayer;
        private int dragLayerIndex, selectionDisplayLayerIndex;
        Point dragStart;
        Point dragCurrent;
        List<Villager> selectedVillagers;
        List<Villager> displayedVillagers;

        public GrabTool() {
            dragLayer = graphics().createImmediateLayer(new Renderer() {
                @Override
                public void render(Surface surface) {
                    if (dragStart != null) {
                        surface.setFillColor(Color.argb(120, 50, 50, 230));
                        Point dragStartScreen = world.screenPointFromWorldPoint(dragStart);
                        Point dragCurrScreen = world.screenPointFromWorldPoint(dragCurrent);
                        surface.fillRect(dragStartScreen.x, dragStartScreen.y, dragCurrScreen.x - dragStartScreen.x, dragCurrScreen.y - dragStartScreen.y);
                        surface.setFillColor(Color.rgb(50, 50, 230));
                        surface.drawLine(dragStartScreen.x, dragStartScreen.y, dragCurrScreen.x, dragStartScreen.y, 2);
                        surface.drawLine(dragStartScreen.x, dragStartScreen.y, dragStartScreen.x, dragCurrScreen.y, 2);
                        surface.drawLine(dragCurrScreen.x, dragCurrScreen.y, dragStartScreen.x, dragCurrScreen.y, 2);
                        surface.drawLine(dragCurrScreen.x, dragCurrScreen.y, dragCurrScreen.x, dragStartScreen.y, 2);

                        selectedVillagers = world.village.villagersInArea(new Rectangle(dragStart, new Dimension(dragCurrent.x - dragStart.x, dragCurrent.y - dragStart.y)));
                        int numVillagers = selectedVillagers.size();

                        int fontColor = Color.rgb(40, 40, 170);
                        int fontSize = 14;
                        Font font = graphics().createFont("Sans serif", Font.Style.BOLD, fontSize);
                        TextLayout layout = graphics().layoutText(numVillagers + " villagers", new TextFormat().withFont(font).withWrapWidth(200));
                        CanvasImage fpsImage = graphics().createImage((int) Math.ceil(layout.width()), (int) Math.ceil(layout.height()));
                        fpsImage.canvas().setFillColor(fontColor);
                        fpsImage.canvas().fillText(layout, 0, 0);
                        surface.drawImage(fpsImage, Math.max(2, Math.min(dragStartScreen.x, dragCurrScreen.x) + 2), Math.max(3, Math.min(dragStartScreen.y, dragCurrScreen.y) - (fpsImage.height() + 3)));
                    }
                }
            });
            dragLayerIndex = world.addExtraLayer(dragLayer);

            selectionDisplayLayer = graphics().createImmediateLayer(new Renderer() {
                @Override
                public void render(Surface surface) {
                    if (displayedVillagers != null) {
                        for (int i = 0; i < displayedVillagers.size(); i++) {
                            if (i < 10) {
                                displayedVillagers.get(i).drawInfoAt(surface, 5, 10 + 85 * i, 300, 80);
                            }
                        }
                    }
                }
            });
            selectionDisplayLayerIndex = world.addExtraLayer(selectionDisplayLayer);
        }


        public void render(Surface surface, float x, float y, float width, float height) {
            surface.setFillColor(Color.rgb((selected ? 255 : 200), 0, 0));
            surface.fillRect(x, y, width, height);
        }

        public String name() {
            return "Grab Tool";
        }

        @Override
        public void press(float x, float y) {
            dragStart = world.worldPointFromScreenPoint(new Point(x, y));
            dragCurrent = dragStart;
        }

        @Override
        public void release(float x, float y) {
            displayedVillagers = selectedVillagers;
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
