package com.willwu.bomgame;

public class GameWorld {

	private Bomb[][] bombs;

	private int score = 0;
	private float runTime = 0;
	private int midPointY;
	private GameRenderer renderer;
	
	
	private int bombWidth = 20;
	private int bombHeight = 20;
	private int bombSpacing = 3;
	
	

	private GameState currentState;

	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
	}

	public GameWorld(int midPointY) {
		currentState = GameState.MENU;
		this.midPointY = midPointY;

		bombs = new Bomb[3][3]; // create 3 x 3 grid

		// declare bombs
		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows
				bombs[i][j] = new Bomb(bombWidth, bombHeight);
				bombs[i][j].createBomb((i + 1) * (bombWidth + bombSpacing), (j + 1) * (bombHeight + bombSpacing));
			}
		}

	}

	public Bomb[][] getBombs() {
		return bombs;
	}

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}

	public void update(float delta) {
		runTime += delta;
	}

}
