/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.pcg;

import com.first.tribes.core.Tile;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public class MountainAgent extends Agent {
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

    public void setHeightPush(float heightPush) {
        heightPush = heightPush;
    }
    
}
