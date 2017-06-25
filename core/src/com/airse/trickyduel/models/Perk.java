package com.airse.trickyduel.models;

import com.airse.trickyduel.PerkType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Perk {


    private ShapeRenderer shape;
    private Vector2 position;
    private Rectangle bounds;
    private boolean isTop;
    private PerkType type;
    private int radius;
    private Texture texture;
    private Random rand;

    public Perk(Texture texture, OrthographicCamera camera, PerkType type, boolean isTop, int playerSize, Border border){
        rand = new Random();
        this.texture = texture;
        this.radius = playerSize / 2;
        if (isTop) {
            this.position = new Vector2(rand.nextInt((int)(camera.viewportWidth - playerSize)) + (int)(camera.position.x - camera.viewportWidth / 2),
                    rand.nextInt((int)(camera.position.y + camera.viewportHeight / 2 - playerSize - border.getPosition().y)) + (int)(border.getPosition().y));

        }
        else {
            this.position = new Vector2(rand.nextInt((int)(camera.viewportWidth - playerSize)) + (int)(camera.position.x - camera.viewportWidth / 2),
                    rand.nextInt((int)(border.getPosition().y - (camera.position.y - camera.viewportHeight / 2) - playerSize)) + (int)(camera.position.y - camera.viewportHeight / 2));
        }

        this.type = type;
        this.isTop = isTop;
        shape = new ShapeRenderer();
        bounds = new Rectangle();
        bounds.setCenter(position.cpy());
        bounds.setSize((int)(Math.sqrt(2 * (radius * radius))));
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public PerkType getType() {
        return type;
    }

    public void update(){

    }

    public void render(OrthographicCamera camera, SpriteBatch sb){

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(texture, position.x, position.y, 2 * radius, 2 * radius);
        sb.end();
    }

    public void dispose(){

    }
}
