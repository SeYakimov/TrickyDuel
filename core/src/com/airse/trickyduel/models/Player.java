package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by qwert on 17.06.2017.
 */

public class Player {
    public static final int PLAYER_WIDTH = 32;
    public static final int PLAYER_HEIGHT = 32;

    private Vector3 position;
    private boolean isTop;

    private ShapeRenderer shape;

    public Player(int x, int y, boolean isTop) {
        position = new Vector3(x, y, 0);
        shape = new ShapeRenderer();
        this.isTop = isTop;
    }


    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void update(){

    }

    public void render(){
        shape.begin(ShapeRenderer.ShapeType.Filled);

        if (isTop) shape.setColor(Color.valueOf(Duel.ORANGE));
        else shape.setColor(Color.valueOf(Duel.CYAN));

        shape.rect(position.x, position.y, PLAYER_WIDTH, PLAYER_HEIGHT);
        shape.end();
    }

    public void dispose() {

    }
}
