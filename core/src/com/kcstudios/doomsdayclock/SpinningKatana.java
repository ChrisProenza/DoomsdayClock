package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class SpinningKatana extends Skill {
    SpinningKatana() {
        lvl = 1;
        speed = 0;
        damage = 2;
        duration = 8;
        numberProjectiles = 4;
        size = 5;
        cooldown = 20;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/SpinningKatana.png");
        passThrough = true;
        id = 1;
        deltaRotation = (float) Math.toRadians(45);
        knockback = 0;
    }

    @Override
    void lvlUp() {
        if(lvl == 1) {
            lvl++;
            duration+=2;
            size++;
        } else if (lvl == 2) {
            lvl++;
            cooldown-=2;
            size++;
        } else if (lvl == 3) {
            lvl++;
            damage+=8;
            size++;
        } else if (lvl == 4) {
            lvl++;
            cooldown-=1;
            size++;
        } else if (lvl == 5) {
            lvl++;
            numberProjectiles*=2;
            size++;
        } else {
            lvl++;
        }
    }
}

