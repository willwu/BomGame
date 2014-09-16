package com.willwu.bomgame;

import java.util.List;
import java.util.Random;

import TweenAccessors.Value;
import TweenAccessors.ValueAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private float bombSpawnCountdown;

	private static float COUNTDOWN_RESET_TIME = 0.25f;
	private static float SUPER_COUNTDOWN_RESET_TIME = 0.1f;
	private static final float BOMB_ANIMATION_FRAME_TIME = 0.2f;

	private static final int COUNTDOWN_TIMER_TIME = 30;

	// graphic assets
	private Texture texture, aniTexture; // initial bomb texture
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

		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);

	}
	
	private TextureRegion[][] animated;

	private void initAssets() {
		texture = AssetLoader.texture;
		aniTexture = AssetLoader.aniTexture;
		
		animated = TextureRegion.split(aniTexture, 135, 135);
		
		for (int i = 0; i < animated[0].length; i++){
			animated[0][i].flip(false, true);
		}

		// WHY DOESNT THIS WORK!?!?!
		// bombTexture1 = AssetLoader.bombTexture1;
		// bombTexture2 = AssetLoader.bombTexture1;
		// bombTexture3 = AssetLoader.bombTexture1;
		
		

		bombTexture1 = new TextureRegion(texture, 0, 0, 50, 50);
		bombTexture2 = new TextureRegion(texture, 50, 0, 50, 50);
		bombTexture3 = new TextureRegion(texture, 100, 0, 50, 50);
		bombTexture1.flip(false, true);
		bombTexture2.flip(false, true);
		bombTexture3.flip(false, true);

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
		// rr.play();

		font = AssetLoader.font;

		menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();
	}

	public void render(float delta, float runTime) {

		// super mode control via music
		if (rr.getPosition() >= 8 && rr.getPosition() <= 24) {
			if (!superMode) {
				prepareTransition(255, 255, 255, .25f);
			}
			superMode = true;
		} else {
			if (superMode) {
				prepareTransition(255, 255, 255, .25f);
			}
			superMode = false;
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// clear screen
		if (superMode) { // SUPERMOOOOOOOOOODE!!!!!!!!!!!!!!
			Gdx.gl.glClearColor(0.4f, 0f, 0.25f, 1f);
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}

		batcher.begin();
		// batcher.disableBlending();
		batcher.enableBlending();

		if (world.isMenu()) {
			for (SimpleButton button : menuButtons) {
				button.draw(batcher);
			}
		} else if (world.isRunning()) {

			// random generator
			chargeRandomBomb(delta);

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

		}

		batcher.end();

		drawTransition(delta);
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
			restart();
		}

		font.setColor(1, 0, 0, 1);

		String text = "Time:" + String.format("%.03f", countdownTimer);

		float boundWidth = font.getBounds(text).width;
		font.draw(batcher, text, cam.viewportWidth / 2 - boundWidth / 2, cam.viewportHeight / 2 - (2 * font.getXHeight()) + (2 * 5));
	}

	public void restart() {
		countdownTimer = COUNTDOWN_TIMER_TIME; // reset countdown timer
		world.resetScore();

		intro.stop();
		loop.stop();
		rrEndLoop.stop();
		rr.stop();
		rr.play();
		prepareTransition(0, 0, 0, 1f);
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
//		batcher.draw(bombTexture1, bombs[i][j].getBomb().x, bombs[i][j].getBomb().y, bombs[i][j].getBomb().width, bombs[i][j].getBomb().height);
		batcher.draw(animated[0][0], bombs[i][j].getBomb().x, bombs[i][j].getBomb().y, bombs[i][j].getBomb().width, bombs[i][j].getBomb().height);
	}

	private void drawSingleBomb(float delta, int i, int j) {

		float currentStateTime = bombs[i][j].getStateTime() + delta;
		bombs[i][j].setStateTime(currentStateTime);

		// draw animation
		if (!bombAnimation[i][j].isAnimationFinished(currentStateTime)) {
			batcher.draw(bombAnimation[i][j].getKeyFrame(currentStateTime, false), bombs[i][j].getX(), bombs[i][j].getY(),
					bombs[i][j].getWidth() / 2.0f, bombs[i][j].getHeight() / 2.0f, bombs[i][j].getWidth(), bombs[i][j].getHeight(), 1, 1, 0);
		}
		
		//explode
		if (bombAnimation[i][j].getKeyFrameIndex(currentStateTime) >= 6 && !bombs[i][j].isExploded()) {
//			bombs[i][j].setCharged(false);
//			bombs[i][j].setStateTime(0);
			bombs[i][j].setExploded(true);
			System.out.println("ur too slow mate, bomb exploded!");
			boom.play();
			world.subtractScore(1);
		}
		

		// reset charge when animation finished
		if (bombAnimation[i][j].getKeyFrameIndex(currentStateTime) == 10) {
//		if (bombAnimation[i][j].isAnimationFinished(currentStateTime)) {
			bombs[i][j].setCharged(false);
			bombs[i][j].setStateTime(0);
			bombs[i][j].setExploded(false);
//			System.out.println("ur too slow mate, bomb exploded!");
//			boom.play();
//			world.subtractScore(1);
		}
	}

	private void setupBombGraphics() {
//		TextureRegion[] bombTexture = { bombTexture2, bombTexture3 };

		bombAnimation = new Animation[bombs.length][bombs.length];

		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows
				
//				bombAnimation[i][j] = new Animation(BOMB_ANIMATION_FRAME_TIME, bombTexture);
//				bombAnimation[i][j].setPlayMode(Animation.PlayMode.NORMAL);
				
				bombAnimation[i][j] = new Animation(BOMB_ANIMATION_FRAME_TIME, animated[0]);
				bombAnimation[i][j].setPlayMode(Animation.PlayMode.NORMAL);
			}
		}
	}

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private List<SimpleButton> menuButtons;
	private Color transitionColor;

	public void prepareTransition(int r, int g, int b, float duration) {
		transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
		alpha.setValue(1);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, duration).target(0).ease(TweenEquations.easeOutQuad).start(manager);
	}

	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(transitionColor.r, transitionColor.g, transitionColor.b, alpha.getValue());
			shapeRenderer.rect(0, 0, 300, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

		}
	}
}
