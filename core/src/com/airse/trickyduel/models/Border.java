package com.airse.trickyduel.models;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by qwert on 17.06.2017.
 */

public class Border {

    public static final int BORDER_WIDTH = 320;
    public static final int BORDER_HEIGHT = 480;

    private Vector3 position;
    private Difficulty difficulty;
    private ShapeRenderer shape;

    public Border(Difficulty difficulty) {
        this.difficulty = difficulty;
        setPosition();
        shape = new ShapeRenderer();

    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition() {
        switch (difficulty)
        {
            case EASY:

                break;
            case NORMAL:
                position = new Vector3(0, 240, 0);
                break;
            case HARD:

                break;
        }
    }

    public void changePosition(int dy){
        position = new Vector3(position.x, position.y + dy, 0);
    }

    public void update(float dt){

    }

    public void render(){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.ORANGE));
        shape.rect(0, 0, BORDER_WIDTH, BORDER_HEIGHT);
        shape.setColor(Color.valueOf(Duel.CYAN));
        shape.rect(position.x, position.y, BORDER_WIDTH, BORDER_HEIGHT);
        shape.end();
    }

    public void dispose() {

    }
}
