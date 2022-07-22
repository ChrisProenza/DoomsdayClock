package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.graphics.Texture;

public class EnemyTextureLoader {
    Texture[] skeletonWarriorRunSprites;
    Texture[] skeletonRunSprites;
    Texture[] evileyeRunSprites;
    Texture[] deathRunSprites;
    Texture[] goblinRunSprites;
    Texture[] huntressRunSprites;

    private static EnemyTextureLoader enemyTextureLoader = null;
    public static EnemyTextureLoader getInstance() {
        if (enemyTextureLoader == null) {
            enemyTextureLoader = new EnemyTextureLoader();
        }
        return enemyTextureLoader;
    }

    private EnemyTextureLoader() {
        // Skeleton Textures
        this.skeletonRunSprites = new Texture[13];
        this.skeletonRunSprites[0] = new Texture("SkeletonWalk/tile000.png");
        this.skeletonRunSprites[1] = new Texture("SkeletonWalk/tile001.png");
        this.skeletonRunSprites[2] = new Texture("SkeletonWalk/tile002.png");
        this.skeletonRunSprites[3] = new Texture("SkeletonWalk/tile003.png");
        this.skeletonRunSprites[4] = new Texture("SkeletonWalk/tile004.png");
        this.skeletonRunSprites[5] = new Texture("SkeletonWalk/tile005.png");
        this.skeletonRunSprites[6] = new Texture("SkeletonWalk/tile006.png");
        this.skeletonRunSprites[7] = new Texture("SkeletonWalk/tile007.png");
        this.skeletonRunSprites[8] = new Texture("SkeletonWalk/tile008.png");
        this.skeletonRunSprites[9] = new Texture("SkeletonWalk/tile009.png");
        this.skeletonRunSprites[10] = new Texture("SkeletonWalk/tile010.png");
        this.skeletonRunSprites[11] = new Texture("SkeletonWalk/tile011.png");
        this.skeletonRunSprites[12] = new Texture("SkeletonWalk/tile012.png");
        // Evileye Textures
        this.evileyeRunSprites = new Texture[8];
        this.evileyeRunSprites[0] = new Texture("EvilEye/tile000.png");
        this.evileyeRunSprites[1] = new Texture("EvilEye/tile001.png");
        this.evileyeRunSprites[2] = new Texture("EvilEye/tile002.png");
        this.evileyeRunSprites[3] = new Texture("EvilEye/tile003.png");
        this.evileyeRunSprites[4] = new Texture("EvilEye/tile004.png");
        this.evileyeRunSprites[5] = new Texture("EvilEye/tile005.png");
        this.evileyeRunSprites[6] = new Texture("EvilEye/tile006.png");
        this.evileyeRunSprites[7] = new Texture("EvilEye/tile007.png");
        // Death Textures
        this.deathRunSprites = new Texture[13];
        this.deathRunSprites[0] = new Texture("Death/tile000.png");
        this.deathRunSprites[1] = new Texture("Death/tile001.png");
        this.deathRunSprites[2] = new Texture("Death/tile002.png");
        this.deathRunSprites[3] = new Texture("Death/tile003.png");
        this.deathRunSprites[4] = new Texture("Death/tile004.png");
        this.deathRunSprites[5] = new Texture("Death/tile005.png");
        this.deathRunSprites[6] = new Texture("Death/tile006.png");
        this.deathRunSprites[7] = new Texture("Death/tile007.png");
        this.deathRunSprites[8] = new Texture("Death/tile008.png");
        this.deathRunSprites[9] = new Texture("Death/tile009.png");
        this.deathRunSprites[10] = new Texture("Death/tile010.png");
        this.deathRunSprites[11] = new Texture("Death/tile011.png");
        this.deathRunSprites[12] = new Texture("Death/tile012.png");
        // Skeleton Warrior Textures
        this.skeletonWarriorRunSprites = new Texture[6];
        this.skeletonWarriorRunSprites[0] = new Texture("SkeletonWarrior/walk_1.png");
        this.skeletonWarriorRunSprites[1] = new Texture("SkeletonWarrior/walk_2.png");
        this.skeletonWarriorRunSprites[2] = new Texture("SkeletonWarrior/walk_3.png");
        this.skeletonWarriorRunSprites[3] = new Texture("SkeletonWarrior/walk_4.png");
        this.skeletonWarriorRunSprites[4] = new Texture("SkeletonWarrior/walk_5.png");
        this.skeletonWarriorRunSprites[5] = new Texture("SkeletonWarrior/walk_6.png");
        // Goblin Textures
        this.goblinRunSprites = new Texture[8];
        this.goblinRunSprites[0] = new Texture("Goblin/tile000.png");
        this.goblinRunSprites[1] = new Texture("Goblin/tile001.png");
        this.goblinRunSprites[2] = new Texture("Goblin/tile002.png");
        this.goblinRunSprites[3] = new Texture("Goblin/tile003.png");
        this.goblinRunSprites[4] = new Texture("Goblin/tile004.png");
        this.goblinRunSprites[5] = new Texture("Goblin/tile005.png");
        this.goblinRunSprites[6] = new Texture("Goblin/tile006.png");
        this.goblinRunSprites[7] = new Texture("Goblin/tile007.png");
        // Goblin Textures
        this.huntressRunSprites = new Texture[8];
        this.huntressRunSprites[0] = new Texture("LaHuntress/tile000.png");
        this.huntressRunSprites[1] = new Texture("LaHuntress/tile001.png");
        this.huntressRunSprites[2] = new Texture("LaHuntress/tile002.png");
        this.huntressRunSprites[3] = new Texture("LaHuntress/tile003.png");
        this.huntressRunSprites[4] = new Texture("LaHuntress/tile004.png");
        this.huntressRunSprites[5] = new Texture("LaHuntress/tile005.png");
        this.huntressRunSprites[6] = new Texture("LaHuntress/tile006.png");
        this.huntressRunSprites[7] = new Texture("LaHuntress/tile007.png");
    }
}
