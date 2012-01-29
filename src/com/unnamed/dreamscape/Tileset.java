package com.unnamed.dreamscape;

public class Tileset {

	private String map;
	private int width, height;
	
	public Tileset(String mapFile, int width, int height) {
		this.map = mapFile;
		this.width = width;
		this.height = height;
	}
	
	public Tile getTile (int x, int y, int unitWidth, int unitHeight, int tileType) {
		return new Tile(x, y, this.width*unitWidth, this.height*unitHeight, tileType);
	}

}
