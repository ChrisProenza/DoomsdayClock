package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
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

	boolean inMenu = false;
	boolean lvlUp = false;
	boolean stageSelect = false;
	boolean setPaused = false;

	double elapsedTime = 0;
	// Spawn triggers
	int triggerSkeletons = 0;
	int triggerTwo = 0;
	int triggerThree = 0;
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
	double lastHit = -.5;
	int playerHit = 0;
	// Enemy info
	List<Enemy> enemies = new ArrayList<Enemy>();
	int enemyID = 0;
	double frameRenderDelta = 0;
	// Camera
	private Camera camera;
	private Viewport viewport;
	// Stage Information
	private final int StageWidth = 100;
	private final int StageHeight = 100;
	// Background
	private Texture background;
	private Texture pauseButton;
	private Touchpad touchpad;
	private int backgroundX = 0;
	private int backgroundY = 0;
	// Animations
	private Texture swordSwing;
	class touchInfo {
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
	}
	private final Map<Integer, touchInfo> touches = new HashMap<Integer, touchInfo>();
	private final List<Projectile> projectiles = new ArrayList<>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new StretchViewport(100, 100, camera);
		background = new Texture("backgrounds/testbg.jpg");
		pauseButton = new Texture("buttons/PauseButton.png");
		swordSwing = new Texture("animations/SliceAnimation.png");
		// TODO - SaveData
		player = new Player();
		backgroundX = 0;
		backgroundY = 0;
		Gdx.input.setInputProcessor(this);
		for (int i = 0; i < 5; i++) {
			touches.put(i, new touchInfo());
		}
		player.addProjectile(new Dagger());
		player.addProjectile(new SpinningKatana());
		player.addProjectile(new SpinningBlades());
	}

	@Override public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render () {
		// TODO - inMenu needs to be reversed once a menu is added
		if (inMenu) {
			batch.begin();
			drawMenu();
			batch.end();
			frameRenderDelta = 0;
		} else if (stageSelect){
			batch.begin();
			drawStage();
			batch.end();
			frameRenderDelta = 0;
		} else {
			if (lvlUp) {
				batch.begin();
				drawBackground();
				drawEnemies();
				drawPlayer();
				drawUI();
				batch.end();
				frameRenderDelta = 0;
			} else if(setPaused) {
				batch.begin();
				drawBackground();
				drawEnemies();
				drawPlayer();
				drawUI();
				batch.end();
				frameRenderDelta = 0;
			} else {
				frameRenderDelta += Gdx.graphics.getDeltaTime();
				playerDeltaTime += Gdx.graphics.getDeltaTime();
				enemyDeltaTime += Gdx.graphics.getDeltaTime();
				elapsedTime += Gdx.graphics.getDeltaTime();
				spawnEnemy(elapsedTime);
				if (player.getState() == 2)
					playerAttackDeltaTime += Gdx.graphics.getDeltaTime();
				else
					playerAttackDeltaTime = 0;
				if (frameRenderDelta > .033) {
					if (player.getState() == 1)
						player.move(deltaVx, deltaVy);
					moveEnemy();
					ScreenUtils.clear(0, 0, 0, 1);
					batch.begin();
					if (player.getState() != 3) {
						drawBackground();
						drawEnemies();
						if ((elapsedTime - lastHit) > .5)
							detectEnemyCollision();
					}
					drawPlayer();
					playerProjectiles();
					projectileCollision();
					drawUI();
					batch.end();
					frameRenderDelta = 0;
				}
			}
		}
	}

	private void drawStage() {
	}

	private void drawMenu() {
	}

	private void moveEnemy() {
		for (Enemy enemy: enemies) {
			float eDeltaVx = 0;
			float eDeltaVy = 0;
			double deltaY = player.getY() - enemy.getY();
			double deltaX = player.getX() - enemy.getX();
			if (Math.hypot(deltaX, deltaY) >= 2) {
				double angle = Math.atan2(deltaY, deltaX);
				eDeltaVx = (float)(enemy.getSpeed() * Math.cos(angle));
				eDeltaVy = (float)(enemy.getSpeed() * Math.sin(angle));
				enemy.move(eDeltaVx, eDeltaVy);
			}
		}
	}

	private void spawnEnemy(double elapsedTime) {
		Random random = new Random();
		// Spawn increasing number of skeletons every 10 seconds
		if (elapsedTime > (triggerSkeletons * 10) && enemies.size() < 300) {
			triggerSkeletons++;
			for (int i = 0; i < (10 * Math.sqrt(triggerSkeletons)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean())
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				else
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 100) - 50;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 100) - 50;
					Y = Y + player.getY();
				}
				enemies.add(new SkeletonEnemy(0, X, Y));
			}
		}
		// Spawn increasing number of enemyTwo every 15 seconds
		if (elapsedTime > ((triggerTwo+1) * 15) && enemies.size() < 300) {
			triggerTwo++;
			for (int i = 0; i < (5 * Math.sqrt(triggerTwo)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean())
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				else
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 100) - 50;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 100) - 50;
					Y = Y + player.getY();
				}
				enemies.add(new SkeletonEnemy(1, X, Y));
			}
		}
		// Spawn increasing number of enemyThree every 30 seconds
		if (elapsedTime > ((triggerThree+1) * 30) && enemies.size() < 300) {
			triggerThree++;
			for (int i = 0; i < (2 * Math.sqrt(triggerThree)); i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean())
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				else
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 100) - 50;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 100) - 50;
					Y = Y + player.getY();
				}
				enemies.add(new SkeletonEnemy(2, X, Y));
			}
		}
		// Spawn Boss every 60 seconds
		if (elapsedTime > ((triggerBoss+1) * 60) && enemies.size() < 200) {
			triggerBoss++;
			for (int i = 0; i < triggerBoss; i++) {
				float X = 0;
				float Y = 0;
				if (random.nextBoolean())
					X = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getX());
				else
					Y = (getSpawn() * (random.nextBoolean() ? -1 : 1) + player.getY());
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 100) - 50;
					X = X + player.getX();
				} else {
					Y = ((random.nextInt(2) - 1) * 100) - 50;
					Y = Y + player.getY();
				}
				enemies.add(new SkeletonEnemy(3, X, Y));
			}
		}
	}

	private int getSpawn() {
		return (new Random()).nextInt(50);
	}

	private void detectEnemyCollision() {
		double deltaX = 0;
		double deltaY = 0;
		double magnitude = 0;
		for (Enemy enemy:enemies) {
			deltaX = player.getX() - enemy.getX();
			deltaY = player.getY() - enemy.getY();
			magnitude = Math.hypot(deltaX, deltaY);
			if (magnitude < 5) {
				player.setHp(player.getHp() - enemy.getDamage());
				lastHit = elapsedTime;
				break;
			}
		}
	}

	// Draws the background image
	public void drawBackground() {
		batch.draw(background, -player.getX(), player.getY(), StageWidth*10, StageHeight*10);
	}

	public void drawPlayer() {
		int state = player.getState();
		boolean nextFrame = false;
		if (playerDeltaTime > .066) {
			playerDeltaTime = 0;
			nextFrame = true;
		}
		if (player.getHp() == 0) {
			player.setState(3);
		}
		Texture currentSprite = player.getCurrentSprite(nextFrame);
		if (state == 1) {
			if (deltaVx > 0)
				batch.draw(currentSprite, 40, 40, 20, 20);
			else
				batch.draw(currentSprite, 40, 40, 20, 20, 0, 0, 96, 64, true, false);
		} else if (state == 2) {
			if (playerAttackDeltaTime > .25) {
				playerManualAttack();
			}
			if (deltaVx > 0)
				batch.draw(currentSprite, 40, 40, 20, 20);
			else
				batch.draw(currentSprite, 40, 40, 20, 20, 0, 0, 144, 64, true, false);
		} else if (state == 3) {
			if (deltaVx > 0)
				batch.draw(currentSprite, 40, 40, 20, 20);
			else
				batch.draw(currentSprite, 40, 40, 20, 20, 0, 0, 144, 64, true, false);
		} else {
			if (deltaVx > 0)
				batch.draw(currentSprite, 40, 40, 20, 20);
			else
				batch.draw(currentSprite, 40, 40, 20, 20, 0, 0, 144, 64, true, false);
		}
	}

	public void drawEnemies() {
		boolean nextFrame = false;
		if (enemyDeltaTime > .066) {
			enemyDeltaTime = 0;
			nextFrame = true;
		}
		Gdx.app.log("enemy", "" + enemies.size());
		if (enemies.size() > 0) {
			for (Enemy enemy : enemies) {
				Texture currentSprite = enemy.getCurrentSprite(nextFrame);
				float h = currentSprite.getHeight();
				float w = currentSprite.getWidth();
				if (player.getX() > enemy.getX())
					batch.draw(currentSprite, ((50 - w/8) - (player.getX() - enemy.getX())), ((50 - h/8) + (player.getY() - enemy.getY())), (w/4), (h/4));
				else
					batch.draw(currentSprite, ((50 - w/8) - (player.getX() - enemy.getX())), ((50 - h/8) + (player.getY() - enemy.getY())), (w/4), (h/4), 0, 0, 22, 33, true, false);
			}
		}
	}

	public void drawUI() {
		BitmapFont font = new BitmapFont();
		font.getData().setScale((float) .5, (float) .5);
		if (player.getState() != 3) {
			int minutes = (int) (10 - (elapsedTime / 60));
			int seconds = ((int) (60 - elapsedTime % 60));
			font.draw(batch, (int) (minutes) + " :" + seconds, 0, 100);
			font.setColor(Color.RED);
			font.draw(batch, "HP:" + player.getHp(), 0, 15);
			font.setColor(Color.GOLD);
			font.draw(batch, "Level:" + player.getLvl(), 65, 15);
			batch.draw(pauseButton, 90, 90, 10, 10);
			if (setPaused) {
				font.draw(batch, "Paused", 35, 60);
				font.draw(batch, "Tap to Unpause", 22, 40);
			}
		} else {
			font.draw(batch, "Tap To Restart", 25, 60);
			font.draw(batch, "Tap and hold to return to menu", 0, 40);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

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
		if (pointer < 5) {
			touches.get(pointer).touchX = 0;
			touches.get(pointer).touchY = 0;
			touches.get(pointer).touched = false;
		}
		if (!setPaused) {
			if ((Gdx.graphics.getHeight() * .1) < screenY
					|| (Gdx.graphics.getWidth() * .9 > screenX)) {
				if (player.getState() != 3) {
					if (player.getState() == 0) {
						player.setState(2);
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
			}
		} else {
			setPaused = false;
		}
		return true;
	}

	private void playerProjectiles() {
		for(Skill skill : player.getProjectiles().values()) {
			if (elapsedTime - skill.lastTick > skill.getCooldown()) {
				skill.setLastTick(elapsedTime);
				for (int i = 0; i < skill.getNumberProjectiles(); i++) {
					projectiles.add(new Projectile(
									skill, player.getX(), player.getY(), i, playerAngle
							)
					);
				}
			}
		}
	}

	private void projectileCollision() {
		Iterator<Projectile> itr = projectiles.iterator();
		while (itr.hasNext()) {
			boolean collision = false;
			Projectile projectile = itr.next();
			TextureRegion projectileTexture = new TextureRegion(projectile.getTexture());
			if (projectile.getID() == 2) {
				batch.draw(projectileTexture,
						(40 - (player.getX() - projectile.getX())),
						(40 + (player.getY() - projectile.getY())),
						10,
						10,
						20, 20,
						1,
						1,
						(float) Math.toDegrees(projectile.getRotation())
				);
			} else if (projectile.getID() != 1) {
				batch.draw(projectileTexture,
						(45 - (player.getX() - projectile.getX())),
						(45 + (player.getY() - projectile.getY())),
						5,
						5,
						10, 10,
						1,
						1,
						(float) Math.toDegrees(projectile.getRotation())
				);
			} else {
				batch.draw(projectileTexture,
						(45 - (player.getX() - projectile.getXOnly())),
						(45 + (player.getY() - projectile.getYOnly())),
						5,
						5,
						10, 10,
						1,
						1,
						(float) Math.toDegrees(projectile.getRotation())
				);
			}
			Iterator<Enemy> itr2 = enemies.iterator();
			while (itr2.hasNext()) {
				Enemy enemy = itr2.next();
				if(Math.hypot(projectile.getX() - enemy.getX(), projectile.getY() - enemy.getY())
						< projectile.size) {
					enemy.setHp(enemy.getHp() - projectile.getDamage());
					enemy.setX(enemy.getX() + 10);
					if(enemy.getHp() <= 0) {
						player.addXP(enemy.getXP());
						itr2.remove();
					}
					if (!projectile.isPassThrough()) {
						collision = true;
						break;
					}
				}
			}

			if (projectile.getID() == 2) {
				projectile.setX((float) (player.getX()));
				projectile.setY((float) (player.getY()));
			} else if (projectile.getID() != 1) {
				projectile.setX((float) (projectile.getXOnly() +
						Math.cos(projectile.getRotation()) * projectile.getSpeed()));
				projectile.setY((float) (projectile.getYOnly() -
						Math.sin(projectile.getRotation()) * projectile.getSpeed()));
			}
			if(projectile.getDuration() < projectile.getActivationTime() || collision) {
				itr.remove();
			} else
				projectile.setActivationTime((float) (projectile.getActivationTime() + Gdx.graphics.getDeltaTime()));
		}
	}

	private void playerManualAttack() {
		if ((elapsedTime - lastHit < 5) && (elapsedTime > .5)) {
			if (deltaVx > 0)
				batch.draw(swordSwing, 40, 40, 20, 20);
			else
				batch.draw(swordSwing, 40, 40, 20, 20, 0, 0, 200, 200, true, false);
		}
		Iterator<Enemy> itr = enemies.iterator();
		while (itr.hasNext()) {
			Enemy enemy = itr.next();
			if (deltaVx > 0) {
				if (enemy.getX() > player.getX() - 2) {
					double deltaX = Math.abs(enemy.getX() - player.getX());
					double deltaY = Math.abs(enemy.getY() - player.getY());
					float magnitude = (float)Math.hypot(deltaX, deltaY);
					if (magnitude < 5) {
						enemy.setHp(enemy.getHp() - player.getSwordDamage());
						enemy.setX(enemy.getX() + 10);
						if(enemy.getHp() <= 0) {
							player.addXP(enemy.getXP());
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
						BitmapFont bitmapFont = new BitmapFont();
						bitmapFont.setColor(Color.RED);
						bitmapFont.getData().setScale(.25F);
						int h = enemy.getCurrentSprite(false).getHeight();
						int w = enemy.getCurrentSprite(false).getWidth();
						bitmapFont.draw(batch, ""+damageDealt,
								((50 - w*.25F) - (player.getX() - enemy.getX())),
								((50 - h*.25F) + (player.getY() - enemy.getY()))
						);
						if(enemy.getHp() <= 0) {
							itr.remove();
							player.addXP(enemy.getXP());
						}
					}
				}
			}
		}
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!setPaused) {
			if ((Gdx.graphics.getHeight() * .1) < touches.get(pointer).touchY
					|| (Gdx.graphics.getWidth() * .9 > touches.get(pointer).touchX)) {
				if (player.getState() != 2) {
					if ((Math.abs(touches.get(pointer).touchX - screenX) > 10
							|| Math.abs(touches.get(pointer).touchY - screenY) > 10)
							&& player.getState() != 3) {
						double deltaY = screenY - touches.get(pointer).touchY;
						double deltaX = screenX - touches.get(pointer).touchX;
						playerAngle = (float) Math.atan2(deltaY, deltaX);
						deltaVx = (float) (player.getSpeed() * Math.cos(playerAngle));
						// Check boundaries
						if (deltaVx + player.getX() < -1000 || deltaVx + player.getX() > 1000
								|| detectMovementCollisionX(player, deltaVx)) {
							deltaVx = 0;
						}
						deltaVy = (float) (player.getSpeed() * Math.sin(playerAngle));
						if (deltaVy + player.getY() < -1000 || deltaVy + player.getY() > 1000
								|| detectMovementCollisionY(player, deltaVy)) {
							deltaVy = 0;
						}
						// Set player is moving
						player.setState(1);
					}
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

	public boolean detectMovementCollisionX(Player player, float speed) {
		return false;
	}
	public boolean detectMovementCollisionY(Player player, float speed) {
		return false;
	}
	public void restart() {
		// TODO - Double Check Everything is reset
		player.setState(0);
		player.setHp(100);
		player.setLvl(1);
		player.setXP(0);
		player.setX(0);
		player.setY(0);
		enemies.clear();
		elapsedTime = 0;
		lastHit = -.5;
		triggerSkeletons = 0;
	}
}
