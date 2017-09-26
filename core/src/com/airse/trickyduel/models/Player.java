package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private Vector2 size;
    private Vector2 sizeOrigin;
    private int speed;
    private float movement;

    private SpeedState speedState;
    private SizeState sizeState;
    private BulletsState bulletsState;

    private boolean isTop;
    private Rectangle bounds;

    private ShapeRenderer shape;

    private boolean speedStateChanged;
    private boolean sizeStateChanged;
    private boolean bulletsStateChanged;

    private enum SpeedState {DEFAULT, FREEZE, INCREASE, DECREASE}
    private enum SizeState {DEFAULT, LARGE}
    private enum BulletsState {DEFAULT, UNLIMITED}

    private Thread freeze;
    private Thread grow;
    private Thread speedBoost;
    private Thread reduceSpeed;

    public Player(OrthographicCamera camera, int width, int height, boolean isTop) {
        this.isTop = isTop;
        Vector2 position;
        if (isTop){
            position = new Vector2((int)(camera.viewportWidth / 2 - width / 2),
                    (int)(camera.viewportHeight * 11 / 12 - height / 2));
        }
        else{
            position = new Vector2((int)(camera.viewportWidth / 2 - width / 2),
                    (int)(camera.viewportHeight / 12 - height / 2));
        }

        size = new Vector2(width, height);
        sizeOrigin = size.cpy();
        speed = width / 8;
        shape = new ShapeRenderer();
        bounds = new Rectangle();
        bounds.setPosition(position);
        bounds.setSize(width, height);
        movement = 1;

        speedState = SpeedState.DEFAULT;
        speedStateChanged = false;
        sizeState = SizeState.DEFAULT;
        sizeStateChanged = false;
        bulletsState = BulletsState.DEFAULT;
        bulletsStateChanged = false;

        freeze = new Thread();
        grow = new Thread();
        speedBoost = new Thread();
        reduceSpeed = new Thread();
    }
    Vector2 getPosition() {
        return new Vector2(bounds.x, bounds.y);
    }
    Vector2 getCenter() {
        return new Vector2(bounds.x + sizeOrigin.x / 2, bounds.y + sizeOrigin.y / 2);
    }

    public void moveLeft()
    {
        bounds.x -= speed * movement;
    }
    public void moveRight()
    {
        bounds.x += speed * movement;
    }
    public void moveUp()
    {
        bounds.y += speed * movement;
    }
    public void moveDown()
    {
        bounds.y -= speed * movement;

    }
    public void move(float x, float y){
        bounds.x += (x / 8) * movement;
        bounds.y += (y / 8) * movement;
    }
    public void update(Border border, OrthographicCamera camera){
        if (speedStateChanged){
            switch (speedState){
                case FREEZE:
                    movement = 0;
                    break;
                case INCREASE:
                    movement = 2;
                    break;
                case DECREASE:
                    movement = 0.5f;
                    break;
                default:
                    movement = 1;
                    break;
            }
            speedStateChanged = false;
        }
        if (sizeStateChanged){
            switch (sizeState){
                case LARGE:
                    size = sizeOrigin.cpy().scl(2, 1);
                    bounds.setHeight(size.y);
                    bounds.setWidth(size.x);
                    break;
                default:
                    size = sizeOrigin.cpy();
                    bounds.setHeight(sizeOrigin.y);
                    bounds.setWidth(sizeOrigin.x);
                    break;
            }
            sizeStateChanged = false;
        }
        if (bulletsStateChanged){
            switch (bulletsState){
                case UNLIMITED:

                    break;
                default:

                    break;
            }
            bulletsStateChanged = false;
        }

        if (isTop){
            if (bounds.y < border.getPosition().y + border.getBorderHeight()) bounds.y = border.getPosition().y + border.getBorderHeight();
            if (bounds.y > camera.position.y + camera.viewportHeight / 2 - size.y)
                bounds.y = camera.position.y + camera.viewportHeight / 2 - size.y;
        }
        else{
            if (bounds.y > border.getPosition().y - size.y) bounds.y = border.getPosition().y - size.y;
            if (bounds.y < camera.position.y - camera.viewportHeight / 2) bounds.y = camera.position.y - camera.viewportHeight / 2;
        }
        if (bounds.x > camera.position.x + camera.viewportWidth / 2 - size.x)
            bounds.x = camera.position.x + camera.viewportWidth / 2 - size.x;
        if (bounds.x < camera.position.x - camera.viewportWidth / 2) bounds.x = camera.position.x - camera.viewportWidth / 2;
    }

    public void render(OrthographicCamera camera){
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(Color.WHITE);
        shape.rect(bounds.x, bounds.y, size.x, size.y);

        switch (speedState){
            case FREEZE:
                shape.setColor(Color.BLACK);
                break;
            case INCREASE:
                shape.setColor(Color.valueOf(Duel.LIME400));
                break;
            case DECREASE:
                shape.setColor(Color.valueOf(Duel.PURPLE));
                break;
            default:
                if (isTop) {
                    shape.setColor(Color.valueOf(Duel.TOP_COLOR));
                }
                else {
                    shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
                }
                break;
        }
        shape.rect(bounds.x + sizeOrigin.x / 8, bounds.y + sizeOrigin.y / 8, size.x - sizeOrigin.x * 2 / 8, size.y - sizeOrigin.y * 2 / 8);

        shape.setColor(Color.WHITE);
        shape.rect(bounds.x + sizeOrigin.x / 3, bounds.y + sizeOrigin.y / 3, size.x - sizeOrigin.x * 2 / 3, size.y - sizeOrigin.y * 2 / 3);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getSize() {
        return size;
    }

    public void freeze(){
        if (freeze.isAlive()){
            freeze.interrupt();
        }
        freeze = new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                speedState = SpeedState.FREEZE;
                speedStateChanged = true;
                while (System.currentTimeMillis() < time + 2000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        speedState = SpeedState.DEFAULT;
                        speedStateChanged = true;
                    }
                });
            }
        });
        freeze.start();
    }

    public void grow(){
        if (grow.isAlive()){
            grow.interrupt();
        }
        grow = new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                sizeState = SizeState.LARGE;
                sizeStateChanged = true;
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        sizeState = SizeState.DEFAULT;
                        sizeStateChanged = true;
                    }
                });
            }
        });
        grow.start();
    }

    public void speedBoost(){
        if (speedBoost.isAlive()){
            speedBoost.interrupt();
        }
        speedBoost = new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                speedState = SpeedState.INCREASE;
                speedStateChanged = true;
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        speedState = SpeedState.DEFAULT;
                        speedStateChanged = true;
                    }
                });
            }
        });
        speedBoost.start();
    }

    public void reduceSpeed(){
        if (reduceSpeed.isAlive()){
            reduceSpeed.interrupt();
        }
        reduceSpeed = new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                speedState = SpeedState.DECREASE;
                speedStateChanged = true;
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        speedState = SpeedState.DEFAULT;
                        speedStateChanged = true;
                    }
                });
            }
        });
        reduceSpeed.start();
    }

    private void drawDefaultSpeed(){

    }
    private void drawDefaultSize(){

    }
}
