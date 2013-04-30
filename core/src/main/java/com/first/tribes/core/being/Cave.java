package com.first.tribes.core.being;

import static playn.core.PlayN.random;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Rectangle;

import com.first.tribes.core.Tile;
import com.first.tribes.core.TribesWorld;
import com.first.tribes.core.util.Updatee;

public class Cave implements Updatee {

    private static final int STARTING_MONSTERS = 7;
    private static final int MONSTER_MULTIPLIER = 10;//how much stronger a monster is than an average human
    private ArrayList<Monster> monsterList;
    private int maxMonsters;
    private TribesWorld world;
    private int monsterColor;
    private float[][] densityMap;

    public Cave(TribesWorld world, int color) {
        this.world = world;
        monsterColor = color;
        maxMonsters = STARTING_MONSTERS;
        monsterList = new ArrayList<Monster>(STARTING_MONSTERS);

        densityMap = new float[world.tileWidth()][world.tileHeight()];
        for (int i = 0; i < densityMap.length; i++) {
            for (int j = 0; j < densityMap[i].length; j++) {
                densityMap[i][j] = 0f;
            }
        }
        
        while (monsterList.size() < maxMonsters) {
            monsterList.add(newMonster());
        }
    }

    private Monster newMonster() {
        boolean isSafe = false;
        float x, y;
        do {
            x = random() * world.getAbsoluteSize().width;
            y = random() * world.getAbsoluteSize().height;

        } while (isUnsafe(x, y));
        Monster monster = new Monster(x, y, this, monsterColor);
        monster.personality.setHardiness(MONSTER_MULTIPLIER * monster.personality.hardiness());
        monster.personality.setStrength(MONSTER_MULTIPLIER * monster.personality.strength());
        monster.personality.setMobility(MONSTER_MULTIPLIER * monster.personality.mobility());
        
        return monster;
    }

    public List<Being> monstersInArea(Rectangle rectangle) {
        if (rectangle.width < 0) {
            rectangle.x = rectangle.x + rectangle.width;
            rectangle.width *= -1;
        }
        if (rectangle.height < 0) {
            rectangle.y = rectangle.y + rectangle.height;
            rectangle.height *= -1;
        }

        List<Being> area = new ArrayList<Being>();
        for (Being being : monsterList) {
            if (rectangle.contains(being.xPos(), being.yPos())) {
                area.add(being);
            }
        }

        return area;

    }

    boolean isUnsafe(float xPos, float yPos) {
        return world.unsafe(xPos, yPos, 0);
    }

    public Tile tileAt(float xPos, float yPos) {
        return world.tileAt(xPos, yPos);
    }

    public List<Village> enemyVillages() {
        List<Village> enemies = world.villages();
        return enemies;
    }

    @Override
    public void update(float delta) {
    	for (int i = 0; i < monsterList.size(); i++) {

            Monster m = monsterList.get(i);
            densityMap[tileAt(m.xPos, m.yPos).getXIndex()][tileAt(m.xPos, m.yPos).getYIndex()] = 1f;

        }
        float SPREAD_CONST = 0.01f;
        float DECR_CONST = 0.90f;

        
        for (int i = 0; i < densityMap.length; i++) {
            for (int j = 0; j < densityMap[i].length; j++) {
                
                densityMap[i][j] *= (DECR_CONST);
                if (densityMap[i][j] < 0 || !world.tiles()[i][j].isSafe(0)) {
                    densityMap[i][j] = 0f;
                }
                if (densityMap[i][j] > 1) {
                    densityMap[i][j] = 1f;
                }

                int rad = 1;
                for (int k = Math.max(i - rad, 0); k <= Math.min(i + rad, densityMap.length - 1); k++) {
                    for (int l = Math.max(j - rad, 0); l <= Math.min(j + rad, densityMap[k].length - 1); l++) {
                        densityMap[i][j] += densityMap[k][l]*SPREAD_CONST;
                    }
                }
            }
        }

    	
    	
    	
    	  for (int i = 0; i < monsterList.size(); i++) {
              Being being = monsterList.get(i);
              being.update(delta);
              if (being.isDead()) {
                 monsterList.remove(being);
                  i--;
              }
          }
    	
    	while (monsterList.size() < maxMonsters) {
            monsterList.add(newMonster());
        }

    }
    
    public float getDensityAt(int x, int y) {
        return densityMap[x][y];

    }

    public float getDensityAt(float x, float y) {
        return getDensityAt(tileAt(x, y));
    }

    public float getDensityAt(Tile t) {
        return densityMap[t.getXIndex()][t.getYIndex()];
    }


    public ArrayList<Monster> monsters() {
        return monsterList;
    }

    public void spawnMonster(float x, float y) {
        Monster monster = new Monster(x, y, this, monsterColor);
        monster.personality.setHardiness(MONSTER_MULTIPLIER * monster.personality.hardiness());
        monster.personality.setStrength(MONSTER_MULTIPLIER * monster.personality.strength());
        monsterList.add(monster);
    }
}
