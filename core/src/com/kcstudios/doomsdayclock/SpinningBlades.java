package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class SpinningBlades extends Skill {
    SpinningBlades() {
        speed = 0;
        damage = 5;
        duration = 5;
        numberProjectiles = 1;
        size = 10;
        cooldown = 10;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/SpectralSword.png");
        passThrough = true;
        id = 2;
        deltaRotation = (float) Math.toRadians(30);
    }

    @Override
    void lvlUp() {
        if(lvl == 1) {
            lvl++;
            numberProjectiles++;
            speed+=3;
        } else if (lvl == 2) {
            lvl++;
            damage+=5;
            cooldown*=.75;
        } else if (lvl == 3) {
            lvl++;
            numberProjectiles++;
            size+=3;
        } else if (lvl == 4) {
            lvl++;
            damage+=10;
            cooldown*=.5;
        }
    }
}

