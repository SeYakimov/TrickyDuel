package com.airse.trickyduel.states;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.models.Border;
import com.airse.trickyduel.models.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qwert on 18.06.2017.
 */

public class PlayState extends State implements InputProcessor {

    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }
    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

    private Border border;
    private Player playerTop, playerBottom;

    private Vector2 lastTouch;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        border = new Border(Difficulty.NORMAL);
        playerTop = new Player(160 - Player.PLAYER_WIDTH / 2, 400 - Player.PLAYER_HEIGHT / 2, true);
        playerBottom = new Player(160 - Player.PLAYER_WIDTH / 2, 80 - Player.PLAYER_HEIGHT / 2, false);

        lastTouch = new Vector2();

        Gdx.input.setInputProcessor(this);
        for(int i = 0; i < 4; i++) {
            touches.put(i, new TouchInfo());
        }
    }

    @Override
    protected void handleInput() {

    }



    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        border.render();
        playerTop.render();
        playerBottom.render();
    }

    @Override
    public void dispose() {
        border.dispose();
        playerTop.dispose();
        playerBottom.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 newTouch = new Vector2(screenX, screenY);

        Vector2 delta = newTouch.cpy().sub(lastTouch);

        lastTouch = newTouch;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
