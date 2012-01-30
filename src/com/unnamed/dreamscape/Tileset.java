package com.unnamed.dreamscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tileset {

	private Texture map;
	private int width, height;
	
	public Tileset(String mapFile, int width, int height) {
		this.map = new Texture(Gdx.files.internal(mapFile));
		this.width = width;
		this.height = height;
	}
	
	public void renderTile (int x, int y, int unitWidth, int unitHeight, OrthographicCamera camera) {
		
		int displayWidth = this.width * unitWidth;
		int displayHeight = this.height  * unitHeight;
		
		SpriteBatch mapTile = new SpriteBatch();
		
		mapTile.setProjectionMatrix(camera.combined);
		
		mapTile.begin();
		mapTile.draw(this.map, x * this.width, y * this.height, displayWidth, displayHeight, x + displayWidth, y + displayHeight, displayWidth, displayHeight, false, false);
        mapTile.end();
		
	}

}
