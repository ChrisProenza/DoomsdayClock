package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.util.Random;

public class DoomsdayClock extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	SpriteBatch batch;

	boolean inMenu = true;
	boolean lvlUp = false;
	boolean stageSelect = false;
	boolean setPaused = false;

	double elapsedTime = 0;
	// Spawn triggers
	int triggerSkeletons = 0;
	int triggerTwo = 0;
	int triggerThree = 0;
	int triggerFour = 0;
	int triggerFive = 0;
	int triggerBoss = 0;

	// Player info
	Player player;
	float deltaVx;
	float deltaVy;
	float playerAngle = (float) Math.toRadians(180);
	double playerDeltaTime;
	double playerDamageCD = 0;
	double enemyDeltaTime;
	double playerAttackDeltaTime;
	double healTick;
	double lastHit = -.5;
	int playerHit = 0;
	int skillSelection = 0;
	// Enemy info
	List<Enemy> enemies = new ArrayList<Enemy>();
	int enemyID = 0;
	double frameRenderDelta = 0;
	// Camera
	private Camera camera;
	private Viewport viewport;
	// Stage Information
	private final int StageWidth = 20;
	private final int StageHeight = 20;
	// Background
	private Texture background;
	private Texture menuScreen;
	private Texture grassTile;
	private Texture horizontalWall;
	private Texture verticalWall;
	private Texture pillarTile;
	private Texture pauseButton;
	private Texture confirmButton;
	private Texture selectionBox;
	private Map<Integer, Texture> skillTextures;
	private Map<Integer, Sound> weaponSounds;
	private Map<Integer, Sound> enemySounds;
	private Map<Integer, Sound> enemyDeathSounds;
	private Map<Integer, Double> enemySoundDelay;
	private List<Integer> skillList;
	private Touchpad touchpad;
	private int backgroundX = 0;
	private int backgroundY = 0;
	private Map<LocationPair, Texture> environment;
	private Map<String, Boolean> obstructionMap;
	// Animations
	private Texture swordSwing;
	class touchInfo {
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
	}
	// Sounds
	private Music menuTheme;
	private Music stage001;
	private Sound playerAttackSound;
	private Sound playerDeathSound;
	private Sound levelUpSound;
	private Sound deathSpawn;

	private final Map<Integer, touchInfo> touches = new HashMap<Integer, touchInfo>();
	private final List<Projectile> projectiles = new ArrayList<>();

	//----- App Functions -----

	@Override
	public void create () {
		Gdx.graphics.setVSync(false);
		Gdx.graphics.getFramesPerSecond();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FillViewport(200, 200, camera);
		menuScreen = new Texture("backgrounds/MainMenuScreen.png");
		background = new Texture("backgrounds/Background.png");
		grassTile = new Texture("Environment/GrassTile.png");
		horizontalWall = new Texture("Environment/Wall.png");
		verticalWall = new Texture("Environment/SideWall.png");
		pillarTile = new Texture("Environment/Pillar.png");
		pauseButton = new Texture("buttons/PauseButton.png");
		confirmButton = new Texture("buttons/ConfirmButton.png");
		selectionBox = new Texture("buttons/SelectionBox.png");
		swordSwing = new Texture("animations/SliceAnimation.png");
		skillTextures = new HashMap<>();
		skillTextures.put(0, new Texture("buttons/PlayerSkill.png"));
		skillTextures.put(1, new Texture("buttons/PlayerSkill1.png"));
		skillTextures.put(2, new Texture("buttons/PlayerSkill2.png"));
		skillTextures.put(3, new Texture("buttons/Skill0.png"));
		skillTextures.put(4, new Texture("buttons/Skill1.png"));
		skillTextures.put(5, new Texture("buttons/Skill2.png"));
		skillTextures.put(6, new Texture("buttons/Skill3.png"));
		skillTextures.put(7, new Texture("animations/FlowerOfLife.png"));
		backgroundX = 0;
		backgroundY = 0;
		environment = new HashMap<>();
		obstructionMap = new HashMap<String, Boolean>();
		generateMap();
		Gdx.input.setInputProcessor(this);
		for (int i = 0; i < 5; i++) {
			touches.put(i, new touchInfo());
		}
		player = new Player();
		// Sounds
		menuTheme = Gdx.audio.newMusic(Gdx.files.internal("sounds/MainTheme.mp3"));
		stage001 = Gdx.audio.newMusic(Gdx.files.internal("sounds/stage001.mp3"));
		menuTheme.setVolume(.5F);
		menuTheme.play();
		playerAttackSound = Gdx.audio.newSound(Gdx.files.internal("sounds/PlayerAttack.mp3"));
		playerDeathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/PlayerDeath.mp3"));
		deathSpawn = Gdx.audio.newSound(Gdx.files.internal("sounds/DeathSpawn.mp3"));
		levelUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/LevelUp!.mp3"));
		enemySoundDelay = new HashMap<>();
		enemySoundDelay.put(1, (double) 0);
		enemySoundDelay.put(2, (double) 0);
		enemySoundDelay.put(12, (double) 0);
		enemySoundDelay.put(42, (double) 0);
		enemySoundDelay.put(69, (double) 0);
		enemySoundDelay.put(111, (double) 0);
		enemySounds = new HashMap<>();
		enemySounds.put(1, Gdx.audio.newSound(Gdx.files.internal("sounds/SkeletonSound.mp3")));
		enemySounds.put(2, Gdx.audio.newSound(Gdx.files.internal("sounds/EyeAttack.mp3")));
		enemySounds.put(12, Gdx.audio.newSound(Gdx.files.internal("sounds/HuntressAttack.mp3")));
		enemySounds.put(42, Gdx.audio.newSound(Gdx.files.internal("sounds/GoblinSound.mp3")));
		enemySounds.put(69, Gdx.audio.newSound(Gdx.files.internal("sounds/SkeletonWarriorAttack.mp3")));
		enemyDeathSounds = new HashMap<>();
		enemyDeathSounds.put(2, Gdx.audio.newSound(Gdx.files.internal("sounds/EyeDeath.mp3")));
		enemyDeathSounds.put(12, Gdx.audio.newSound(Gdx.files.internal("sounds/HuntressDeath.mp3")));
		enemyDeathSounds.put(42, Gdx.audio.newSound(Gdx.files.internal("sounds/GoblinDeath.mp3")));
		enemyDeathSounds.put(69, Gdx.audio.newSound(Gdx.files.internal("sounds/SkeletonWarriorDeath.mp3")));
		weaponSounds = new HashMap<>();
		weaponSounds.put(0, Gdx.audio.newSound(Gdx.files.internal("sounds/DaggerSound.mp3")));
		weaponSounds.put(1, Gdx.audio.newSound(Gdx.files.internal("sounds/DaggerSound.mp3")));
		weaponSounds.put(2, Gdx.audio.newSound(Gdx.files.internal("sounds/SpectralSound.mp3")));
		weaponSounds.put(3, Gdx.audio.newSound(Gdx.files.internal("sounds/AxeSound.mp3")));
		weaponSounds.put(4, Gdx.audio.newSound(Gdx.files.internal("sounds/FlowerSound.mp3")));
	}

	@Override public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		if (inMenu) {
			batch.begin();
			drawMenu();
			batch.end();
			frameRenderDelta = 0;
		} else {
			if (lvlUp) {
				batch.begin();
				drawBackground();
				drawEnemies();
				drawPlayer();
				drawForeground();
				drawUI();
				batch.end();
				frameRenderDelta = 0;
			} else if(setPaused) {
				batch.begin();
				drawBackground();
				drawEnemies();
				drawPlayer();
				drawForeground();
				drawUI();
				batch.end();
				frameRenderDelta = 0;
			} else {
				batch.begin();
				frameRenderDelta += Gdx.graphics.getDeltaTime();
				playerDeltaTime += Gdx.graphics.getDeltaTime();
				enemyDeltaTime += Gdx.graphics.getDeltaTime();
				elapsedTime += Gdx.graphics.getDeltaTime();
				spawnEnemy(elapsedTime);
				if(elapsedTime - healTick > 2) {
					player.heal();
					healTick = elapsedTime;
				}
				if (player.getState() == 2) {
					playerAttackDeltaTime += Gdx.graphics.getDeltaTime();
				}
				else {
					playerAttackDeltaTime = 0;
				}
				if (frameRenderDelta > .033) {
					if (player.getState() == 1) {
						// Check boundaries
						if (deltaVx + player.getX() < -1000 || deltaVx + player.getX() > 1000
								|| detectMovementCollisionX(player, deltaVx)) {
							deltaVx = 0;
						}
						if (deltaVy + player.getY() < -1000 || deltaVy + player.getY() > 1000
								|| detectMovementCollisionY(player, deltaVy)) {
							deltaVy = 0;
						}
						if(player.getX() > 999) {
							player.setX(999);
						}
						if(player.getX() < -999) {
							player.setX(-999);
						}
						if(player.getY() > 999) {
							player.setY(999);

						}
						if(player.getY() < -999) {
							player.setY(-999);
						}
						player.move(deltaVx, deltaVy);
					}
					moveEnemy();
					if (player.getState() != 3 && (elapsedTime - lastHit) > .5) {
						detectEnemyCollision();
					}
					playerProjectiles();
					projectileCollision();
					frameRenderDelta = 0;
				}
				if (player.getState() != 3) {
					drawBackground();
					drawEnemies();
				}
				drawProjectiles();
				drawPlayer();
				if (player.getState() != 3)
					drawForeground();
				drawUI();
				batch.end();
			}
		}
	}


	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	//----- UI Functions -----

	@Override
	public boolean keyDown(int key) {
		return false;
	}
	@Override
	public boolean keyUp(int key) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer < 5) {
			touches.get(pointer).touchX = screenX;
			touches.get(pointer).touchY = screenY;
			touches.get(pointer).touched = true;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (inMenu) {
			menuTheme.stop();
			deathSpawn.play();
			stage001.setVolume(.5F);
			stage001.play();
			inMenu = false;
		} else {
			if (!setPaused) {
				if ((Gdx.graphics.getHeight() * .1) < screenY
						|| (Gdx.graphics.getWidth() * .9 > screenX)) {
					if (player.getState() != 3) {
						if (player.getState() != 2 && touches.get(pointer).touchX > Gdx.graphics.getWidth()/2) {
							player.setState(2);
							playerAttackSound.play();
							playerAttackDeltaTime = 0;
						}
						if (player.getState() == 1) {
							player.setState(0);
						}
					} else {
						restart();
					}
				} else {
					player.setState(0);
					deltaVx = 0;
					deltaVy = 0;
					setPaused = true;
					stage001.pause();
				}
			} else {
				setPaused = false;
				stage001.play();
			}
			if (lvlUp) {
				if (screenX > Gdx.graphics.getWidth() * .4
						&& screenX < Gdx.graphics.getWidth() * .6
						&& screenY > Gdx.graphics.getHeight() * .8
						&& skillSelection != 0) {
					lvlUp = false;
					int skill = skillList.get(skillSelection - 1);
					if (skill == 0) {
						player.setHealing(player.getHealing() + 1);
					} else if (skill == 1) {
						player.setSpeed(player.getSpeed() + 1);
					} else if (skill == 2) {
						player.setSwordDamage(player.getSwordDamage() * 2);
					} else if (skill == 3) {
						if (player.getProjectiles().containsKey(0)) {
							player.getProjectiles().get(0).lvlUp();
						} else
							player.getProjectiles().put(0, new Dagger());
					} else if (skill == 4) {
						if (player.getProjectiles().containsKey(1)) {
							player.getProjectiles().get(1).lvlUp();
						} else
							player.getProjectiles().put(1, new SpinningKatana());
					} else if (skill == 5) {
						if (player.getProjectiles().containsKey(2)) {
							player.getProjectiles().get(2).lvlUp();
						} else
							player.getProjectiles().put(2, new SpinningBlades());
					} else if (skill == 6) {
						if (player.getProjectiles().containsKey(3)) {
							player.getProjectiles().get(3).lvlUp();
						} else {
							player.getProjectiles().put(3, new BoomerangAxe());
						}
					} else if (skill == 7) {
						if (player.getProjectiles().containsKey(4)) {
							player.getProjectiles().get(4).lvlUp();
						} else {
							player.getProjectiles().put(4, new FlowerOfLife());
						}
					}
				}
				if (screenX > Gdx.graphics.getWidth() * .4
						&& screenX < Gdx.graphics.getWidth() * .6
						&& screenY < Gdx.graphics.getHeight() * .8
						&& screenY > Gdx.graphics.getHeight() * .6) {
					skillSelection = 1;
				} else if (screenX > Gdx.graphics.getWidth() * .4
						&& screenX < Gdx.graphics.getWidth() * .6
						&& screenY < Gdx.graphics.getHeight() * .6
						&& screenY > Gdx.graphics.getHeight() * .4) {
					skillSelection = 2;
				} else if (screenX > Gdx.graphics.getWidth() * .4
						&& screenX < Gdx.graphics.getWidth() * .6
						&& screenY < Gdx.graphics.getHeight() * .4
						&& screenY > Gdx.graphics.getHeight() * .2) {
					skillSelection = 3;
				} else {
					skillSelection = 0;
				}
			}
		}
		if (pointer < 5) {
			touches.get(pointer).touchX = 0;
			touches.get(pointer).touchY = 0;
			touches.get(pointer).touched = false;
		}
		return true;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (player.getState() != 2 && !setPaused && (touches.get(pointer).touchX < Gdx.graphics.getWidth()/2)) {
			if ((Gdx.graphics.getHeight() * .1) < touches.get(pointer).touchY
					|| (Gdx.graphics.getWidth() * .9 > touches.get(pointer).touchX)) {
				if ((Math.abs(touches.get(pointer).touchX - screenX) > 10
						|| Math.abs(touches.get(pointer).touchY - screenY) > 10)
						&& player.getState() != 3) {
					double deltaY = screenY - touches.get(pointer).touchY;
					double deltaX = screenX - touches.get(pointer).touchX;
					playerAngle = (float) Math.atan2(deltaY, deltaX);
					deltaVx = (float) (player.getSpeed() * Math.cos(playerAngle));
					deltaVy = (float) (player.getSpeed() * Math.sin(playerAngle));
					// Set player is moving
					player.setState(1);
				}
			} else {
				deltaVy = 0;
				deltaVx = 0;
				player.setState(0);
			}
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	//----- Draw Functions -----

	private void generateMap() {
		Random random = new Random();
		for(int x = 0; x < 10; x++) {
			for(int y = 0; y < 10; y++) {
				int randX = (random.nextInt(200) + x * 200) - 1000;
				int randY = (random.nextInt(200) + y * 200) - 1000;
				if (randX != 0 && randY != 0) {
					for (int i = randX / 5 - 3; i < randX / 5 + 3; i++) {
						for (int j = randY / 5 - 3; j < randY / 5 + 3; j++) {
							obstructionMap.put(i + "." + j, true);
						}
					}
					environment.put(new LocationPair(randX, randY), pillarTile);
				}
			}
		}
	}


	private void drawForeground() {
		for (LocationPair pair:environment.keySet()) {
			if(player.getY() < pair.Second) {
				batch.draw(environment.get(pair), (85 - (player.getX() - pair.First)),
						(85 + (player.getY() - pair.Second)), 30, 30);
			}
		}
		if (player.getY() > 850) {
			for(int x = -1; x < 11; x++) {
				batch.draw(horizontalWall, (backgroundX + x) * 20 - player.getX(),
						player.getY() - 900,
						20, 20);
			}
		}
	}

	private void drawMenu() {
		batch.draw(menuScreen, 0, 50, 200, 100);
		BitmapFont font = new BitmapFont();
		font.getData().setScale((float) .5, (float) .5);
		font.draw(batch, "Tap to start...", 75, 60);
	}
	// Draws the background image
	public void drawBackground() {
		backgroundX = (int) (player.getX() / 20);
		backgroundY = (int) -(player.getY() / 20);
		for(int x = -1; x < 11; x++) {
			for(int y = -1; y < 11; y++) {
				batch.draw(grassTile, (backgroundX + x) * 20 - player.getX(), (backgroundY + y) * 20 + player.getY(),
						20, 20);
			}
		}
		if(player.getX() - 50 < -1000) {
			for(int y = -1; y < 11; y++) {
				batch.draw(verticalWall, -902 - player.getX(),
						(backgroundY + y) * 20 + player.getY(),
						2, 20);
			}
		} else if (player.getX() + 150 > 1000) {
			for(int y = -1; y < 11; y++) {
				batch.draw(verticalWall, 1102 - player.getX(),
						(backgroundY + y) * 20 + player.getY(),
						2, 20);
			}
		}
		for (LocationPair pair:environment.keySet()) {
			if (player.getY() > pair.Second) {
				batch.draw(environment.get(pair), (85 - (player.getX() - pair.First)),
						(85 + (player.getY() - pair.Second)), 30, 30);
			}
		}
		if(player.getY() < -850) {
			for(int x = -1; x < 11; x++) {
				batch.draw(horizontalWall, (backgroundX + x) * 20 - player.getX(),
						player.getY() + 1090,
						20, 20);
			}
		}

	}

	public void drawPlayer() {
		int state = player.getState();
		boolean nextFrame = false;
		if (playerDeltaTime > .066) {
			playerDeltaTime = 0;
			nextFrame = true;
		}
		if (player.getHp() <= 0 && player.getState() != 3) {
			playerDeathSound.play();
			player.setState(3);
		}
		Texture currentSprite = player.getCurrentSprite(nextFrame);
		if (state == 1) {
			batch.draw(currentSprite, 90, 90, 20, 20, 0, 0,
					96, 64, !(deltaVx > 0), false);
		} else if (state == 2) {
			if (playerAttackDeltaTime > .25) {
				playerManualAttack();
			}
			batch.draw(currentSprite, 90, 90, 20, 20, 0, 0,
					96, 64, !(deltaVx > 0), false);
		} else if (state == 3) {
			batch.draw(currentSprite, 90, 90, 20, 20, 0, 0,
					144, 64, !(deltaVx > 0), false);
		} else {
			batch.draw(currentSprite, 90, 90, 20, 20, 10, 0,
					96, 64, !(deltaVx > 0), false);
		}
	}

	public void drawEnemies() {
		boolean nextFrame = false;
		if (enemyDeltaTime > .066) {
			enemyDeltaTime = 0;
			nextFrame = true;
		}
		if (enemies.size() > 0) {
			for (Enemy enemy : enemies) {
				if(enemySounds.containsKey(enemy.type)
						&& elapsedTime - enemySoundDelay.get(enemy.type) > 3) {
					enemySoundDelay.put(enemy.type, elapsedTime);
					enemySounds.get(enemy.type).play();
				}
				Texture currentSprite = enemy.getCurrentSprite(nextFrame);
				int h = currentSprite.getHeight() * enemy.getSize();
				int w = currentSprite.getWidth() * enemy.getSize();
				if (player.getX() > enemy.getX())
					batch.draw(currentSprite, ((100 - w / 8) - (player.getX() - enemy.getX())),
							((100 - h / 8) + (player.getY() - enemy.getY())),
							(w / 4), (h / 4));
				else
					batch.draw(currentSprite, ((100 - w / 8) - (player.getX() - enemy.getX())),
							((100 - h / 8) + (player.getY() - enemy.getY())),
							(w / 4), (h / 4), 0, 0, (w/enemy.getSize()), (h/enemy.getSize()),
							true, false);
			}
		}
	}

	public void drawUI() {
		BitmapFont font = new BitmapFont();
		font.getData().setScale((float) .5, (float) .5);
		if (player.getState() != 3) {
			int minutes = (int) (10 - (elapsedTime / 60));
			int seconds = ((int) (60 - elapsedTime % 60));
			font.draw(batch, (int) (minutes) + " :" + seconds, 0, 150);
			font.setColor(Color.RED);
			font.draw(batch, "HP:" + player.getHp(), 0, 56);
			font.setColor(Color.GOLD);
			font.draw(batch, "Level:" + player.getLvl(), 170, 56);
			batch.draw(pauseButton, 180, 143, 20, 11);
			if (setPaused) {
				font.draw(batch, "Paused", 85, 120);
				font.draw(batch, "Tap to Unpause", 74, 80);
			}
			if(lvlUp) {
				batch.draw(confirmButton, 80, 46, 40, 20);
				int i = 1;
				for(Integer skill:skillList) {
					batch.draw(skillTextures.get(skill), 80, 46 + (20 * i),
							40, 20);
					i++;
				}
				if(skillSelection != 0) {
					batch.draw(selectionBox, 80, 46 + (20 * skillSelection),
							40, 20);
				}

				font.draw(batch, "LEVEL UP!!!", 80, 136);
			}
		} else {
			font.draw(batch, "Tap To Restart", 75, 120);
			//font.draw(batch, "Tap and hold to return to menu", 50, 80);
		}
	}

	private void drawProjectiles() {
		for (Projectile projectile:projectiles) {
			TextureRegion projectileTexture = new TextureRegion(projectile.getTexture());
			if (projectile.getID() == 2) {
				batch.draw(projectileTexture,
						((100 - projectile.getSize()) - (player.getX() - projectile.getXOnly())),
						((100 - projectile.getSize()) + (player.getY() - projectile.getYOnly())),
						projectile.getSize(),
						projectile.getSize(),
						projectile.getSize()*2, projectile.getSize()*2,
						1,
						1,
						(float) Math.toDegrees(projectile.getCurrentRotation())
				);
			} else {
				batch.draw(projectileTexture,
						((100 - projectile.getSize()) - (player.getX() - projectile.getX())),
						((100 - projectile.getSize()) + (player.getY() - projectile.getY())),
						projectile.getSize(),
						projectile.getSize(),
						projectile.getSize()*2, projectile.getSize()*2,
						1,
						1,
						(float) Math.toDegrees(projectile.getCurrentRotation())
				);
			}
		}
	}

	//----- Object movement and collisions -----

	// Move the enemies on map
	private void moveEnemy() {
		for (Enemy enemy: enemies) {

			float eDeltaVx = 0;
			float eDeltaVy = 0;
			double deltaY = player.getY() - enemy.getY();
			double deltaX = player.getX() - enemy.getX();

			if (Math.hypot(deltaX, deltaY) >= 2) {
				double angle = Math.atan2(deltaY, deltaX);
				eDeltaVx = (float)(enemy.getSpeed() * Math.cos(angle));
				if (detectMovementCollisionX(enemy, eDeltaVx)) {
					eDeltaVx = 0;
				}
				eDeltaVy = (float)(enemy.getSpeed() * Math.sin(angle));
				if (detectMovementCollisionY(enemy, eDeltaVy)) {
					eDeltaVy = 0;
				}
				if (eDeltaVx == 0 && eDeltaVy == 0) {
					eDeltaVx = 1;
					eDeltaVy = 1;
				}
				enemy.move(eDeltaVx, eDeltaVy);
			}
		}
	}

	// Spawn the various enemies
	private void spawnEnemy(double elapsedTime) {
		Random random = new Random();
		// Spawn increasing number of skeletons every 10 seconds
		if (elapsedTime > (triggerSkeletons * 10) && enemies.size() < 300) {
			triggerSkeletons++;
			for (int i = 0; i < (10 * Math.sqrt(triggerSkeletons)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean()) {
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				}
				else {
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				}
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
					Y = Y + player.getY();
				}
				enemies.add(new SkeletonEnemy(0, X, Y, triggerSkeletons/6));
			}
		}
		// Spawn increasing number of enemyTwo every 15 seconds
		if (elapsedTime > ((triggerTwo+1) * 15) && enemies.size() < 300) {
			triggerTwo++;
			for (int i = 0; i < (5 * Math.sqrt(triggerTwo)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean()) {
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				}
				else {
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				}
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
					Y = Y + player.getY();
				}
				enemies.add(new EvilEyeEnemy(1, X, Y, triggerTwo/4));
			}
		}
		// Spawn increasing number of enemyThree every 30 seconds
		if (elapsedTime > ((triggerThree+1) * 30) && enemies.size() < 300) {
			triggerThree++;
			for (int i = 0; i < (2 * Math.sqrt(triggerThree)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean()) {
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				}
				else {
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				}
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
					Y = Y + player.getY();
				}
				enemies.add(new SkeletonWarrior(2, X, Y, triggerThree/3));
			}
		}
		// Spawn increasing number of enemyFour every 20 seconds
		if (elapsedTime > ((triggerFour+1) * 30) && enemies.size() < 300) {
			triggerFour++;
			for (int i = 0; i < (2 * Math.sqrt(triggerFour)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean()) {
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				}
				else {
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				}
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
					Y = Y + player.getY();
				}
				enemies.add(new GoblinEnemy(2, X, Y, triggerFour/2));
			}
		}
		// Spawn increasing number of enemyFive every 60 seconds
		if (elapsedTime > ((triggerFive+1) * 25) && enemies.size() < 300) {
			triggerFive++;
			for (int i = 0; i < (2 * Math.sqrt(triggerFive)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean()) {
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				}
				else {
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				}
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
					Y = Y + player.getY();
				}
				enemies.add(new Huntress(2, X, Y, triggerFive));
			}
		}
		// Spawn Boss every 60 seconds
		if (elapsedTime > ((triggerBoss+1) * 60) && enemies.size() < 200) {
			triggerBoss++;
			for (int i = 0; i < triggerBoss; i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean()) {
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				}
				else
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
					Y = Y + player.getY();
				}
				enemies.add(new DeathEnemy(3, X, Y, triggerBoss));
			}
		}
	}

	// Get a random spawn
	private int getSpawn() {
		return (new Random()).nextInt(100);
	}

	// Detect enemy collision
	private void detectEnemyCollision() {
		double deltaX = 0;
		double deltaY = 0;
		double magnitude = 0;
		for (Enemy enemy:enemies) {
			deltaX = player.getX() - enemy.getX();
			deltaY = player.getY() - enemy.getY();
			magnitude = Math.hypot(deltaX, deltaY);
			if (magnitude < 5 * enemy.getSize()) {
				player.setHp(player.getHp() - enemy.getDamage());
				lastHit = elapsedTime;
				break;
			}
		}
	}

	// Generate projectiles
	private void playerProjectiles() {
		for(Skill skill : player.getProjectiles().values()) {
			if (elapsedTime - skill.lastTick > skill.getCooldown()) {
				skill.setLastTick(elapsedTime);
				weaponSounds.get(skill.getId()).play();
				for (int i = 0; i < skill.getNumberProjectiles(); i++) {
					projectiles.add(new Projectile(
									skill, player.getX(), player.getY(), i, playerAngle, elapsedTime
							)
					);
				}
			}
		}
	}

	// Detects projectile collision
	private void projectileCollision() {
		Iterator<Projectile> itr = projectiles.iterator();
		while (itr.hasNext()) {
			boolean collision = false;
			Projectile projectile = itr.next();
			if (!projectile.isPassThrough() || elapsedTime - projectile.getLastDamageTick() > 1) {
				Iterator<Enemy> itr2 = enemies.iterator();
				while (itr2.hasNext()) {
					Enemy enemy = itr2.next();
					if (Math.hypot(projectile.getX() - enemy.getX(), projectile.getY() - enemy.getY())
							< (projectile.size + enemy.getSize() * 10)) {
						enemy.setHp(enemy.getHp() - projectile.getDamage());
						double angle = Math.atan2((player.getY() - enemy.getY()), (player.getX() - enemy.getX()));
						enemy.setX((float) (enemy.getX() + Math.cos(angle) * projectile.getKnockback()));
						enemy.setY((float) (enemy.getY() + Math.sin(angle) * projectile.getKnockback()));
						if (enemy.getHp() <= 0) {
							if(enemyDeathSounds.containsKey(enemy.type)) {
								enemyDeathSounds.get(enemy.type).play();
							}
							if (player.addXP(enemy.getXP())){
								levelUpSound.play();
								lvlUp = true;
								randomizeAbilities();
							}
							itr2.remove();
						}
						if (!projectile.isPassThrough()) {
							collision = true;
							break;
						}
						projectile.setLastDamageTick(elapsedTime);
					}
				}
			}
			if (projectile.getID() == 2) {
				projectile.getRotation();
				projectile.setX((float) (player.getX()));
				projectile.setY((float) (player.getY()));
			} else if (projectile.getID() == 3) {
				projectile.setSpeed(projectile.getSpeed() + .05);
				projectile.setX((float) (projectile.getXOnly() +
						Math.cos(projectile.getRotation()) * projectile.getSpeed()));
				projectile.setY((float) (projectile.getYOnly() -
						Math.sin(projectile.getRotation()) * projectile.getSpeed()));
			} else if (projectile.getID() == 1) {
				projectile.getRotation();
			} else {
				projectile.setX((float) (projectile.getXOnly() +
						Math.cos(projectile.getRotation()) * projectile.getSpeed()));
				projectile.setY((float) (projectile.getYOnly() -
						Math.sin(projectile.getRotation()) * projectile.getSpeed()));
			}
			if(projectile.getDuration() < elapsedTime - projectile.getActivationTime() || collision) {
				itr.remove();
			}
		}
	}

	//----- Player Functions-----

	// Randomizes the levelup abilities
	private void randomizeAbilities() {
		Random random = new Random();
		List<Integer> skills = new ArrayList<>();
		for(int i = 0; i < 8; i++) {
			skills.add(i);
		}
		skillList = new ArrayList<>();
		for(int j = 0; j < 3; j++) {
			int rand = random.nextInt(skills.size());
			skillList.add(skills.get(rand));
			skills.remove(rand);
		}
	}


	// Called whenever the user manually hits attack
	private void playerManualAttack() {
		if ((elapsedTime - lastHit < 5) && (elapsedTime > .5)) {
			if (deltaVx > 0)
				batch.draw(swordSwing, 90, 90, 20, 20);
			else
				batch.draw(swordSwing, 90, 90, 20, 20, 0, 0, 200, 200, true, false);
		}
		Iterator<Enemy> itr = enemies.iterator();
		while (itr.hasNext()) {
			Enemy enemy = itr.next();
			if (deltaVx > 0) {
				if (enemy.getX() > player.getX() - 2) {
					double deltaX = Math.abs(enemy.getX() - player.getX());
					double deltaY = Math.abs(enemy.getY() - player.getY());
					float magnitude = (float)Math.hypot(deltaX, deltaY);
					if (magnitude < 5 + enemy.getSize() * 10) {
						enemy.setHp(enemy.getHp() - player.getSwordDamage());
						enemy.setX(enemy.getX() + 10);
						if(enemy.getHp() <= 0) {
							if(enemyDeathSounds.containsKey(enemy.type)) {
								enemyDeathSounds.get(enemy.type).play();
							}
							if (player.addXP(enemy.getXP())){
								levelUpSound.play();
								lvlUp = true;
								randomizeAbilities();
							}
							itr.remove();
						}
					}
				}
			} else {
				if (enemy.getX() < player.getX() + 2) {
					double deltaX = Math.abs(enemy.getX() - player.getX());
					double deltaY = Math.abs(enemy.getY() - player.getY());
					if (deltaX == 0) {
						deltaX = 1;
					}
					if (deltaY == 0) {
						deltaY = 1;
					}
					float magnitude = (float)Math.hypot(deltaX, deltaY);
					if (magnitude < 10) {
						int damageDealt = player.getSwordDamage();
						enemy.setHp(enemy.getHp() - damageDealt);
						enemy.setX(enemy.getX() - 10);
						if(enemy.getHp() <= 0) {
							if(enemyDeathSounds.containsKey(enemy.type)) {
								enemyDeathSounds.get(enemy.type).play();
							}
							itr.remove();
							if (player.addXP(enemy.getXP())){
								levelUpSound.play();
								lvlUp = true;
								randomizeAbilities();
							}
						}
					}
				}
			}
		}
	}


	// Detect X-Movement Collision
	private boolean detectMovementCollisionX(Player player, float deltaVx) {
		return obstructionMap.containsKey((new LocationPair(((player.getX()+deltaVx) / 5),
				(player.getY()/5))).getKey());
	}

	// Detect Y-Movement Collision
	private boolean detectMovementCollisionY(Player player, float deltaVy) {
		return obstructionMap.containsKey((new LocationPair((player.getX() / 5),
				((player.getY() + deltaVy)/5))).getKey());
	}
	// Detect X-Movement Collision
	private boolean detectMovementCollisionX(Enemy enemy, float deltaVx) {
		return obstructionMap.containsKey((new LocationPair(((enemy.getX()+deltaVx) / 5),
				(enemy.getY()/5))).getKey());
	}

	// Detect Y-Movement Collision
	private boolean detectMovementCollisionY(Enemy enemy, float deltaVy) {
		return obstructionMap.containsKey((new LocationPair((enemy.getX() / 5),
				((enemy.getY() + deltaVy)/5))).getKey());
	}


	// Reset gamestate
	public void restart() {
		environment.clear();
		projectiles.clear();
		generateMap();
		player = new Player();
		enemies.clear();
		elapsedTime = 0;
		lastHit = -.5;
		triggerSkeletons = 0;
		triggerTwo = 0;
		triggerThree = 0;
		triggerFour = 0;
		triggerFive = 0;
		triggerBoss = 0;
		for(Integer key:enemySoundDelay.keySet()) {
			enemySoundDelay.put(key, (double) 0);
		}
	}
}
