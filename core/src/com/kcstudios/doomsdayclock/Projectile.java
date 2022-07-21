package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class Projectile {
    protected int id;
    protected int damage;
    protected double speed;
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
    protected double activationTime;
    protected double lastDamageTick;
    protected int knockback;

    Projectile(Skill skill, float x, float y, int i, double angle, double activation) {
        damage = skill.getDamage();
        speed = skill.getSpeed();
        X = x;
        Y = y;
        duration = skill.getDuration();
        size = skill.getSize();
        texture = skill.getTexture();
        passThrough = skill.isPassThrough();
        if(skill.getId() == 2 || skill.getId() ==  3) {
            rotation = Math.toRadians(360/skill.getNumberProjectiles() * (i));
        } else
            rotation = -angle;
        if(skill.id == 0) {
            xOffset = (float) (2 * Math.sin(Math.toRadians(90) * i) * Math.cos(rotation));
            X = (float) (x + (4 * i * Math.cos(rotation)));
            yOffset = (float) (2 * Math.cos(Math.toRadians(90) * i) * Math.sin(rotation));
            Y = (float) (y - (4 * i * Math.sin(rotation)));
        } else if (skill.id == 1){
            xOffset = (float) (10 * Math.cos(
                    Math.toRadians(360/(skill.getNumberProjectiles())) * (i)));
            yOffset = (float) (10 * Math.sin(
                    Math.toRadians(360/(skill.getNumberProjectiles())) * (i)));
        } else {
            xOffset = (float) (2 * i * Math.cos(rotation));
            yOffset = (float) (2 * i * Math.sin(rotation));
        }
        rotationSpeed = skill.getDeltaRotation();
        this.id = skill.id;
        lastDamageTick = -1;
        activationTime = activation;
        knockback = skill.getKnockback();
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
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

    public int getID() {
        return id;
    }

    public void setLastDamageTick(double lastDamageTick) {
        this.lastDamageTick = lastDamageTick;
    }

    public double getLastDamageTick() {
        return lastDamageTick;
    }

    public int getKnockback() {
        return knockback;
    }
}
