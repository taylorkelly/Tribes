/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.pcg;

import com.first.tribes.core.Tile;
import java.util.ArrayList;
import java.util.List;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public abstract class Agent {

    protected int tokens;
    protected int tokenLimit;
    protected Coordinate coord;

    public Agent(int x, int y, int tokens, int tokenLimit) {
        this.coord = new Coordinate(x, y);
        this.tokens = tokens;
        this.tokenLimit = tokenLimit;
    }

    public abstract void act(Tile[][] map);
    
    public final int tokens() {
        return tokens;
    }

    protected static class Coordinate {

        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}


