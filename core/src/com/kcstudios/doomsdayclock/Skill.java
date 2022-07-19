package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

abstract class Skill {
    protected int damage;
    protected int speed;
    protected float deltaRotation;
    protected int lvl;
    protected double duration;
    protected double lastTick;
    protected int cooldown;
    protected int size;
    protected int numberProjectiles;
    protected Texture texture;
    protected boolean passThrough;
    protected int id;
    protected double rotation;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getLvl() {
        return lvl;
    }

    abstract void lvlUp();


    public double getLastTick() {
        return lastTick;
    }

    public void setLastTick(double lastTick) {
        this.lastTick = lastTick;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isPassThrough() {
        return passThrough;
    }

    public int getNumberProjectiles() {
        return numberProjectiles;
    }

    public int getId() {
        return id;
    }

    public float getRotation() {
        return (float)rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }


    public float getDeltaRotation() {
        return deltaRotation;
    }

    public void setDeltaRotation(float deltaRotation) {
        this.deltaRotation = deltaRotation;
    }
}
