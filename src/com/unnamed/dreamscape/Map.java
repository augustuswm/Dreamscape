package com.unnamed.dreamscape;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Map {

	private int[][] tilePlacement;
	private int width, height;
	
	public Map (String data, int width, int height) {
		this.tilePlacement = new int[width][height];
		this.width = width;
		this.height = height;
		
		String[] tiles = data.split(",");
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.tilePlacement[i][j] = Integer.parseInt(tiles[i*width + height]);
			}
		}
		
	}
	
	public void renderMap (Tileset tileset, OrthographicCamera camera) {
		
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				tileset.renderTile(i, j, 1, 1, camera);
			}
		}
	}
	
}