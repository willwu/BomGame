package com.willwu.bomgame;

import com.badlogic.gdx.math.Rectangle;

//bomb object

public class Bomb {

	private int x;
	private int y;

	private int width;
	private int height;

	private boolean isCharged = false;

	private Rectangle rect;

	public Bomb(int width, int height) {
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

		// if (isCharged) {
		// // RESET
		// setColor(new Color(255, 0, 0, 1));
		// isCharged = true;
		// // AssetLoader.flap.play();
		//
		// } else {
		// // if touched and bomb is not charged
		// // EXPLODE
		// setColor(new Color(0, 0, 255, 1));
		// }
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

}
