package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class SpinningBlades extends Skill {
    SpinningBlades() {
        lvl = 1;
        speed = 0;
        damage = 1;
        duration = 5;
        numberProjectiles = 1;
        size = 15;
        cooldown = 10;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/SpectralSword.png");
        passThrough = true;
        id = 2;
        deltaRotation = (float) Math.toRadians(15);
        knockback = 5;
    }

    @Override
    void lvlUp() {
        if(lvl == 1) {
            lvl++;
            numberProjectiles++;
            damage++;
        } else if (lvl == 2) {
            lvl++;
            numberProjectiles++;
            damage++;
            cooldown--;
        } else if (lvl == 3) {
            lvl++;
            numberProjectiles++;
            damage++;
        } else if (lvl == 4) {
            lvl++;
            numberProjectiles++;
            damage++;
            cooldown--;
        } else if (lvl == 5) {
            lvl++;
            numberProjectiles++;
            damage++;
        } else {
            lvl++;
            size++;
        }
    }
}

