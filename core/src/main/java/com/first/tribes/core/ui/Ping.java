package com.first.tribes.core.ui;

import playn.core.Color;
import pythagoras.f.Point;


public class Ping {
	public static final int REDCOLOR =255;
	public static final int GREENCOLOR =255;
	public static final int BLUECOLOR =0;
	public static final int INITIAL_ALPHA=255;
	public static final int ALPHA_DECREMENT=25;
	public static final int RADIUS=10;
	
	private Point point;
	private int alphaLevel;
	
	
	public Ping(Point p){
		alphaLevel=INITIAL_ALPHA;
		
		point =p;
	}
	
	public void update(){
		alphaLevel-=ALPHA_DECREMENT;
	}
	
	public int alphaLevel(){
		return alphaLevel;
	}
	public Point point(){
		return point;
	}
}
