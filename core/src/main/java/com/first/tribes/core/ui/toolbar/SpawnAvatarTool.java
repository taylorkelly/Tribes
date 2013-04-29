package com.first.tribes.core.ui.toolbar;

import com.first.tribes.core.Tribes.PointerFocusable;
import com.first.tribes.core.TribesWorld;

import playn.core.Color;
import playn.core.Surface;
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
	private static int currentTrait=0;
	
	
	public SpawnAvatarTool(TribesWorld world) {
		super(world);
	}

	@Override
	public void render(Surface surface, float x, float y, float width,
			float height) {
		int thickness=5;
		 surface.setFillColor(Color.rgb(50, (selected ? 255 : 200), (selected ? 255 : 200)));
         surface.fillRect(x, y, width, height);
         surface.setFillColor(Color.rgb(0,0,0));
         switch(currentTrait){
         case 0://A
        	 //left bar
        	 surface.fillRect(x+width/thickness, y+5, width/thickness, height-10);
        	 //right bar
        	 surface.fillRect(x+(3)*width/thickness, y+5, width/thickness, height-10);
        	 //top bar
        	 surface.fillRect(x+width/thickness, y+5, 2*width/thickness, (height-10)/thickness);
        	//middle bar
        	 surface.fillRect(x+width/thickness, y+5+2*(height-10)/thickness, 2*width/thickness, (height-10)/thickness);
        	 break;
         case 1://S
        	 //top
        	 surface.fillRect(x+width/thickness, y+5,(thickness-2)*width/thickness, (height-10)/thickness);
        	//middle
        	 surface.fillRect(x+width/thickness, y+5+2*(height-10)/thickness, (thickness-2)*width/thickness, (height-10)/thickness);
        	 //left half bar
        	 surface.fillRect(x+width/thickness, y+5, width/thickness, 2*(height-10)/thickness);
        	 //right half bar
        	 surface.fillRect(x+(3)*width/thickness, y+5+2*(height-10)/thickness, width/thickness, 3*(height-10)/thickness);
        	 //bottom
        	 surface.fillRect(x+width/thickness, y+5+(thickness-1)*(height-10)/thickness,  (thickness-2)*width/thickness,(height-10)/thickness);
        	 break;
         case 2://C
        	 //left bar
        	 surface.fillRect(x+width/thickness, y+5, width/thickness, height-10);
        	 //top
        	 surface.fillRect(x+width/thickness, y+5,(thickness-2)*width/thickness, (height-10)/thickness);
        	 //bottom
        	 surface.fillRect(x+width/thickness, y+5+(thickness-1)*(height-10)/thickness,  (thickness-2)*width/thickness,(height-10)/thickness);
        	break;
         case 3://I
        	 //top
        	 surface.fillRect(x+width/thickness, y+5,(thickness-2)*width/thickness, (height-10)/thickness);
        	 //bottom
        	 surface.fillRect(x+width/thickness, y+5+(thickness-1)*(height-10)/thickness,  (thickness-2)*width/thickness,(height-10)/thickness);
        	//middle bar 
        	 surface.fillRect(x+width/2-(width/thickness)/2, y+5, width/thickness, height-10);
         	break;
         case 4://H
        	//left bar
        	 surface.fillRect(x+width/thickness, y+5, width/thickness, height-10);
        	//right bar
        	 surface.fillRect(x+(3)*width/thickness, y+5, width/thickness, height-10);
        	//middle
        	 surface.fillRect(x+width/thickness, y+5+2*(height-10)/thickness, (thickness-2)*width/thickness, (height-10)/thickness);
        	break;
         case 5://L
        	//left bar
        	 surface.fillRect(x+width/thickness, y+5, width/thickness, height-10);
        	//bottom
        	 surface.fillRect(x+width/thickness, y+5+(thickness-1)*(height-10)/thickness,  (thickness-2)*width/thickness,(height-10)/thickness);
        	break;
         case 6://M
        	//left bar
        	 surface.fillRect(x+width/(2*thickness), y+5, width/thickness, height-10);
        	//right bar
        	 surface.fillRect(x+(2*thickness-3)*width/(2*thickness), y+5, width/thickness, height-10);
        	//top-left diagonal
        	 surface.drawLine(x+width/thickness, y+5, x+width/2, y+height/2, width/thickness);
        	//top-Right diagonal
        	 surface.drawLine(x+width/2,y+height/2, x+(thickness-1)*width/thickness, y+5, width/thickness);
        	 
        	 break;
        	 default://R
        		//left bar
            	 surface.fillRect(x+width/thickness, y+5, width/thickness, height-10);
            	//middle
            	 surface.fillRect(x+width/thickness, y+5+2*(height-10)/thickness, (thickness-2)*width/thickness, (height-10)/thickness);
            	//top
            	 surface.fillRect(x+width/thickness, y+5,(thickness-2)*width/thickness, (height-10)/thickness);
            	 //right halfbar
            	 surface.fillRect(x+(3)*width/thickness, y+5, width/thickness, 2*(height-10)/thickness);
            	 //bottom left diagonal
            	 surface.drawLine(thickness+x+width/thickness, y+5+height/2, x+(thickness-1)*width/thickness, y+(height-10), -1+width/thickness);
             	
        		 break;
         }
	}
	
	@Override
	public String name() {
		return "Spawn Avatar of "+SpawnAvatarTool.getNameOfCurrentTrait()+" Tool";
	}
public static void setCurrentTrait(int i){
	currentTrait=i;
}
	@Override
	public PointerFocusable press(float x, float y) {
		return this;

	}

	public static String getNameOfCurrentTrait(){
		switch(currentTrait){
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
           world.villages().get(0).spawnAvatar(worldPoint.x, worldPoint.y,currentTrait);
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
