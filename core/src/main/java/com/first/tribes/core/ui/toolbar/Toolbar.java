package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tribes;
import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import java.util.ArrayList;
import java.util.List;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font;
import playn.core.Key;
import playn.core.Surface;
import playn.core.TextFormat;
import playn.core.TextLayout;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class Toolbar implements PointerFocusable {

    private TribesWorld world;
    private List<Tool> tools;
    private int activeTool;
    private Font textFont;
    private Font costFont;

    public Toolbar(TribesWorld world) {
        this.world = world;
        tools = new ArrayList<Tool>(10);
        tools.add(new GrabTool(world));
        tools.add(new PushPullTool(world));
        tools.add(new FoodTool(world));
        tools.add(new SpawnTool(world));
        tools.add(new SpawnAvatarTool(world));
        tools.add(new FloodTool(world));
        tools.add(new IrrigationTool(world));
        tools.get(0).selected = true;
        activeTool = 0;

        textFont = graphics().createFont("Sans serif", Font.Style.BOLD, 15);
        costFont = graphics().createFont("Sans serif", Font.Style.PLAIN, 15);


    }

    public float width() {
        return (tools.size() * Tool.width()) + 10f;
    }

    public float height() {
        return Tool.height() + 10f + 20f;
    }

    public float x() {
        return (Tribes.SCREEN_WIDTH - width()) / 2;
    }

    public float y() {
        return (Tribes.SCREEN_HEIGHT) - height();
    }

    public void render(Surface surface) {
        surface.setFillColor(Color.rgb(0, 0, 0));
        surface.fillRect(0, 0, width(), height());

        TextLayout nameLayout = graphics().layoutText(tools.get(activeTool).name(), new TextFormat().withFont(textFont));
        TextLayout costLayout = graphics().layoutText(tools.get(activeTool).costDescription(), new TextFormat().withFont(costFont));
        CanvasImage nameBox = graphics().createImage(nameLayout.width() + costLayout.width() + 5, (int) 20f);
//        nameBox.canvas().setFillColor(Color.argb(200, 255, 255, 255));
        nameBox.canvas().setFillColor(Color.argb(200, 255, 255, 255));
        nameBox.canvas().fillText(nameLayout, 0, 0);
        nameBox.canvas().setFillColor(Color.argb(200, 200, 200, 200));
        nameBox.canvas().fillText(costLayout, nameLayout.width() + 5, 0);

        surface.drawImage(nameBox, (width() - nameBox.width()) / 2, height() - 22f);

        for (int i = 0; i < tools.size(); i++) {
            Tool tool = tools.get(i);

            if (tool != null) {
                tool.render(surface, i * 50f + 5f, ((this.height() - 20f) - tool.height()) / 2, tool.width(), tool.height());
            }
        }
    }

    public PointerFocusable press(float x, float y) {
        if (pointInToolbar(x, y)) {
            int index = (int) ((x - x()) / 50);
            if (index >= 0 && index < tools.size()) {
                tools.get(activeTool).selected = false;
                activeTool = index;
                tools.get(activeTool).selected = true;
            }
            return this;
        } else {
            return tools.get(activeTool).press(x, y);
        }
    }

    public boolean pointInToolbar(float x, float y) {
        return x > x() && x < x() + width() && y > y() && y < y() + height();
    }

    public void release(float x, float y) {
        if (pointInToolbar(x, y)) {
            int index = (int) ((x - x()) / 50);
            if (index >= 0 && index < tools.size()) {
                tools.get(activeTool).selected = false;
                activeTool = index;
                tools.get(activeTool).selected = true;
            }
        }
    }

    public void drag(float x, float y) {
        if (pointInToolbar(x, y)) {
            int index = (int) ((x - x()) / 50);
            if (index >= 0 && index < tools.size()) {
                tools.get(activeTool).selected = false;
                activeTool = index;
                tools.get(activeTool).selected = true;
            }
        }
    }

    public void keyPress(Key key) {
        int index = -1;
        switch (key) {
            case K1:
                index = 0;
                break;
            case K2:
                index = 1;
                break;
            case K3:
                index = 2;
                break;
            case K4:
                index = 3;
                break;
            case K5:
                index = 4;
                break;
            case K6:
                index = 5;
                break;
            case K7:
                index = 6;
                break;
            case K8:
                index = 7;
                break;
            case K9:
                index = 8;
                break;
            case K0:
                index = 9;
                break;
        }

        if (index != -1 && index < tools.size()) {
            tools.get(activeTool).selected = false;
            activeTool = index;
            tools.get(activeTool).selected = true;
        }
    }
}
