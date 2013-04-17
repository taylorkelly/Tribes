/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;



import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;
import playn.core.TextFormat;
import playn.core.TextLayout;


import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class FPSRenderer implements Renderer {

    private long last = System.currentTimeMillis();
    private long prevUpdate = -1;
    private CanvasImage fpsImage;

    public void render(Surface surface) {
        if (prevUpdate < System.currentTimeMillis() - 500) {
            long now = System.currentTimeMillis();
            long deltaMillis = now - last;
            long fps = (long) (1000.0 / deltaMillis);
            String text = fps + " fps";

            int fontColor = Color.rgb(255, 0, 0);
            int fontSize = 13;
            Font font = graphics().createFont("Sans serif", Font.Style.PLAIN, fontSize);
            TextLayout layout = graphics().layoutText(text, new TextFormat().withFont(font).withWrapWidth(200));
            fpsImage = graphics().createImage((int) Math.ceil(layout.width()),
                    (int) Math.ceil(layout.height()));
            fpsImage.canvas().setFillColor(fontColor);
            fpsImage.canvas().fillText(layout, 0, 0);

            prevUpdate = System.currentTimeMillis();
        }
        surface.drawImage(fpsImage, 0, 0);


        last = System.currentTimeMillis();
    }
}
