package com.airse.trickyduel.models;

import com.airse.trickyduel.Duel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    private int movement;

    private int radius;
    private ShapeRenderer shape;
    private Vector2 position;
    private Circle bounds;
    private boolean isTop;

    public Bullet(Vector2 position, boolean isTop, int bulletRadius) {
        radius = bulletRadius / 6;
        movement = bulletRadius / 10;
        this.position = position;
        this.isTop = isTop;
        bounds = new Circle();
        bounds.setRadius(radius);
        bounds.setPosition(position.cpy().add(-radius, -radius));
        shape = new ShapeRenderer();
    }

    public boolean isTop() {
        return isTop;
    }
    public int getRadius() {
        return radius;
    }

    public void update(){
        if (isTop){
            moveDown();
        }
        else {
            moveUp();
        }
    }

    public boolean isCollides(Circle circle){
        return bounds.overlaps(circle);
    }
    public boolean isCollides(Rectangle rect)
    {
        Vector2 circleDistance = new Vector2();
        Vector2 rectCenter = new Vector2();
        rect.getCenter(rectCenter);
        float cornerDistance_sq;

        circleDistance.x = Math.abs(position.x - rectCenter.x);
        circleDistance.y = Math.abs(position.y - rectCenter.y);

        if (circleDistance.x > (rect.width/2 + radius)) { return false; }
        if (circleDistance.y > (rect.height/2 + radius)) { return false; }

        if (circleDistance.x <= (rect.width/2)) { return true; }
        if (circleDistance.y <= (rect.height/2)) { return true; }

        cornerDistance_sq = (circleDistance.x - rect.width/2) * (circleDistance.x - rect.width/2) +
                (circleDistance.y - rect.height/2) * (circleDistance.y - rect.height/2);

        return (cornerDistance_sq <= (radius * radius));
    }

    private void moveDown(){
        position.y -= movement;
        bounds.setPosition(position.cpy().add(-radius, -radius));

    }
    private void moveUp(){
        position.y += movement;
        bounds.setPosition(position.cpy().add(-radius, -radius));
    }

    public void render(Border border ,OrthographicCamera camera){
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (isTop){
            if (position.y < border.getPosition().y + border.getBorderHeight()) {
                shape.setColor(Color.BLACK);
            }
            else {
                shape.setColor(Color.valueOf(Duel.TOP_COLOR));
            }
            shape.circle(position.x, position.y, radius);
//            shape.setColor(Color.BLACK);
//            shape.rect(bounds.x, bounds.y, radius * 2, radius * 2);
        }
        else{
            if (position.y > border.getPosition().y) {
                shape.setColor(Color.BLACK);
            }
            else {
                shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
            }
            shape.circle(position.x, position.y, radius);
//            shape.setColor(Color.BLACK);
//            shape.rect(bounds.x, bounds.y, radius * 2, radius * 2);
        }
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }

    public Circle getBounds() {
        return bounds;
    }

    public Vector2 getCenter(){
        return new Vector2(bounds.x + radius, bounds.y + radius);
    }
}