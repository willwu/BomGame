package com.willwu.bomgame;

public class GameWorld {

	private Bomb[][] bombs;

	private int score = 0;
	private float runTime = 0;
	private float gameWidth;
	private float gameHeight;
	private GameRenderer renderer;

//	private int bombWidth = 169;

	private GameState currentState;

	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
	}

	public GameWorld(float gameWidth, float gameHeight) {
		currentState = GameState.MENU;
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;

		createBombs3();

		currentState = GameState.MENU;
	}

	private int bombColumns = 4;
	private int bombRows = 3;

//	private void createBombs() {
//
//		// declare bombs
//		bombs = new Bomb[getBombColumns()][getBombRows()]; // create 3 x 3 grid
//
//		bombWidth = (int) (gameWidth * 0.2);
//		int bombSpacer = (int) (bombWidth * 0.2);
//
//		int midpointWidth = (int) (gameWidth / 2);
//		int leftX = midpointWidth - (bombWidth / 2) - bombSpacer - bombWidth;
//
//		for (int i = 0; i < getBombColumns(); i++) { // do columns
//			for (int j = 0; j < getBombRows(); j++) { // do rows
//
//				bombs[i][j] = new Bomb(this, bombWidth, bombWidth);
//				bombs[i][j].createBomb(leftX + (i * bombWidth) + (i * bombSpacer), leftX + (j * bombWidth) + (j * bombSpacer));
//			}
//		}
//	}
	
	private void createBombs3() {

		// declare bombs
		bombs = new Bomb[getBombColumns()][getBombRows()]; // create 3 x 3 grid

		int bombWidth = AssetLoader.animatedBomb[0][0].getRegionWidth();
		int bombHeight = AssetLoader.animatedBomb[0][0].getRegionHeight();
		System.out.println("bomb width is: " + bombWidth);
		System.out.println("bomb height is: " + bombHeight);
		
		int bombSpacer = 150;

		int leftX = 54;
		int leftY = 150;

		for (int i = 0; i < getBombColumns(); i++) { // do columns
			for (int j = 0; j < getBombRows(); j++) { // do rows

				bombs[i][j] = new Bomb(this, bombWidth, bombHeight);
				bombs[i][j].createBomb(leftX + (i * bombWidth), leftY + (j * bombHeight));
				System.out.println("created bomb at: " + (i * bombWidth) + ",  " +  (leftY + (j * bombHeight)));
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

		switch (currentState) {
		case MENU:
			// updateReady(delta);
			break;

		case RUNNING:
			// updateRunning(delta);
			break;
		default:
			break;
		}
	}

	public void setRunning() {
		currentState = GameState.RUNNING;
	}

	public void setReady() {
		currentState = GameState.READY;
		renderer.prepareTransition(0, 0, 0, 1f);
	}

	public void restart() {
		renderer.restart();
		// setReady();
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}

	public boolean isMenu() {
		return currentState == GameState.MENU;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
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

	public int getBombColumns() {
		return bombColumns;
	}

	public void setBombColumns(int bombColumns) {
		this.bombColumns = bombColumns;
	}

	public int getBombRows() {
		return bombRows;
	}

	public void setBombRows(int bombRows) {
		this.bombRows = bombRows;
	}

}
