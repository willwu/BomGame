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
	public static Music intro, loop, rrIntro, rrSuper, rrOut, rr, rrEndLoop;
	public static Texture texture; // initial bomb texture
	public static TextureRegion bombTexture1, bombTexture2, bombTexture3; // bomb's split texture regions

	public static BitmapFont font;

	private static Preferences prefs;

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

		// load font
//		font = new BitmapFont(Gdx.files.internal("whitetext.fnt"));
//		font.setScale(.15f, -.15f);
		font = new BitmapFont(Gdx.files.internal("font.txt"));
		font.setScale(.09f, -.1f);

		// setup preferences
		prefs = Gdx.app.getPreferences("BomGame");

		// create high score
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
		
		bombTexture1 = new TextureRegion(texture, 0, 0, 50, 50);
		bombTexture1.flip(false, true);
		bombTexture2 = new TextureRegion(texture, 50, 0, 50, 50);
		bombTexture2.flip(false, true);
		bombTexture3 = new TextureRegion(texture, 100, 0, 50, 50);
		bombTexture3.flip(false, true);
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

		correct.dispose();
		boom.dispose();
		intro.dispose();
		loop.dispose();
		
		rrIntro.dispose();
		rr.dispose();
		rrOut.dispose();
		rrSuper.dispose();

		font.dispose();
	}
}
