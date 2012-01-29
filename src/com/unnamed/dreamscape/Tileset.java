package com.unnamed.dreamscape;

import com.badlogic.gdx.Gdx;
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
	
	public void renderTile (int x, int y, int unitWidth, int unitHeight) {
		
		SpriteBatch mapTile = new SpriteBatch();
		
		mapTile.begin();
        mapTile.draw(this.map, this.width * x, this.height * y, 1, 1, x + this.width*unitWidth, y + this.height*unitHeight, this.width*unitWidth, this.height*unitHeight, false, false);
        mapTile.end();
		
	}

}
