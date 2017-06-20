package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public static final int RADIUS = (int)(Duel.WIDTH * 0.02f);
    public static final int INNER_RADIUS = (int)(Math.sqrt((RADIUS * RADIUS)));
    public static final int MOVEMENT = 3;

    private ShapeRenderer shape;
    private Vector2 position;
    private Rectangle bounds;

    public boolean isTop() {
        return isTop;
    }

    private boolean isTop;

    public Bullet(Vector2 position, boolean isTop) {
        this.position = position;
        this.isTop = isTop;
        bounds = new Rectangle();
        bounds.setSize(INNER_RADIUS, INNER_RADIUS);
        bounds.setCenter(position.cpy());
        shape = new ShapeRenderer();
        System.out.println(RADIUS);
        System.out.println(INNER_RADIUS);
    }


    public void update(){
        if (isTop){
            moveDown();
        }
        else {
            moveUp();
        }
    }

    public boolean isCollides(Rectangle rect){
        return bounds.overlaps(rect);
    }

    private void moveDown(){
        position.y -= MOVEMENT;
        bounds.setCenter(position);
    }
    private void moveUp(){
        position.y += MOVEMENT;
        bounds.setCenter(position);
    }

    public void render(Border border ,OrthographicCamera camera){
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (isTop){
            if (position.y < border.getPosition().y) {
                shape.setColor(Color.BLACK);
            }
            else {
                shape.setColor(Color.valueOf(Duel.TOP_COLOR));
            }
            shape.circle(position.x, position.y, RADIUS);
        }
        else{
            if (position.y > border.getPosition().y) {
                shape.setColor(Color.BLACK);
            }
            else {
                shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
            }
            shape.circle(position.x, position.y, RADIUS);
        }
//        shape.setColor(Color.YELLOW);
//        shape.rect(bounds.x, bounds.y, bounds.getWidth(), bounds.getHeight());
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}