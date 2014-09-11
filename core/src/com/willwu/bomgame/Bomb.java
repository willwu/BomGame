package com.willwu.bomgame;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.math.Rectangle;

//bomb object

public class Bomb {

	private int x;
	private int y;

	private int width;
	private int height;

	private boolean isCharged = false;

	private Rectangle rect;
	private Color color;

	public Bomb(int width, int height) {
		setColor(new Color(255, 0, 0, 1));
		
		this.width = width;
		this.height = height;
		
	}

	public Rectangle getBomb() {
		return rect;
	}
	
	public Color getColor(){
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public void createBomb(int x, int y) {
		rect = new Rectangle(x, y, width, height);
	}

	public void onClick() {
		setColor(new Color(0, 0, 255, 1));
		
//		if (isCharged) {
//			// RESET
//			setColor(new Color(255, 0, 0, 1));
//			isCharged = true;
//			// AssetLoader.flap.play();
//
//		} else {
//			// if touched and bomb is not charged
//			// EXPLODE
//			setColor(new Color(0, 0, 255, 1));
//		}
	}

}
