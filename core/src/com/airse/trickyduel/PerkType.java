package com.airse.trickyduel;

/**
 * Created by qwert on 22.06.2017.
 */

public enum PerkType {OPPO_FREEZE, YOU_FREEZE, OPPO_GROW, YOU_GROW, OPPO_BULLETS, YOU_BULLETS,
                      INCREASE_SPEED, DECREASES_SPEED, RANDOM_PERK;
    public static PerkType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
