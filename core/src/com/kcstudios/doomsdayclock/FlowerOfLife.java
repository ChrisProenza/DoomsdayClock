package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class FlowerOfLife extends Skill {
    FlowerOfLife() {
        lvl = 1;
        speed = 0;
        damage = 50;
        duration = 1;
        numberProjectiles = 1;
        size = 50;
        cooldown = 45;
        lastTick = -cooldown - .1;
        texture = new Texture("animations/FlowerOfLife.png");
        passThrough = true;
        id = 4;
        deltaRotation = (float) Math.toRadians(0);
        knockback = 0;
    }

    @Override
    void lvlUp() {
        if(lvl == 1) {
            lvl++;
            duration++;
            damage+=25;
        } else if (lvl == 2) {
            lvl++;
            duration++;
            size+=10;
        } else if (lvl == 3) {
            lvl++;
            duration++;
            damage+=25;
        } else if (lvl == 4) {
            lvl++;
            duration++;
            size+=10;
        } else if (lvl == 5) {
            lvl++;
            duration++;
            damage+=25;
        } else {
            lvl++;
            duration++;
            size+=5;
        }
    }
}

