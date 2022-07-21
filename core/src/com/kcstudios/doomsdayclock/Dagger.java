package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class Dagger extends Skill {
    Dagger() {
        lvl = 1;
        speed = 2;
        damage = 5;
        duration = 3;
        numberProjectiles = 1;
        size = 3;
        cooldown = 2;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/Dagger.png");
        passThrough = false;
        id = 0;
        deltaRotation = 0;
        knockback = 5;
    }

    @Override
    void lvlUp() {
        if(lvl == 1) {
            lvl++;
            numberProjectiles*=2;
            speed+=3;
        } else if (lvl == 2) {
            lvl++;
            damage+=5;
            cooldown-=1;
        } else if (lvl == 3) {
            lvl++;
            numberProjectiles*=2;
            size+=3;
        } else if (lvl == 4) {
            lvl++;
            damage+=10;
            cooldown-=.5;
        } else if (lvl == 5) {
            lvl++;
            damage+=10;
            cooldown-=.5;
        } else {
            lvl++;
            damage += 1;
            size += 1;
            speed += 1;
            cooldown *= .8;
        }
    }
}
