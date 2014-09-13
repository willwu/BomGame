package com.willwu.bomgame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private float countDown;

	private static final float COUNTDOWN_RESET_TIME = 0.25f;
	private static final float BOMB_ANIMATION_FRAME_TIME = 1f;
	private static final int COUNTDOWN_TIMER_TIME = 10;

	// graphic assets
	private Texture texture; // initial bomb texture
	private TextureRegion bombTexture1, bombTexture2, bombTexture3; // bomb's split texture regions
	private BitmapFont font;

	// sound assets
	public Sound correct, boom;
	public Music intro, loop;

	// animation objects
	private Animation[][] bombAnimation;

	// game objects
	private Bomb[][] bombs;

	float countdownTimer = COUNTDOWN_TIMER_TIME;

	public GameRenderer(GameWorld world, float gameWidth, float gameHeight) {

		this.world = world;

		// this.midPointY = midPointY;

		// initialise everything
		cam = new OrthographicCamera();
		cam.setToOrtho(true, gameWidth, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		initAssets();
		bombs = world.getBombs();

		setupBombGraphics();

		countDown = COUNTDOWN_RESET_TIME;

	}

	private void initAssets() {
		texture = AssetLoader.texture;
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		correct = AssetLoader.correct;
		boom = AssetLoader.boom;
		intro = AssetLoader.intro;
		loop = AssetLoader.loop;

		intro.setLooping(false);
		loop.setLooping(true);
		intro.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(Music music) {
				loop.play();
			}
		});
		intro.play();

		font = AssetLoader.font;
	}

	public void render(float delta, float runTime) {

		// clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// random generator
		chargeRandomBomb(delta);

		batcher.begin();
		batcher.disableBlending();

		// draw standard bomb
		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows

				// if not charged
				if (!bombs[i][j].isCharged()) {
					// draw bomb normally
					drawStaticBomb(i, j);
				} else {
					// carry on drawing animation
					drawSingleBomb(delta, i, j);
				}
			}
		}

		int score = world.getScore();
		drawScore(score);
		drawHighScore(score);

		handleCountdownTimer(delta);

		batcher.end();
	}

	private void handleCountdownTimer(float delta) {
		countdownTimer -= delta;
		if(countdownTimer <= 0){
			countdownTimer = COUNTDOWN_TIMER_TIME; //reset countdown timer
		}
		font.setColor(1, 0, 0, 1);
		font.draw(batcher, "Time:" + String.format("%.03f", countdownTimer), bombs[0][0].getX(), cam.viewportHeight / 2 - (2 * font.getXHeight()) + (2 * 5));
	}

	private void drawScore(int score) {
		font.setColor(0, 1, 0, 1);
		// font.draw(batcher, "FPS:" + Gdx.graphics.getFramesPerSecond(), bombs[0][0].getX(), cam.viewportHeight / 2);

		font.draw(batcher, "Score:" + score, bombs[0][0].getX(), cam.viewportHeight / 2);
	}

	private void drawHighScore(int score) {
		if (score > AssetLoader.getHighScore()) {
			AssetLoader.setHighScore(score);
		}
		font.setColor(1, 1, 0, 1);
		font.draw(batcher, "High Score:" + AssetLoader.getHighScore(), bombs[0][0].getX(), cam.viewportHeight / 2 - font.getXHeight() + 5);
	}

	private void chargeRandomBomb(float delta) {
		countDown -= delta;
		if (countDown <= 0) {
			int row = 0;
			int col = 0;
			// do{
			row = randomGenerator();
			col = randomGenerator();

			if (!bombs[row][col].isCharged()) {
				bombs[row][col].setCharged(true);
			}

			// }while(!bombs[row][col].isCharged());
			countDown = COUNTDOWN_RESET_TIME; // reset countdown
		}
	}

	private int randomGenerator() {
		Random rand = new Random();
		int random = rand.nextInt(bombs.length);
		return random;
	}

	private void drawStaticBomb(int i, int j) {
		batcher.draw(bombTexture1, bombs[i][j].getBomb().x, bombs[i][j].getBomb().y, bombs[i][j].getBomb().width, bombs[i][j].getBomb().height);
	}

	private void drawSingleBomb(float delta, int i, int j) {

		float currentStateTime = bombs[i][j].getStateTime() + delta;
		bombs[i][j].setStateTime(currentStateTime);

		// draw animation
		if (!bombAnimation[i][j].isAnimationFinished(currentStateTime)) {
			batcher.draw(bombAnimation[i][j].getKeyFrame(currentStateTime, false), bombs[i][j].getX(), bombs[i][j].getY(),
					bombs[i][j].getWidth() / 2.0f, bombs[i][j].getHeight() / 2.0f, bombs[i][j].getWidth(), bombs[i][j].getHeight(), 1, 1, 0);
		}

		// reset charge when animation finished
		if (bombAnimation[i][j].isAnimationFinished(currentStateTime)) {
			bombs[i][j].setCharged(false);
			bombs[i][j].setStateTime(0);
			System.out.println("ur too slow mate, bomb exploded!");
			boom.play();
			world.subtractScore(1);
		}
	}

	private void setupBombGraphics() {
		bombTexture1 = new TextureRegion(texture, 0, 0, 50, 50);
		bombTexture1.flip(false, true);
		bombTexture2 = new TextureRegion(texture, 50, 0, 50, 50);
		bombTexture2.flip(false, true);
		bombTexture3 = new TextureRegion(texture, 100, 0, 50, 50);
		bombTexture3.flip(false, true);

		TextureRegion[] bombTexture = { bombTexture2, bombTexture3 };

		bombAnimation = new Animation[bombs.length][bombs.length];

		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows
				bombAnimation[i][j] = new Animation(BOMB_ANIMATION_FRAME_TIME, bombTexture);
				bombAnimation[i][j].setPlayMode(Animation.PlayMode.NORMAL);
			}
		}
	}
}
