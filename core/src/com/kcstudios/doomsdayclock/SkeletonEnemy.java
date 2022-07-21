package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class SkeletonEnemy extends Enemy {

    SkeletonEnemy(int id, float x, float y, int wave) {
        this.hp = wave;
        this.id = id;
        this.X = x;
        this.Y = y;
        this.xp = wave;
        this.type = 1;
        this.damage = 5 * wave/2;
        this.speed = .5F * wave/2;
        this.frame = 0;
        this.runSprites = EnemyTextureLoader.getInstance().skeletonRunSprites;
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
