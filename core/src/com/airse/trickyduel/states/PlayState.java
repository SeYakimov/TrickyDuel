package com.airse.trickyduel.states;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.airse.trickyduel.models.Border;
import com.airse.trickyduel.models.Bullet;
import com.airse.trickyduel.models.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PlayState extends State implements InputProcessor {

    private Border border;
    private Player playerBottom, playerTop;
    private Array<Bullet> bullets;
    private ShapeRenderer shape;
    private BitmapFont font;
    private String s;

    private boolean UpKeyDown;
    private boolean DownKeyDown;
    private boolean LeftKeyDown;
    private boolean RightKeyDown;
    private boolean WKeyDown;
    private boolean SKeyDown;
    private boolean AKeyDown;
    private boolean DKeyDown;

    private boolean topWon;
    private boolean bottomWon;
    private int winner;

    private Vector2 lastTouch;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        Gdx.input.setInputProcessor(this);

        border = new Border(Difficulty.NORMAL);
        playerTop = new Player(new Vector2((int)(0.5f * Duel.WIDTH) - Player.PLAYER_WIDTH / 2, (int)(0.8f * Duel.HEIGHT) - Player.PLAYER_HEIGHT / 2), true);
        playerBottom = new Player(new Vector2((int)(0.5f * Duel.WIDTH) - Player.PLAYER_WIDTH / 2, (int)(0.2f * Duel.HEIGHT) - Player.PLAYER_HEIGHT / 2), false);
        shape = new ShapeRenderer();
        font = new BitmapFont();

        UpKeyDown = false;
        DownKeyDown = false;
        LeftKeyDown = false;
        RightKeyDown = false;
        WKeyDown = false;
        SKeyDown = false;
        AKeyDown = false;
        DKeyDown = false;

        topWon = false;
        bottomWon = false;

        winner = 0;

        bullets = new Array<Bullet>();

    }

    @Override
    protected void handleInput() {

    }



    @Override
    public void update(float dt) {
        if (UpKeyDown) playerBottom.moveUp();
        if (DownKeyDown) playerBottom.moveDown();
        if (LeftKeyDown) playerBottom.moveLeft();
        if (RightKeyDown) playerBottom.moveRight();
        if (WKeyDown) playerTop.moveUp();
        if (SKeyDown) playerTop.moveDown();
        if (AKeyDown) playerTop.moveLeft();
        if (DKeyDown) playerTop.moveRight();

        playerTop.update(border);
        playerBottom.update(border);

        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isTop()) {
                if (bullet.isCollides(playerBottom)){
                    border.moveDown();
                    bullets.removeValue(bullet, true);
                }
            }
            else {
                if (bullet.isCollides(playerTop)){
                    border.moveUp();
                    bullets.removeValue(bullet, true);
                }
            }
        }
        if (border.isGameOver() != winner) {
            winner = border.isGameOver();
            if (winner == 1) topWon = true;
            else if (winner == -1) bottomWon = true;
        }



//        camera.setToOrtho(false, Duel.WIDTH, Duel.HEIGHT);
//        camera.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        border.render();
        playerTop.render();
        playerBottom.render();
        for (Bullet bullet : bullets) {
            bullet.render(border);
        }
        if (winner != 0){
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1, 1, 1, 0.1f);
            shape.rect(0, 0, Duel.WIDTH, Duel.HEIGHT);
            shape.end();
            sb.begin();
            if (topWon) s = "Orange won";
            else if (bottomWon) s = "Cyan won";
            font.draw(sb, s, 100, Duel.HEIGHT / 2);
            sb.end();
        }
    }

    @Override
    public void dispose() {
        border.dispose();
        playerTop.dispose();
        playerBottom.dispose();
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.UP:
                if (!UpKeyDown) UpKeyDown = true;
                //playerBottom.moveUp();
                break;
            case Input.Keys.DOWN:
                if (!DownKeyDown) DownKeyDown = true;
                //playerBottom.moveDown();
                break;
            case Input.Keys.LEFT:
                if (!LeftKeyDown) LeftKeyDown = true;
                //playerBottom.moveLeft();
                break;
            case Input.Keys.RIGHT:
                if (!RightKeyDown) RightKeyDown = true;
                //playerBottom.moveRight();
                break;
            case Input.Keys.W:
                if (!WKeyDown) WKeyDown = true;
                break;
            case Input.Keys.S:
                if (!SKeyDown) SKeyDown = true;
                break;
            case Input.Keys.A:
                if (!AKeyDown) AKeyDown = true;
                break;
            case Input.Keys.D:
                if (!DKeyDown) DKeyDown = true;
                break;
            // Bottom player shoots
            case Input.Keys.SPACE:
                bullets.add(new Bullet(playerBottom.getPosition().cpy().add(playerBottom.PLAYER_WIDTH / 2, playerBottom.PLAYER_HEIGHT), false));
                break;
            // Top player shoots
            case Input.Keys.P:
                bullets.add(new Bullet(playerTop.getPosition().cpy().add(playerTop.PLAYER_WIDTH / 2, playerTop.PLAYER_HEIGHT), true));
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.UP:
                UpKeyDown = false;
                    break;
            case Input.Keys.DOWN:
                DownKeyDown = false;
                break;
            case Input.Keys.LEFT:
                LeftKeyDown = false;
                break;
            case Input.Keys.RIGHT:
                RightKeyDown = false;
                break;
            case Input.Keys.W:
                WKeyDown = false;
                break;
            case Input.Keys.S:
                SKeyDown = false;
                break;
            case Input.Keys.A:
                AKeyDown = false;
                break;
            case Input.Keys.D:
                DKeyDown = false;
                break;
            default:
                break;
        }
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
