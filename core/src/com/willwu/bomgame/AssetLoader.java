package com.willwu.bomgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Sound correct, boom;
	public static Music intro, loop;
	public static Texture texture; // initial bomb texture
	public static TextureRegion bombTexture1, bombTexture2, bombTexture3; // bomb's split texture regions
	
	public static void load(){
		correct = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
		boom = Gdx.audio.newSound(Gdx.files.internal("boom.wav"));
		intro = Gdx.audio.newMusic(Gdx.files.internal("intro.ogg"));
		loop = Gdx.audio.newMusic(Gdx.files.internal("loop.ogg"));
		
		
		texture = new Texture(Gdx.files.internal("bomb.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
}
