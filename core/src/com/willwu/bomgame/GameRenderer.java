package com.willwu.bomgame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	public static Texture texture; // initial bomb texture
	private TextureRegion bombTexture1, bombTexture2, bombTexture3; // bomb's split texture regions

	// sound assets
	Sound correct = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
	Sound boom = Gdx.audio.newSound(Gdx.files.internal("boom.wav"));
	Music music = Gdx.audio.newMusic(Gdx.files.internal("cc.wav"));

	// animation objects
	private Animation[][] bombAnimation;
	private float[][] stateTime;

	// game objects
	private Bomb[][] bombs;

	Vector3 touchPos; // creates a vector3 object for our touch event

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {

		this.world = world;

		this.midPointY = midPointY;

		// initialise everything
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		texture = new Texture(Gdx.files.internal("bomb.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		touchPos = new Vector3();

		bombs = world.getBombs();

		setupBombGraphic();

		countDown = COUNTDOWN_RESET_TIME;
		music.setLooping(true);
		music.play();
	}

	public void render(float delta, float runTime) {

		// random generator
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

		batcher.begin();
		batcher.disableBlending();

		// check if bomb touched
		if (Gdx.input.justTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(touchPos); // calibrate touch coord to camera

			for (int i = 0; i < bombs.length; i++) { // do columns
				for (int j = 0; j < bombs.length; j++) { // do rows

					// check if touched a bomb
					if (touchPos.x > bombs[i][j].getBomb().x && touchPos.x < bombs[i][j].getBomb().x + bombs[i][j].getBomb().width
							&& touchPos.y > bombs[i][j].getBomb().y && touchPos.y < bombs[i][j].getBomb().y + bombs[i][j].getBomb().height) {

						// TOUCHED
						// if (!bombs[i][j].isCharged()) {
						// bombs[i][j].setCharged(true);
						// System.out.println("charging bomb " + i + " " + j);
						// drawOneBomb(stateTime[i][j], i, j);
						// }
						if (bombs[i][j].isCharged()) {
							bombs[i][j].setCharged(false);
							stateTime[i][j] = 0;
							System.out.println("good job!");
							correct.play();
						} else {
							System.out.println("u fucked up bro! touched an uncharged bomb");
							boom.play();
						}
					}
				}
			}

		} else { // if not touched
			// draw standard bomb
			for (int i = 0; i < bombs.length; i++) { // do columns
				for (int j = 0; j < bombs.length; j++) { // do rows

					// if not charged
					if (!bombs[i][j].isCharged()) {
						// draw bomb normally
						drawStaticBomb(i, j);
					} else {
						// carry on drawing animation
						drawOneBomb(delta, i, j);
					}
				}
			}
		}

		batcher.end();
	}

	private int randomGenerator() {
		Random rand = new Random();
		int random = rand.nextInt(bombs.length);
		return random;
	}

	private void drawStaticBomb(int i, int j) {
		batcher.draw(bombTexture1, bombs[i][j].getBomb().x, bombs[i][j].getBomb().y, bombs[i][j].getBomb().width, bombs[i][j].getBomb().height);
	}

	private void drawOneBomb(float delta, int i, int j) {
		stateTime[i][j] += delta;

		if (!bombAnimation[i][j].isAnimationFinished(stateTime[i][j])) {
			batcher.draw(bombAnimation[i][j].getKeyFrame(stateTime[i][j], false), bombs[i][j].getX(), bombs[i][j].getY(),
					bombs[i][j].getWidth() / 2.0f, bombs[i][j].getHeight() / 2.0f, bombs[i][j].getWidth(), bombs[i][j].getHeight(), 1, 1, 0);
		}

		// reset charge when animation finished
		if (bombAnimation[i][j].isAnimationFinished(stateTime[i][j])) {
			bombs[i][j].setCharged(false);
			stateTime[i][j] = 0;
			System.out.println("ur too slow mate, bomb exploded!");
			boom.play();
		}
	}

	private void setupBombGraphic() {
		bombTexture1 = new TextureRegion(texture, 0, 0, 50, 50);
		bombTexture1.flip(false, true);
		bombTexture2 = new TextureRegion(texture, 50, 0, 50, 50);
		bombTexture2.flip(false, true);
		bombTexture3 = new TextureRegion(texture, 100, 0, 50, 50);
		bombTexture3.flip(false, true);

		TextureRegion[] bombTexture = { bombTexture1, bombTexture2, bombTexture3 };

		bombAnimation = new Animation[bombs.length][bombs.length];
		stateTime = new float[bombs.length][bombs.length];

		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows
				bombAnimation[i][j] = new Animation(0.67f, bombTexture);
				stateTime[i][j] = 0;
				bombAnimation[i][j].setPlayMode(Animation.PlayMode.NORMAL);
			}
		}
	}
}
