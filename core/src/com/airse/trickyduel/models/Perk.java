package com.airse.trickyduel.models;

import com.airse.trickyduel.PerkType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Perk {


    private ShapeRenderer shape;
    private Vector2 position, playerCenterPos;
    private Circle bounds;
    private boolean isTop;
    private PerkType type;
    private int radius;
    private Texture texture;

    public Perk(Texture texture, OrthographicCamera camera, PerkType type, boolean isTop, int playerSize, Border border, Vector2 playerCenterPos){
        this.texture = texture;
        this.radius = playerSize / 2;
//        bounds = new Circle();
        this.isTop = isTop;
        this.type = type;
        this.playerCenterPos = playerCenterPos;
        bounds = randPosition(camera, border, playerSize);
        shape = new ShapeRenderer();

    }


    public boolean isCollides(Rectangle rect)
    {
        Vector2 circleDistance = new Vector2();
        Vector2 rectCenter = new Vector2();
        rect.getCenter(rectCenter);
        Vector2 circleCenter = position.cpy().add(radius, radius);
        float cornerDistance_sq;

        circleDistance.x = Math.abs(circleCenter.x - rectCenter.x);
        circleDistance.y = Math.abs(circleCenter.y - rectCenter.y);

        if (circleDistance.x > (rect.width/2 + radius)) { return false; }
        if (circleDistance.y > (rect.height/2 + radius)) { return false; }

        if (circleDistance.x <= (rect.width/2)) { return true; }
        if (circleDistance.y <= (rect.height/2)) { return true; }

        cornerDistance_sq = (circleDistance.x - rect.width/2) * (circleDistance.x - rect.width/2) +
                (circleDistance.y - rect.height/2) * (circleDistance.y - rect.height/2);

        return (cornerDistance_sq <= (radius * radius));
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Circle getBounds() {
        return bounds;
    }

    public PerkType getType() {
        return type;
    }

    public int getRadius(){
        return radius;
    }

    public void update(){

    }

    public void render(OrthographicCamera camera, SpriteBatch sb){

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(texture, bounds.x,  bounds.y, 2 * radius, 2 * radius);
        sb.end();
    }

    public void dispose(){

    }

    private Circle randPosition(OrthographicCamera camera, Border border, int playerSize){
        Random rand = new Random();
        Circle bounds = new Circle();
        Circle exceptionBounds = new Circle();
        exceptionBounds.setPosition(playerCenterPos);
        exceptionBounds.setRadius(playerSize);
        if (isTop) {
            do{
                position = new Vector2(rand.nextInt((int)(camera.viewportWidth - playerSize)),
                    rand.nextInt((int)(camera.viewportHeight - playerSize - border.getPosition().y - border.getBorderHeight()) + 1) + (int)(border.getPosition().y) + border.getBorderHeight());
                bounds.setPosition(position);
            }
            while (bounds.overlaps(exceptionBounds));
        }
        else {
            do{
                position = new Vector2(rand.nextInt((int)(camera.viewportWidth - playerSize)),
                        rand.nextInt((int)(border.getPosition().y - playerSize) + 1));
                bounds.setPosition(position);
            }
            while (bounds.overlaps(exceptionBounds));
        }
        bounds.setPosition(position.cpy());
        bounds.setRadius(radius);
        return bounds;
    }
}
