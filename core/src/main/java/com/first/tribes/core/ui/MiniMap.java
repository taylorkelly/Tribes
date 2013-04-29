/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui;

import com.first.tribes.core.Tribes;
import com.first.tribes.core.TribesWorld;
import com.first.tribes.core.being.Village;
import com.first.tribes.core.being.Villager;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Image;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class MiniMap {

    private CanvasImage image;
    private TribesWorld world;

    public MiniMap(TribesWorld world) {
        this.world = world;
    }

    public Image image() {
        if (image == null) {
            image = graphics().createImage(width(), height());
        }

        Canvas canvas = image.canvas();
        float tileWidthDensity = world.tiles().length / width();
        float tileHeightDensity = world.tiles()[0].length / height();

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                int i = (int) (x * tileWidthDensity);
                int j = (int) (y * tileHeightDensity);
                canvas.setFillColor(world.tiles()[i][j].color());
                canvas.fillRect(x, y, 1, 1);
            }
        }

        float viewPortStartX = world.viewPort().x / pointDensity() + 0.1f;
        float viewPortStartY = world.viewPort().y / pointDensity() + 1.0f;
        float viewPortEndX = (world.viewPort().x + world.viewPort().width) / pointDensity() - 1.0f;
        float viewPortEndY = (world.viewPort().y + world.viewPort().height) / pointDensity() - 0.1f;


        for (Village village : world.villages()) {
            int brightenedVillageColor = Color.rgb((int) (Color.red(village.color()) * 1.25), (int) (Color.green(village.color()) * 1.25), (int) (Color.blue(village.color()) * 1.25));
            canvas.setFillColor(Color.withAlpha(brightenedVillageColor, 15));
            for (Villager villager : village.villagers()) {
                canvas.fillCircle(villager.xPos() / pointDensity(), villager.yPos() / pointDensity(), 10);
            }
        }

        canvas.setStrokeColor(Color.rgb(255, 0, 0));
        canvas.drawLine(viewPortStartX, viewPortStartY, viewPortStartX, viewPortEndY);
        canvas.drawLine(viewPortStartX, viewPortStartY, viewPortEndX, viewPortStartY);
        canvas.drawLine(viewPortEndX, viewPortEndY, viewPortStartX, viewPortEndY);
        canvas.drawLine(viewPortEndX, viewPortEndY, viewPortEndX, viewPortStartY);


        return image;
    }
    
    float pointDensity() {
        return world.absoluteSize().width / width();
    }

    private final float MAX_DIMENSION_SIZE() {
        return 250f;
    }

    public float height() {
        float scale = world.width() > world.height() ? MAX_DIMENSION_SIZE() / world.width() : MAX_DIMENSION_SIZE() / world.height();

        return world.height() * scale;
    }

    public float width() {
        float scale = world.width() > world.height() ? MAX_DIMENSION_SIZE() / world.width() : MAX_DIMENSION_SIZE() / world.height();
        return world.width() * scale;
    }
    
    public float x() {
        return Tribes.SCREEN_WIDTH - (width() + 10);
    }
    
    public float y() {
        return 10;
    }

    public boolean pointInMiniMap(float x, float y) {
        return x > x() && x < x() + width() && y > y() && y < y() + height();
    }

    public void press(float x, float y) {
        float worldX = (x - x()) * pointDensity();
        float worldY = (y - y()) * pointDensity();
        
        world.viewPort().x = worldX - world.viewPort().width/2;
        world.viewPort().y = worldY - world.viewPort().height/2;
    }

    public void release(float x, float y) {
    }

    public void drag(float x, float y) {
        press(x, y);
    }
}
