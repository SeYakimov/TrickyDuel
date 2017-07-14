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

    private boolean isTop;
    private Rectangle bounds;

    private ShapeRenderer shape;

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
    }
    public Vector2 getPosition() {
        return new Vector2(bounds.x, bounds.y);
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
        if (isTop) {
            shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        }
        else {
            shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        }

        shape.rect(bounds.x, bounds.y, size.x, size.y);

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                movement = 0;
                while (System.currentTimeMillis() < time + 2000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        movement = 1;
                    }
                });
            }
        }).start();
    }

    public void grow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                final Vector2 temp = sizeOrigin.cpy();
                size.scl(2, 1);
                bounds.setHeight(size.y);
                bounds.setWidth(size.x);
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        size = temp.cpy();
                        bounds.setHeight(sizeOrigin.y);
                        bounds.setWidth(sizeOrigin.x);
                    }
                });
            }
        }).start();
    }

    public void speedBoost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                movement = 2;
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        movement = 1;
                    }
                });
            }
        }).start();
    }

    public void reduceSpeed(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                movement = 0.5f;
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        movement = 1;
                    }
                });
            }
        }).start();
    }
}
