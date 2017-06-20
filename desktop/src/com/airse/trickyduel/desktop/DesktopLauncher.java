package com.airse.trickyduel.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.airse.trickyduel.Duel;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Duel.HEIGHT + 100;
		config.width = Duel.WIDTH;
		config.title = Duel.TITLE;
		new LwjglApplication(new Duel(), config);
	}
}
