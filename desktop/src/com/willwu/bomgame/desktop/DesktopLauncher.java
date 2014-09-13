package com.willwu.bomgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.willwu.bomgame.BomGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "BomGame";
		config.width = 1080 / 3;
		config.height = 1920 / 3;
		
		
		
		new LwjglApplication(new BomGame(), config);
	}
}
