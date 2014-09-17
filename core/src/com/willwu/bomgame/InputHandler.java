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

	private SimpleButton playButton, sfxButton;

	public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {
		this.world = world;

		bombs = world.getBombs();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

		menuButtons = new ArrayList<SimpleButton>();
		playButton = new SimpleButton(50, 50, 200, 200, AssetLoader.bombTexture2, AssetLoader.bombTexture3);
		getMenuButtons().add(playButton);

		sfxButton = new SimpleButton(50, 300, 200, 200, AssetLoader.bombTexture1, AssetLoader.bombTexture3);
		if (AssetLoader.getSfx()) {
			sfxButton.setSwitchOn(true);
		}else{
			sfxButton.setSwitchOn(false);
		}
		getMenuButtons().add(sfxButton);
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
			sfxButton.isTouchDown(screenX, screenY);
		}

		if (world.isRunning()) {
			for (int i = 0; i < world.getBombColumns(); i++) { // do columns
				for (int j = 0; j < world.getBombRows(); j++) { // do rows

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
			} else if (sfxButton.isTouchUp(screenX, screenY)) {
				if (AssetLoader.getSfx()) {
					sfxButton.setSwitchOn(false);
					AssetLoader.setSfx(false);
				} else {
					sfxButton.setSwitchOn(true);
					AssetLoader.setSfx(true);
				}

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
