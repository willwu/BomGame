package com.willwu.bomgame;

import com.badlogic.gdx.math.Rectangle;

//bomb object

public class Bomb {

	GameWorld world;

	private int x;
	private int y;

	private int width;
	private int height;

	private boolean isCharged = false;

	private Rectangle rect;

	private float stateTime = 0;

	private boolean exploded = false;

	public Bomb(GameWorld world, int width, int height) {
		this.world = world;
		this.setWidth(width);
		this.setHeight(height);
	}

	public Rectangle getBomb() {
		return rect;
	}

	public void createBomb(int x, int y) {
		this.setX(x);
		this.setY(y);
		rect = new Rectangle(this.getX(), this.getY(), getWidth(), getHeight());
	}

	public void onClick() {

		// TOUCHED
		if (isCharged() && !isExploded()) {
			setCharged(false);
			setStateTime(0);
			System.out.println("good job!");
			if (AssetLoader.getSfx()) {
				AssetLoader.correct.play();
			}
			world.addScore(1);
		} else if (!isCharged() && !isExploded()){
			setCharged(true);
			setExploded(true);
			setStateTime(0);

			System.out.println("u fucked up bro! touched an uncharged bomb");
			if (AssetLoader.getSfx()) {
				AssetLoader.boom.play();
			}
			world.subtractScore(1);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isCharged() {
		return isCharged;
	}

	public void setCharged(boolean isCharged) {
		this.isCharged = isCharged;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public boolean isExploded() {
		return exploded;
	}

	public void setExploded(boolean exploded) {
		this.exploded = exploded;
	}

}
