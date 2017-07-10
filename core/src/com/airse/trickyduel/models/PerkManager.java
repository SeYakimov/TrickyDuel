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

    public void addTopPerk(PerkType type, OrthographicCamera camera, Border border){

        topPerks.add(new Perk(chooseTexture(type), camera, type, true, perkSize, border));

    }

    public void addBottomPerk(PerkType type, OrthographicCamera camera, Border border){
        bottomPerks.add(new Perk(chooseTexture(type), camera, type, false, perkSize, border));

    }
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

            case DECREASES_SPEED:
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

    public void update(OrthographicCamera camera, Border border, Player topPlayer, Player bottomPlayer){
        if (System.currentTimeMillis() - startTimeTop > 8000 && topPerks.size < 2){
            PerkType temp;
            temp = PerkType.getRandom();
            topPerks.add(new Perk(chooseTexture(temp), camera, temp, true, perkSize, border));
            startTimeTop = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - startTimeBottom > 8000 && bottomPerks.size < 2){
            PerkType temp;
            temp = PerkType.getRandom();
            bottomPerks.add(new Perk(chooseTexture(temp), camera, temp, false, perkSize, border));
            startTimeBottom = System.currentTimeMillis();
        }
        for (Perk top: topPerks){
            if (top.isCollides(topPlayer.getBounds())){
                topPerks.removeValue(top, true);
                //TODO top player intersects perk
            }
            if (top.getBounds().y < border.getPosition().y + border.getBorderHeight()){
                topPerks.removeValue(top, true);
            }
        }
        for (Perk bottom : bottomPerks){
            if (bottom.isCollides(bottomPlayer.getBounds())){
                bottomPerks.removeValue(bottom, true);
                //TODO bottom player intersects perk
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

}
