/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import com.first.tribes.core.being.Villager;
import java.util.List;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.Surface;
import playn.core.TextFormat;
import playn.core.TextLayout;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
class GrabTool extends Tool {

    private ImmediateLayer dragLayer;
    private ImmediateLayer selectionDisplayLayer;
    private int dragLayerIndex, selectionDisplayLayerIndex;
    private Point dragStart;
    private Point dragCurrent;
    private List<Villager> selectedVillagers;
    private List<Villager> displayedVillagers;
    private Font font;
    private Image pointerImage;

    public GrabTool(TribesWorld world) {
        super(world);
        font = graphics().createFont("Sans serif", Font.Style.BOLD, 14);
        pointerImage = assets().getImage("images/pointer.png");
        
        dragLayer = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            @Override
            public void render(Surface surface) {
                if (dragStart != null) {
                    surface.setFillColor(Color.argb(120, 50, 50, 230));
                    Point dragStartScreen = GrabTool.this.world.screenPointFromWorldPoint(dragStart);
                    Point dragCurrScreen = GrabTool.this.world.screenPointFromWorldPoint(dragCurrent);
                    surface.fillRect(dragStartScreen.x, dragStartScreen.y, dragCurrScreen.x - dragStartScreen.x, dragCurrScreen.y - dragStartScreen.y);
                    surface.setFillColor(Color.rgb(50, 50, 230));
                    surface.drawLine(dragStartScreen.x, dragStartScreen.y, dragCurrScreen.x, dragStartScreen.y, 2);
                    surface.drawLine(dragStartScreen.x, dragStartScreen.y, dragStartScreen.x, dragCurrScreen.y, 2);
                    surface.drawLine(dragCurrScreen.x, dragCurrScreen.y, dragStartScreen.x, dragCurrScreen.y, 2);
                    surface.drawLine(dragCurrScreen.x, dragCurrScreen.y, dragCurrScreen.x, dragStartScreen.y, 2);

                    selectedVillagers = GrabTool.this.world.villagersInArea(new Rectangle(dragStart, new Dimension(dragCurrent.x - dragStart.x, dragCurrent.y - dragStart.y)));
                    int numVillagers = selectedVillagers.size();

                    TextLayout layout = graphics().layoutText(numVillagers + " villagers", new TextFormat().withFont(font).withWrapWidth(200));
                    CanvasImage fpsImage = graphics().createImage((int) Math.ceil(layout.width()), (int) Math.ceil(layout.height()));
                    fpsImage.canvas().setFillColor(Color.rgb(40, 40, 170));
                    fpsImage.canvas().fillText(layout, 0, 0);
                    surface.drawImage(fpsImage, Math.max(2, Math.min(dragStartScreen.x, dragCurrScreen.x) + 2), Math.max(3, Math.min(dragStartScreen.y, dragCurrScreen.y) - (fpsImage.height() + 3)));
                }
            }
        });
        dragLayerIndex = world.addExtraLayer(dragLayer);

        selectionDisplayLayer = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            @Override
            public void render(Surface surface) {
                if (displayedVillagers != null) {
                    for (int i = 0; i < displayedVillagers.size(); i++) {
                        if (i < 10) {
                            displayedVillagers.get(i).drawStatsBoxAt(surface, 5, 10 + (Villager.STATS_BOX_HEIGHT + 5) * i, 300, Villager.STATS_BOX_HEIGHT);
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
        surface.drawImage(pointerImage, x + width * 0.125f, y + height * 0.125f, width * 0.75f, height  * 0.75f);
    }

    public String name() {
        return "Grab Tool";
    }

    public String costDescription() {
        return "Free!";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        dragStart = world.worldPointFromScreenPoint(new Point(x, y));
        dragCurrent = dragStart;

        return this;
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