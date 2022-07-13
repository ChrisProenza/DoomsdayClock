package com.kcstudios.doomsdayclock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.util.Random;

import jdk.internal.org.jline.utils.Log;

public class DoomsdayClock extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	SpriteBatch batch;

	double elapsedTime = 0;
	// Spawn triggers
	int triggers = 0;

	// Player info
	Player player;
	int deltaVx;
	int deltaVy;
	double playerDeltaTime;
	double playerDamageCD = 0;
	double enemyDeltaTime;
	double playerAttackDeltaTime;
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
	private Button attackButton;
	private Texture background;
	private Touchpad touchpad;
	private int backgroundX = 0;
	private int backgroundY = 0;
	// UI
	// TODO - Create Buttons for movement and attacking (Textures)
	class touchInfo {
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
	}
	private Map<Integer, touchInfo> touches = new HashMap<Integer, touchInfo>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new StretchViewport(100, 100, camera);
		background = new Texture("backgrounds/testbg.jpg");
		// TODO - SaveData
		player = new Player();
		backgroundX = 0;
		backgroundY = 0;
		Gdx.input.setInputProcessor(this);
		for (int i = 0; i < 5; i++) {
			touches.put(i, new touchInfo());
		}
	}

	@Override public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render () {
		frameRenderDelta += Gdx.graphics.getDeltaTime();
		playerDeltaTime += Gdx.graphics.getDeltaTime();
		enemyDeltaTime += Gdx.graphics.getDeltaTime();
		elapsedTime += Gdx.graphics.getDeltaTime();
		spawnEnemy(elapsedTime);
		if (player.getState() == 2)
			playerAttackDeltaTime += Gdx.graphics.getDeltaTime();
		if (frameRenderDelta > .033) {
			if (player.getState() == 1)
				player.move(deltaVx, deltaVy);
			moveEnemy();
			ScreenUtils.clear(0, 0, 0, 1);
			batch.begin();
			if (player.getState() != 3) {
				drawBackground();
				drawEnemies();
				detectEnemyCollision();
			}
			drawPlayer();
			drawUI();
			batch.end();
			frameRenderDelta = 0;
		}
	}

	private void moveEnemy() {
		for (Enemy enemy: enemies) {
			int eDeltaVx = 0;
			int eDeltaVy = 0;
			double deltaY = player.getY() - enemy.getY();
			double deltaX = player.getX() - enemy.getX();
			double angle = Math.atan2(deltaY, deltaX);
			eDeltaVx = (int) (2 * Math.cos(angle));
			eDeltaVy = (int) (2 * Math.sin(angle));
			enemy.move(eDeltaVx, eDeltaVy);
		}
	}

	private void spawnEnemy(double elapsedTime) {
		Random random = new Random();
		if (triggers == 0 && elapsedTime > 0) {
			triggers++;
			for (int i = 0; i < 10; i++) {
				int X = 0;
				int Y = 0;
				if (random.nextBoolean())
					X = getSpawn();
				else
					Y = getSpawn();
				if (X == 0) {
					X = ((random.nextInt(2) - 1) * 200) - 100;
				} else {
					Y = ((random.nextInt(2) - 1) * 200) - 100;
				}
				enemies.add(new SkeletonEnemy(enemyID, X, Y));
				enemyID++;
			}
		}
	}

	private int getSpawn() {
		return (new Random()).nextInt(100);
	}

	private void detectEnemyCollision() {
		double deltaX = 0;
		double deltaY = 0;
		double magnitude = 0;
		for (Enemy enemy:enemies) {
			deltaX = player.getX() - enemy.getX();
			deltaY = player.getY() - enemy.getY();
			magnitude = Math.hypot(deltaX, deltaY);
			if (magnitude < 10) {
				player.setHp(player.getHp() - enemy.getDamage());
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
		if (state == 1) {
			if (deltaVx > 0)
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20);
			else
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20, 0, 0, 96, 64, true, false);
		} else if (state == 2) {
			if (deltaVx > 0)
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20);
			else
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20, 0, 0, 144, 64, true, false);
		} else if (state == 3) {
			if (deltaVx > 0)
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20);
			else
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20, 0, 0, 144, 64, true, false);
		} else {
			if (deltaVx > 0)
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20);
			else
				batch.draw(player.getCurrentSprite(nextFrame), 37, 25, 20, 20, 0, 0, 144, 64, true, false);
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
				if (player.getX() > enemy.getX())
					batch.draw(enemy.getCurrentSprite(nextFrame), 37 - (player.getX() - enemy.getX()), 25 + (player.getY() - enemy.getY()), 5, 15);
				else
					batch.draw(enemy.getCurrentSprite(nextFrame), 37 - (player.getX() - enemy.getX()), 25 + (player.getY() - enemy.getY()), 5, 15, 0, 0, 22, 33, true, false);
			}
		}
	}

	public void drawUI() {
	}

	public void drawInventory() {

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
		if (player.getState() != 3){
			if (player.getState() == 0) {
				player.setState(2);
			} else
				player.setState(0);
		} else {
			restart();
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if ((Math.abs(touches.get(pointer).touchX - screenX) > 10
				|| Math.abs(touches.get(pointer).touchY - screenY) > 10)
				&& player.getState() != 3){
			double deltaY = screenY - touches.get(pointer).touchY;
			double deltaX = screenX - touches.get(pointer).touchX;
			double angle = Math.atan2(deltaY, deltaX);
			deltaVx = (int) (3 * Math.cos(angle));
			// Check boundaries
			if (deltaVx + player.getX() < -1000 || deltaVx + player.getX() > 1000
					|| detectMovementCollisionX(player, deltaVx)) {
				deltaVx = 0;
			}
			deltaVy = (int) (3 * Math.sin(angle));
			if (deltaVy + player.getY() < -1000 || deltaVy + player.getY() > 1000
					|| detectMovementCollisionY(player, deltaVy)) {
				deltaVy = 0;
			}
			// Set player is moving
			player.setState(1);
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

	public boolean detectMovementCollisionX(Player player, int speed) {
		return false;
	}
	public boolean detectMovementCollisionY(Player player, int speed) {
		return false;
	}
	public void restart() {
		// TODO - add reset correctly
		player.setState(0);
		player.setHp(100);
	}
}
