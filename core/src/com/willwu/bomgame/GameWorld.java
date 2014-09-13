package com.willwu.bomgame;

public class GameWorld {

	private Bomb[][] bombs;

	private int score = 0;
	private float runTime = 0;
	private float gameWidth;
	private float gameHeight;
	private GameRenderer renderer;

	private int bombWidth = 20;
	private static final int bombHeight = 20;
	private static final int bombSpacing = 3;

	private GameState currentState;

	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
	}

	public GameWorld(float gameWidth, float gameHeight) {
		currentState = GameState.MENU;
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;

		createBombs2();
		
		currentState = GameState.MENU;
	}

	private void createBombs() {
		// declare bombs
		bombs = new Bomb[3][3]; // create 3 x 3 grid

		int widthSpacer = (int) gameWidth / 3;

		bombWidth = (int) (gameWidth * 0.2);

		int xDraw = (widthSpacer - bombWidth) / 2;

		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows

				bombs[i][j] = new Bomb(this, bombWidth, bombWidth);

				// bombs[i][j].createBomb((i * widthSpacer) + xDraw, xDraw + j * (bombWidth + xDraw));
				bombs[i][j].createBomb(xDraw + i * (bombWidth + xDraw), xDraw + j * (bombWidth + xDraw));
			}
		}
	}

	private void createBombs2() {
		// declare bombs
		bombs = new Bomb[3][3]; // create 3 x 3 grid

		bombWidth = (int) (gameWidth * 0.2);
		int bombSpacer = (int) (bombWidth * 0.2);

		int midpointWidth = (int) (gameWidth / 2);
		int leftX = midpointWidth - (bombWidth / 2) - bombSpacer - bombWidth;

		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows

				bombs[i][j] = new Bomb(this, bombWidth, bombWidth);
				bombs[i][j].createBomb(leftX + (i * bombWidth) + (i * bombSpacer), leftX + (j * bombWidth) + (j * bombSpacer));
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

	public int getScore() {
		return score;
	}
	
	public void resetScore() {
		score = 0;
	}

	public void addScore(int increment) {
		this.score += increment;
	}

	public void subtractScore(int decrement) {
		this.score -= decrement;
	}

}
