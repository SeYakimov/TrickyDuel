package com.airse.trickyduel.states;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.airse.trickyduel.PerkType;
import com.airse.trickyduel.models.Border;
import com.airse.trickyduel.models.Bullet;
import com.airse.trickyduel.models.Perk;
import com.airse.trickyduel.models.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class PlayState extends State implements InputProcessor {
    private int playerSize;
    private int i;
    private Random rand;

    private Border border;
    private Player playerBottom, playerTop;
    private Array<Bullet> topBullets, bottomBullets;
    private Array<Perk> topPerks, bottomPerks;
    private ShapeRenderer shape;
    private BitmapFont font;
    private String s;

    private Perk perk1;

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

    private Vector2 lastTouch;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        Gdx.input.setInputProcessor(this);

//        camera.setToOrtho(false, 320, 480);
        camera.setToOrtho(false, Duel.WIDTH, Duel.HEIGHT);

        camera.update();
        playerSize = (int)(camera.viewportWidth / 10);

        rand = new Random();

        border = new Border(Difficulty.NORMAL, camera);
        playerTop = new Player(new Vector2((int)(camera.position.x - playerSize / 2),
                (int)(camera.position.y + camera.viewportHeight * 0.25f + playerSize / 2)),
                playerSize, playerSize, true);
        playerBottom = new Player(new Vector2((int)(camera.position.x - playerSize / 2),
                (int)(camera.position.y - camera.viewportHeight * 0.25f - 3 * playerSize / 2)),
                playerSize, playerSize, false);
        shape = new ShapeRenderer();
        font = new BitmapFont();
        lastTouch = new Vector2();

//        perk1 = new Perk(new Texture("red_bullet.png"), camera, PerkType.YOU_BULLETS, false, playerSize);

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

        topBullets = new Array<Bullet>();
        bottomBullets = new Array<Bullet>();

        topPerks = new Array<Perk>();
        bottomPerks = new Array<Perk>();

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 320;
        camera.viewportHeight = camera.viewportWidth * height / width;
        camera.update();
        System.out.println("x: " + camera.position.x);
        System.out.println("y: " + camera.position.y);
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

        playerTop.update(border, camera);
        playerBottom.update(border, camera);

        for (Bullet top : topBullets) {
            for (Bullet bottom : bottomBullets) {
                if (top.isCollides(bottom.getBounds()))
                {
                    topBullets.removeValue(top, true);
                    bottomBullets.removeValue(bottom, true);
                }
            }
        }

        for (Bullet top : topBullets) {
            top.update();
            if (top.isCollides(playerBottom.getBounds())){
                border.moveDown();
                topBullets.removeValue(top, true);
            }
            if (top.getPosition().y < camera.position.y - camera.viewportHeight / 2 - top.RADIUS){
                topBullets.removeValue(top, true);
            }
        }
        for (Bullet bottom : bottomBullets) {
            bottom.update();
            if (bottom.isCollides(playerTop.getBounds())){
                border.moveUp();
                bottomBullets.removeValue(bottom, true);
            }
            if (bottom.getPosition().y > camera.position.y + camera.viewportHeight / 2){
                bottomBullets.removeValue(bottom, true);
            }
        }
        switch(border.isGameOver(camera)){
            case -1:
                bottomWon = true;
                break;
            case 1:
                topWon = true;
                break;
            default:
                break;
        }

        camera.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        shape.setProjectionMatrix(camera.combined);
        sb.setProjectionMatrix(camera.combined);
        border.render(camera);
        playerTop.render(camera);
        playerBottom.render(camera);
//        perk1.render(camera, sb);
        for (Bullet top : topBullets) {
            top.render(border, camera);
        }
        for (Bullet bottom : bottomBullets) {
            bottom.render(border, camera);
        }
        for (Perk perk : bottomPerks) {
            perk.render(camera, sb);
        }
        if (topWon || bottomWon){
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1, 1, 1, 0.1f);
            shape.rect(0, 0, Duel.WIDTH, Duel.HEIGHT);
            shape.end();
            sb.begin();
            if (topWon) s = "Orange won!";
            else if (bottomWon) s = "Cyan won!";
            font.draw(sb, s, 100, Duel.HEIGHT / 2);
            sb.end();
        }

    }

    @Override
    public void dispose() {
        border.dispose();
        playerTop.dispose();
        playerBottom.dispose();
        for (Bullet top : topBullets) {
            top.dispose();
        }
        for (Bullet bottom : bottomBullets) {
            bottom.dispose();
        }
        for (Perk perk : bottomPerks) {
            perk.dispose();
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
            case Input.Keys.P:
                bottomBullets.add(new Bullet(playerBottom.getPosition().cpy().add(playerBottom.getSize().x / 2, playerBottom.getSize().y), false));
                break;
            // Top player shoots
            case Input.Keys.SPACE:
                topBullets.add(new Bullet(playerTop.getPosition().cpy().add(playerTop.getSize().x / 2, 0), true));
                break;
            case Input.Keys.O:
                bottomPerks.add(new Perk(new Texture("red_bullet.png"), camera, PerkType.YOU_BULLETS, false, playerSize, border));
                break;
            case Input.Keys.I:
                bottomPerks.add(new Perk(new Texture("red_bullet.png"), camera, PerkType.YOU_BULLETS, true, playerSize, border));
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
