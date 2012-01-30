package com.unnamed.dreamscape;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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
	}

	@Override
	public void render() {
        camera.update();
        camera.apply(Gdx.gl10);
        
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
		level.render(camera);
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
