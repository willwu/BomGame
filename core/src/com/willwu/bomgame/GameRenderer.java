package com.willwu.bomgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class GameRenderer {

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private int midPointY;

	// game objects
	private Bomb[][] bombs;

	Vector3 touchPos; // creates a vector3 object for our touch event

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {

		myWorld = world;

		bombs = world.getBombs();

		this.midPointY = midPointY;

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		touchPos = new Vector3();
	}

	public void render(float delta, float runTime) {
		// Tells shapeRenderer to begin drawing filled shapes
		shapeRenderer.begin(ShapeType.Filled);

		// random generator

		// check if bomb touched
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0); // when the screen is touched, the coordinates are inserted into the vector
			cam.unproject(touchPos); // calibrates the input to your camera's dimentions

			for (int i = 0; i < bombs.length; i++) { // do columns
				for (int j = 0; j < bombs.length; j++) { // do rows
					if (touchPos.x > bombs[i][j].getBomb().x && touchPos.x < bombs[i][j].getBomb().x + bombs[i][j].getBomb().width
							&& touchPos.y > bombs[i][j].getBomb().y && touchPos.y < bombs[i][j].getBomb().y + bombs[i][j].getBomb().height) {

						// TOUCHED
						bombs[i][j].onClick();

					}
				}

			}

		}

		// draw bombs
		for (int i = 0; i < bombs.length; i++) { // do columns
			for (int j = 0; j < bombs.length; j++) { // do rows

				shapeRenderer.setColor(bombs[i][j].getColor());
				shapeRenderer.rect(bombs[i][j].getBomb().x, bombs[i][j].getBomb().y, bombs[i][j].getBomb().width, bombs[i][j].getBomb().height);
			}
		}

		shapeRenderer.end();
	}
}
