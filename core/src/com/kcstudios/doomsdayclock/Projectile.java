package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class Projectile {
    protected int id;
    protected int damage;
    protected int speed;
    protected float X;
    protected float Y;
    protected float xOffset = 0;
    protected float yOffset = 0;
    protected double duration;
    protected int size;
    protected Texture texture;
    protected boolean passThrough;
    protected double rotation;
    protected float rotationSpeed;
    protected float activationTime;

    Projectile(Skill skill, float x, float y, int i, double angle) {
        damage = skill.getDamage();
        speed = skill.getSpeed();
        X = x;
        Y = y;
        duration = skill.getDuration();
        size = skill.getSize();
        texture = skill.getTexture();
        passThrough = skill.isPassThrough();
        rotation = -angle;
        if(skill.id != 1) {
            xOffset = (float) (2 * i * Math.cos(rotation));
            yOffset = (float) (2 * i * Math.sin(rotation));
        }
        rotationSpeed = skill.getDeltaRotation();
        activationTime = 0;
        this.id = skill.id;
    }

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

    public float getXOnly() {
        return X;
    }

    public float getYOnly() {
        return Y;
    }

    public float getX() {
        return (float) (X + yOffset);
    }

    public float getY() {
        return (float) (Y + xOffset);
    }

    public void setX(float x) {
        X = x;
    }

    public void setY(float y) {
        Y = y;
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

    public Texture getTexture() {
        return texture;
    }

    public boolean isPassThrough() {
        return passThrough;
    }


    public float getRotation() {
        rotation+=rotationSpeed;
        return (float)rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(float activationTime) {
        this.activationTime = activationTime;
    }

    public int getID() {
        return id;
    }
}
