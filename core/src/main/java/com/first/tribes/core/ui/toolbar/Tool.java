/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import playn.core.Surface;

public abstract class Tool implements PointerFocusable {
    protected boolean selected;
    protected final TribesWorld world;
    
    public Tool(TribesWorld world) {
        this.world = world;
    }

    public abstract void render(Surface surface, float x, float y, float width, float height);

    public abstract String name();
    
    public abstract String costDescription();

    public abstract PointerFocusable press(float x, float y);

    public abstract void release(float x, float y);

    public abstract void drag(float x, float y);

    public static float width() {
        return 50f;
    }

    public static float height() {
        return 50f;
    }
}