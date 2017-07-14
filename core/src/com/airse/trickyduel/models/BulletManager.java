package com.airse.trickyduel.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

public class BulletManager {

    private Array<Bullet> topBullets, bottomBullets;
    private int bulletMaxNum;
    private int bulletNumTop;
    private int bulletNumBottom;
    private int bulletRadius;
    private long startTimeTop, startTimeBottom;
    private boolean isInfinityBulletsTop, isInfinityBulletsBottom;

    public BulletManager(int bulletRadius, int bulletMaxNum) {
        this.bulletRadius = bulletRadius;
        this.bulletMaxNum = bulletMaxNum;
        bulletNumTop = bulletMaxNum;
        bulletNumBottom = bulletMaxNum;
        topBullets = new Array<Bullet>();
        bottomBullets = new Array<Bullet>();
        startTimeTop = System.currentTimeMillis();
        startTimeBottom = System.currentTimeMillis();
        isInfinityBulletsBottom = false;
        isInfinityBulletsTop = false;
    }

    public void addTopBullet(Player playerTop){
        if (bulletNumTop > 0){
            topBullets.add(new Bullet(playerTop.getPosition().cpy().add(playerTop.getSize().x / 2, 0), true, bulletRadius));
            if (!isInfinityBulletsTop){
                bulletNumTop--;
            }
        }
    }
    public void addBottomBullet(Player playerBottom){
        if (bulletNumBottom > 0){
            bottomBullets.add(new Bullet(playerBottom.getPosition().cpy().add(playerBottom.getSize().x / 2, playerBottom.getSize().y), false, bulletRadius));
            if (!isInfinityBulletsBottom){
                bulletNumBottom--;
            }
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
            if (top.getBounds().y < -2 * top.getRadius()){
                topBullets.removeValue(top, true);
            }
        }
        for (Bullet bottom : bottomBullets) {
            bottom.update();
            if (bottom.isCollides(playerTop.getBounds())){
                border.moveUp();
                bottomBullets.removeValue(bottom, true);
            }
            if (bottom.getBounds().y > camera.viewportHeight){
                bottomBullets.removeValue(bottom, true);
            }
        }

        if (System.currentTimeMillis() - startTimeTop > 1000){
            if (bulletNumTop < bulletMaxNum && !isInfinityBulletsTop){
                bulletNumTop++;
            }
            startTimeTop = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - startTimeBottom > 1000){
            if (bulletNumBottom < bulletMaxNum && !isInfinityBulletsBottom){
                bulletNumBottom++;
            }
            startTimeBottom = System.currentTimeMillis();
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

    public void infinityBullets(final boolean isTop){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long time = System.currentTimeMillis();
                if (isTop){
                    isInfinityBulletsTop = true;
                    bulletNumTop = bulletMaxNum;
                }
                else {
                    isInfinityBulletsBottom = true;
                    bulletNumBottom = bulletMaxNum;
                }
                while (System.currentTimeMillis() < time + 5000){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (isTop){
                            isInfinityBulletsTop = false;
                        }
                        else {
                            isInfinityBulletsBottom = false;
                        }
                    }
                });
            }
        }).start();
    }
}
