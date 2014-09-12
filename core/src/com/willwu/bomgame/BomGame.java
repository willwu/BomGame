package com.willwu.bomgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BomGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private GameScreen screen;

	@Override
	public void create() {
		AssetLoader.load();
		screen = new GameScreen();

		
	}

	@Override
	public void render() {
		 Gdx.gl.glClearColor(0, 0, 0, 1);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screen.render(Gdx.graphics.getDeltaTime());
	}
}
