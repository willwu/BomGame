package com.willwu.bomgame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private float bombSpawnCountdown;

	private static float COUNTDOWN_RESET_TIME = 0.25f;
	private static float SUPER_COUNTDOWN_RESET_TIME = 0.1f;
	private static final float BOMB_ANIMATION_FRAME_TIME = 1f;

	private static final int COUNTDOWN_TIMER_TIME = 30;

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

	// SUPER MODE TESTS
	private boolean superMode = false;
	private Color COUNTDOWN_TIMER_COLOR_SUPER = new Color(0, 0, 1, 1);
	Music rrIntro;
	Music rrSuper;
	Music rrOut;
	Music rr;
	Music rrEndLoop;
	private static final int SUPER_COUNTDOWN_TIMER_TIME = 16;
	float superModeTimer = SUPER_COUNTDOWN_TIMER_TIME;

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

		bombSpawnCountdown = COUNTDOWN_RESET_TIME;

	}

	private void initAssets() {
		texture = AssetLoader.texture;
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		correct = AssetLoader.correct;
		boom = AssetLoader.boom;
		intro = AssetLoader.intro;
		loop = AssetLoader.loop;

		// intro.setLooping(false);
		//
		// intro.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(Music music) {
		// superMode = true;
		// loop.play();
		// }
		// });
		// intro.play();
		//
		// loop.setLooping(false);
		// loop.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(Music music) {
		// superMode = false;
		// loop.setLooping(true);
		// loop.play();
		// }
		// });

		// ////////////////////////////////

		rrIntro = AssetLoader.rrIntro;
		rrSuper = AssetLoader.rrSuper;
		rrOut = AssetLoader.rrOut;
		rr = AssetLoader.rr;
		rrEndLoop = AssetLoader.rrEndLoop;

		// rrIntro.setLooping(false);
		// rrIntro.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(Music music) {
		// COUNTDOWN_RESET_TIME = 0.1f;
		// superMode = true;
		// rrSuper.play();
		// }
		// });
		// rrIntro.play();
		// rrOut.setLooping(true);
		// rrSuper.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(Music music) {
		// rrOut.play();
		// }
		// });

		rrEndLoop.setLooping(true);
		rr.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(Music music) {
				rrEndLoop.play();
			}
		});
		rr.play();

		font = AssetLoader.font;
	}

	public void render(float delta, float runTime) {

		if (rr.getPosition() >= 8 && rr.getPosition() <= 24) {
			superMode = true;
		} else {
			superMode = false;
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// clear screen
		if (superMode) { // SUPERMOOOOOOOOOODE!!!!!!!!!!!!!!
			Gdx.gl.glClearColor(0.4f, 0f, 0.25f, 1f);
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}

		// random generator
		chargeRandomBomb(delta);

		batcher.begin();
		// batcher.disableBlending();
		batcher.enableBlending();

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

		handleSuperModeTimer(delta);

		batcher.end();
	}

	private void handleSuperModeTimer(float delta) {

		if (superModeTimer <= 0) {
			COUNTDOWN_RESET_TIME = 0.25f;
			superMode = false;
			superModeTimer = SUPER_COUNTDOWN_TIMER_TIME; // reset countdown timer
		} else {
			if (superMode) {
				superModeTimer -= delta;
				font.setColor(COUNTDOWN_TIMER_COLOR_SUPER);

				String text = "SUPER:" + String.format("%.03f", superModeTimer);

				float boundWidth = font.getBounds(text).width;
				font.draw(batcher, text, cam.viewportWidth / 2 - boundWidth / 2, cam.viewportHeight / 2 - (3 * font.getXHeight()) + (3 * 5));
			}
		}
	}

	private void handleCountdownTimer(float delta) {
		if (!superMode) {
			countdownTimer -= delta;
		}

		if (countdownTimer <= 0) {
			countdownTimer = COUNTDOWN_TIMER_TIME; // reset countdown timer
			world.resetScore();

			rrEndLoop.stop();
			rr.play();
		}

		font.setColor(1, 0, 0, 1);

		String text = "Time:" + String.format("%.03f", countdownTimer);

		float boundWidth = font.getBounds(text).width;
		font.draw(batcher, text, cam.viewportWidth / 2 - boundWidth / 2, cam.viewportHeight / 2 - (2 * font.getXHeight()) + (2 * 5));
	}

	private void drawScore(int score) {
		font.setColor(0, 1, 0, 1);

		String text = "Score:" + score;

		float boundWidth = font.getBounds(text).width;
		font.draw(batcher, text, cam.viewportWidth / 2 - boundWidth / 2, cam.viewportHeight / 2);
	}

	private void drawHighScore(int score) {
		if (score > AssetLoader.getHighScore()) {
			AssetLoader.setHighScore(score);
		}
		font.setColor(1, 1, 0, 1);

		String text = "High Score:" + AssetLoader.getHighScore();

		float boundWidth = font.getBounds(text).width;
		font.draw(batcher, text, cam.viewportWidth / 2 - boundWidth / 2, cam.viewportHeight / 2 - font.getXHeight() + 5);
	}

	private void chargeRandomBomb(float delta) {
		bombSpawnCountdown -= delta;
		if (bombSpawnCountdown <= 0) {
			int row = 0;
			int col = 0;
			do {
				row = randomGenerator();
				col = randomGenerator();

				if (!bombs[row][col].isCharged()) {
					bombs[row][col].setCharged(true);
				}

			} while (!bombs[row][col].isCharged());

			// reset countdown
			if (superMode) {
				bombSpawnCountdown = SUPER_COUNTDOWN_RESET_TIME;
			} else {
				bombSpawnCountdown = COUNTDOWN_RESET_TIME;
			}
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
		bombTexture1 = AssetLoader.bombTexture1;
		bombTexture2 = AssetLoader.bombTexture1;
		bombTexture3 = AssetLoader.bombTexture1;

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
