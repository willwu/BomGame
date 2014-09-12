package com.willwu.bomgame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private int midPointY;
	private float countDown;

	private static final float COUNTDOWN_RESET_TIME = 0.25f;
	private static final float BOMB_ANIMATION_TIME = 2f;

	// graphic assets
	private Texture texture; // initial bomb texture
	private TextureRegion bombTexture1, bombTexture2, bombTexture3; // bomb's split texture regions

	// sound assets
	public Sound correct, boom;
	public Music intro, loop;

	// animation objects
	private Animation[][] bombAnimation;
	// private float[][] stateTime;

	// game objects
	private Bomb[][] bombs;

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
		intro.play();
		loop.setLooping(true);
	}

	public void render(float delta, float runTime) {

		if (!intro.isPlaying() && !loop.isPlaying()) {
			loop.play();
		}

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

		batcher.end();
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
		}
	}

	private void setupBombGraphics() {
		bombTexture1 = new TextureRegion(texture, 0, 0, 50, 50);
		bombTexture1.flip(false, true);
		bombTexture2 = new TextureRegion(texture, 50, 0, 50, 50);
		bombTexture2.flip(false, true);
		bombTexture3 = new TextureRegion(texture, 100, 0, 50, 50);
		bombTexture3.flip(false, true);

		TextureRegion[] bombTexture = { bombTexture1, bombTexture2, bombTexture3 };

		bombAnimation = new Animation[bombs.length][bombs.length];

		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows
				bombAnimation[i][j] = new Animation(0.67f, bombTexture);
				bombAnimation[i][j].setPlayMode(Animation.PlayMode.NORMAL);
			}
		}
	}
}
