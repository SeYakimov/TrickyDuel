package com.airse.trickyduel.models;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by qwert on 17.06.2017.
 */

public class Border {

    public static final int BORDER_WIDTH = Duel.WIDTH;
    public static final int BORDER_HEIGHT = Duel.HEIGHT;
    public static final int MOVEMENT = 4;

    private Vector2 position;
    private Difficulty difficulty;
    private ShapeRenderer shape;

    public Border(Difficulty difficulty) {
        this.difficulty = difficulty;
        setPosition();
        shape = new ShapeRenderer();

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition() {
        switch (difficulty)
        {
            case EASY:
                position = new Vector2(0, 0.75f * Duel.HEIGHT);
                break;
            case NORMAL:
                position = new Vector2(0, 0.5f * Duel.HEIGHT);
                break;
            case HARD:
                position = new Vector2(0, 0.25f * Duel.HEIGHT);
                break;
        }
    }

    public void moveUp(){
        position.y += MOVEMENT;
    }
    public void moveDown(){
        position.y -= MOVEMENT;
    }

    public void update(float dt){

    }

    public void render(){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        shape.rect(0, 0, BORDER_WIDTH, BORDER_HEIGHT);
        shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        shape.rect(position.x, position.y, BORDER_WIDTH, BORDER_HEIGHT);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }
}
