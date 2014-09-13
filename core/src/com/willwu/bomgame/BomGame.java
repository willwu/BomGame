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
		 
		screen.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
