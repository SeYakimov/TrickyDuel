package com.airse.trickyduel.models;

import com.airse.trickyduel.PerkType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PerkManager {

    private Array<Perk> topPerks, bottomPerks;
    private int perkSize;
    public PerkManager(int perkSize) {
        this.perkSize = perkSize;

        topPerks = new Array<Perk>();
        bottomPerks = new Array<Perk>();
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
            case FREEZE:
                texture = new Texture("red_bullet.png");
                break;

            case OPPO_BIG:
                texture = new Texture("red_bullet.png");
                break;

            case YOU_BIG:
                texture = new Texture("red_bullet.png");
                break;

            case OPPO_LITTLE:
                texture = new Texture("red_bullet.png");
                break;

            case YOU_LITTLE:
                texture = new Texture("red_bullet.png");
                break;

            case OPPO_BULLETS:
                texture = new Texture("red_bullet.png");
                break;

            case YOU_BULLETS:
                texture = new Texture("red_bullet.png");
                break;

            default:
                texture = new Texture("red_bullet.png");
                break;
        }
        return texture;
    }

    public void update(){

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
