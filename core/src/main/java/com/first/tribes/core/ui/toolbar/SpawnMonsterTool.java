package com.first.tribes.core.ui.toolbar;

import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Point;

import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;
import playn.core.Image;
import playn.core.PlayN;

public class SpawnMonsterTool extends Tool {

    public static final int MANNA_COST_PER_DROP = 600;
    private int color = Color.rgb(200, 51, 200);
    private int selectedColor = Color.rgb(255, 51, 255);
    private Image monsterImage;

    public SpawnMonsterTool(TribesWorld world) {
        super(world);
        monsterImage = PlayN.assets().getImage("images/monster.png");
    }

    @Override
    public void render(Surface surface, float x, float y, float width, float height) {
        surface.setFillColor(selected ? selectedColor : color);
        surface.fillRect(x, y, width, height);
        surface.drawImage(monsterImage, x + width * 0.05f, y + height * 0.05f, width * 0.9f, height  * 0.9f);

    }

    public String name() {
        return "Spawn Monster Tool";
    }

    public String costDescription() {
        return MANNA_COST_PER_DROP + " manna";
    }

    @Override
    public PointerFocusable press(float x, float y) {
        return this;
    }

    public void spawn(float x, float y) {
        world.caves().get(0).spawnMonster(x, y);
    }

    @Override
    public void release(float x, float y) {
        if (world.villages().get(0).manna() >= MANNA_COST_PER_DROP) {
            Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
            spawn(worldPoint.x, worldPoint.y);
            world.villages().get(0).costManna(MANNA_COST_PER_DROP);
        }

    }

    @Override
    public void drag(float x, float y) {
    }
}