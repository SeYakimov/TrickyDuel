package com.airse.trickyduel;
public enum PerkType {OPPO_FREEZE, YOU_FREEZE, OPPO_GROW, YOU_GROW, OPPO_BULLETS, YOU_BULLETS,
                      INCREASE_SPEED, DECREASE_SPEED, RANDOM_PERK;
    public static PerkType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
