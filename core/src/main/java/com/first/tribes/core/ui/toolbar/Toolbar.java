package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tribes;
import com.first.tribes.core.TribesWorld;
import java.util.ArrayList;
import java.util.List;
import playn.core.Color;
import playn.core.Surface;


/**
 *
 * @author taylor
 */
public class Toolbar {
    private TribesWorld world;
    private List<Tool> tools;
    private int activeTool;

    public Toolbar(TribesWorld world) {
        this.world = world;
        tools = new ArrayList<Tool>(10);
        tools.add(new GrabTool(world));
        tools.add(new PushPullTool(world));
        tools.add(new FoodTool(world));
        tools.add(new SpawnTool(world));
        tools.get(0).selected = true;
        activeTool = 0;
    }

    public float width() {
        return (tools.size() * Tool.width()) + 10f;
    }

    public float height() {
        return Tool.height() + 10f;
    }

    public float x() {
        return (Tribes.SCREEN_WIDTH - width()) / 2;
    }

    public float y() {
        return (Tribes.SCREEN_HEIGHT - 20) - height();
    }

    public void render(Surface surface) {
        surface.setFillColor(Color.rgb(0, 0, 0));
        surface.fillRect(0, 0, width(), height());

        for (int i = 0; i < tools.size(); i++) {
            Tool tool = tools.get(i);

            if (tool != null) {
                tool.render(surface, i * 50f + 5f, (this.height() - tool.height()) / 2, tool.width(), tool.height());
            }
        }
    }

    public void press(float x, float y) {
        if (pointInToolbar(x, y)) {
            int index = (int) ((x - x()) / 50);
            tools.get(activeTool).selected = false;
            activeTool = index;
            tools.get(activeTool).selected = true;
        } else {
            tools.get(activeTool).press(x, y);
        }
    }

    public boolean pointInToolbar(float x, float y) {
        return x > x() && x < x() + width() && y > y() && y < y() + height();
    }

    public void release(float x, float y) {
        if (!pointInToolbar(x, y))
            tools.get(activeTool).release(x, y);
    }

    public void drag(float x, float y) {
        if (!pointInToolbar(x, y))
            tools.get(activeTool).drag(x, y);
    }
}
