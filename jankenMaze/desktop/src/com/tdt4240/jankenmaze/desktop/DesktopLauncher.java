package com.tdt4240.jankenmaze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tdt4240.jankenmaze.JankenMaze;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		DesktopPlayServiceLauncher playServiceLauncher = new DesktopPlayServiceLauncher();
		//new LwjglApplication(new JankenMaze(), config);
		new LwjglApplication(new JankenMaze(playServiceLauncher), config);
	}
}
