package com.willwu.bomgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BomGame extends Game {
	SpriteBatch batch;
	Texture img;

	private SplashScreen splashScreen;
	private GameScreen gameScreen;

	@Override
	public void create() {
		AssetLoader.load();
		splashScreen = new SplashScreen(this);
		setScreen(splashScreen);
//		setScreen(new GameScreen());
		
//		gameScreen = new GameScreen();
		
	}

	@Override
	public void render() {
		 super.render();
//		gameScreen.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
