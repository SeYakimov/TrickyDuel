package com.airse.trickyduel.models;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Border {

    public static final int BORDER_WIDTH = Duel.WIDTH;
    public static final int BORDER_HEIGHT = Duel.HEIGHT;
    private int movement;

    private Vector2 position;
    private Difficulty difficulty;
    private ShapeRenderer shape;

    public Border(Difficulty difficulty, OrthographicCamera camera, int playerSize) {
        this.difficulty = difficulty;
        setPosition(camera);
        shape = new ShapeRenderer();
        movement = playerSize;

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(OrthographicCamera camera) {
        switch (difficulty)
        {
            case EASY:
                position = new Vector2(0, 0.75f * camera.viewportHeight);
                break;
            case NORMAL:
                position = new Vector2(0, 0.5f * camera.viewportHeight);
                break;
            case HARD:
                position = new Vector2(0, 0.25f * camera.viewportHeight);
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
        if (position.y > camera.position.y + camera.viewportHeight / 2 - movement){
            return 1;
        }
        else if (position.y < camera.position.y - camera.viewportHeight / 2 + movement){
            return -1;
        }
        else return 0;
    }

    public void render(OrthographicCamera camera){
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        shape.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,
                   camera.viewportWidth, camera.viewportHeight);
        shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        shape.rect(camera.position.x - camera.viewportWidth / 2, position.y, camera.viewportWidth, camera.viewportHeight);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }
}