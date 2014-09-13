package com.willwu.bomgame;

public class GameWorld {

	private Bomb[][] bombs;

	private int score = 0;
	private float runTime = 0;
	private float gameWidth;
	private float gameHeight;
	private GameRenderer renderer;
	
	
	private  int bombWidth = 20;
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

		createBombs();
	}

	private void createBombs() {
		// declare bombs
		bombs = new Bomb[3][3]; // create 3 x 3 grid
		
		int widthSpacer = (int) gameWidth / 3;
		int heightSpacer = (int) gameHeight / 3;
		
		bombWidth = (int) (gameWidth * 0.2);
		
		int xDraw = (widthSpacer - bombWidth) / 2;
		System.out.println("game width is " + gameWidth);
		System.out.println("game height is " + gameHeight);
		System.out.println("widthspacer is " + widthSpacer);
		System.out.println("xdraw is " + xDraw);
		System.out.println("bombwidth is " + bombWidth);
		
		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows
				
				bombs[i][j] = new Bomb(this, bombWidth, bombWidth);
				
				bombs[i][j].createBomb((i * widthSpacer) + xDraw, xDraw + j * (bombWidth + xDraw));
				System.out.println("created bomb at " + ((i * widthSpacer) + xDraw) + " " +  (j * bombWidth + xDraw));
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

	public void addScore(int increment) {
		this.score += increment;
	}
	
	public void subtractScore(int decrement) {
		this.score -= decrement;
	}

}
