package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class Dagger extends Skill {
    Dagger() {
        speed = 2;
        damage = 5;
        duration = 3;
        numberProjectiles = 2;
        size = 5;
        cooldown = 3;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/Dagger.png");
        passThrough = false;
        id = 0;
        deltaRotation = 0;
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
