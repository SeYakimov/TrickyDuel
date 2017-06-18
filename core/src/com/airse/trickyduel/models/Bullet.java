package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by qwert on 17.06.2017.
 */

public class Bullet {
    public static final int RADIUS = (int)(Duel.WIDTH * 0.02f);
    public static final int MOVEMENT = 2;

    private ShapeRenderer shape;
    private Vector2 position;
    private Circle bounds;
    boolean isTop;

    public Bullet(Vector2 position, boolean isTop) {
        this.position = position;
        this.isTop = isTop;
        bounds = new Circle();
        bounds.setPosition(position);
        bounds.setRadius(RADIUS);
        shape = new ShapeRenderer();
    }


    public void update(){
        if (isTop){
            moveDown();
        }
        else {
            moveUp();
        }
    }
    private void moveDown(){
        position.y -= MOVEMENT;
        bounds.setPosition(position);
    }
    private void moveUp(){
        position.y += MOVEMENT;
        bounds.setPosition(position);
    }

    public void render(){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (isTop){
            shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        }
        else{
            shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        }
        shape.circle(position.x, position.y, RADIUS);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }

    public boolean isCollide(Rectangle rect)
    {
        return false;
    }
}
