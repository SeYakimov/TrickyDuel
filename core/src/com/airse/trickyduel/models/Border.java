package com.airse.trickyduel.models;

import com.airse.trickyduel.Difficulty;
import com.airse.trickyduel.Duel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Border {

    private int movement;

    private Vector2 position;
    private int borderHeight;
    private int GAP;
    private int BULLET_BAR_LENGTH;

    private Difficulty difficulty;
    private ShapeRenderer shape;

    private Vector2[] shadowPosChessBoard;
    private Vector2[] shadowPosLines;
    private Vector2 shadowSizeChessBoard;
    private Vector2 shadowSizeLines;

    private Texture texture;

    public Border(Difficulty difficulty, OrthographicCamera camera, int playerSize) {
        this.difficulty = difficulty;
        borderHeight = playerSize;
        setPosition(camera);
        shape = new ShapeRenderer();
        movement = playerSize;

        shadowPosChessBoard = new Vector2[9];
        shadowPosChessBoard[0] = new Vector2(0,                            camera.viewportHeight * 5 / 6);
        shadowPosChessBoard[1] = new Vector2(0,                            camera.viewportHeight * 3 / 6);
        shadowPosChessBoard[2] = new Vector2(0,                            camera.viewportHeight * 1 / 6);
        shadowPosChessBoard[3] = new Vector2(camera.viewportWidth * 1 / 3, camera.viewportHeight * 4 / 6);
        shadowPosChessBoard[4] = new Vector2(camera.viewportWidth * 1 / 3, camera.viewportHeight * 2 / 6);
        shadowPosChessBoard[5] = new Vector2(camera.viewportWidth * 1 / 3, 0);
        shadowPosChessBoard[6] = new Vector2(camera.viewportWidth * 2 / 3, camera.viewportHeight * 5 / 6);
        shadowPosChessBoard[7] = new Vector2(camera.viewportWidth * 2 / 3, camera.viewportHeight * 3 / 6);
        shadowPosChessBoard[8] = new Vector2(camera.viewportWidth * 2 / 3, camera.viewportHeight * 1 / 6);

        shadowPosLines = new Vector2[8];
        for (int i = 0; i < shadowPosLines.length; i++){
            shadowPosLines[i] = new Vector2(0, camera.viewportHeight * 2 * i / 15);
        }
        shadowSizeChessBoard = new Vector2(camera.viewportWidth / 3, camera.viewportHeight / 6);
        shadowSizeLines = new Vector2(camera.viewportWidth, camera.viewportHeight / 15);

        texture = new Texture("perks/without bg/random.png");
        GAP = borderHeight / 8;
        BULLET_BAR_LENGTH = (int)((camera.viewportWidth - 3.5f * getBorderHeight()) / 2);
    }

    public int getBorderHeight() {
        return borderHeight;
    }

    public Vector2 getPosition() {
        return position;
    }

    private void setPosition(OrthographicCamera camera) {
        switch (difficulty)
        {
            case EASY:
                position = new Vector2(0, 0.75f * camera.viewportHeight - borderHeight / 2);
                break;
            case NORMAL:
                position = new Vector2(0, 0.5f * camera.viewportHeight - borderHeight / 2);
                break;
            case HARD:
                position = new Vector2(0, 0.25f * camera.viewportHeight - borderHeight / 2);
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
        if (position.y > camera.position.y + camera.viewportHeight / 2 - movement - borderHeight){
            return 1;
        }
        else if (position.y < camera.position.y - camera.viewportHeight / 2 + movement){
            return -1;
        }
        else return 0;
    }

    public void render(SpriteBatch sb, OrthographicCamera camera, BulletManager bulletManager){
        sb.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.DARK_BLUE));
        shape.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
        shape.setColor(Color.valueOf(Duel.DARK_BLUE));
        shape.rect(camera.position.x - camera.viewportWidth / 2, position.y, camera.viewportWidth, camera.viewportHeight);
        shape.end();

        drawShadow(camera);
        drawBulletsBar(camera, bulletManager);
        drawScore(camera, sb);
        drawPerk(camera, sb);

    }

    public void dispose() {
        shape.dispose();
    }

    private void drawShadow(OrthographicCamera camera){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(0, 0, 0, 0.1f);
        for (Vector2 shadowPosLine : shadowPosLines) {
            shape.rect(shadowPosLine.x, shadowPosLine.y, shadowSizeLines.x, shadowSizeLines.y);
        }

        shape.setColor(Color.WHITE);
        shape.rect(position.x, position.y, camera.viewportWidth, getBorderHeight());
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawBulletsBar(OrthographicCamera camera, BulletManager bulletManager){

//        shape.begin(ShapeRenderer.ShapeType.Filled);
//        shape.setColor(Color.valueOf(Duel.CYAN100));
//        shape.rect(position.x, position.y + borderHeight / 3, camera.viewportWidth / 3, borderHeight / 3);
//        shape.setColor(Color.valueOf(Duel.CYAN400));
//        shape.rect(position.x, position.y + borderHeight / 3, camera.viewportWidth / 3 * bulletManager.getBulletNumBottom() / bulletManager.getBulletMaxNum(), borderHeight / 3);
//
//        shape.setColor(Color.valueOf(Duel.RED100));
//        shape.rect(camera.viewportWidth * 2 / 3, position.y + borderHeight / 3, camera.viewportWidth / 3, borderHeight / 3);
//        shape.setColor(Color.valueOf(Duel.RED400));
//        shape.rect(camera.viewportWidth * 2 / 3 + camera.viewportWidth / 3 * (bulletManager.getBulletMaxNum() - bulletManager.getBulletNumTop()) / bulletManager.getBulletMaxNum(),
//                position.y + borderHeight / 3, camera.viewportWidth / 3 * bulletManager.getBulletNumTop() / bulletManager.getBulletMaxNum(), borderHeight / 3);
//        shape.end();

//        shape.begin(ShapeRenderer.ShapeType.Filled);
//        shape.setColor(Color.valueOf(Duel.CYAN100));
//        shape.rect(position.x, position.y + borderHeight / 4, camera.viewportWidth / 3, borderHeight / 2);
//        shape.setColor(Color.valueOf(Duel.CYAN400));
//        shape.rect(position.x, position.y + borderHeight / 4, camera.viewportWidth / 3 * bulletManager.getBulletNumBottom() / bulletManager.getBulletMaxNum(), borderHeight / 2);
//
//        shape.setColor(Color.valueOf(Duel.RED100));
//        shape.rect(camera.viewportWidth * 2 / 3, position.y + borderHeight / 4, camera.viewportWidth / 3, borderHeight / 2);
//        shape.setColor(Color.valueOf(Duel.RED400));
//        shape.rect(camera.viewportWidth * 2 / 3 + camera.viewportWidth / 3 * (bulletManager.getBulletMaxNum() - bulletManager.getBulletNumTop()) / bulletManager.getBulletMaxNum(),
//                position.y + borderHeight / 4, camera.viewportWidth / 3 * bulletManager.getBulletNumTop() / bulletManager.getBulletMaxNum(), borderHeight / 2);
//        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        shape.rect(camera.viewportWidth - BULLET_BAR_LENGTH,
                   getPosition().y + getBorderHeight() / 8,
                   BULLET_BAR_LENGTH * bulletManager.getBulletNumTop() / bulletManager.getBulletMaxNum(),
                   getBorderHeight() * 6 / 8);

        shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        shape.rect(BULLET_BAR_LENGTH * (bulletManager.getBulletMaxNum() - bulletManager.getBulletNumBottom()) / bulletManager.getBulletMaxNum(),
                   getPosition().y + getBorderHeight() / 8,
                   BULLET_BAR_LENGTH * bulletManager.getBulletNumBottom() / bulletManager.getBulletMaxNum(),
                   getBorderHeight() * 6 / 8);
        shape.end();
    }

    private void drawScore(OrthographicCamera camera, SpriteBatch sb){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.TOP_COLOR));
        shape.rect(camera.position.x + getBorderHeight() / 2 + GAP, getPosition().y, getBorderHeight(), getBorderHeight());
        shape.setColor(Color.valueOf(Duel.BOTTOM_COLOR));
        shape.rect(camera.position.x - getBorderHeight() * 3 / 2 - GAP, getPosition().y, getBorderHeight(), getBorderHeight());
        shape.end();
    }

    private void drawPerk(OrthographicCamera camera, SpriteBatch sb){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(Duel.RED400));
        shape.rect(camera.position.x - getBorderHeight() / 2, getPosition().y, getBorderHeight(), getBorderHeight());
        shape.end();
        sb.begin();
        sb.draw(texture, (int)(camera.position.x - getBorderHeight() / 2), (int)getPosition().y, getBorderHeight(), getBorderHeight());
        sb.end();
    }
}