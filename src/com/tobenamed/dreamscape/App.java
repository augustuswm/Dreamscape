package com.tobenamed.dreamscape;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class App implements ApplicationListener {
	/**
	 * The time the last frame was rendered, used for throttling framerate
	 */
	private long lastRender;
	
	/**
	 * Box2d works best with small values. If you use pixels directly you will
	 * get weird results -- speeds and accelerations not feeling quite right.
	 * Common practice is to use a constant to convert pixels to and from
	 * "meters".
	 */
	public static final float PIXELS_PER_METER = 60.0f;
	
	private Map tiledMap;

	/**
	 * The libgdx SpriteBatch -- used to optimize sprite drawing.
	 */
	private SpriteBatch spriteBatch;

	/**
	 * This is the main box2d "container" object. All bodies will be loaded in
	 * this object and will be simulated through calls to this object.
	 */
	private World world;

	/**
	 * This is the player character. It will be created as a dynamic object.
	 */
	private Player player;

	/**
	 * This box2d debug renderer comes from libgdx test code. It draws lines
	 * over all collision boundaries, so it is immensely useful for verifying
	 * that the world collisions are as you expect them to be. It is, however,
	 * slow, so only use it for testing.
	 */
	private Box2DDebugRenderer debugRenderer;

	/**
	 * The screen's width and height. This may not match that computed by
	 * libgdx's gdx.graphics.getWidth() / getHeight() on devices that make use
	 * of on-screen menu buttons.
	 */
	private int screenWidth;
	private int screenHeight;
	
	private float max_speed = 3.0f;

	public App() {
		super();

		// Defer until create() when Gdx is initialized.
		screenWidth = -1;
		screenHeight = -1;
	}
	
	@Override
	public void create() {
		
		/**
		 * If the viewport's size is not yet known, determine it here.
		 */
		if (screenWidth == -1) {
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		}

		tiledMap = new Map();

		tiledMap.setPackerDirectory("data/packer");

		tiledMap.loadMap("data/world/level1/level.tmx");

		tiledMap.prepareCamera(screenWidth, screenHeight);
		
		/**
		 * Load up the overall texture and chop it in to pieces. In this case,
		 * piece.
		 */
		/*overallTexture = new Texture(Gdx.files.internal("data/sprite.png"));
		overallTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		jumperSprite = new Sprite(overallTexture, 0, 0, 21, 37);*/

		spriteBatch = new SpriteBatch();

		/**
		 * You can set the world's gravity in its constructor. Here, the gravity
		 * is negative in the y direction (as in, pulling things down).
		 */
		world = new World(new Vector2(0.0f, -10.0f), true);
		
		player = new Player(this.world);

		/*BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;
		jumperBodyDef.position.set(1.0f, 3.0f);

		jumper = world.createBody(jumperBodyDef);*/

		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		/*PolygonShape jumperShape = new PolygonShape();
		jumperShape.setAsBox(jumperSprite.getWidth() / (2 * PIXELS_PER_METER),
				jumperSprite.getHeight() / (2 * PIXELS_PER_METER));

		EdgeShape jumperBase = new EdgeShape();
		jumperBase.set( -1 * jumperSprite.getWidth() / (2 * PIXELS_PER_METER) + 0.01f,
						-1 * jumperSprite.getHeight() / (2 * PIXELS_PER_METER),
						jumperSprite.getWidth() / (2 * PIXELS_PER_METER) - 0.01f,
						-1 * jumperSprite.getHeight() / (2 * PIXELS_PER_METER));
		*/
		
		/**
		 * The character should not ever spin around on impact.
		 */
		//jumper.setFixedRotation(true);

		/**
		 * The density and friction of the jumper were found experimentally.
		 * Play with the numbers and watch how the character moves faster or
		 * slower.
		 */
		/*FixtureDef jumperFixtureDef = new FixtureDef();
		jumperFixtureDef.shape = jumperShape;
		jumperFixtureDef.density = 1.0f;
		jumperFixtureDef.friction = 5.0f;
		
		FixtureDef jumperBaseFixtureDef = new FixtureDef();
		jumperBaseFixtureDef.shape = jumperBase;
		jumperBaseFixtureDef.isSensor = true;

		jumper.createFixture(jumperFixtureDef);
		jumper.createFixture(jumperBaseFixtureDef);
		jumperShape.dispose();
		jumperBase.dispose();
		*/

		tiledMap.loadCollisions("data/collisions.txt", world,
				PIXELS_PER_METER);

		debugRenderer = new Box2DDebugRenderer();

		lastRender = System.nanoTime();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		//System.out.println(player.getBody().getLinearVelocity());
		
		long now = System.nanoTime();

		/**
		 * Detect requested motion.
		 */
		boolean moveLeft = false;
		boolean moveRight = false;
		boolean doJump = false;
		
	
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			moveRight = true;
		}/* else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() > Gdx.graphics.getWidth() * 0.80f) {
					moveRight = true;
				}
			}
		}*/
	
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			moveLeft = true;
		}/* else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() < Gdx.graphics.getWidth() * 0.20f) {
					moveLeft = true;
				}
			}
		}*/
	
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			doJump = true;
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getY() < Gdx.graphics.getHeight() * 0.20f) {
					doJump = true;
				}
			}
		}
	
		/**
		 * Act on that requested motion.
		 * 
		 * This code changes the jumper's direction. It's handled separately
		 * from the jumping so that the player can jump and move simultaneously.
		 * The horizontal figure was arrived at experimentally -- try other
		 * values to experience different speeds.
		 * 
		 * The impulses are applied to the center of the jumper.
		 */
		if (moveRight && player.getBody().getLinearVelocity().x < max_speed) {
			//player.getBody().setTransform(player.getBody().getPosition().x + 0.04f, player.getBody().getPosition().y, 0f);
			player.getBody().applyLinearImpulse(new Vector2(0.05f, 0.0f), player.getBody().getWorldCenter());
			if (player.direction() == false) {
				player.getSprite().flip(true, false);
			}
			player.setDirection(true);
		} else if (moveLeft && player.getBody().getLinearVelocity().x > -1*max_speed) {
			//player.getBody().setTransform(player.getBody().getPosition().x - 0.04f, player.getBody().getPosition().y, 0f);
			player.getBody().applyLinearImpulse(new Vector2(-0.05f, 0.0f),player.getBody().getWorldCenter());
			if (player.direction() == true) {
				player.getSprite().flip(true, false);
			}
			player.setDirection(false);
		}
	
		/**
		 * The jumper dude can only jump while on the ground. There are better
		 * ways to detect ground contact, but for our purposes it is sufficient
		 * to test that the vertical velocity is zero (or close to it). As in
		 * the above code, the vertical figure here was found through
		 * experimentation. It's enough to get the guy off the ground.
		 * 
		 * As before, impulse is applied to the center of the jumper.
		 */
		if (doJump && Math.abs(player.getBody().getLinearVelocity().y) < 1e-9) {
			player.getBody().applyLinearImpulse(new Vector2(0.0f, 1.0f),
					player.getBody().getWorldCenter());
		}
	
		/**
		 * Have box2d update the positions and velocities (and etc) of all
		 * tracked objects. The second and third argument specify the number of
		 * iterations of velocity and position tests to perform -- higher is
		 * more accurate but is also slower.
		 */
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		/**
		 * A nice(?), blue backdrop.
		 */
		Gdx.gl.glClearColor(0, 0.5f, 0.9f, 0);
	
		/**
		 * The camera is now controlled primarily by the position of the main
		 * character, and secondarily by the map boundaries.
		 */
	
		tiledMap.getCamera().position.x = PIXELS_PER_METER
				* player.getBody().getPosition().x;
	
		/**
		 * Ensure that the camera is only showing the map, nothing outside.
		 */
		if (tiledMap.getCamera().position.x < Gdx.graphics.getWidth() / 2) {
			tiledMap.getCamera().position.x = Gdx.graphics.getWidth() / 2;
		}
		if (tiledMap.getCamera().position.x >= tiledMap.getWidth()
				- Gdx.graphics.getWidth() / 2) {
			tiledMap.getCamera().position.x = tiledMap.getWidth()
					- Gdx.graphics.getWidth() / 2;
		}
	
		if (tiledMap.getCamera().position.y < Gdx.graphics.getHeight() / 2) {
			tiledMap.getCamera().position.y = Gdx.graphics.getHeight() / 2;
		}
		if (tiledMap.getCamera().position.y >= tiledMap.getHeight()
				- Gdx.graphics.getHeight() / 2) {
			tiledMap.getCamera().position.y = tiledMap.getHeight()
					- Gdx.graphics.getHeight() / 2;
		}
	
		tiledMap.getCamera().update();
		tiledMap.render();
	
		/**
		 * Prepare the SpriteBatch for drawing.
		 */
		spriteBatch.setProjectionMatrix(tiledMap.getCamera().combined);
		spriteBatch.begin();
	
		player.getSprite().setPosition(
				PIXELS_PER_METER * player.getBody().getPosition().x
						- player.getSprite().getWidth() / 2,
				PIXELS_PER_METER * player.getBody().getPosition().y
						- player.getSprite().getHeight() / 2);
		player.getSprite().draw(spriteBatch);
	
		/**
		 * "Flush" the sprites to screen.
		 */
		spriteBatch.end();
	
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		debugRenderer.render(world, tiledMap.getCamera().combined.scale(
				PIXELS_PER_METER,
				PIXELS_PER_METER,
				PIXELS_PER_METER));
	
		now = System.nanoTime();
		if (now - lastRender < 30000000) { // 30 ms, ~33FPS
			try {
				Thread.sleep(30 - (now - lastRender) / 1000000);
			} catch (InterruptedException e) {
			}
		}
	
		lastRender = now;

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
