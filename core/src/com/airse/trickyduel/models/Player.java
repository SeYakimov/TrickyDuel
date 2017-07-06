package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
//    public static final int PLAYER_WIDTH = (int)(0.1f * Duel.WIDTH);
//    public static final int PLAYER_HEIGHT = PLAYER_WIDTH;
//    public static final int MOVEMENT = (int)(0.02f * Duel.WIDTH);

    private Vector2 size;
    private int speed;


    private boolean isTop;
    private Vector2 position;
    private Rectangle bounds;
    private int bulletsNum;
    private int bulletsLimit;

    private ShapeRenderer shape;

    public Player(OrthographicCamera camera, int width, int height, boolean isTop) {
        this.isTop = isTop;
        if (isTop){
            position = new Vector2((int)(camera.viewportWidth / 2 - width / 2),
                    (int)(camera.viewportHeight * 11 / 12 - height / 2));
        }
        else{
            position = new Vector2((int)(camera.viewportWidth / 2 - width / 2),
                    (int)(camera.viewportHeight / 12 - height / 2));
        }

        size = new Vector2(width, height);
        speed = (int)(width / 8);
//        this.position = position;
        shape = new ShapeRenderer();
        bounds = new Rectangle();
        bounds.setPosition(position);
        bounds.setSize(width, height);
        bulletsNum = 30;
        bulletsLimit = 30;
    }

//    public Vector2 getPosition() {
//        return position;
//    }
    public Vector2 getPosition() {
        return new Vector2(bounds.x, bounds.y);
    }
//    public void setPosition(Vector2 position) {
//        this.position = position;
//    }

    public void moveLeft()
    {
        bounds.x -= speed;
//        if (position.x < 0) position.x = 0;
    }
    public void moveRight()
    {
        bounds.x += speed;
//        if (position.x > Duel.WIDTH - PLAYER_WIDTH) position.x = Duel.WIDTH - PLAYER_WIDTH;
    }
    public void moveUp()
    {
        bounds.y += speed;
    }
    public void moveDown()
    {
        bounds.y -= speed;

    }
    public void move(float x, float y){
        bounds.x += (x / 8);
        bounds.y += (y / 8);
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

//        bounds.setPosition(bounds);
//        System.out.print(isTop? "Top:" : "Bottom:");
//        System.out.println(position.y);
//        System.out.println("cam0:" + (camera.position.y - camera.viewportHeight / 2));
//        System.out.println("camCenter:" + (camera.position.y));

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
        shape.rect(bounds.x + size.x / 3, bounds.y + size.y / 3, size.x / 3, size.y / 3);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public int getBulletsNum() {
        return bulletsNum;
    }

    public void setBulletsNum(int bulletsNum) {
        this.bulletsNum = bulletsNum;
    }

    public void addBullet(){
        bulletsNum++;
    }

    public int getBulletsLimit() {
        return bulletsLimit;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
}
