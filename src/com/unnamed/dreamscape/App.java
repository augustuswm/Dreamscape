package com.unnamed.dreamscape;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class App implements ApplicationListener {
	
	private Level level;
	
	@Override
	public void create() {
		level = new Level ("data/map.jpg");
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        level.render();
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
