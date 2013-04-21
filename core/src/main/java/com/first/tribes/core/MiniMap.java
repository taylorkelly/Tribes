/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

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

    Image image() {
        if (image == null) {
            image = graphics().createImage(200, 200);
        }

        Canvas canvas = image.canvas();
        float tileWidthDensity = world.tiles.length / width();
        float tileHeightDensity = world.tiles[0].length / height();

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                int i = (int) (x * tileWidthDensity);
                int j = (int) (y * tileHeightDensity);
                canvas.setFillColor(world.tiles[i][j].color());
                canvas.fillRect(x, y, 1, 1);
            }
        }

        float pointWidthDensity = world.absoluteSize.width / width();
        float pointHeightDensity = world.absoluteSize.height / height();

        float viewPortStartX = world.viewPort.x / pointWidthDensity + 0.1f;
        float viewPortStartY = world.viewPort.y / pointHeightDensity + 1.0f;
        float viewPortEndX = (world.viewPort.x + world.viewPort.width) / pointWidthDensity - 1.0f;
        float viewPortEndY = (world.viewPort.y + world.viewPort.height) / pointHeightDensity - 0.1f;


        float villageXPos = world.village.xPos() / pointWidthDensity;
        float villageYPos = world.village.yPos() / pointHeightDensity;
        canvas.setFillColor(Color.rgb(0, 0, 255));
        canvas.fillCircle(villageXPos, villageYPos, 3);

        canvas.setStrokeColor(Color.rgb(255, 0, 0));
        canvas.drawLine(viewPortStartX, viewPortStartY, viewPortStartX, viewPortEndY);
        canvas.drawLine(viewPortStartX, viewPortStartY, viewPortEndX, viewPortStartY);
        canvas.drawLine(viewPortEndX, viewPortEndY, viewPortStartX, viewPortEndY);
        canvas.drawLine(viewPortEndX, viewPortEndY, viewPortEndX, viewPortStartY);


        return image;
    }

    final float MAX_DIMENSION_SIZE() {
        return 150f;
    }

    float height() {
        float scale = world.width() > world.height() ? MAX_DIMENSION_SIZE() / world.width() : MAX_DIMENSION_SIZE() / world.height();

        return world.height() * scale;
    }

    float width() {
        float scale = world.width() > world.height() ? MAX_DIMENSION_SIZE() / world.width() : MAX_DIMENSION_SIZE() / world.height();
        return world.width() * scale;
    }
}
