package com.airse.trickyduel.states;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.airse.trickyduel.State;
import com.airse.trickyduel.models.Border;
import com.airse.trickyduel.models.BulletManager;
import com.airse.trickyduel.models.PerkManager;
import com.airse.trickyduel.models.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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


    private BulletManager bulletManager;
    private PerkManager perkManager;

    private ShapeRenderer shape;
    private BitmapFont winner;
    private GlyphLayout glyphLayout;
    private String s;
    private Matrix4 mx4Font, oldMatrix;

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
        playerSize = (int)(camera.viewportHeight / 15);

        rand = new Random();

        border = new Border(Difficulty.NORMAL, camera, playerSize);
        playerTop = new Player(camera, playerSize, playerSize, true);
        playerBottom = new Player(camera, playerSize, playerSize, false);

        shape = new ShapeRenderer();
//        winner = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("Britannic.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int)(playerSize);
        fontGenerator.generateData(freeTypeFontParameter);
        winner = fontGenerator.generateFont(freeTypeFontParameter);
        glyphLayout = new GlyphLayout();
        mx4Font = new Matrix4();
        oldMatrix = new Matrix4();

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

        bulletManager = new BulletManager(playerSize, 20);
        perkManager = new PerkManager(playerSize);

        maxTouches = 10;
        touches = new HashMap<Integer,TouchInfo>();
        for(int i = 0; i < 10; i++){
            touches.put(i, new TouchInfo());
        }
        topRightTouched = false;
        bottomLeftTouched = false;

        bottomCanShoot = true;
        topCanShoot = true;
//        Gdx.gl.glLineWidth(playerSize / 2);
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
        mx4Font.setToRotation(new Vector3(camera.position.x, camera.position.y, 0), 180);
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
            case 1:
                bottomWon = true;
                break;
            case -1:
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
        border.render(camera, bulletManager);
        playerTop.render(camera);
        playerBottom.render(camera);
        bulletManager.render(camera, border);
        if (topWon || bottomWon){

            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (bottomWon){
                shape.setColor(Color.valueOf(Duel.CYAN100));
                shape.rect(0, 0, w, h);
                shape.end();
                playerBottom.render(camera);
            }
            else {
                shape.setColor(Color.valueOf(Duel.RED100));
                shape.rect(0, 0, w, h);
                shape.end();
                playerTop.render(camera);
            }

            if (topWon){
                printText(sb, camera.position.x, camera.position.y, 180, "Red won!", Color.valueOf(Duel.RED400));
            }
            else {
                printText(sb, camera.position.x, camera.position.y, 0, "Cyan won!", Color.valueOf(Duel.CYAN400));
            }

            if (Gdx.input.justTouched()){
                gsm.push(new PlayState(gsm));
            }
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (topStickOrigin != null) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1, 1, 1, 0.5f);
            shape.circle(topStickOrigin.x, topStickOrigin.y, playerSize);
            shape.end();
        }
        if (bottomStickOrigin != null) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1, 1, 1, 0.5f);
            shape.circle(bottomStickOrigin.x, bottomStickOrigin.y, playerSize);
            shape.end();
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
        for(int i = 0; i < maxTouches; i++){
            if(touches.get(i).state == State.BOTTOM_STICK){
                x = touches.get(i).pos.x;
                y = touches.get(i).pos.y;
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(Color.WHITE);
                shape.circle(x, y, playerSize / 2);
                shape.setColor(Color.valueOf(Duel.CYAN400));
                shape.circle(x, y, playerSize / 4);
                shape.end();

            }
            else if(touches.get(i).state == State.TOP_STICK){
                x = touches.get(i).pos.x;
                y = touches.get(i).pos.y;
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(Color.WHITE);
                shape.circle(x, y, playerSize / 2);
                shape.setColor(Color.valueOf(Duel.RED400));
                shape.circle(x, y, playerSize / 4);
                shape.end();
            }
        }
    }
    public void printText(SpriteBatch sb, float posX, float posY, float angle, String text, Color color)
    {
        Matrix4 oldTransformMatrix = sb.getTransformMatrix().cpy();

        Matrix4 mx4Font = new Matrix4();
        mx4Font.rotate(new Vector3(0, 0, 1), angle);
        mx4Font.trn(posX, posY, 0);
        sb.setTransformMatrix(mx4Font);

        sb.begin();
        s = text;
        winner.setColor(color);
        glyphLayout.setText(winner,s);
        float winnerTextWidth = glyphLayout.width;
        float winnerTextHeight = glyphLayout.height;
        winner.draw(sb, glyphLayout, - (winnerTextWidth / 2), winnerTextHeight / 2);
        sb.end();

        sb.setTransformMatrix(oldTransformMatrix);
    }
    @Override
    public void dispose() {
        border.dispose();
        playerTop.dispose();
        playerBottom.dispose();
        bulletManager.dispose();
        perkManager.dispose();
        shape.dispose();
        winner.dispose();

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
                // TODO Show top perk
                break;
            case Input.Keys.I:
                // TODO Show bottom perk
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
