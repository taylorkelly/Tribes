/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core;

import pythagoras.f.Rectangle;
import static playn.core.PlayN.*;

/**
 *
 * @author taylor
 */
public abstract class Being extends DrawnObject {

    protected Personality personality;
    protected float xPos, yPos;
    protected float xVel, yVel;
    protected float width, height;

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

    public class Personality {

        public static final float MUTATION_RATE = 0.05f;
        private float aggression; //determines threshold distance for attacking
        private float strength; //determines how much damage an attack does
        private float courage;
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
            courage = random();
            intelligence = random();
            hardiness = random();
            longevity = random();
            mobility = random();
            reproductiveAppeal = random();
            loyalty = random();
            
            normalize();
        }

        private void normalize() {
            total = aggression + strength + courage + intelligence + hardiness + longevity + mobility + reproductiveAppeal + loyalty;
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

            newPersonality.courage = (random() < 0.5 ? this.courage : personality.courage);
            if (random() < MUTATION_RATE)
                newPersonality.courage = random();

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

        float loyalty() {
            return loyalty / total;
        }
    }
}
