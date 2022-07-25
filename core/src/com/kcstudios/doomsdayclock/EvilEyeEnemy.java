package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class EvilEyeEnemy extends Enemy {

    EvilEyeEnemy(int id, float x, float y, int wave) {
        wave += 1;
        this.hp = 10 * wave;
        this.id = id;
        this.X = x;
        this.Y = y;
        this.xp = 2 * wave;
        this.type = 2;
        this.damage = 5 * wave;
        this.speed = .5F * wave;
        this.frame = 0;
        this.runSprites = EnemyTextureLoader.getInstance().evileyeRunSprites;
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
