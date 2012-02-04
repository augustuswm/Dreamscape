package com.unnamed.dreamscape;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class App implements ApplicationListener {
	
	private Level level;
    private OrthographicCamera camera;
	
	@Override
	public void create() {
		level = new Level ("data/map.jpg");
	}

	@Override
	public void resize(int width, int height) {
       camera = new OrthographicCamera(width, height);
       camera.position.set(width / 2, height / 2, 0);
	}

	@Override
	public void render() {
        handleInput();
        camera.update();
        camera.apply(Gdx.gl10);
        
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
		level.render(camera);
	}
	
	private void handleInput() {
	        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
	        	camera.zoom += 0.02;
	        }
	        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
	        	camera.zoom -= 0.02;
	        }
	        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	                if (camera.position.x > 0)
	                	camera.translate(-3, 0, 0);
	        }
	        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	                if (camera.position.x < 1024)
	                	camera.translate(3, 0, 0);
	        }
	        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
	                if (camera.position.y > 0)
	                	camera.translate(0, -3, 0);
	        }
	        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
	                if (camera.position.y < 1024)
	                	camera.translate(0, 3, 0);
	        }
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
