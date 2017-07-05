package com.airse.trickyduel.states;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.airse.trickyduel.PerkType;
import com.airse.trickyduel.State;
import com.airse.trickyduel.models.Border;
import com.airse.trickyduel.models.BulletManager;
import com.airse.trickyduel.models.Perk;
import com.airse.trickyduel.models.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayState extends com.airse.trickyduel.states.State implements InputProcessor {
    private class TouchInfo {
        Vector2 pos = new Vector2();
        boolean touched = false;
        State state = State.NONE;
    }
    private boolean bottomLeftTouched;
    private boolean topRightTouched;
    private Vector2 topStickOrigin;
    private Vector2 bottomStickOrigin;

    private Map<Integer,TouchInfo> touches;

    private int playerSize;
    private int i, w, h, maxTouches;
    private float x, y;
    private Random rand;

    private Border border;
    private Player playerBottom, playerTop;

    private Array<Perk> topPerks, bottomPerks;

    private BulletManager bulletManager;

    private ShapeRenderer shape;
    private BitmapFont font;
    private String s;
    private String message;

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

    private boolean topCanShoot;
    private boolean bottomCanShoot;
    public PlayState(GameStateManager gsm) {
        super(gsm);

        Gdx.input.setInputProcessor(this);

        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        camera.setToOrtho(false, w, h);
//        camera.setToOrtho(false, Duel.WIDTH, Duel.HEIGHT);

        camera.update();
        playerSize = (int)(camera.viewportHeight / 16);

        rand = new Random();

        border = new Border(Difficulty.NORMAL, camera, playerSize);
        playerTop = new Player(new Vector2((int)(camera.position.x - playerSize / 2),
                (int)(camera.position.y + camera.viewportHeight * 0.25f + playerSize / 2)),
                playerSize, playerSize, true);
        playerBottom = new Player(new Vector2((int)(camera.position.x - playerSize / 2),
                (int)(camera.position.y - camera.viewportHeight * 0.25f - 3 * playerSize / 2)),
                playerSize, playerSize, false);
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

        bulletManager = new BulletManager(playerSize, 30);

        topPerks = new Array<Perk>();
        bottomPerks = new Array<Perk>();
        maxTouches = 4;

        touches = new HashMap<Integer,TouchInfo>();
        for(int i = 0; i < 10; i++){
            touches.put(i, new TouchInfo());
        }
        topRightTouched = false;
        bottomLeftTouched = false;

        bottomCanShoot = true;
        topCanShoot = true;
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = h;
        camera.viewportWidth = camera.viewportHeight * width / height;
        camera.update();

        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        System.out.println("w: " + w);
        System.out.println("h: " + h);
    }

    @Override
    public void update(float dt) {

        for (int i = 0; i < touches.size(); i++){
            TouchInfo info = touches.get(i);
            float dx;
            float dy;
            switch (info.state){
                case TOP_STICK:
                    if (info.pos.dst(topStickOrigin) > playerSize){
                        info.pos = topStickOrigin.cpy().mulAdd(info.pos.cpy().sub(topStickOrigin), playerSize / (info.pos.dst(topStickOrigin)));
                    }
                    dx = info.pos.x - topStickOrigin.x;
                    dy = info.pos.y - topStickOrigin.y;
                    playerTop.move(dx, dy);
                    break;
                case BOTTOM_STICK:

                    if (info.pos.dst(bottomStickOrigin) > playerSize){
                        info.pos = bottomStickOrigin.cpy().mulAdd(info.pos.cpy().sub(bottomStickOrigin), playerSize / (info.pos.dst(bottomStickOrigin)));
                    }
                    dx = info.pos.x - bottomStickOrigin.x;
                    dy = info.pos.y - bottomStickOrigin.y;
                    playerBottom.move(dx, dy);
                    break;
                case TOP_SHOOT:
                    if (topCanShoot){
                        bulletManager.addTopBullet(playerTop, playerBottom);
                        topCanShoot = false;
                    }
                    break;
                case BOTTOM_SHOOT:
                    if (bottomCanShoot){
                        bulletManager.addBottomBullet(playerTop, playerBottom);
                        bottomCanShoot = false;
                    }
                    break;
                default:
                    break;
            }
        }

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

        bulletManager.update(camera, playerTop, playerBottom, border);

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
        bulletManager.render(camera, border);
        for (Perk perk : bottomPerks) {
            perk.render(camera, sb);
        }
        if (topWon || bottomWon){
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1, 1, 1, 0.1f);
            shape.rect(0, 0, w, h);
            shape.end();
            sb.begin();
            if (topWon) s = "Red won!";
            else if (bottomWon) s = "Cyan won!";
            font.draw(sb, s, 100, Duel.HEIGHT / 2);
            sb.end();
        }
        sb.begin();

        message = "";
        for(int i = 0; i < maxTouches; i++){
            if(touches.get(i).touched){
                message += "Finger: " + Integer.toString(i) + " touch at: " +
                        Float.toString(touches.get(i).pos.x) +
                        ", " +
                        Float.toString(touches.get(i).pos.y) +
                        "\n";
                x = touches.get(i).pos.x;
                y = touches.get(i).pos.y;

            }
            font.draw(sb, message, x, y);
        }
        sb.end();
        if (topStickOrigin != null) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.circle(topStickOrigin.x, topStickOrigin.y, playerSize);
            shape.end();
        }
        if (bottomStickOrigin != null) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.circle(bottomStickOrigin.x, bottomStickOrigin.y, playerSize);
            shape.end();
        }
    }

    @Override
    public void dispose() {
        border.dispose();
        playerTop.dispose();
        playerBottom.dispose();
        bulletManager.dispose();
        for (Perk perk : bottomPerks) {
            perk.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.UP:
                if (!UpKeyDown) UpKeyDown = true;
                break;
            case Input.Keys.DOWN:
                if (!DownKeyDown) DownKeyDown = true;
                break;
            case Input.Keys.LEFT:
                if (!LeftKeyDown) LeftKeyDown = true;
                break;
            case Input.Keys.RIGHT:
                if (!RightKeyDown) RightKeyDown = true;
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
                bulletManager.addBottomBullet(playerTop, playerBottom);
                break;
            // Top player shoots
            case Input.Keys.SPACE:
                bulletManager.addTopBullet(playerTop, playerBottom);
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < maxTouches){
            x = screenX * camera.viewportWidth / w;
            y = (h - screenY) * camera.viewportHeight / h;
            touches.get(pointer).pos = new Vector2(x, y);
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < maxTouches){
            TouchInfo info = touches.get(pointer);
            x = screenX * camera.viewportWidth / w;
            y = (h - screenY) * camera.viewportHeight / h;
            info.pos = new Vector2(x, y);
            info.touched = true;
            if (y < border.getPosition().y){
                if (bottomLeftTouched){
                    info.state = State.BOTTOM_SHOOT;
                }
                else{
                    if (x < camera.position.x){
                        bottomLeftTouched = true;
                        info.state = State.BOTTOM_STICK;
                        bottomStickOrigin = new Vector2(x, y);
                    }
                    else {
                        info.state = State.BOTTOM_SHOOT;
                    }
                }
            }
            else{
                if (topRightTouched){
                    info.state = State.TOP_SHOOT;
                }
                else{
                    if (x > camera.position.x){
                        topRightTouched = true;
                        info.state = State.TOP_STICK;
                        topStickOrigin = new Vector2(x, y);
                    }
                    else {
                        info.state = State.TOP_SHOOT;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(pointer < maxTouches){
            TouchInfo info = touches.get(pointer);
            info.pos = new Vector2(0, 0);
            info.touched = false;
            if (info.state == State.BOTTOM_STICK) {
                bottomLeftTouched = false;
                bottomStickOrigin = null;
            }
            else if (info.state == State.TOP_STICK) {
                topRightTouched = false;
                topStickOrigin = null;
            }
            else if (info.state == State.BOTTOM_SHOOT){
                bottomCanShoot = true;
            }
            else if (info.state == State.TOP_SHOOT){
                topCanShoot = true;
            }
            info.state = State.NONE;


        }
        return true;
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
