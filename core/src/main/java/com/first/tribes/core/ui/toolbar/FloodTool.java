package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tile;
import com.first.tribes.core.Tribes;

import playn.core.Color;
import playn.core.Surface;

public class FloodTool extends Tool {

	public FloodTool(){
		
	}
	@Override
	public void render(Surface surface, float x, float y, float width,
			float height) {
		surface.setFillColor(Color.rgb(0, 0, (selected ? 255 : 200)));
        surface.fillRect(x, y, width, height);

	}

	@Override
	public String name() {
		return "Flood Tool";
	}

	@Override
	public void press(float x, float y) {
		if(Tribes.SHIFT){
			Tile.lowerWaterLevel();
		}else{
		Tile.raiseWaterLevel();
		}
	}

	@Override
	public void release(float x, float y) {
		Tile.restoreWaterLevel();

	}

	@Override
	public void drag(float x, float y) {
		this.press(x, y);

	}

}
