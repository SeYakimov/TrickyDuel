package com.airse.trickyduel.models;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class BulletManager {

    private Array<Bullet> topBullets, bottomBullets;
    private int bulletMaxNum;
    private int bulletNumTop;
    private int bulletNumBottom;
    private int bulletRadius;
    private long startTime;

    public BulletManager(int bulletRadius, int bulletMaxNum) {
        this.bulletRadius = bulletRadius;
        this.bulletMaxNum = bulletMaxNum;
        bulletNumTop = bulletMaxNum;
        bulletNumBottom = bulletMaxNum;
        topBullets = new Array<Bullet>();
        bottomBullets = new Array<Bullet>();
        startTime = System.currentTimeMillis();
    }

    public void addTopBullet(Player playerTop, Player playerBottom){
        if (bulletNumTop > 0){
            topBullets.add(new Bullet(playerTop.getPosition().cpy().add(playerTop.getSize().x / 2, 0), true, bulletRadius));
            bulletNumTop--;
        }
    }
    public void addBottomBullet(Player playerTop, Player playerBottom){
        if (bulletNumBottom > 0){
            bottomBullets.add(new Bullet(playerBottom.getPosition().cpy().add(playerBottom.getSize().x / 2, playerBottom.getSize().y), false, bulletRadius));
            bulletNumBottom--;
        }
    }

    public int getBulletMaxNum() {
        return bulletMaxNum;
    }

    public int getBulletNumTop() {
        return bulletNumTop;
    }

    public int getBulletNumBottom() {
        return bulletNumBottom;
    }

    public void update(OrthographicCamera camera, Player playerTop, Player playerBottom, Border border){
        for (Bullet top : topBullets) {
            for (Bullet bottom : bottomBullets) {
                if (top.isCollides(bottom.getBounds()))
                {
                    topBullets.removeValue(top, true);
                    bottomBullets.removeValue(bottom, true);
                }
            }
        }

        for (Bullet top : topBullets) {
            top.update();
            if (top.isCollides(playerBottom.getBounds())){
                border.moveDown();
                topBullets.removeValue(top, true);
            }
            if (top.getPosition().y < camera.position.y - camera.viewportHeight / 2 - top.getRadius()){
                topBullets.removeValue(top, true);
            }
        }
        for (Bullet bottom : bottomBullets) {
            bottom.update();
            if (bottom.isCollides(playerTop.getBounds())){
                border.moveUp();
                bottomBullets.removeValue(bottom, true);
            }
            if (bottom.getPosition().y > camera.position.y + camera.viewportHeight / 2){
                bottomBullets.removeValue(bottom, true);
            }
        }

        if (System.currentTimeMillis() - startTime > 1000){
            if (bulletNumTop < bulletMaxNum){
                bulletNumTop++;
            }
            if (bulletNumBottom < bulletMaxNum){
                bulletNumBottom++;
            }
            startTime = System.currentTimeMillis();
        }
    }

    public void render(OrthographicCamera camera, Border border){
        for (Bullet top : topBullets) {
            top.render(border, camera);
        }
        for (Bullet bottom : bottomBullets) {
            bottom.render(border, camera);
        }
    }

    public void dispose(){
        for (Bullet top : topBullets) {
        top.dispose();
    }
        for (Bullet bottom : bottomBullets) {
            bottom.dispose();
        }

    }
}
