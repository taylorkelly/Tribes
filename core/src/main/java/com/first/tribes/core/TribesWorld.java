/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import static playn.core.PlayN.*;
import java.util.ArrayList;
import java.util.List;
import playn.core.Color;
import playn.core.Connection;
import playn.core.GroupLayer;
import playn.core.ImmediateLayer;
import playn.core.Layer;
import playn.core.Mouse.LayerListener;
import playn.core.Pointer.Listener;
import playn.core.Surface;
import playn.core.Touch;
import playn.core.gl.GLShader;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import pythagoras.f.Transform;

/**
 *
 * @author taylor
 */
class TribesWorld {

    private GroupLayer.Clipped groupLayer;
    Dimension absoluteSize;
    Rectangle viewPort;
    private List<Villager> villagers = new ArrayList<Villager>();
    Village village;
    Tile[][] tiles;
    MiniMap miniMap = new MiniMap(this);
    private static final float MAX_MAGNIFICATION = 1.0f;
    private static final float MIN_MAGNIFICATION = 0.1f;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public TribesWorld() {
        absoluteSize = new Dimension(10000, 10000);
        viewPort = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        tiles = new Tile[(int) (absoluteSize.width / Tile.TILE_SIZE)][(int) (absoluteSize.height / Tile.TILE_SIZE)];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Tile(i, j);
                tiles[i][j].tileMap = tiles;
            }
        }

        generateMap(tiles);

        boolean isSafe = false;
        float xPos;
        float yPos;
        do {
            xPos = random() * absoluteSize.width;
            yPos = random() * absoluteSize.height;
            isSafe = !this.unsafe(xPos, yPos);
        } while (!isSafe);
        village = new Village(xPos, yPos, 10, this);
        villagers = village.villagers;

        viewPort.x = village.xPos() - viewPort.width/2;
        viewPort.y = village.yPos() - viewPort.height/2;
        
        groupLayer = graphics().createGroupLayer(SCREEN_WIDTH, SCREEN_HEIGHT);
        groupLayer.add(graphics().createImmediateLayer(SCREEN_WIDTH, SCREEN_HEIGHT, new ImmediateLayer.Renderer() {
            @Override
            public void render(Surface surface) {
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[i].length; j++) {
                        tiles[i][j].paint(surface, viewPort, scale());
                    }
                }
            }
        }));
        groupLayer.add(graphics().createImmediateLayer(SCREEN_WIDTH, SCREEN_HEIGHT, new ImmediateLayer.Renderer() {
            @Override
            public void render(Surface surface) {
                for (Villager villager : villagers) {
                    villager.paint(surface, viewPort, scale());
                }
            }
        }));
        groupLayer.addAt(graphics().createImmediateLayer(150, 150, new ImmediateLayer.Renderer() {
            @Override
            public void render(Surface surface) {
                surface.drawImage(miniMap.image(), 0, 0);
            }
        }), SCREEN_WIDTH - 150 - 10, 10);

