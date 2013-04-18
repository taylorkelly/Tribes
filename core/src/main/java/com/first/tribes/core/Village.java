/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import com.first.tribes.core.Being.Personality;
import com.first.tribes.core.util.Updatee;
import java.util.ArrayList;
import java.util.List;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class Village implements Updatee {

    public List<Villager> villagers;
    public TribesWorld world;
    public static final float POSITION_DEVIATION = 10.0f;
    public static final float REPRODUCTIVE_BASE_RATE = 0.08f;
    
    public float foodPool = 0;

    public Village(float x, float y, int numVillagers, TribesWorld world) {
        this.world = world;
        villagers = new ArrayList<Villager>(numVillagers * 30);

        for (int i = 0; i < numVillagers; i++) {
            float villagerX = randomDist(x, POSITION_DEVIATION);
            float villagerY = randomDist(y, POSITION_DEVIATION);
            villagers.add(new Villager(villagerX, villagerY, this));
        }
    }

    public float xPos() {
        float xPos = 0;

        for (Villager villager : villagers) {
            xPos += villager.xPos;
        }

        return xPos / villagers.size();
    }

    public float yPos() {
        float yPos = 0;

        for (Villager villager : villagers) {
            yPos += villager.yPos;
        }

        return yPos / villagers.size();
    }

    public static float randomDist(float mean, float stddev) {
        float random = (random() * 2 - 1) + (random() * 2 - 1) + (random() * 2 - 1);
        return random * stddev + mean;
    }

    float worldWidth() {
        return world.width();
    }

    float worldHeight() {
        return world.height();
    }

    public float reproductiveBaseRate() {
        return REPRODUCTIVE_BASE_RATE  / 2; /// (float) (Math.sqrt(villagers.size()));
    }

    @Override
    public void update(float delta) {
        int villageSize = villagers.size();
        for (Villager villager : new ArrayList<Villager>(villagers.subList(0, villageSize))) {
            if (villager.personality.reproductiveAppeal() * reproductiveBaseRate() > random()) {
                reproduce(villager);
            }
        }

        float repAppeal = 0;
        float longevity = 0;
        float intelligence = 0;
        float loyalty = 0;

        for (Villager villager : villagers) {
            repAppeal += villager.personality.reproductiveAppeal();
            longevity += villager.personality.longevity();
            intelligence += villager.personality.intelligence();
            loyalty += villager.personality.loyalty();
        }
        System.out.println(villagers.size() + "\t" + foodPool + "\t" + repAppeal / villagers.size() + "\t" + longevity / villagers.size() + "\t" + intelligence / villagers.size()+ "\t" + loyalty / villagers.size());
    }

    public void reproduce(Villager matingVillager) {
        float totalMusk = 0;
        for (Villager villager : villagers) {
            if (villager != matingVillager) {
                totalMusk += villager.personality.reproductiveAppeal();
            }
        }

        float chosenMusk = totalMusk * random();

        Villager mate = null;

        for (Villager villager : villagers) {
            if (villager != matingVillager) {
                chosenMusk -= villager.personality.reproductiveAppeal();
            }
            if (chosenMusk <= 0) {
                mate = villager;
                break;
            }
        }

        if (mate != null) {
            Personality childPersonality = matingVillager.personality.reproduceWith(mate.personality);
            float villagerX = randomDist(xPos(), POSITION_DEVIATION);
            float villagerY = randomDist(yPos(), POSITION_DEVIATION);

            villagerX = matingVillager.xPos;
            villagerY = matingVillager.yPos;
            
            Villager child = new Villager(villagerX, villagerY, this);
            child.personality = childPersonality;
            villagers.add(child);
        }
    }

    void kill(Villager aThis) {
        villagers.remove(aThis);
    }

    boolean isUnsafe(float xPos, float yPos) {
        return world.unsafe(xPos, yPos);
    }

    float gatherFood(Villager villager, float requestedFood) {
        Tile tile = world.tileAt(villager.xPos, villager.yPos);
        float foodGathered = Math.min(tile.numFood, requestedFood * (float)Math.pow(1 - villager.personality.intelligence(),5));
        tile.numFood -= foodGathered;
        foodGathered = foodGathered / (float)Math.pow(1 - villager.personality.intelligence(),5);
        return foodGathered;
    }
}
