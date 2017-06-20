package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by qwert on 17.06.2017.
 */

public class Player {
    public static final int PLAYER_WIDTH = (int)(0.1f * Duel.WIDTH);
    public static final int PLAYER_HEIGHT = PLAYER_WIDTH;
    public static final int MOVEMENT = (int)(0.02f * Duel.WIDTH);

    private boolean isTop;
    private Vector2 position;
    private Rectangle bounds;
    private int bulletsNum;
    private int bulletsLimit;

    private ShapeRenderer shape;

    public Player(Vector2 position, boolean isTop) {
        this.position = position;
        shape = new ShapeRenderer();
        this.isTop = isTop;
        bounds = new Rectangle();
        bounds.setPosition(position);
        bounds.setSize(PLAYER_WIDTH, PLAYER_HEIGHT);
        bulletsNum = 30;
        bulletsLimit = 30;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void moveLeft()
    {
        position.x -= MOVEMENT;
        if (position.x < 0) position.x = 0;
    }
    public void moveRight()
    {
        position.x += MOVEMENT;
        if (position.x > Duel.WIDTH - PLAYER_WIDTH) position.x = Duel.WIDTH - PLAYER_WIDTH;
    }
    public void moveUp()
    {
        position.y += MOVEMENT;
    }
    public void moveDown()
    {
        position.y -= MOVEMENT;

    }
    public void update(Border border, OrthographicCamera camera){
        if (isTop){
            if (position.y < border.getPosition().y) position.y = border.getPosition().y;
            if (position.y > camera.position.y + camera.viewportHeight / 2 - PLAYER_HEIGHT)
                position.y = camera.position.y + camera.viewportHeight / 2 - PLAYER_HEIGHT;
//            if (position.y > Duel.HEIGHT - PLAYER_HEIGHT) position.y = Duel.HEIGHT - PLAYER_HEIGHT;
        }
        else{
//            if (position.y < 0) position.y = 0;
            if (position.y > border.getPosition().y - PLAYER_HEIGHT) position.y = border.getPosition().y - PLAYER_HEIGHT;
            if (position.y < camera.position.y - camera.viewportHeight / 2) position.y = camera.position.y - camera.viewportHeight / 2;
        }
        bounds.setPosition(position);
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

        shape.rect(position.x, position.y, PLAYER_WIDTH, PLAYER_HEIGHT);
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
}