//        groupLayer.add(graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
//            public void render(Surface surface) {
//                surface.setFillColor(Color.BLACK.getRGB());
//                surface.fillRect(viewPort.x, viewPort.y, viewPort.width, viewPort.height);
//            }
//        }));
    }

    public Layer getLayer() {
        return groupLayer;
    }

    public float width() {
        return absoluteSize.width;
    }

    public float height() {
        return absoluteSize.height;
    }

    void update(float delta) {
        for (int i = 0; i < villagers.size(); i++) {
            Villager villager = villagers.get(i);
            villager.update(delta);
            if (villager.dead) {
                villagers.remove(villager);
                i--;
            }
        }
        village.update(delta);
    }

    float scale() {
        return SCREEN_WIDTH / viewPort.width;
    }

    void moveViewPort(float deltaX, float deltaY) {
        viewPort.translate(deltaX / scale(), deltaY / scale());
        verifyViewPortPosition();
    }

    void zoomDelta(float zoomAmount) {
        float newWidth = viewPort.width * zoomAmount;
        float newHeight = viewPort.height * zoomAmount;

        if (SCREEN_WIDTH / newWidth > MAX_MAGNIFICATION) {
            newWidth = SCREEN_WIDTH / MAX_MAGNIFICATION;
        } else if (SCREEN_WIDTH / newWidth < MIN_MAGNIFICATION) {
            newWidth = SCREEN_WIDTH / MIN_MAGNIFICATION;
        }
        if (SCREEN_HEIGHT / newHeight > MAX_MAGNIFICATION) {
            newHeight = SCREEN_HEIGHT / MAX_MAGNIFICATION;
        } else if (SCREEN_HEIGHT / newHeight < MIN_MAGNIFICATION) {
            newHeight = SCREEN_HEIGHT / MIN_MAGNIFICATION;
        }

        float deltaWidth = viewPort.width - newWidth;
        float deltaHeight = viewPort.height - newHeight;

        float newX = viewPort.x + deltaWidth / 2;
        float newY = viewPort.y + deltaHeight / 2;

        viewPort = new Rectangle(newX, newY, newWidth, newHeight);

        verifyViewPortPosition();
    }

    void verifyViewPortPosition() {

        if (viewPort.width <= absoluteSize.width) {
            if (viewPort.x < 0) {
                viewPort.x = 0;
            }
            if (viewPort.x + viewPort.width > absoluteSize.width) {
                viewPort.x = absoluteSize.width - viewPort.width;
            }
        } else {
            //Center it
            viewPort.x = (absoluteSize.width - viewPort.width) / 2;

        }

        if (viewPort.height <= absoluteSize.height) {
            if (viewPort.y < 0) {
                viewPort.y = 0;
            }
            if (viewPort.y + viewPort.height > absoluteSize.height) {
                viewPort.y = absoluteSize.height - viewPort.height;
            }
        } else {
            //Center it
            viewPort.y = (absoluteSize.height - viewPort.height) / 2;
        }
    }

    void pull(float x, float y) {
        float realX = (x / scale()) + viewPort.x;
        float realY = (y / scale()) + viewPort.y;

        int tileX = (int) (realX / Tile.TILE_SIZE);
        int tileY = (int) (realY / Tile.TILE_SIZE);

        tiles[tileX][tileY].setHeight(tiles[tileX][tileY].height() + 10);
    }

    void push(float x, float y) {
        float realX = (x / scale()) + viewPort.x;
        float realY = (y / scale()) + viewPort.y;

        int tileX = (int) (realX / Tile.TILE_SIZE);
        int tileY = (int) (realY / Tile.TILE_SIZE);

        tiles[tileX][tileY].setHeight(tiles[tileX][tileY].height() - 10);
    }

    boolean unsafe(float xPos, float yPos) {
        int tileX = (int) (xPos / Tile.TILE_SIZE);
        int tileY = (int) (yPos / Tile.TILE_SIZE);
        if (tileX >= tiles.length) {
            tileX = tiles.length - 1;
        }
        if (tileY >= tiles[0].length) {
            tileY = tiles[0].length - 1;
        }

        if (tileX < 0) {
            tileX = 0;
        }
        if (tileY < 0) {
            tileY = 0;
        }

        return tiles[tileX][tileY].height() < -4;
    }

    private static void generateMap(Tile[][] map) {
        int tokens = 2000;
        int pooling = 4;
        while (tokens > pooling) {
            CoastLineAgent coastAgent = new CoastLineAgent((int) (random() * map.length), (int) (random() * map[0].length), (int) (random() * (tokens / pooling + 1)), (int) (random() * tokens / 2));
            tokens -= coastAgent.tokens;
            coastAgent.act(map);
        }

        tokens = 6000;
        while (tokens > 2) {
            MountainAgent mountainAgent = new MountainAgent((int) (random() * map.length), (int) (random() * map[0].length), (int) (random() * tokens), (int) (random() * tokens / 2));
            tokens -= mountainAgent.tokens;
            mountainAgent.act(map);
        }

        tokens = 1000;
        while (tokens > 10) {
            MountainAgent mountainAgent = new MountainAgent((int) (random() * map.length), (int) (random() * map[0].length), (int) (random() * (tokens / 10 + 1)), (int) (random() * tokens / 2));
            mountainAgent.heightPush = 1;
            tokens -= mountainAgent.tokens;
            mountainAgent.act(map);
        }
    }

    private static abstract class Agent {

        protected int tokens;
        protected int tokenLimit;
        protected Coordinate coord;

        public Agent(int x, int y, int tokens, int tokenLimit) {
            this.coord = new Coordinate(x, y);
            this.tokens = tokens;
            this.tokenLimit = tokenLimit;
        }

        public abstract void act(Tile[][] map);

        protected static class Coordinate {

            public int x;
            public int y;

            public Coordinate(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
    }

    private static class MountainAgent extends Agent {

        protected float direction;
        protected float initialDirection;
        protected float heightPush;

        public MountainAgent(int x, int y, int tokens, int tokenLimit) {
            super(x, y, tokens, tokenLimit);
            initialDirection = random() * 2 - 1;
            heightPush = 25;
        }

        public void act(Tile[][] map) {
            direction = initialDirection;
            while (tokens > 0) {
                map[coord.x][coord.y].setHeight(map[coord.x][coord.y].height() + heightPush);
                coord.x += Math.acos(direction) * 0.5 + (random() - 0.5);
                coord.y += Math.asin(direction) * 0.5 + (random() - 0.5);
                if (coord.x > map.length - 1)
                    tokens = 0;
                if (coord.x == 0) {
                    tokens = 0;
                }
                if (coord.x < 0)
                    tokens = 0;
                if (coord.y > map[0].length - 1)
                    tokens = 0;
                if (coord.y < 0)
                    tokens = 0;
                if (coord.y == 0) {
                    tokens = 0;
                }

                if (tokens % 10 == 0) {
                    direction = initialDirection + ((random() - 0.5f) / 2);
                    if (direction > 1.0) {
                        direction = 2.0f - direction;
                    }
                    if (direction < -1.0) {
                        direction = -2.0f - direction;
                    }
                }

                if (random() < 0.10) {
                    MountainAgent childAgent = new MountainAgent(coord.x, coord.y, tokens / 2, tokenLimit);
                    this.tokens -= childAgent.tokens;
                    childAgent.act(map);

                    MountainAgent childAgent2 = new MountainAgent(coord.x, coord.y, tokens, tokenLimit);
                    this.tokens -= childAgent2.tokens;
                    childAgent2.act(map);
                }

                tokens--;
            }
        }
    }

    private static class CoastLineAgent extends Agent {

        public CoastLineAgent(int x, int y, int tokens, int tokenLimit) {
            super(x, y, tokens, tokenLimit);
        }

        public void act(Tile[][] map) {
            if (tokens > tokenLimit) {
                Coordinate child1Pos = randomBorderPoint(map.length - 1, map[0].length - 1, map);
                CoastLineAgent childAgent1 = new CoastLineAgent(child1Pos.x, child1Pos.y, this.tokens / 2, tokenLimit);
                Coordinate child2Pos = randomBorderPoint(map.length - 1, map[0].length - 1, map);
                CoastLineAgent childAgent2 = new CoastLineAgent(child2Pos.x, child2Pos.y, this.tokens / 2, tokenLimit);

                childAgent1.act(map);
                childAgent2.act(map);
            } else {
                while (tokens > 0) {
                    coord = randomBorderPoint(map.length - 1, map[0].length - 1, map);
                    map[coord.x][coord.y].setHeight(map[coord.x][coord.y].height() - 10);
                    tokens--;
                }
            }
        }

        private Coordinate randomBorderPoint(int maxX, int maxY, Tile[][] map) {
            List<Coordinate> points = new ArrayList<Coordinate>(4);
            if (coord.x > 0 && map[coord.x][coord.y].height() >= -15) {
                points.add(new Coordinate(coord.x - 1, coord.y));
            }
            if (coord.y > 0 && map[coord.x][coord.y].height() >= -15) {
                points.add(new Coordinate(coord.x, coord.y - 1));
            }
            if (coord.x < maxX && map[coord.x][coord.y].height() >= -15) {
                points.add(new Coordinate(coord.x + 1, coord.y));
            }
            if (coord.y < maxY && map[coord.x][coord.y].height() >= -15) {
                points.add(new Coordinate(coord.x, coord.y + 1));
            }

            if (!points.isEmpty()) {
                return points.get((int) (random() * points.size()));
            } else {
                coord.x += (int) (random() * 3) - 1;
                if (coord.x > maxX)
                    coord.x = maxX;
                if (coord.x < 0)
                    coord.x = 0;
                coord.y += (int) (random() * 3) - 1;
                if (coord.y > maxY)
                    coord.y = maxY;
                if (coord.y < 0)
                    coord.y = 0;
                return randomBorderPoint(maxX, maxY, map);
            }


        }
    }
}
