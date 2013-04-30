package com.first.tribes.core;

import pythagoras.f.Point;

import com.first.tribes.core.being.Village;
import com.first.tribes.core.ui.toolbar.*;
import com.first.tribes.core.util.Timer.TimerTask;
import static playn.core.PlayN.*;

public class EnemyGod implements TimerTask {
	private static final int FLOOD_POSITION=5;//Flood tool Position in toolbar
	private static final int FOOD_POSITION=2;//Food Tool Position in toolbar
//	private static final int IRRIGATION_POSITION=;//Irrigation tool
//	private static final int PUSHPULL_POSITION=;//push
//	private static final int SPAWN_AVATAR_POSITION=;
//	private static final int SPAWN_POSITION=;
//	private static final int SPAWN_MONSTER_POSITION=;
	
	private static final int numberOfPowers=7;
	private static final int REPEATAMOUNT = 4;
	private TribesWorld world;
	private Village enemyVillage, ownVillage;
	
	
	public EnemyGod(TribesWorld world){
		this.world=world;
		enemyVillage=world.villages().get(0);
		ownVillage=world.villages().get(0);
	}
	
	
	public Point safeCoordinates(){
		Point p = new Point(enemyVillage.xPos(),enemyVillage.yPos());
		while(ownVillage.isUnsafe(p.x, p.y)){
			p= randomPoint();}
		return p;
	}
	
	public Point randomPoint(){
		return new Point(random()*world.getAbsoluteSize().width,random()*world.getAbsoluteSize().height);
	}
	
	@Override
	public void run() {
		switch((int) random()*10){
		case 0://flood
			FloodTool flTool = (FloodTool) world.toolbar().getTools().get(FLOOD_POSITION);
			if(ownVillage.manna()>flTool.MANNA_COST_PER_DELTA()){
				boolean up = random()<.5f;
				for(int i=0;i<REPEATAMOUNT;i++){
				flTool.flood(up);
				}
				ownVillage.costManna(flTool.MANNA_COST_PER_DELTA());
				flTool.release(0,0);
			}
			break;
		case 1://food
			FoodTool fTool =(FoodTool) world.toolbar().getTools().get(FOOD_POSITION);
			if(ownVillage.manna()>fTool.MANNA_COST_PER_DROP){
				Point p = randomPoint();
				fTool.dropFood(p.x,p.y);
				ownVillage.costManna(fTool.MANNA_COST_PER_DROP);
			}
			break;
		case 2://irrigation
	
			break;
		case 3://push pull
	
			break;
		case 4://spawn avatar of aggression
			break;
		case 5://spawn monster
			
			break;
		default://spawn tool
				
			break;
			
		}

	}

}
