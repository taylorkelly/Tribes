package com.first.tribes.core.ui.toolbar;

import static playn.core.PlayN.graphics;

import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;

import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font;
import playn.core.Surface;
import playn.core.TextFormat;
import playn.core.TextLayout;
import pythagoras.f.Point;

public class SpawnAvatarTool extends Tool {

    /*aggression
     *strength
     *courage
     *Intelligence
     *Hardiness
     *Loyalty
     *Mobility
     *Reproductive Appeal
     */
    private static int currentTrait = 0;
    private Font textFont;

    public SpawnAvatarTool(TribesWorld world) {
        super(world);
        textFont = graphics().createFont("Sans serif", Font.Style.BOLD, 48);
    }

    @Override
    public void render(Surface surface, float x, float y, float width,
            float height) {
        int thickness = 5;
        surface.setFillColor(Color.rgb(50, (selected ? 255 : 200), (selected ? 255 : 200)));
        surface.fillRect(x, y, width, height);
        String letter;
        switch (currentTrait) {
            case 0://A
                letter = "A";
                break;
            case 1://S
                letter = "S";
                break;
            case 2://C
                letter = "C";
                break;
            case 3://I
                letter = "I";
                break;
            case 4://H
                letter = "H";
                break;
            case 5://L
                letter = "L";
                break;
            case 6://M
                letter = "M";
                break;
            default://R
                letter = "R";
                break;
        }
        
        TextLayout nameLayout = graphics().layoutText(letter, new TextFormat().withFont(textFont));
        CanvasImage nameBox = graphics().createImage(nameLayout.width(), nameLayout.height());
        nameBox.canvas().setFillColor(Color.rgb(0, 0, 0));
        nameBox.canvas().fillText(nameLayout, 0, 0);
        float yMargin = (height - nameBox.canvas().height());
        float xMargin = (width - nameBox.canvas().width());

        surface.drawImageCentered(nameBox, x + width/2, y + height/2);
    }

    @Override
    public String name() {
        return "Spawn Avatar of " + SpawnAvatarTool.getNameOfCurrentTrait() + " Tool";
    }

    public static void setCurrentTrait(int i) {
        currentTrait = i;
    }

    @Override
    public PointerFocusable press(float x, float y) {
        return this;

    }

    public static String getNameOfCurrentTrait() {
        switch (currentTrait) {
            case 0:
                return "Aggression";
            case 1:
                return "Strength";
            case 2:
                return "Courage";
            case 3:
                return "Intelligence";
            case 4:
                return "Hardiness";
            case 5:
                return "Loyalty";
            case 6:
                return "Mobility";
            default:
                return "Reproductive Appeal";
        }
    }

    @Override
    public void release(float x, float y) {
        Point worldPoint = world.worldPointFromScreenPoint(new Point(x, y));
        world.villages().get(0).spawnAvatar(worldPoint.x, worldPoint.y, currentTrait);
    }

    @Override
    public void drag(float x, float y) {
        // TODO Auto-generated method stub
    }

    @Override
    public String costDescription() {
        return "3 souls";
    }
}
