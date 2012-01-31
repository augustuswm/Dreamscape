package com.unnamed.dreamscape;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Map {

	private int[][] tilePlacement;
	private int width, height;
	
	public Map (String data, int width, int height) {
		this.tilePlacement = new int[height][width];
		this.width = width;
		this.height = height;
		
		String[] tiles = data.split(",");
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.tilePlacement[i][j] = Integer.parseInt(tiles[i*width + j]);
			}
		}
		
	}
	
	public void renderMap (Tileset tileset, OrthographicCamera camera) {
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				tileset.renderTile(j, i, this.tilePlacement[i][j], 1, 1, camera);
			}
		}
	}
	
}