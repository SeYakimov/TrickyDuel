package com.airse.trickyduel;

import com.airse.trickyduel.states.GameStateManager;
import com.airse.trickyduel.states.PlayState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Duel extends ApplicationAdapter {
	public static final int WIDTH = 320;
	public static final int HEIGHT = 480;
	public static final int PLAYER_WIDTH = (int)(0.1f * WIDTH);
	public static final int PLAYER_HEIGHT = PLAYER_WIDTH;

    public static final String PURPLE = "651fff";
    public static final String LIME400 = "c6ff00";
    public static final String CYAN400 = "00e5ff";
    public static final String CYAN200 = "18ffff";
    public static final String CYAN100 = "84ffff";
    public static final String ORANGE = "ff6e40";
    public static final String RED400 = "ff1744";
    public static final String RED200 = "ff5252";
    public static final String RED100 = "ff8a80";
    public static final String GREEN400 = "00e676";
    public static final String TOP_COLOR = RED400;
    public static final String BOTTOM_COLOR = CYAN400;

	public static final String TITLE = "Tricky Duel";

	private GameStateManager gsm;
	private SpriteBatch sb;
	
	@Override
	public void create () {
        sb = new SpriteBatch();
        gsm = new GameStateManager();
        Gdx.gl.glClearColor(0.35f, 0, 1, 1);
        gsm.push(new PlayState(gsm));
    }
	@Override
	public void resize(int width, int height){
		gsm.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(sb);
	}
	
	@Override
	public void dispose () {
		sb.dispose();
	}
}
