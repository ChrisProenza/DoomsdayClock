package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Player {
    private float X;
    private float Y;
    private int hp;
    private int lvl;
    private int xp;
    private int swordDamage;
    private double speed;
    private final Map<Integer, Skill> projectiles;
    private Texture[] runSprites;
    private Texture[] attackSprites;
    private Texture[] rollSprites;
    private Texture[] deathSprites;
    private Texture currentSprite;
    private int frame;
    private int state;


    Player() {
        speed = 1.5;
        swordDamage = 5;
        lvl = 1;
        xp = 0;
        projectiles = new HashMap<>();
        runSprites = new Texture[8];
        runSprites[0] = new Texture("KnightRun/tile000.png");
        runSprites[1] = new Texture("KnightRun/tile001.png");
        runSprites[2] = new Texture("KnightRun/tile002.png");
        runSprites[3] = new Texture("KnightRun/tile003.png");
        runSprites[4] = new Texture("KnightRun/tile004.png");
        runSprites[5] = new Texture("KnightRun/tile005.png");
        runSprites[6] = new Texture("KnightRun/tile006.png");
        runSprites[7] = new Texture("KnightRun/tile007.png");
        attackSprites = new Texture[7];
        attackSprites[0] = new Texture("Knight3Hit/tile001.png");
        attackSprites[1] = new Texture("Knight3Hit/tile007.png");
        attackSprites[2] = new Texture("Knight3Hit/tile008.png");
        attackSprites[3] = new Texture("Knight3Hit/tile009.png");
        attackSprites[4] = new Texture("Knight3Hit/tile010.png");
        attackSprites[5] = new Texture("Knight3Hit/tile011.png");
        attackSprites[6] = new Texture("Knight3Hit/tile012.png");
        new Texture("Knight3Hit/tile021.png");
        rollSprites = new Texture[15];
        rollSprites[0] = new Texture("KnightRoll/tile000.png");
        rollSprites[1] = new Texture("KnightRoll/tile001.png");
        rollSprites[2] = new Texture("KnightRoll/tile002.png");
        rollSprites[3] = new Texture("KnightRoll/tile003.png");
        rollSprites[4] = new Texture("KnightRoll/tile004.png");
        rollSprites[5] = new Texture("KnightRoll/tile005.png");
        rollSprites[6] = new Texture("KnightRoll/tile006.png");
        rollSprites[7] = new Texture("KnightRoll/tile007.png");
        rollSprites[8] = new Texture("KnightRoll/tile008.png");
        rollSprites[9] = new Texture("KnightRoll/tile009.png");
        rollSprites[10] = new Texture("KnightRoll/tile010.png");
        rollSprites[11] = new Texture("KnightRoll/tile011.png");
        rollSprites[12] = new Texture("KnightRoll/tile012.png");
        rollSprites[13] = new Texture("KnightRoll/tile013.png");
        rollSprites[14] = new Texture("KnightRoll/tile014.png");
        deathSprites = new Texture[15];
        deathSprites[0] = new Texture("KnightDeath/tile000.png");
        deathSprites[1] = new Texture("KnightDeath/tile001.png");
        deathSprites[2] = new Texture("KnightDeath/tile002.png");
        deathSprites[3] = new Texture("KnightDeath/tile003.png");
        deathSprites[4] = new Texture("KnightDeath/tile004.png");
        deathSprites[5] = new Texture("KnightDeath/tile005.png");
        deathSprites[6] = new Texture("KnightDeath/tile006.png");
        deathSprites[7] = new Texture("KnightDeath/tile007.png");
        deathSprites[8] = new Texture("KnightDeath/tile008.png");
        deathSprites[9] = new Texture("KnightDeath/tile009.png");
        deathSprites[10] = new Texture("KnightDeath/tile010.png");
        deathSprites[11] = new Texture("KnightDeath/tile011.png");
        deathSprites[12] = new Texture("KnightDeath/tile012.png");
        deathSprites[13] = new Texture("KnightDeath/tile013.png");
        deathSprites[14] = new Texture("KnightDeath/tile014.png");
        state = 0;
        frame = 0;
        X = 0;
        Y = 0;
        hp = 100;
        currentSprite = attackSprites[0];
    }

    public float getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void move (float dVx, float dVy) {
        X = X + dVx;
        Y = Y + dVy;
    }

    public Texture getCurrentSprite(boolean nextFrame) {
        if (nextFrame) {
            if (state == 1) {
                if (frame > runSprites.length - 1)
                    frame = 0;
                currentSprite = runSprites[frame];
                frame++;
            } else if (state == 2) {
                if (frame > attackSprites.length - 1) {
                    frame = 0;
                    state = 0;
                }
                currentSprite = attackSprites[frame];
                frame++;
            } else if (state == 3) {
                currentSprite = deathSprites[frame];
                if (!(frame > deathSprites.length-2))
                    frame++;
                else
                    frame = 14;
            } else {
                frame = 0;
                currentSprite = attackSprites[frame];
            }
        }
        return currentSprite;
    }

    public int getFrame() {
        return frame;
    }

    public double getSpeed() {
        return speed;
    }

    public int getSwordDamage() {
        Random random = new Random();
        return random.nextInt(swordDamage + 1);
    }

    public void addXP(int xp) {
        if (xp > (lvl * 20)) {
            lvl++;
        }
    }

    public int getXP() {
        return xp;
    }

    public void setXP(int xp) {
        this.xp = xp;
    }

    public Map<Integer, Skill> getProjectiles() {
        return projectiles;
    }

    public void addProjectile(Skill skill) {
        projectiles.put(skill.id, skill);
    }
}
