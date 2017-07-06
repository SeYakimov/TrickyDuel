package com.airse.trickyduel.models;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;

public class Border {

    public static final int BORDER_WIDTH = Duel.WIDTH;
    public static final int BORDER_HEIGHT = Duel.HEIGHT;
    private int movement;

    private Vector2 position;
    private int borderHeight;

    private Difficulty difficulty;
    private ShapeRenderer shape;

    private Vector2[] shadowPos;
    private Vector2 shadowSize;

    public Border(Difficulty difficulty, OrthographicCamera camera, int playerSize) {
        this.difficulty = difficulty;
        borderHeight = playerSize;
        setPosition(camera);
        shape = new ShapeRenderer();
        movement = playerSize;

        shadowPos = new Vector2[9];
        shadowPos[0] = new Vector2(0,                            camera.viewportHeight * 5 / 6);
        shadowPos[1] = new Vector2(0,                            camera.viewportHeight * 3 / 6);
        shadowPos[2] = new Vector2(0,                            camera.viewportHeight * 1 / 6);
        shadowPos[3] = new Vector2(camera.viewportWidth * 1 / 3, camera.viewportHeight * 4 / 6);
        shadowPos[4] = new Vector2(camera.viewportWidth * 1 / 3, camera.viewportHeight * 2 / 6);
        shadowPos[5] = new Vector2(camera.viewportWidth * 1 / 3, 0);
        shadowPos[6] = new Vector2(camera.viewportWidth * 2 / 3, camera.viewportHeight * 5 / 6);
        shadowPos[7] = new Vector2(camera.viewportWidth * 2 / 3, camera.viewportHeight * 3 / 6);
        shadowPos[8] = new Vector2(camera.viewportWidth * 2 / 3, camera.viewportHeight * 1 / 6);
        shadowSize = new Vector2(camera.viewportWidth / 3, camera.viewportHeight / 6);
    }

    public int getBorderHeight() {
        return borderHeight;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(OrthographicCamera camera) {
        switch (difficulty)
        {
            case EASY:
                position = new Vector2(0, 0.75f * camera.viewportHeight - borderHeight / 2);
                break;
            case NORMAL:
                position = new Vector2(0, 0.5f * camera.viewportHeight - borderHeight / 2);
                break;
            case HARD:
                position = new Vector2(0, 0.25f * camera.viewportHeight - borderHeight / 2);
                break;
        }
    }

    public void moveUp(){
        position.y += movement;
    }
    public void moveDown(){
        position.y -= movement;
    }

    public void update(float dt){

    }

    public int isGameOver(OrthographicCamera camera){
        if (position.y > camera.position.y + camera.viewportHeight / 2 - movement - borderHeight){
            return 1;
        }
        else if (position.y < camera.position.y - camera.viewportHeight / 2 + movement){
            return -1;
        }
        else return 0;
    }

    public void render(OrthographicCamera camera, BulletManager bulletManager){
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        shape.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,
                   camera.viewportWidth, camera.viewportHeight);
        shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        shape.rect(camera.position.x - camera.viewportWidth / 2, position.y, camera.viewportWidth, camera.viewportHeight);
        shape.end();

        drawShadow(camera);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.CYAN100));
        shape.rect(position.x, position.y + borderHeight / 3, camera.viewportWidth / 3, borderHeight / 3);
        shape.setColor(Color.valueOf(Duel.CYAN400));
        shape.rect(position.x, position.y + borderHeight / 3, camera.viewportWidth / 3 * bulletManager.getBulletNumBottom() / bulletManager.getBulletMaxNum(), borderHeight / 3);

        shape.setColor(Color.valueOf(Duel.RED100));
        shape.rect(camera.viewportWidth * 2 / 3, position.y + borderHeight / 3, camera.viewportWidth / 3, borderHeight / 3);
        shape.setColor(Color.valueOf(Duel.RED400));
        shape.rect(camera.viewportWidth * 2 / 3 + camera.viewportWidth / 3 * (bulletManager.getBulletMaxNum() - bulletManager.getBulletNumTop()) / bulletManager.getBulletMaxNum(),
                   position.y + borderHeight / 3, camera.viewportWidth / 3 * bulletManager.getBulletNumTop() / bulletManager.getBulletMaxNum(), borderHeight / 3);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }

    private void drawShadow(OrthographicCamera camera){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(0, 0, 0, 0.2f);
        for (int i = 0; i < shadowPos.length; i++){
            shape.rect(shadowPos[i].x, shadowPos[i].y, shadowSize.x, shadowSize.y);
        }

        shape.setColor(Color.WHITE);
        shape.rect(position.x, position.y, camera.viewportWidth, borderHeight);
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}