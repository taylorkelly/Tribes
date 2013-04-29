/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.being;

import com.first.tribes.core.DrawnObject;

import playn.core.CanvasImage;
import pythagoras.f.Rectangle;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public abstract class Being extends DrawnObject {

	
	enum DeathReason {

        AGE, STARVING, DROWNING, KILLED_BY_VILLAGER, KILLED_BY_MONSTER;

        String description() {
            switch (this) {
                case AGE:
                    return "lived a full life.";
                case STARVING:
                    return "forgot to eat.";
                case DROWNING:
                    return "drank too much water.";
                case KILLED_BY_VILLAGER:
                    return "was killed by a villager.";
                case KILLED_BY_MONSTER:
                    return "was killed by a monster.";
                default:
                    return "is Dead.";
            }
        }
    }

	
    protected Personality personality;
    protected float xPos, yPos;
    protected float xVel, yVel;
    protected float width, height;

    
    protected DeathReason deathReason;
    
    protected CanvasImage visualInfo;
    
    
    public Being(float xPos, float yPos, float width, float height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
        this.width = width;
        this.personality = new Personality();
    }

    public Rectangle bounds() {
        return new Rectangle(xPos, yPos, width, height);
    }

    
    public float xPos() {
        return xPos;
    }
    
    public float yPos() {
        return yPos;
    }
    
    public class Personality {

        public static final float MUTATION_RATE = 0.05f;
        private float aggression; //determines threshold distance for attacking
        private float strength; //determines how much damage an attack does
        private float intelligence;//Intelligence and courage are two traits that affect goal choice
        private float hardiness;//how much health they have
        private float longevity;//how long they live
        private float mobility;//how fast they move or how many resources they use to move
        private float reproductiveAppeal;//would affect the genetic algo somehow
        private float loyalty;//affects how much resource advantadge another village has to have to switch
        private float total;

        public Personality() {
            aggression = random();
            strength = random();
            intelligence = random();
            hardiness = random();
            longevity = random();
            mobility = random();
            reproductiveAppeal = random();
            loyalty = random();

            normalize();
        }

        public void setAggression(float a){
        	aggression=a;
        }
        public void setStrength(float a){
        	strength=a;
        }
       
        public void setIntelligence(float a){
        	intelligence=a;
        }
        public void setLongevity(float a){
        	longevity=a;
        }
        public void setHardiness(float a){
        	hardiness=a;
        }
        public void setMobility(float a){
        	mobility=a;
        }
        public void setReproductiveAppeal(float a){
        	reproductiveAppeal=a;
        }
        public void setLoyalty(float a){
        	loyalty=a;
        }
        
        private void normalize() {
            total = aggression + strength  + intelligence + hardiness + longevity + mobility + reproductiveAppeal + loyalty;
        }

        public float reproductiveAppeal() {
            return reproductiveAppeal / total;
        }

        Personality reproduceWith(Personality personality) {
            Personality newPersonality = new Personality();


            newPersonality.aggression = (random() < 0.5 ? this.aggression : personality.aggression);
            if (random() < MUTATION_RATE)
                newPersonality.aggression = random();

            newPersonality.strength = (random() < 0.5 ? this.strength : personality.strength);
            if (random() < MUTATION_RATE)
                newPersonality.strength = random();

            newPersonality.intelligence = (random() < 0.5 ? this.intelligence : personality.intelligence);
            if (random() < MUTATION_RATE)
                newPersonality.intelligence = random();

            newPersonality.hardiness = (random() < 0.5 ? this.hardiness : personality.hardiness);
            if (random() < MUTATION_RATE)
                newPersonality.hardiness = random();

            newPersonality.longevity = (random() < 0.5 ? this.longevity : personality.longevity);
            if (random() < MUTATION_RATE)
                newPersonality.longevity = random();

            newPersonality.mobility = (random() < 0.5 ? this.mobility : personality.mobility);
            if (random() < MUTATION_RATE)
                newPersonality.mobility = random();

            newPersonality.reproductiveAppeal = (random() < 0.5 ? this.reproductiveAppeal : personality.reproductiveAppeal);
            if (random() < MUTATION_RATE)
                newPersonality.reproductiveAppeal = random();

            newPersonality.loyalty = (random() < 0.5 ? this.loyalty : personality.loyalty);
            if (random() < MUTATION_RATE)
                newPersonality.loyalty = random();

            newPersonality.normalize();

            return newPersonality;
        }

        public String toString() {
            return "{ RepApp:" + reproductiveAppeal() + ", Long:" + longevity() + "}";
        }

        float longevity() {
            return longevity / total;
        }

        float intelligence() {
            return intelligence / total;
        }
        
        float strength(){
        	return strength /total;
        }

        float loyalty() {
            return loyalty / total;
        }

        float mobility() {
            return mobility / total;
        }

        float hardiness() {
            return hardiness / total;
        }

        float aggression() {
            return aggression / total;
        }
    }
    
    public boolean isDead() {
        return deathReason != null;
    }
    
    public abstract void attack(Being b);
    
    protected void setDead(DeathReason deathReason) {
        if (this.deathReason != deathReason) {
            this.deathReason = deathReason;
            visualInfo = null; // Invalidate visualInfo
        }
    }
}
