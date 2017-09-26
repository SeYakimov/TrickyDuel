package com.airse.trickyduel.models;

import com.airse.trickyduel.PerkType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PerkManager {

    private Array<Perk> topPerks, bottomPerks;
    private int perkSize;
    private long startTimeTop, startTimeBottom;

    public PerkManager(int perkSize) {
        this.perkSize = perkSize;

        topPerks = new Array<Perk>();
        bottomPerks = new Array<Perk>();

        startTimeTop = System.currentTimeMillis();
        startTimeBottom = System.currentTimeMillis();

    }
//    public void addTopPerk(PerkType type, OrthographicCamera camera, Border border){
//        topPerks.add(new Perk(chooseTexture(type), camera, type, true, perkSize, border));
//    }
//
//    public void addBottomPerk(PerkType type, OrthographicCamera camera, Border border){
//        bottomPerks.add(new Perk(chooseTexture(type), camera, type, false, perkSize, border));
//    }
    private Texture chooseTexture(PerkType type){
        Texture texture;
        switch (type){
            case OPPO_FREEZE:
                texture = new Texture("perks/ices_opponent.png");
                break;

            case YOU_FREEZE:
                texture = new Texture("perks/ices_yourself.png");
                break;

            case OPPO_GROW:
                texture = new Texture("perks/opponent_grows.png");
                break;

            case YOU_GROW:
                texture = new Texture("perks/you_grows.png");
                break;

            case INCREASE_SPEED:
                texture = new Texture("perks/speed_boost.png");
                break;

            case DECREASE_SPEED:
                texture = new Texture("perks/decreases_speed.png");
                break;

            case OPPO_BULLETS:
                texture = new Texture("perks/infinity_bullets_opponent.png");
                break;

            case YOU_BULLETS:
                texture = new Texture("perks/infinity_bullets_you.png");
                break;

            default:
                texture = new Texture("perks/random_perk.png");
                break;
        }
        return texture;
    }

    public void update(OrthographicCamera camera, Border border, Player topPlayer, Player bottomPlayer, BulletManager bulletManager){
        if (System.currentTimeMillis() - startTimeTop > 6000 && topPerks.size < 2){
            PerkType tempType = PerkType.getRandom();
            Texture tempTexture = chooseTexture(tempType);

            while (tempType == PerkType.RANDOM_PERK){
                tempType = PerkType.getRandom();
            }

            topPerks.add(new Perk(tempTexture, camera, tempType, true, perkSize, border, topPlayer.getCenter()));
            startTimeTop = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - startTimeBottom > 6000 && bottomPerks.size < 2){
            PerkType tempType = PerkType.getRandom();
            Texture tempTexture = chooseTexture(tempType);

            while (tempType == PerkType.RANDOM_PERK){
                tempType = PerkType.getRandom();
            }

            bottomPerks.add(new Perk(tempTexture, camera, tempType, false, perkSize, border, bottomPlayer.getCenter()));

            startTimeBottom = System.currentTimeMillis();
        }
        for (Perk top: topPerks){
            if (top.isCollides(topPlayer.getBounds())){
                doPerk(top.getType(), topPlayer, bottomPlayer, bulletManager,  true);
                topPerks.removeValue(top, true);
            }
            if (top.getBounds().y < border.getPosition().y + border.getBorderHeight()){
                topPerks.removeValue(top, true);
            }
        }
        for (Perk bottom : bottomPerks){
            if (bottom.isCollides(bottomPlayer.getBounds())){
                doPerk(bottom.getType(), topPlayer, bottomPlayer, bulletManager, false);
                bottomPerks.removeValue(bottom, true);
            }
            if (bottom.getBounds().y > border.getPosition().y - bottom.getRadius() * 2){
                bottomPerks.removeValue(bottom, true);
            }
        }
    }

    public void render(OrthographicCamera camera, SpriteBatch sb){

        for (Perk perk : bottomPerks) {
            perk.render(camera, sb);
        }
        for (Perk perk : topPerks) {
            perk.render(camera, sb);
        }
    }

    public void dispose(){
        for (Perk perk : bottomPerks) {
            perk.dispose();
        }
        for (Perk perk : topPerks) {
            perk.dispose();
        }
    }

    public Array<Perk> getTopPerks() {
        return topPerks;
    }

    public Array<Perk> getBottomPerks() {
        return bottomPerks;
    }

    private void doPerk(PerkType type, Player topPlayer, Player bottomPlayer, BulletManager bulletManager,  boolean isTop){
        switch (type){
            case OPPO_FREEZE:
                if (isTop){
                    bottomPlayer.freeze();
                }
                else{
                    topPlayer.freeze();
                }
                break;

            case YOU_FREEZE:
                if (isTop){
                    topPlayer.freeze();
                }
                else {
                    bottomPlayer.freeze();
                }
                break;

            case OPPO_GROW:
                if (isTop){
                    bottomPlayer.grow();
                }
                else {
                    topPlayer.grow();
                }
                break;

            case YOU_GROW:
                if (isTop){
                    topPlayer.grow();
                }
                else {
                    bottomPlayer.grow();
                }
                break;

            case INCREASE_SPEED:
                if (isTop){
                    topPlayer.speedBoost();
                }
                else {
                    bottomPlayer.speedBoost();
                }
                break;

            case DECREASE_SPEED:
                if (isTop){
                    topPlayer.reduceSpeed();
                }
                else {
                    bottomPlayer.reduceSpeed();
                }
                break;

            case OPPO_BULLETS:
                bulletManager.infinityBullets(!isTop);
                break;

            case YOU_BULLETS:
                bulletManager.infinityBullets(isTop);
                break;

            default:

                break;
        }
    }
}
