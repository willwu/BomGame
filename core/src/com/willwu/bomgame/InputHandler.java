package com.willwu.bomgame;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
	private Bomb[][] bombs;
	private GameWorld world;

	private float scaleFactorX;
	private float scaleFactorY;

	public InputHandler(GameWorld world, float scaleFactorX,
			float scaleFactorY) {
		this.world = world;
		bombs = world.getBombs();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		//check if t
		bombs[0][0].onClick();
		
		

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
