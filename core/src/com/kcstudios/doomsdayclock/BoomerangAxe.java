package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class BoomerangAxe extends Skill {
    BoomerangAxe() {
        lvl = 1;
        speed = 3;
        damage = 10;
        duration = 4;
        numberProjectiles = 1;
        size = 5;
        cooldown = 12;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/BoomerangAxe.png");
        passThrough = true;
        id = 3;
        deltaRotation = (float) Math.toRadians(5);
        knockback = 10;
    }

    @Override
    void lvlUp() {
        if(lvl == 1) {
            lvl++;
            damage+=15;
            size+=2;
        } else if (lvl == 2) {
            lvl++;
            cooldown-=2;
            duration+=1;
        } else if (lvl == 3) {
            lvl++;
            numberProjectiles++;
            size+=3;
        } else if (lvl == 4) {
            lvl++;
            damage+=20;
            cooldown-=2;
        } else if (lvl == 5) {
            lvl++;
            numberProjectiles++;
            size+=5;
        } else {
            lvl++;
            damage+=5;
        }
    }
}

