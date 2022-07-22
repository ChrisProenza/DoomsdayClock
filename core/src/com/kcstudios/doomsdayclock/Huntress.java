package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class Huntress extends Enemy {

    Huntress(int id, float x, float y, int wave) {
        this.hp = 5 * wave;
        this.id = id;
        this.X = x;
        this.Y = y;
        this.xp = 2 * wave;
        this.type = 12;
        this.damage = 2 * wave;
        this.speed = 2F * wave;
        this.frame = 0;
        this.runSprites = EnemyTextureLoader.getInstance().huntressRunSprites;
        this.currentSprite = runSprites[0];
        this.size = 1;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public Texture getCurrentSprite(boolean nextFrame) {
        if (nextFrame) {
            if (frame > runSprites.length - 1)
                frame = 0;
            currentSprite = runSprites[frame];
            frame++;
        }
        return currentSprite;
    }

    public void move(float deltaVx, float deltaVy) {
        this.X += deltaVx;
        this.Y += deltaVy;
    }

    public int getType() {
        return type;
    }

    public int getXP() {
        return xp;
    }
}
