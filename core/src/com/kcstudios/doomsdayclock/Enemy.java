package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

abstract class Enemy {
    protected float X;
    protected float Y;
    protected float speed;
    protected int hp;
    protected int damage;
    protected int xp;
    protected int id;
    protected int type;
    protected int frame;
    protected int size;
    protected Texture[] runSprites;
    protected Texture currentSprite;

    public int getId() {
        return id;
    }

    public float getX() {
        return X;
    }

    public void setX(float X) {
        this.X = X;
    }

    public float getY() {
        return Y;
    }

    public void setY(float X) {
        this.X = X;
    }

    public float getSpeed() {
        return speed;
    }

    abstract public int getHp();

    abstract public void setHp(int hp);

    abstract public int getDamage();

    abstract public Texture getCurrentSprite(boolean nextFrame);

    public abstract void move(float deltaVx, float deltaVy);

    public abstract int getXP();

    public int getSize() {
        return size;
    }
}


