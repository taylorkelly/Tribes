/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import com.first.tribes.core.util.Updatee;
import playn.core.Surface;
import pythagoras.f.Rectangle;

/**
 *
 * @author taylor
 */
public abstract class DrawnObject implements Updatee {
    
    protected final Rectangle adjustedRectForViewPort(Rectangle viewPort, float scale) {
        Rectangle objectRect = bounds();
        if (viewPort.intersects(objectRect)) {
            float x = (objectRect.x - viewPort.x) * scale;
            float y = (objectRect.y - viewPort.y) * scale;
            float width = objectRect.width * scale;
            float height = objectRect.height * scale;
            return new Rectangle(x, y, width, height);
        } else {
            return null;
        }
    }

    protected abstract Rectangle bounds();

    public final void paint(Surface surface, Rectangle viewPort, float scale) {
        Rectangle objectRect = adjustedRectForViewPort(viewPort, scale);
        if (objectRect != null) {
            paintToRect(objectRect, surface);
        }
    }
    
    public abstract void paintToRect(Rectangle rect, Surface surface);
}
