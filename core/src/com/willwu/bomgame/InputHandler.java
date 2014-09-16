package com.willwu.bomgame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {

	private GameWorld world;

	private float scaleFactorX;
	private float scaleFactorY;
	private Bomb[][] bombs;

	private List<SimpleButton> menuButtons;

	private SimpleButton playButton;

	public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {
		this.world = world;

		bombs = world.getBombs();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

		menuButtons = new ArrayList<SimpleButton>();
		playButton = new SimpleButton(50, 50, 50, 50, AssetLoader.bombTexture2, AssetLoader.bombTexture3);
		getMenuButtons().add(playButton);
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
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (world.isMenu()) {
			playButton.isTouchDown(screenX, screenY);
		}

		if (world.isRunning()) {
			for (int i = 0; i < bombs.length; i++) { // do columns
				for (int j = 0; j < bombs.length; j++) { // do rows

					// check if touched a bomb
					if (bombs[i][j].getBomb().contains(screenX, screenY)) {
						bombs[i][j].onClick();
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		
		if (world.isMenu()) {
			if (playButton.isTouchUp(screenX, screenY)) {
				world.restart();
				world.setRunning();
				return true;
			}
		}

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

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}

	public List<SimpleButton> getMenuButtons() {
		return menuButtons;
	}

}
