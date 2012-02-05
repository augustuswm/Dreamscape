package com.tobenamed.dreamscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

	/**
	 * As the name implies, this is the sprite for the player character. The
	 * boolean is just to track which direction the player is facing. There are
	 * better ways to handle this, but in a real game you would handle the
	 * character sprite a lot differently (with animations and all that) so
	 * let's call that outside the scope of this example.
	 */
	private Sprite playerSprite;
	private boolean playerFacingRight;

	/**
	 * This is the player character. It will be created as a dynamic object.
	 */
	private Body player;
	
	/**
	 * Holder of the texture for the various non-map sprites the game will have.
	 */
	private Texture playerTexture;
	
	public Player(World world) {
		
		/**
		 * Load up the overall texture and chop it in to pieces. In this case,
		 * piece.
		 */
		playerTexture = new Texture(Gdx.files.internal("data/sprite.png"));
		playerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		playerSprite = new Sprite(playerTexture, 0, 0, 21, 37);
		
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(1.0f, 3.0f);
		
		player = world.createBody(playerBodyDef);
		
		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(playerSprite.getWidth() / (2 * App.PIXELS_PER_METER),
				playerSprite.getHeight() / (2 * App.PIXELS_PER_METER));

		EdgeShape playerBase = new EdgeShape();
		playerBase.set( -1 * playerSprite.getWidth() / (2 * App.PIXELS_PER_METER) + 0.01f,
						-1 * playerSprite.getHeight() / (2 * App.PIXELS_PER_METER),
						playerSprite.getWidth() / (2 * App.PIXELS_PER_METER) - 0.01f,
						-1 * playerSprite.getHeight() / (2 * App.PIXELS_PER_METER));
		
		/**
		 * The character should not ever spin around on impact.
		 */
		player.setFixedRotation(true);

		/**
		 * The density and friction of the player were found experimentally.
		 * Play with the numbers and watch how the character moves faster or
		 * slower.
		 */
		FixtureDef playerFixtureDef = new FixtureDef();
		playerFixtureDef.shape = playerShape;
		playerFixtureDef.density = 1.0f;
		playerFixtureDef.friction = 5.0f;
		
		FixtureDef playerBaseFixtureDef = new FixtureDef();
		playerBaseFixtureDef.shape = playerBase;
		playerBaseFixtureDef.isSensor = true;

		player.createFixture(playerFixtureDef);
		player.createFixture(playerBaseFixtureDef);
		playerShape.dispose();
		playerBase.dispose();
	}
	
	public Body getBody() {
		return this.player;
	}
	
	public Sprite getSprite() {
		return this.playerSprite;
	}
	
	public boolean direction() {
		return this.playerFacingRight;
	}
	
	public void setDirection(boolean dir) {
		this.playerFacingRight = dir;
	}

	public class FootListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			// TODO Auto-generated method stub

		}

		@Override
		public void endContact(Contact contact) {
			// TODO Auto-generated method stub

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}

	}

}
