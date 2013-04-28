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
public class CoastLineAgent extends Agent {

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
