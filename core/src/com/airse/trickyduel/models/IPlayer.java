package com.airse.trickyduel.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by qwert on 18.06.2017.
 */

public interface IPlayer {
    int PLAYER_WIDTH = 32;
    int PLAYER_HEIGHT = 32;
    int MOVEMENT = 3;

    Vector3 getPosition();
    void setPosition(Vector3 position);
    void moveLeft();
    void moveRight();
    void moveUp(Border border);
    void moveDown(Border border);
    void update();
    void render();
    void dispose();
}
