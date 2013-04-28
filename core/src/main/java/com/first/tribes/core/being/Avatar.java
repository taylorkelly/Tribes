package com.first.tribes.core.being;

import static playn.core.PlayN.random;

public class Avatar extends Villager {
	private static final float AVATAR_SIZE = 20.0f;
	
	/* aggression
       strength
       courage
       intelligence
       hardiness 
       longevity
       mobility
       reproductiveAppeal
       loyalty*/
	
	public Avatar(float xPos, float yPos, Village village, int color, int trait) {
		super(xPos, yPos, village, color);
		this.width=AVATAR_SIZE;
		this.height=AVATAR_SIZE;
		
		switch (trait) {
		case 0:
			this.personality.setAggression(1);
			break;
		case 1:
			this.personality.setStrength(1);
			break;
		case 2:
			this.personality.setCourage(1);
			break;
		case 3:
			this.personality.setIntelligence(1);
			break;
		case 4:
			this.personality.setHardiness(1);
			break;
		case 5:
			this.personality.setLongevity(1);
			break;
		case 6:
			this.personality.setLoyalty(1);
			break;
		case 7: this.personality.setMobility(1);
		break;
		default: this.personality.setReproductiveAppeal(1);
	}
		
		}

}
