package com.first.tribes.core.being;

import static playn.core.PlayN.random;

public class Avatar extends Villager {
	private static final float AVATAR_SIZE = 20.0f;
	private static final float AVATAR_TRAIT = 5f;
	private static final float AVATAR_LONGEVITY= 1f;
	
	/* aggression 0
       strength 1
       intelligence 2
       hardiness 3
       loyalty 4
       mobility 5
       reproductiveAppeal
       */
	
	public Avatar(float xPos, float yPos, Village village, int color, int trait) {
		super(xPos, yPos, village, color);
		this.width=AVATAR_SIZE;
		this.height=AVATAR_SIZE;
		
		switch (trait) {
		case 0:
			this.personality.setAggression(AVATAR_TRAIT);
			break;
		case 1:
			this.personality.setStrength(AVATAR_TRAIT);
			break;
		case 2:
			this.personality.setIntelligence(AVATAR_TRAIT);
			break;
		case 3:
			this.personality.setHardiness(AVATAR_TRAIT);
			break;
		case 4:
			this.personality.setLoyalty(AVATAR_TRAIT);
			
			break;
		case 5: 
			this.personality.setMobility(AVATAR_TRAIT);
		break;
		default: this.personality.setReproductiveAppeal(AVATAR_TRAIT);
		break;
	}
		this.personality.setLongevity(AVATAR_LONGEVITY);
		}

}
