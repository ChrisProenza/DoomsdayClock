package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

abstract class Enemy {
    protected int X;
    protected int Y;
    protected int hp;
    protected int damage;
    protected int id;
    protected int frame;
    protected Texture[] runSprites;
    protected Texture currentSprite;

    public int getId() {
        return id;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    abstract public int getHp();

    abstract public void setHp(int hp);

    abstract public int getDamage();

    abstract public Texture getCurrentSprite(boolean nextFrame);

    public abstract void move(int deltaVx, int deltaVy);
}


