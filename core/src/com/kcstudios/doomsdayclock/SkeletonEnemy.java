package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class SkeletonEnemy extends Enemy {

    SkeletonEnemy(int id, float x, float y) {
        this.hp = 21;
        this.id = id;
        this.X = x;
        this.Y = y;
        this.xp = 1;
        this.type = 1;
        this.damage = 5;
        this.speed = .5F;
        this.frame = 0;
        this.currentSprite = new Texture("SkeletonWalk/tile000.png");
        this.runSprites = new Texture[13];
        this.runSprites[0] = new Texture("SkeletonWalk/tile000.png");
        this.runSprites[1] = new Texture("SkeletonWalk/tile001.png");
        this.runSprites[2] = new Texture("SkeletonWalk/tile002.png");
        this.runSprites[3] = new Texture("SkeletonWalk/tile003.png");
        this.runSprites[4] = new Texture("SkeletonWalk/tile004.png");
        this.runSprites[5] = new Texture("SkeletonWalk/tile005.png");
        this.runSprites[6] = new Texture("SkeletonWalk/tile006.png");
        this.runSprites[7] = new Texture("SkeletonWalk/tile007.png");
        this.runSprites[8] = new Texture("SkeletonWalk/tile008.png");
        this.runSprites[9] = new Texture("SkeletonWalk/tile009.png");
        this.runSprites[10] = new Texture("SkeletonWalk/tile010.png");
        this.runSprites[11] = new Texture("SkeletonWalk/tile011.png");
        this.runSprites[12] = new Texture("SkeletonWalk/tile012.png");
    }

    @Override
    public int getHp() {
        return 0;
    }

    @Override
    public void setHp(int hp) {

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
