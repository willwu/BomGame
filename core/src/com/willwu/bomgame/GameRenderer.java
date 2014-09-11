package com.willwu.bomgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private int midPointY;

	//graphic assets
	public static Texture texture;
	private TextureRegion bombTexture;
	private TextureRegion bombChargedTexture;
	
	
	// game objects
	private Bomb[][] bombs;

	Vector3 touchPos; // creates a vector3 object for our touch event

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {

		this.world = world;

		this.midPointY = midPointY;

		
		//initialise everything
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		texture = new Texture(Gdx.files.internal("bomb.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		setBombTexture();

		touchPos = new Vector3();
	}

	public void render(float delta, float runTime) {
		
		// random generator

		
		//set initial texture
		for (int i = 0; i < getBombs().length; i++) { // do columns
			for (int j = 0; j < getBombs().length; j++) { // do rows
				getBombs()[i][j].setBombTexture(bombTexture);
			}
		}
		
		// check if bomb touched
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0); // when the screen is touched, the coordinates are inserted into the vector
			cam.unproject(touchPos); // calibrates the input to your camera's dimentions

			for (int i = 0; i < getBombs().length; i++) { // do columns
				for (int j = 0; j < getBombs().length; j++) { // do rows
					
					if (touchPos.x > getBombs()[i][j].getBomb().x && touchPos.x < getBombs()[i][j].getBomb().x + getBombs()[i][j].getBomb().width
							&& touchPos.y > getBombs()[i][j].getBomb().y && touchPos.y < getBombs()[i][j].getBomb().y + getBombs()[i][j].getBomb().height) {

						// TOUCHED
						getBombs()[i][j].onClick();
						getBombs()[i][j].setBombTexture(bombChargedTexture);
					}
				}

			}

		}

//		// Tells shapeRenderer to begin drawing filled shapes
//		shapeRenderer.begin(ShapeType.Filled);
//		// draw bombs
//		for (int i = 0; i < bombs.length; i++) { // do columns
//			for (int j = 0; j < bombs.length; j++) { // do rows
//
//				shapeRenderer.setColor(bombs[i][j].getColor());
//				shapeRenderer.rect(bombs[i][j].getBomb().x, bombs[i][j].getBomb().y, bombs[i][j].getBomb().width, bombs[i][j].getBomb().height);
//			}
//		}
//		shapeRenderer.end();
		
		
		batcher.begin();
		batcher.disableBlending();
		for (int i = 0; i < getBombs().length; i++) { // do columns
			for (int j = 0; j < getBombs().length; j++) { // do rows
				batcher.draw(getBombs()[i][j].getBombTexture(), getBombs()[i][j].getBomb().x, getBombs()[i][j].getBomb().y, getBombs()[i][j].getBomb().width, getBombs()[i][j].getBomb().height);		
			}
		}
		batcher.end();
	}

	public TextureRegion getBombTexture() {
		return bombTexture;
	}

	public void setBombTexture() {
		this.bombTexture = new TextureRegion(texture, 0, 0, 50, 50);
		this.bombTexture.flip(false, true);
		
		this.bombChargedTexture= new TextureRegion(texture, 50, 0, 50, 50);
		this.bombChargedTexture.flip(false, true);
	}

	public Bomb[][] getBombs() {
		return bombs;
	}

	public void setBombs(Bomb[][] bombs) {
		this.bombs = bombs;
	}
}
