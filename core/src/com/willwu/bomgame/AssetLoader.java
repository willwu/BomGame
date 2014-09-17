package com.willwu.bomgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Sound correct, boom;
	public static Music intro, loop;
	public static Music rrIntro, rrSuper, rrOut;
	public static Music rr, rrEndLoop;

	public static Texture texture;// initial bomb texture
	public static Texture bombTexture;
	public static Texture explosionTexture;
	public static Texture gameBackgroundTexture;
	public static Texture gameBackgroundTexture2;

	public static TextureRegion bombTexture1, bombTexture2, bombTexture3; // bomb's split texture regions

	public static BitmapFont font;

	private static Preferences prefs;

	public static TextureRegion[][] animatedBomb;
	public static TextureRegion[][] animatedExplosion;
	public static TextureRegion gameBackground;
	public static TextureRegion gameBackground2;

	public static void load() {

		// load sounds and music
		correct = Gdx.audio.newSound(Gdx.files.internal("correct.wav"));
		boom = Gdx.audio.newSound(Gdx.files.internal("boom.ogg"));
		intro = Gdx.audio.newMusic(Gdx.files.internal("intro.ogg"));
		loop = Gdx.audio.newMusic(Gdx.files.internal("loop.ogg"));

		rrIntro = Gdx.audio.newMusic(Gdx.files.internal("RRintro.ogg"));
		rrSuper = Gdx.audio.newMusic(Gdx.files.internal("RRsuper.ogg"));
		rrOut = Gdx.audio.newMusic(Gdx.files.internal("RRout.ogg"));

		rr = Gdx.audio.newMusic(Gdx.files.internal("rrbig.ogg"));
		rrEndLoop = Gdx.audio.newMusic(Gdx.files.internal("rrendloop.ogg"));

		// load textures
		texture = new Texture(Gdx.files.internal("bomb.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		gameBackgroundTexture = new Texture(Gdx.files.internal("Game_BG02_540x960.png"));
		gameBackgroundTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		gameBackgroundTexture2 = new Texture(Gdx.files.internal("Game_BG01_540x960.png"));
		gameBackgroundTexture2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		gameBackground = new TextureRegion(gameBackgroundTexture);
		gameBackground.flip(false, true);
		gameBackground2 = new TextureRegion(gameBackgroundTexture2);
		gameBackground2.flip(false, true);

		bombTexture = new Texture(Gdx.files.internal("BomSheet_109x157.png"));
		bombTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		animatedBomb = TextureRegion.split(bombTexture, 109, 157);
		
		explosionTexture = new Texture(Gdx.files.internal("ExpSheet_173x185.png"));
		explosionTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		animatedExplosion = TextureRegion.split(explosionTexture, 173, 185);

		// load font
		// font = new BitmapFont(Gdx.files.internal("whitetext.fnt"));
		// font.setScale(.15f, -.15f);
		font = new BitmapFont(Gdx.files.internal("font.txt"));
		font.setScale(.5f, -.5f);

		// setup preferences
		setupInitialPreferences();

		bombTexture1 = new TextureRegion(texture, 0, 0, 50, 50);
		bombTexture2 = new TextureRegion(texture, 50, 0, 50, 50);
		bombTexture3 = new TextureRegion(texture, 100, 0, 50, 50);

	}

	private static void setupInitialPreferences() {
		prefs = Gdx.app.getPreferences("BomGame");

		// create high score
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}

		if (!prefs.contains("sfx")) {
			prefs.putBoolean("sfx", true);
		}
	}

	public static void setSfx(boolean enabled) {
		prefs.putBoolean("sfx", enabled);
		prefs.flush(); // save that shit
	}

	public static boolean getSfx() {
		return prefs.getBoolean("sfx");
	}

	public static void setHighScore(int highscore) {
		prefs.putInteger("highScore", highscore);
		prefs.flush(); // save that shit
	}

	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}

	public static void dispose() {
		// clean up
		texture.dispose();
		bombTexture.dispose();

		correct.dispose();
		boom.dispose();
		intro.dispose();
		loop.dispose();

		rrIntro.dispose();
		rr.dispose();
		rrOut.dispose();
		rrSuper.dispose();

		rr.dispose();
		rrEndLoop.dispose();

		font.dispose();
	}
}
