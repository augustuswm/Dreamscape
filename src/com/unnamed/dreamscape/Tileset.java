package com.unnamed.dreamscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tileset {
	
	/*
	 * 00 01 02 03 04 05 06 07
	 * 08 09 10 11 12 13 14 15
	 * 16 17 18 19 20 21 22 23
	 */

	private Texture map;
	private int width, height;
	
	public Tileset(String mapFile, int width, int height) {
		this.map = new Texture(Gdx.files.internal(mapFile));
		this.width = width;
		this.height = height;
	}
	
	public void renderTile (int posX, int posY, int tilePosition, int unitWidth, int unitHeight, OrthographicCamera camera) {
		
		int displayWidth = this.width * unitWidth,
			displayHeight = this.height  * unitHeight,
			initX = posX * this.width,
			initY = posY * this.height,
			tileInitX = tilePosition % (this.map.getWidth() / this.width) * this.height,
			tileInitY = (int) (Math.floor(tilePosition / (this.map.getHeight() / this.height)) * this.width);
		
		SpriteBatch mapTile = new SpriteBatch();
		
		mapTile.setProjectionMatrix(camera.combined);
		
		mapTile.begin();
		mapTile.draw(this.map, initX, initY, displayWidth, displayHeight, tileInitX, tileInitY, displayWidth, displayHeight, false, false);
        mapTile.end();
		
	}

}
